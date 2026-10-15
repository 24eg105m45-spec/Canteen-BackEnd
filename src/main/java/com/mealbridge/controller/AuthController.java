package com.mealbridge.controller;
import com.mealbridge.dto.AuthRequest;
import com.mealbridge.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AccountService service;
    public AuthController(AccountService service){this.service=service;}
    @PostMapping("/register") @ResponseStatus(HttpStatus.CREATED)
    public Map<String,Object> register(@Valid @RequestBody AuthRequest req){return service.register(req);}
    @PostMapping("/login")
    public Map<String,Object> login(@Valid @RequestBody AuthRequest req){return service.login(req);}
}