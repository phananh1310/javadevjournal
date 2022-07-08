package com.hust.sercurity.filter;

import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.hust.sercurity.dto.LoginRequest;
import com.hust.sercurity.dto.LoginResponse;
import com.hust.sercurity.dto.UserLoginResponse;
import com.hust.sercurity.entity.AppUser;
import com.hust.sercurity.service.loginToken.LoginToken;
import com.hust.sercurity.service.loginToken.LoginTokenService;
import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

import static javax.print.attribute.standard.ReferenceUriSchemesSupported.HTTP;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final LoginTokenService loginTokenService;
    private final Algorithm algorithm;

    public AuthenticationFilter(AuthenticationManager authenticationManager, LoginTokenService loginTokenService, Algorithm algorithm) {
        this.authenticationManager = authenticationManager;
        this.loginTokenService = loginTokenService;
        this.algorithm = algorithm;
    }



    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String reqString;
        try {
            reqString = request.getReader().lines().collect(Collectors.joining());
            log.info(reqString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        LoginRequest loginRequest = new Gson().fromJson(reqString, LoginRequest.class);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        return authenticationManager.authenticate(authenticationToken);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        AppUser user = (AppUser) authResult.getPrincipal();
        UserLoginResponse userLoginResponse = new UserLoginResponse();
        LoginToken loginToken = loginTokenService.createToken(user);
        BeanUtils.copyProperties(user,userLoginResponse);
        LoginResponse loginResponse = new LoginResponse(loginToken,userLoginResponse);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),loginResponse);
    }

}
