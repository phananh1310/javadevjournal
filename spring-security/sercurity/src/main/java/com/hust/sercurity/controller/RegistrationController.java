package com.hust.sercurity.controller;

import com.hust.sercurity.dto.AppUserRegisterRequest;
import com.hust.sercurity.dto.AppUserRegisterResponse;
import com.hust.sercurity.exception.UserAlreadyExistException;
import com.hust.sercurity.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/register", produces = MediaType.APPLICATION_JSON_VALUE)
public class RegistrationController {

    private final AppUserService appUserService;

    @PostMapping
    public ResponseEntity<?> userRegistration(final @Valid @RequestBody AppUserRegisterRequest userData){
        try {
            AppUserRegisterResponse appUserRegisterResponse = appUserService.register(userData);
            return ResponseEntity.status(HttpStatus.CREATED).body(appUserRegisterResponse);
        }catch (UserAlreadyExistException e){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }

    @GetMapping(path = "confirm")
    public ResponseEntity<?> confirm(@RequestParam("token") String token){
        try {
            appUserService.confirmToken(token);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Confirmed");
        } catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }


}
