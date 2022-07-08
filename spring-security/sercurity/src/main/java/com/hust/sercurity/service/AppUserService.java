package com.hust.sercurity.service;

import com.hust.sercurity.dto.AppUserRegisterRequest;
import com.hust.sercurity.dto.AppUserRegisterResponse;
import com.hust.sercurity.entity.AppUser;
import com.hust.sercurity.exception.UserAlreadyExistException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AppUserService {
    AppUserRegisterResponse register(AppUserRegisterRequest user) throws UserAlreadyExistException;
    void confirmToken(String token) throws IllegalStateException;

    void sendConfirmToken(String token, String to);

    void enableAppUser(String accountName);

    Boolean existsByEmail(String email);

    AppUser findByEmail(String email);

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
