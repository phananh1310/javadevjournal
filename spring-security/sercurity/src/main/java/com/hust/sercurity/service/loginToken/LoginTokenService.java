package com.hust.sercurity.service.loginToken;

import com.hust.sercurity.entity.AppUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface LoginTokenService {
    LoginToken refreshToken(HttpServletRequest request) throws IOException;

    LoginToken createToken(AppUser user);
}
