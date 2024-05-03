package com.yuzhi.ainms.core.config;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.PREFERRED_USERNAME;

import com.yuzhi.ainms.core.security.SecurityUtils;
import com.yuzhi.ainms.core.security.oauth2.AudienceValidator;
import com.yuzhi.ainms.core.security.oauth2.CustomAuthorizationRequestResolver;
import com.yuzhi.ainms.core.security.oauth2.CustomClaimConverter;
import com.yuzhi.ainms.core.security.oauth2.CustomTimestampValidator;
import com.yuzhi.ainms.core.security.oauth2.JwtGrantedAuthorityConverter;

import java.time.Duration;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import tech.jhipster.config.JHipsterProperties;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {
    private final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

    private final JHipsterProperties jHipsterProperties;

    @Value("${spring.security.oauth2.client.provider.oidc.issuer-uri}")
    private String issuerUri;

    public SecurityConfiguration(JHipsterProperties jHipsterProperties) {
        this.jHipsterProperties = jHipsterProperties;
    }

    @Bean
    public OAuth2AuthorizationRequestResolver customAuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
        log.debug("====clinetRegistrationRepository: {}", clientRegistrationRepository.findByRegistrationId("oidc"));
        return new CustomAuthorizationRequestResolver(clientRegistrationRepository,DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, ClientRegistrationRepository clientRegistrationRepository, MvcRequestMatcher.Builder mvc) throws Exception {
        http
            .cors(withDefaults())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(
                authz ->
                authz
                    .requestMatchers(mvc.pattern("/**")).permitAll()
                    .requestMatchers(mvc.pattern("/static/index.html")).permitAll()
                    .requestMatchers(mvc.pattern("/api/authenticate")).permitAll()
                    .requestMatchers(mvc.pattern("/api/auth-info")).permitAll()
                    .requestMatchers(mvc.pattern("/websocket/**")).authenticated()
                    .requestMatchers(mvc.pattern("/api/admin/**")).authenticated()
                    .requestMatchers(mvc.pattern("/api/**")).authenticated()
                    .requestMatchers(mvc.pattern("/v3/api-docs/**")).authenticated()
                    .requestMatchers(mvc.pattern("/management/health")).authenticated()
                    .requestMatchers(mvc.pattern("/management/health/**")).authenticated()
                    .requestMatchers(mvc.pattern("/management/info")).authenticated()
                    .requestMatchers(mvc.pattern("/management/prometheus")).authenticated()
                    .requestMatchers(mvc.pattern("/management/**")).authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/")
                .userInfoEndpoint(userInfo -> userInfo.oidcUserService(this.oidcUserService()))
                .authorizationEndpoint(authorizationEndpointConfig ->
                    authorizationEndpointConfig.authorizationRequestResolver(
                        customAuthorizationRequestResolver(clientRegistrationRepository)
                     )
                )
                .successHandler(this.oauth2LoginSuccessHandler())
                .failureHandler((request, response, exception) -> {
                    log.error("Authentication failed: " + exception.getMessage());
                    response.sendRedirect("/?error=" + exception.getMessage());
                })
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt
                .decoder(jwtDecoder(clientRegistrationRepository, new RestTemplateBuilder())) 
                .jwtAuthenticationConverter(authenticationConverter())))
            .oauth2Client(withDefaults());
        return http.build();
    }

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    Converter<Jwt, AbstractAuthenticationToken> authenticationConverter() {
        log.debug("==jwt converter:");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new JwtGrantedAuthorityConverter());
        jwtAuthenticationConverter.setPrincipalClaimName(PREFERRED_USERNAME);

        return jwtAuthenticationConverter;
    }

    OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        log.debug(" ==== start oidcUserService");
        final OidcUserService delegate = new OidcUserService();

        return userRequest -> {
            OidcUser oidcUser = delegate.loadUser(userRequest);
            return new DefaultOidcUser(oidcUser.getAuthorities(), oidcUser.getIdToken(), oidcUser.getUserInfo(), PREFERRED_USERNAME);
        };
    }

    /**
     * Map authorities from "groups" or "roles" claim in ID Token.
     *
     * @return a {@link GrantedAuthoritiesMapper} that maps groups from
     * the IdP to Spring Security Authorities.
     */
    @Bean
    public GrantedAuthoritiesMapper userAuthoritiesMapper() {
        log.debug("====start userAuthoritiesMapper");
        return authorities -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

            authorities.forEach(authority -> {
                // Check for OidcUserAuthority because Spring Security 5.2 returns
                // each scope as a GrantedAuthority, which we don't care about.
                if (authority instanceof OidcUserAuthority) {
                    OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) authority;
                    mappedAuthorities.addAll(SecurityUtils.extractAuthorityFromClaims(oidcUserAuthority.getUserInfo().getClaims()));
                }
            });
            return mappedAuthorities;
        };
    }

    @Bean
    JwtDecoder jwtDecoder(ClientRegistrationRepository clientRegistrationRepository, RestTemplateBuilder restTemplateBuilder) {
        log.debug("jwtDecoder::");
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromOidcIssuerLocation(issuerUri);

        JwtTimestampValidator timestampValidator = new JwtTimestampValidator(Duration.ofSeconds(60));  // Example: 60 seconds tolerance
        CustomTimestampValidator customTimestampValidator = new CustomTimestampValidator();
        // Validator for audience checking
        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(jHipsterProperties.getSecurity().getOauth2().getAudience());
        log.debug("Audience validator configured: " + audienceValidator);
        // Validator for issuer
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);
        // Combine all validators into one DelegatingOAuth2TokenValidator
        OAuth2TokenValidator<Jwt> combinedValidators = new DelegatingOAuth2TokenValidator<>(
            timestampValidator,
            customTimestampValidator,
            audienceValidator,
            withIssuer
        );

        // Set the combined validator on the JwtDecoder
        jwtDecoder.setJwtValidator(combinedValidators);
        jwtDecoder.setClaimSetConverter(
            new CustomClaimConverter(clientRegistrationRepository.findByRegistrationId("oidc"), restTemplateBuilder.build())
        );
        log.debug("Finished cust jwtDecoder");

        return jwtDecoder;
    }

    @Bean
    public AuthenticationSuccessHandler oauth2LoginSuccessHandler() {
        log.debug("==start oauth2LoginSuccessHandler");
        return (request, response, authentication) -> {
            log.debug("Authentication successful for user: {}", authentication.getName());
            request.getSession().setAttribute("loginSuccess", true);
            response.sendRedirect("/");
        };
    }
}
