package com.moyu.sc.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "apps")
public class AppProperties {

    @Setter
    @Getter
    Jwt jwt = new Jwt();

    @Getter
    @Setter
    public static class Jwt {
        private Long accessTokenExpireTime = 60 * 1000L;
        private Long accessRefreshTokenExpireTime = 7 * 24 * 3600 * 1000L;
        private String header = "Authorization";
        private String prefix = "Bearer ";
    }
}
