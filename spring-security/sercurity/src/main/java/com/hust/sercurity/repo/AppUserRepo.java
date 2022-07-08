package com.hust.sercurity.repo;

import com.hust.sercurity.entity.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface AppUserRepo extends MongoRepository<AppUser,String> {
    AppUser findByEmail (String email);
    AppUser findByUsername (String username);
    AppUser findByTelephone (String telephone);

    Boolean existsByEmail(String email);

    Optional<AppUser> findByEmailOrUsernameOrTelephone(String email, String username, String telephone);
}
