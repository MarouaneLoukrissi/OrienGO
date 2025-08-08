package com.example.oriengo.service;

import com.example.oriengo.exception.custom.PathVarException;
import com.example.oriengo.exception.custom.Token.TokenCreationException;
import com.example.oriengo.exception.custom.Token.TokenDeleteException;
import com.example.oriengo.exception.custom.Token.TokenGetException;
import com.example.oriengo.exception.custom.Token.TokenUpdateException;
import com.example.oriengo.mapper.TokenMapper;
import com.example.oriengo.model.dto.TokenCreateDTO;
import com.example.oriengo.model.entity.Token;
import com.example.oriengo.model.entity.User;
import com.example.oriengo.repository.TokenRepository;
import com.example.oriengo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;
    private final TokenMapper tokenMapper;
    private final UserRepository userRepository;
    private final MessageSource messageSource;

    private String getMessage(String key, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, args, locale);
    }

    public List<Token> getTokens() {
        try {
            log.info("Fetching all tokens");
            List<Token> tokens = tokenRepository.findAll();
            log.info("Found {} tokens", tokens.size());
            return tokens;
        } catch (Exception e) {
            log.error("Error fetching tokens: {}", e.getMessage(), e);
            throw new TokenGetException(HttpStatus.NOT_FOUND, getMessage("token.not.found"));
        }
    }
    public Token getTokenById(Long id) {
        if (id == null) {
            log.warn("Token ID is null");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("token.id.empty"));
        }
        return tokenRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Token not found with ID: {}", id);
                    return new TokenGetException(HttpStatus.NOT_FOUND, getMessage("token.not.found"));
                });
    }
    public List<Token> getTokensByUserId(Long userId) {
        if (userId == null) {
            log.warn("User ID is null for fetching tokens");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("user.id.empty"));
        }
        try {
            List<Token> tokens = tokenRepository.findAllByUserId(userId);
            log.info("Found {} tokens for user ID {}", tokens.size(), userId);
            return tokens;
        } catch (Exception e) {
            log.error("Error fetching tokens for user ID {}: {}", userId, e.getMessage());
            throw new TokenGetException(HttpStatus.BAD_REQUEST, getMessage("token.user.invalid", userId));
        }
    }
    public List<Token> getValidTokensByUserId(Long userId) {
        if (userId == null) {
            log.warn("User ID is null for valid token fetch");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("user.id.empty"));
        }
        try {
            List<Token> tokens = tokenRepository.findByUserIdAndExpiredFalseAndRevokedFalse(userId);
            log.info("Found {} valid tokens for user ID {}", tokens.size(), userId);
            return tokens;
        } catch (Exception e) {
            log.error("Error fetching valid tokens for user ID {}: {}", userId, e.getMessage());
            throw new TokenGetException(HttpStatus.BAD_REQUEST, getMessage("token.user.invalid", userId));
        }
    }
    public Token getTokenObjByToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            log.warn("Token string is null or empty");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("token.value.empty"));
        }
        try {
            Token tokenObj = tokenRepository.findByTokenValue(token);
            if (tokenObj != null) {
                log.info("Token found by value");
                return tokenObj;
            } else {
                log.warn("Token not found for value: {}", token);
                throw new TokenGetException(HttpStatus.NOT_FOUND, getMessage("token.not.found"));
            }
        } catch (Exception e) {
            log.error("Error retrieving token by value {}: {}", token, e.getMessage());
            throw new TokenGetException(HttpStatus.BAD_REQUEST, getMessage("token.invalid", token));
        }
    }

    @Transactional
    public Token createToken(TokenCreateDTO dto) {
        if (dto == null) {
            log.warn("TokenCreateDTO is null");
            throw new TokenCreationException(HttpStatus.BAD_REQUEST, getMessage("token.dto.empty"));
        }
        try {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new TokenCreationException(HttpStatus.NOT_FOUND, getMessage("user.not.found")));

            Token token = tokenMapper.toEntity(dto);
            token.setUser(user);

            Token saved = tokenRepository.save(token);
            log.info("Token created with ID: {}", saved.getId());
            return saved;
        } catch (TokenCreationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error creating token: {}", e.getMessage(), e);
            throw new TokenCreationException(HttpStatus.BAD_REQUEST, getMessage("token.create.failed"));
        }
    }

    @Transactional
    public List<Token> createTokens(List<Token> tokens) {
        if (tokens == null || tokens.isEmpty()) {
            log.warn("Token list is null or empty");
            throw new TokenCreationException(HttpStatus.BAD_REQUEST, getMessage("token.list.empty"));
        }
        try {
            List<Token> savedTokens = tokenRepository.saveAll(tokens);
            log.info("Created {} token(s)", savedTokens.size());
            return savedTokens;
        } catch (Exception e) {
            log.error("Error creating tokens: {}", e.getMessage(), e);
            throw new TokenCreationException(HttpStatus.BAD_REQUEST, getMessage("token.create.failed"));
        }
    }

    @Transactional
    public Token updateToken(Long id, TokenCreateDTO dto) {
        if (id == null) {
            log.warn("Token ID is null for update");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("token.id.empty"));
        }
        if (dto == null) {
            log.warn("TokenCreateDTO is null for update");
            throw new TokenUpdateException(HttpStatus.BAD_REQUEST, getMessage("token.dto.empty"));
        }
        try {
            Token existing = getTokenById(id);

            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new TokenUpdateException(HttpStatus.NOT_FOUND, getMessage("user.not.found")));

            Token updated = tokenMapper.toEntity(dto);
            updated.setId(existing.getId());
            updated.setUser(user);

            Token saved = tokenRepository.save(updated);
            log.info("Token updated with ID: {}", saved.getId());
            return saved;
        } catch (TokenUpdateException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating token with ID {}: {}", id, e.getMessage(), e);
            throw new TokenUpdateException(HttpStatus.BAD_REQUEST, getMessage("token.update.failed"));
        }
    }

    @Transactional
    public void deleteToken(Long id) {
        if (id == null) {
            log.warn("Token ID is null for deletion");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("token.id.empty"));
        }
        try {
            Token token = getTokenById(id);
            tokenRepository.deleteById(token.getId());
            log.info("Token deleted with ID: {}", token.getId());
        } catch (Exception e) {
            log.error("Error deleting token with ID {}: {}", id, e.getMessage(), e);
            throw new TokenDeleteException(HttpStatus.BAD_REQUEST, getMessage("token.delete.failed"));
        }
    }
}
