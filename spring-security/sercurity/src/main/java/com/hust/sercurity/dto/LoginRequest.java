package com.hust.sercurity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    String username;
    String password;
}
