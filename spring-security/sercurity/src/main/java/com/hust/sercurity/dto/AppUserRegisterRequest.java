package com.hust.sercurity.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Getter
@Setter
public class AppUserRegisterRequest implements Serializable {
    @NotEmpty(message = "First name can not be empty")
    private String firstName;

    @NotEmpty(message = "Last name can not be empty")
    private String lastName;

    @NotEmpty(message = "Email can not be empty")
    @Email(message = "Please provide a valid email id")
    private String email;

    @NotEmpty(message = "Password can not be empty")
    private String password;

    @NotEmpty(message = "Username can not be empty")
    private String username;

    @NotEmpty(message = "Telephone can not be empty")
    // TO-DO validate phone
    private String telephone;
}
