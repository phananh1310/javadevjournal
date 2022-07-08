package com.hust.sercurity.service.loginToken;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.auth0.jwt.algorithms.Algorithm;

@Configuration
public class LoginConfig {
    private static final String APP_SECRET = "Travelwala Group";
    public static final int ACCESS_TOKEN_TIME = 10*60*1000;
    public static final int REFRESH_TOKEN_TIME = 30*60*1000;

    @Bean
    public Algorithm algorithm() {
        return Algorithm.HMAC256(APP_SECRET.getBytes());
    }
}
