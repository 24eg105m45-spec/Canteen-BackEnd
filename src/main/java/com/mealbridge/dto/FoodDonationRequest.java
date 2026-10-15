package com.mealbridge.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
public class FoodDonationRequest {
    @NotBlank @Size(max=160) private String foodName;
    @NotBlank @Size(max=80) private String category;
    @NotNull @Min(1) private Integer quantity;
    @NotBlank @Size(max=30) private String unit;
    @NotNull private LocalDateTime pickupDeadline;
    @NotBlank @Size(max=255) private String pickupAddress;
    @Size(max=1000) private String notes;
    public String getFoodName(){return foodName;} public void setFoodName(String v){foodName=v;}
    public String getCategory(){return category;} public void setCategory(String v){category=v;}
    public Integer getQuantity(){return quantity;} public void setQuantity(Integer v){quantity=v;}
    public String getUnit(){return unit;} public void setUnit(String v){unit=v;}
    public LocalDateTime getPickupDeadline(){return pickupDeadline;} public void setPickupDeadline(LocalDateTime v){pickupDeadline=v;}
    public String getPickupAddress(){return pickupAddress;} public void setPickupAddress(String v){pickupAddress=v;}
    public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
}