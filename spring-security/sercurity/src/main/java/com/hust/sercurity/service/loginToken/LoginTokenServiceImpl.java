package com.hust.sercurity.service.loginToken;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hust.sercurity.entity.AppUser;
import com.hust.sercurity.service.AppUserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
public class LoginTokenServiceImpl implements LoginTokenService {
    @Autowired
    Algorithm algorithm;
    @Autowired
    AppUserService userService;

    @Override
    public LoginToken refreshToken(HttpServletRequest request) throws BadCredentialsException, RuntimeException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")) {
            String refresh_token = authorizationHeader.substring("Bearer ".length());
            // same as previous
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(refresh_token);
            String username = decodedJWT.getSubject();
            // try to use access token to take a new pair of token
            if (decodedJWT.getClaim("roles").asArray(String.class)==null) {
                UserDetails user = userService.loadUserByUsername(username);
                LoginToken loginToken = this.createToken((AppUser) user);
                return loginToken;
            } else throw new RuntimeException("Invalid refresh token");

        }
        else {
            throw new RuntimeException("Refresh token is missing");
        }

    }

    @Override
    public LoginToken createToken(AppUser user) {
        String access_token = JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis()+LoginConfig.ACCESS_TOKEN_TIME))
                .withIssuer("phananhdev")
                .withClaim("roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        String refresh_token = JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis()+LoginConfig.REFRESH_TOKEN_TIME))
                .withIssuer("phananhdev")
                .sign(algorithm);
        LoginToken loginToken = new LoginToken();
        loginToken.setAccess_token(access_token);
        loginToken.setRefresh_token(refresh_token);

        return loginToken;
    }
}
