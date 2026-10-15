package com.mealbridge.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
public class AuthRequest {
    @Email @NotBlank private String email;
    @NotBlank private String password;
    private String name;
    private String organization;
    private String phone;
    private String role;
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getPassword(){return password;} public void setPassword(String v){password=v;}
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getOrganization(){return organization;} public void setOrganization(String v){organization=v;}
    public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
    public String getRole(){return role;} public void setRole(String v){role=v;}
}