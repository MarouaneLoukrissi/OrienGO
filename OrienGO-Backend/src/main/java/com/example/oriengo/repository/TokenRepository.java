package com.example.oriengo.repository;

import com.example.oriengo.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByTokenValue(String tokenValue);
    List<Token> findByUserIdAndExpiredFalseAndRevokedFalse(Long userId);
    List<Token> findAllByUserId(Long userId);
}
