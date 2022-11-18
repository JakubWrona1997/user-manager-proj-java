package com.usermanagerproj.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "user-manager")
public class ConfigProperties {
    private String jwtSecret;
    private int jwtExpirationMs;
    private String issuer;
}
