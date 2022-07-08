package com.hust.sercurity.service.loginToken;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginToken {
    private String type = "Bearer";
    private String access_token;
    private String refresh_token;
}
