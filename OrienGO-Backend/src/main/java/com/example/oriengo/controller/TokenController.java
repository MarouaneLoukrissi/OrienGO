package com.example.oriengo.controller;

import com.example.oriengo.model.dto.TokenCreateDTO;
import com.example.oriengo.model.entity.Token;
import com.example.oriengo.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @GetMapping
    public ResponseEntity<List<Token>> getTokens(){
        List<Token> tokens = tokenService.getTokens();
        return ResponseEntity.ok(tokens);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Token> getTokenById(@PathVariable Long id){
        Token token = tokenService.getTokenById(id);
        return ResponseEntity.ok(token);
    }

    @PostMapping
    public ResponseEntity<Token> createToken(@Validated @RequestBody TokenCreateDTO tokenInfo){
        Token token = tokenService.createToken(tokenInfo);
        URI location = URI.create("/api/token/" + token.getId());
        return ResponseEntity.created(location).body(token);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Token> updateToken(@PathVariable Long id, @Validated @RequestBody TokenCreateDTO tokenInfo){
        Token token = tokenService.updateToken(id, tokenInfo);
        return ResponseEntity.ok(token);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteToken(@PathVariable Long id){
        tokenService.deleteToken(id);
        return ResponseEntity.noContent().build();
    }



    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Token>> getTokensByUserId(@PathVariable Long userId){
        List<Token> tokens = tokenService.getTokensByUserId(userId);
        return ResponseEntity.ok(tokens);
    }

    @GetMapping("/validUser/{userId}")
    public ResponseEntity<List<Token>> getValidatedTokensByUserId(@PathVariable Long userId){
        List<Token> tokens = tokenService.getValidTokensByUserId(userId);
        return ResponseEntity.ok(tokens);
    }

    @GetMapping("/tk/{token}")
    public ResponseEntity<Token> getTokenObjByToken(@PathVariable String token){
        Token tokenObj = tokenService.getTokenObjByToken(token);
        return ResponseEntity.ok(tokenObj);
    }

    @PostMapping("/tokens")
    public ResponseEntity<List<Token>> createTokens(@Valid @RequestBody List<@Valid Token> tokens){
        List<Token> tokensObj = tokenService.createTokens(tokens);
//        URI location = URI.create("/api/token/" + IntStream.range(0, tokensObj.size())
//                .mapToObj(i -> (i + 1) + "token=" + tokensObj.get(i).getId())
//                .collect(Collectors.joining("&")));
        return ResponseEntity.status(HttpStatus.CREATED).body(tokensObj); //ResponseEntity.created(location).body(tokensObj);
    }
}
