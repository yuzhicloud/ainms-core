package com.yuzhi.ainms.core.security.oauth2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.util.Assert;
import org.springframework.security.oauth2.jwt.JwtClaimNames;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CustomJwtDecoder implements JwtDecoder {
    private final Logger log = LoggerFactory.getLogger(CustomJwtDecoder.class);
    private final NimbusJwtDecoder jwtDecoder;
    private final String expectedAudience;

    public CustomJwtDecoder(String jwkSetUri, String expectedAudience) {
        this.jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
        this.expectedAudience = expectedAudience;
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        Jwt jwt = this.jwtDecoder.decode(token);
/*
        List<JwtClaimValidator<?>> validators = Arrays.asList(
            new JwtTimestampValidator(),
            new JwtClaimValidator<List<String>>(JwtClaimsSet::getAudience, audiences -> audiences.contains(expectedAudience))
        );

        for (JwtClaimValidator<?> validator : validators) {
            try {
                validator.validate(jwt);
            } catch (JwtValidationException e) {
                throw new JwtException("JWT validation failed", e);
            }
        }
*/
        log.debug("custom aud validator");
        List<String> audiences = jwt.getClaimAsStringList(JwtClaimNames.AUD);
        if (audiences == null || !audiences.contains(expectedAudience)) {
            throw new JwtValidationException("Invalid audience",null);
        }

        return jwt;
    }
}
