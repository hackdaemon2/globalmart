package com.globalmart.app.config;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@NoArgsConstructor
@ConfigurationProperties(prefix = "app")
public class AppConfiguration {

    @NestedConfigurationProperty
    private Jwt jwt;

    @Data
    public static class Jwt {
        private String signingKey;
        private String tokenExpiry;
        private String refreshTokenExpiry;
    }

}
