package com.hust.sercurity.dto;

import com.hust.sercurity.service.loginToken.LoginToken;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    LoginToken loginToken;
    UserLoginResponse user;
}
