package com.yuzhi.ainms.core.security.oauth2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.DefaultOAuth2AccessTokenResponseMapConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.util.*;
import java.util.stream.Collectors;


public class CustomTokenResponseConverter implements
    Converter<Map<String, Object>, OAuth2AccessTokenResponse> {

    private final Logger log = LoggerFactory.getLogger(CustomTokenResponseConverter.class);

    @Override
    public OAuth2AccessTokenResponse convert(Map<String, Object> tokenResponseParameters) {
        try {
            log.debug("Converting token response: {}", tokenResponseParameters);

            String accessToken = (String) tokenResponseParameters.get("access_token");
            long expiresIn = ((Number) tokenResponseParameters.get("expires_in")).longValue();  // 安全地处理可能为Integer或Long的数值
            String refreshToken = (String) tokenResponseParameters.get("refresh_token");
            String idToken = (String) tokenResponseParameters.get("id_token");
            log.debug("wow idToken is :{}", idToken);
            String scopeString = (String) tokenResponseParameters.get("scope");
            Set<String> scopes = scopeString != null ? Arrays.stream(scopeString.split("\\s+")).collect(Collectors.toSet()) : Collections.emptySet();

            Map<String, Object> additionalParameters = new HashMap<>();
            additionalParameters.put(OidcParameterNames.ID_TOKEN, idToken);
            return OAuth2AccessTokenResponse.withToken(accessToken)
                .tokenType(OAuth2AccessToken.TokenType.BEARER)
                .expiresIn(expiresIn)
                .scopes(scopes)
                .refreshToken(refreshToken)
                .additionalParameters(additionalParameters)
                .build();
        } catch (Exception e) {
            log.error("Failed to convert token response", e);
            throw new OAuth2AuthenticationException(new OAuth2Error("token_response_conversion_error"), "Failed to convert token response", e);
        }
    }

    private Set<String> parseScopes(String scope) {
        return scope != null ? Arrays.stream(scope.split(" ")).collect(Collectors.toSet()) : Collections.emptySet();
    }
}
