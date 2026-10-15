package com.mealbridge.dto;
import jakarta.validation.constraints.Size;
public class CollectionRequestDto {
    @Size(max=500) private String message;
    public String getMessage(){return message;} public void setMessage(String v){message=v;}
}