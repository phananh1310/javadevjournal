package com.hust.sercurity.dto;

import com.hust.sercurity.entity.AppUserRole;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
public class UserLoginResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String telephone;
}
