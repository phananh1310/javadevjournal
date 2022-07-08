package com.hust.sercurity.service.confirmation_token;

import com.hust.sercurity.entity.AppUser;
import com.hust.sercurity.entity.ConfirmationToken;

import java.util.Optional;

public interface ConfirmationServiceInterface {
    ConfirmationToken createConformationToken(AppUser userEntity);

    void updateConfirmedAt(ConfirmationToken token);

    Optional<ConfirmationToken> findConfirmationToken(String token);
}
