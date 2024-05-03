package com.yuzhi.ainms.core.security.oauth2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;


import java.time.Duration;
import java.time.Instant;

public class CustomTimestampValidator implements OAuth2TokenValidator<Jwt> {

    private final Logger log = LoggerFactory.getLogger(CustomTimestampValidator.class);
    private static final Duration ALLOWED_SKEW = Duration.ofMinutes(1); // Allowable clock skew

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        log.debug("CustomTimestampValidator invoked::");
        Instant now = Instant.now();
        Instant issuedAt = jwt.getIssuedAt();
        
        log.debug(("CustomTimestampValidator JWT 'iat' claim: " + jwt.getIssuedAt()));
        // Check if the 'iat' is in the future, considering allowed skew
        if (issuedAt != null && now.plus(ALLOWED_SKEW).isBefore(issuedAt)) {
            return OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "The token was issued in the future", null));
        }


        return OAuth2TokenValidatorResult.success();
    }
}