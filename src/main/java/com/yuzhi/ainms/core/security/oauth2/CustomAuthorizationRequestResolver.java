package com.yuzhi.ainms.core.security.oauth2;

import com.yuzhi.ainms.core.web.rest.ProvinceResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import jakarta.servlet.http.HttpServletRequest;

public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {
    private final Logger log = LoggerFactory.getLogger(CustomAuthorizationRequestResolver.class);
    private final OAuth2AuthorizationRequestResolver defaultResolver;

    public CustomAuthorizationRequestResolver(ClientRegistrationRepository repo, String authorizationRequestBaseUri) {
        log.debug("CustomAuthorizationRequestResolver constructor");
        this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(repo, authorizationRequestBaseUri);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        log.debug("CustomAuthorizationRequestResolver.resolve(request called)");
        //OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request);
        //return customizeAuthorizationRequest(authorizationRequest);
        try {
            OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request);
            return customizeAuthorizationRequest(authorizationRequest);
        } catch (Exception e) {
            log.error("1:Error resolving OAuth2 Authorization Request: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        log.debug("CustomAuthorizationRequestResolver.resolve(request,clientRegistrationId) called");
        try {
            OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request, clientRegistrationId);
            return customizeAuthorizationRequest(authorizationRequest);
        } catch (Exception e) {
            log.error("2:Error resolving OAuth2 Authorization Request: {}", e.getMessage());
            return null;
        }
    }

    private OAuth2AuthorizationRequest customizeAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest) {
        if (authorizationRequest == null) {
            log.debug("Authorization Request is null");
            return null;
        }
        log.debug("Cust Original Request: {}", authorizationRequest);
        OAuth2AuthorizationRequest newRequest = OAuth2AuthorizationRequest.from(authorizationRequest)
            //.state(authorizationRequest.getState()) // 保留原始 state
            .scope("openid", "offline_access")
            .state("test")
            .build();

        log.debug("New Request: {}", newRequest);

        return newRequest;
    }
}
