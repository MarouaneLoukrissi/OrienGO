package com.example.oriengo.controller;

import com.example.oriengo.mapper.TokenMapper;
import com.example.oriengo.model.dto.TokenCreateDTO;
import com.example.oriengo.model.dto.TokenResponseDTO;
import com.example.oriengo.model.entity.Token;
import com.example.oriengo.payload.response.ApiResponse;
import com.example.oriengo.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
@Validated
public class TokenController {

    private final TokenService tokenService;
    private final TokenMapper tokenMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TokenResponseDTO>>> getTokens() {
        List<Token> tokens = tokenService.getTokens();
        List<TokenResponseDTO> tokenResps = tokenMapper.toDTO(tokens);
        ApiResponse<List<TokenResponseDTO>> response = ApiResponse.<List<TokenResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Tokens fetched successfully")
                .data(tokenResps)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TokenResponseDTO>> getTokenById(@PathVariable Long id) {
        Token token = tokenService.getTokenById(id);
        TokenResponseDTO tokenResp = tokenMapper.toDTO(token);
        ApiResponse<TokenResponseDTO> response = ApiResponse.<TokenResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Token fetched successfully")
                .data(tokenResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TokenResponseDTO>> createToken(@Valid @RequestBody TokenCreateDTO tokenInfo) {
        Token token = tokenService.createToken(tokenInfo);
        TokenResponseDTO tokenResp = tokenMapper.toDTO(token);
        ApiResponse<TokenResponseDTO> response = ApiResponse.<TokenResponseDTO>builder()
                .code("SUCCESS")
                .status(201)
                .message("Token created successfully")
                .data(tokenResp)
                .build();
        URI location = URI.create("/api/token/" + token.getId());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TokenResponseDTO>> updateToken(@PathVariable Long id, @Valid @RequestBody TokenCreateDTO tokenInfo) {
        Token token = tokenService.updateToken(id, tokenInfo);
        TokenResponseDTO tokenResp = tokenMapper.toDTO(token);
        ApiResponse<TokenResponseDTO> response = ApiResponse.<TokenResponseDTO>builder()
                .code("TOKEN_UPDATED")
                .status(200)
                .message("Token updated successfully")
                .data(tokenResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteToken(@PathVariable Long id) {
        tokenService.deleteToken(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code("TOKEN_DELETED")
                .status(204)
                .message("Token deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<TokenResponseDTO>>> getTokensByUserId(@PathVariable Long userId) {
        List<Token> tokens = tokenService.getTokensByUserId(userId);
        List<TokenResponseDTO> tokenResps = tokenMapper.toDTO(tokens);
        ApiResponse<List<TokenResponseDTO>> response = ApiResponse.<List<TokenResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Tokens fetched successfully for user")
                .data(tokenResps)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validUser/{userId}")
    public ResponseEntity<ApiResponse<List<TokenResponseDTO>>> getValidatedTokensByUserId(@PathVariable Long userId) {
        List<Token> tokens = tokenService.getValidTokensByUserId(userId);
        List<TokenResponseDTO> tokenResps = tokenMapper.toDTO(tokens);
        ApiResponse<List<TokenResponseDTO>> response = ApiResponse.<List<TokenResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Valid tokens fetched successfully for user")
                .data(tokenResps)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tk/{token}")
    public ResponseEntity<ApiResponse<TokenResponseDTO>> getTokenObjByToken(@PathVariable String token) {
        Token tokenObj = tokenService.getTokenObjByToken(token);
        TokenResponseDTO tokenResp = tokenMapper.toDTO(tokenObj);
        ApiResponse<TokenResponseDTO> response = ApiResponse.<TokenResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Token object fetched successfully by token string")
                .data(tokenResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/tokens")
    public ResponseEntity<ApiResponse<List<TokenResponseDTO>>> createTokens(@Valid @RequestBody List<@Valid Token> tokens) {
        List<Token> tokensObj = tokenService.createTokens(tokens);
        List<TokenResponseDTO> tokenResps = tokenMapper.toDTO(tokensObj);
        ApiResponse<List<TokenResponseDTO>> response = ApiResponse.<List<TokenResponseDTO>>builder()
                .code("SUCCESS")
                .status(201)
                .message("Tokens created successfully")
                .data(tokenResps)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
