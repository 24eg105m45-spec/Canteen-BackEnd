package com.mealbridge.service;

import com.mealbridge.dto.AuthRequest;
import com.mealbridge.model.Account;
import com.mealbridge.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class AccountService {
    private final AccountRepository accounts;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    @Value("${app.admin.email:admin@mealbridge.local}") private String adminEmail;
    @Value("${app.admin.password:ChangeMe123!}") private String adminPassword;

    public AccountService(AccountRepository accounts){this.accounts=accounts;}

    @Transactional
    public Map<String,Object> register(AuthRequest req) {
        String role=normalizeRole(req.getRole());
        if ("ADMIN".equals(role)) throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Admin accounts cannot be created through public registration.");
        String email=req.getEmail().trim().toLowerCase(Locale.ROOT);
        if(accounts.existsByEmailIgnoreCase(email)) throw new ResponseStatusException(HttpStatus.CONFLICT,"An account with this email already exists.");
        if(req.getPassword().length()<8) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Password must contain at least 8 characters.");
        if(req.getName()==null||req.getName().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Name is required.");
        Account a=new Account(); a.setName(req.getName().trim());a.setOrganization(req.getOrganization());a.setPhone(req.getPhone());a.setEmail(email);a.setPasswordHash(encoder.encode(req.getPassword()));a.setRole(role);a.setStatus("ACTIVE");
        a=accounts.save(a);
        return Map.of("message","Registration successful","user",publicUser(a));
    }

    public Map<String,Object> login(AuthRequest req) {
        String email=req.getEmail().trim().toLowerCase(Locale.ROOT);
        if(email.equalsIgnoreCase(adminEmail) && req.getPassword().equals(adminPassword)) {
            return Map.of("message","Admin login successful","user",Map.of("id",0,"name","Administrator","email",adminEmail,"role","ADMIN","status","ACTIVE"));
        }
        Account a=accounts.findByEmailIgnoreCase(email).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid email or password."));
        if(!encoder.matches(req.getPassword(),a.getPasswordHash())) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid email or password.");
        if(!"ACTIVE".equals(a.getStatus())) throw new ResponseStatusException(HttpStatus.FORBIDDEN,"This account is not active.");
        return Map.of("message","Login successful","user",publicUser(a));
    }

    public List<Map<String,Object>> listUsers() {return accounts.findAll().stream().map(this::publicUser).toList();}
    public Map<String,Object> updateStatus(Long id,String status) {
        String s=status==null?"":status.toUpperCase(Locale.ROOT);
        if(!List.of("ACTIVE","SUSPENDED","PENDING").contains(s)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Status must be ACTIVE, SUSPENDED, or PENDING.");
        Account a=accounts.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Account not found."));
        a.setStatus(s);return publicUser(accounts.save(a));
    }
    public Account requireAccount(Long id,String expectedRole) {
        Account a=accounts.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Account not found."));
        if(expectedRole!=null&&!a.getRole().equals(expectedRole))throw new ResponseStatusException(HttpStatus.FORBIDDEN,"This account does not have the required role.");
        return a;
    }
    public Map<String,Object> publicUser(Account a) {
        return Map.of("id",a.getId(),"name",a.getName(),"organization",a.getOrganization()==null?"":a.getOrganization(),"email",a.getEmail(),"phone",a.getPhone()==null?"":a.getPhone(),"role",a.getRole(),"status",a.getStatus(),"createdAt",a.getCreatedAt().toString());
    }
    private String normalizeRole(String role) {
        if(role==null)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Role is required.");
        String r=role.trim().toUpperCase(Locale.ROOT);
        if(r.equals("DONOR"))return "DONOR";
        if(r.equals("ORGANIZER"))return "ORGANIZER";
        if(r.equals("ADMIN"))return "ADMIN";
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Role must be Donor or Organizer.");
    }
}