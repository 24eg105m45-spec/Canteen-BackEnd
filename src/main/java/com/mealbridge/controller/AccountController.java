package com.mealbridge.controller;
import com.mealbridge.service.AccountService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/users")
public class AccountController {
    private final AccountService service;
    public AccountController(AccountService service){this.service=service;}
    @GetMapping public List<Map<String,Object>> all(){return service.listUsers();}
    @PatchMapping("/{id}/status") public Map<String,Object> status(@PathVariable Long id,@RequestBody Map<String,String> body){return service.updateStatus(id,body.get("status"));}
}