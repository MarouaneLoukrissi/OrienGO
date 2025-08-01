package com.example.oriengo.service;

import com.example.oriengo.exception.PathVarException;
import com.example.oriengo.exception.Token.TokenCreationException;
import com.example.oriengo.exception.Token.TokenGetException;
import com.example.oriengo.exception.Token.TokenUpdateException;
import com.example.oriengo.exception.user.UserDeleteException;
import com.example.oriengo.mapper.TokenMapper;
import com.example.oriengo.model.dto.TokenCreateDTO;
import com.example.oriengo.model.entity.Token;
import com.example.oriengo.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;
    private final TokenMapper tokenMapper;

    public List<Token> getTokens() {
        try {
            return tokenRepository.findAll();
        } catch (Exception e){
            throw new TokenGetException(HttpStatus.NOT_FOUND, "No Token Object found");
        }
    }
    public Token getTokenById(Long id) {
        if (id == null) {
            throw new PathVarException(HttpStatus.BAD_REQUEST, "Token Object ID cannot be empty");
        }
        return tokenRepository.findById(id)
                .orElseThrow(() -> new TokenGetException(HttpStatus.NOT_FOUND, "Token Object not found"));
    }
    public List<Token> getTokensByUserId(Long userId) {
        if (userId == null) {
            throw new PathVarException(HttpStatus.BAD_REQUEST, "User ID cannot be empty");
        }
        try {
            return tokenRepository.findAllByUserId(userId);
        } catch (Exception e){
            throw new TokenGetException(HttpStatus.BAD_REQUEST, "Invalid User Id: " + userId);
        }
    }
    public List<Token> getValidTokensByUserId(Long userId) {
        if (userId == null) {
            throw new PathVarException(HttpStatus.BAD_REQUEST, "User ID cannot be empty");
        }
        try {
            return tokenRepository.findByUserIdAndExpiredFalseAndRevokedFalse(userId);
        } catch (Exception e){
            throw new TokenGetException(HttpStatus.BAD_REQUEST, "Invalid User Id: " + userId);
        }
    }
    public Token getTokenObjByToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new PathVarException(HttpStatus.BAD_REQUEST, "Token cannot be empty");
        }
        try {
            Token tokenObj = tokenRepository.findByTokenValue(token);
            if(tokenObj!=null){
                return tokenObj;
            }else{
                throw new TokenGetException(HttpStatus.NOT_FOUND, "Token Object not found");
            }
        } catch (Exception e){
            throw new TokenGetException(HttpStatus.BAD_REQUEST, "Invalid Token: " + token);
        }
    }



    public Token createToken(TokenCreateDTO dto) {
        try {
            Token token = tokenMapper.toEntity(dto);
            Token tokenOutput = tokenRepository.save(token);
            log.info("Token object created with ID: {}", tokenOutput.getId());
            return tokenOutput;
        } catch (Exception e) {
            log.error("Error creating token object: {}", e.getMessage());
            throw new TokenCreationException("Failed to create token object");
        }
    }
    public List<Token> createTokens(List<Token> tokens) {
        try {
            List<Token> tokensOutput = tokenRepository.saveAll(tokens);
            log.info("Token objects created with IDs: {}", tokensOutput.stream()
                    .map(Token::getId)
                    .map(String::valueOf)
                    .collect(Collectors.joining(", ")));
            return tokensOutput;
        } catch (Exception e) {
            log.error("Error creating token objects: {}", e.getMessage());
            throw new TokenCreationException("Failed to create token objects");
        }
    }

    public Token updateToken(Long id, TokenCreateDTO dto) {
        if (id == null) {
            throw new PathVarException(HttpStatus.BAD_REQUEST, "Token Object ID cannot be empty");
        }
        try {
            Token token = tokenMapper.toEntity(dto);
            Token tokenChecked = getTokenById(id);
            token.setId(tokenChecked.getId());
            Token tokenOutput = tokenRepository.save(token);
            log.info("Token object updated with ID: {}", tokenOutput.getId());
            return tokenOutput;
        } catch (Exception e) {
            log.error("Error updating token object: {}", e.getMessage());
            throw new TokenUpdateException("Failed to update token object");
        }
    }

    public void deleteToken(Long id) {
        if (id == null){
            throw new PathVarException(HttpStatus.BAD_REQUEST, "Token Object ID cannot be empty");
        }
        try {
            Token token = getTokenById(id);
            tokenRepository.deleteById(token.getId());
            log.info("Token object deleted with ID: {}", token.getId());
        } catch (Exception e) {
            log.error("Error deleting token object: {}", e.getMessage());
            throw new UserDeleteException("Failed to update token object");
        }
    }
}
