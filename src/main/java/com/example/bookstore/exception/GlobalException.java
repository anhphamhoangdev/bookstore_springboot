package com.example.bookstore.exception;

import jakarta.persistence.EntityNotFoundException;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.thymeleaf.exceptions.TemplateInputException;
import org.thymeleaf.exceptions.TemplateOutputException;


@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFoundException(EntityNotFoundException ex) {
        return "redirect:/entity-not-found";
    }


    @ExceptionHandler(AuthenticationException.class)
    public String handleAuthenticationException(AuthenticationException ex) {
        return "redirect:/login";
    }


}
