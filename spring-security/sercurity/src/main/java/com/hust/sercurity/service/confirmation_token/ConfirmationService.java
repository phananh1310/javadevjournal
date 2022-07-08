package com.hust.sercurity.service.confirmation_token;

import com.hust.sercurity.entity.AppUser;
import com.hust.sercurity.entity.ConfirmationToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationService implements ConfirmationServiceInterface {
    private final ConfirmationTokenRepo confirmationTokenRepo;
    private final static int tokenExpiredMinutes = 15;

    @Override
    public ConfirmationToken createConformationToken(AppUser userEntity) {
        String token = java.util.UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(tokenExpiredMinutes),
                userEntity
        );
        confirmationTokenRepo.save(confirmationToken);
        return confirmationToken;
    }

    @Override
    public void updateConfirmedAt(ConfirmationToken token) {
        token.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepo.save(token);
    }

    @Override
    public Optional<ConfirmationToken> findConfirmationToken(String token) {
        return confirmationTokenRepo.findByToken(token);
    }

}
