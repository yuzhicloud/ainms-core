package com.yuzhi.ainms.core.config;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.PREFERRED_USERNAME;

import com.yuzhi.ainms.core.security.SecurityUtils;
import com.yuzhi.ainms.core.security.oauth2.AudienceValidator;
import com.yuzhi.ainms.core.security.oauth2.CustomClaimConverter;
import com.yuzhi.ainms.core.security.oauth2.JwtGrantedAuthorityConverter;
import java.util.*;
import com.yuzhi.ainms.core.web.filter.SpaWebFilter;
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
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.web.filter.CookieCsrfFilter;

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
    public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http
            .cors(withDefaults())
            .csrf(csrf -> csrf.disable())
//            .csrf(csrf ->
//                csrf
//                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                    // See https://stackoverflow.com/q/74447118/65681
//                    .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
//            .addFilterAfter(new SpaWebFilter(), BasicAuthenticationFilter.class)
//            .addFilterAfter(new CookieCsrfFilter(), BasicAuthenticationFilter.class)
//            .headers(
//                headers ->
//                    headers
//                        .contentSecurityPolicy(csp -> csp.policyDirectives(jHipsterProperties.getSecurity().getContentSecurityPolicy()))
//                        .frameOptions(FrameOptionsConfig::sameOrigin)
//                        .referrerPolicy(
//                            referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
//                        )
//                        .permissionsPolicy(
//                            permissions ->
//                                permissions.policy(
//                                    "camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()"
//                                )
//                        )
//            )
            .authorizeHttpRequests(
                authz ->
                    // prettier-ignore
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
            .oauth2Login(oauth2 -> oauth2.loginPage("/").userInfoEndpoint(userInfo -> userInfo.oidcUserService(this.oidcUserService()))
                .successHandler(this.oauth2LoginSuccessHandler())
                .failureHandler((request, response, exception) -> {
                    log.error("Authentication failed: " + exception.getMessage());
                    response.sendRedirect("/?error=" + exception.getMessage()); // 自定义错误重定向
                }))
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(authenticationConverter())))
            .oauth2Client(withDefaults());
        return http.build();
    }

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    Converter<Jwt, AbstractAuthenticationToken> authenticationConverter() {
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
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromOidcIssuerLocation(issuerUri);

        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(jHipsterProperties.getSecurity().getOauth2().getAudience());
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

        jwtDecoder.setJwtValidator(withAudience);
        jwtDecoder.setClaimSetConverter(
            new CustomClaimConverter(clientRegistrationRepository.findByRegistrationId("oidc"), restTemplateBuilder.build())
        );
        return jwtDecoder;
    }

    @Bean
    public AuthenticationSuccessHandler oauth2LoginSuccessHandler() {
        log.debug("==start oauth2LoginSuccessHandler");
        return (request, response, authentication) -> {
            // 在这里，我们将用户的登录状态存储在一个session属性中
            request.getSession().setAttribute("loginSuccess", true);
            // 重定向回首页
            response.sendRedirect("/");
        };
    }
}
