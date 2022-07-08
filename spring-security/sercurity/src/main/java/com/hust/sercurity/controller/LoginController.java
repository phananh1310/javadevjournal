package com.hust.sercurity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.hust.sercurity.service.AppUserService;
import com.hust.sercurity.service.loginToken.LoginToken;
import com.hust.sercurity.service.loginToken.LoginTokenService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(value = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginController {
    LoginTokenService loginTokenService;

    AuthenticationManager authenticationManager;

    AppUserService userService;
    private OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            LoginToken loginToken = loginTokenService.refreshToken(request);
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            new ObjectMapper().writeValue(response.getOutputStream(),loginToken);
        } catch (Exception e) {
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            new ObjectMapper().writeValue(response.getOutputStream(),e.getMessage());
        }
    }

    @GetMapping("/oauth2/loginSuccess")
    public String getLoginInfo(OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName());

        String userInfoEndpointUri = client.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUri();

        if (!StringUtils.isEmpty(userInfoEndpointUri)) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken()
                    .getTokenValue());
            HttpEntity entity = new HttpEntity("", headers);
            ResponseEntity<Map> response = restTemplate
                    .exchange(userInfoEndpointUri, HttpMethod.GET, entity, Map.class);
            Map userAttributes = response.getBody();
            String email = (String) userAttributes.get("email");

            if (userService.existsByEmail(email)){
                //return new Gson().toJson(loginTokenService.createToken(userService.findByEmail(email)));
                return userAttributes.toString();
            } else {
                // create new user

                return userAttributes.toString();
            }
        }

        return "some thing bruh";
    }

}
