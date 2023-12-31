package com.example.autodeal.user.repository;

import com.example.autodeal.user.model.UserModel;
import com.example.autodeal.user.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);

    Optional<VerificationToken> findByUserModel(UserModel user);

    Optional<VerificationToken> findByUserModelEmail(String token);


}

