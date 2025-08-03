package com.example.oriengo.controller;

import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
public class HelloController {

    private final MessageSource messageSource;

    public HelloController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @GetMapping("/ok")
    public String helloWorld(){
        return "hello world";
    }

    @GetMapping("/welcome-msg")
    public String welcomeMessage(Locale locale) { // Automatically injects that value into the method parameter:
        String username = "Marouane";
        int newMessages = 5;
        int tasks = 3;

        Object[] args = { username, newMessages, tasks };

        return messageSource.getMessage("home.title", args, locale);
    }
}
