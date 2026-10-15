package com.mealbridge.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "food_donations")
public class FoodDonation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, length=160) private String foodName;
    @Column(nullable=false, length=80) private String category;
    @Column(nullable=false) private Integer quantity;
    @Column(nullable=false, length=30) private String unit;
    @Column(nullable=false) private LocalDateTime pickupDeadline;
    @Column(nullable=false, length=255) private String pickupAddress;
    @Column(length=1000) private String notes;
    @Column(nullable=false, length=20) private String status = "AVAILABLE";
    @ManyToOne(fetch=FetchType.EAGER, optional=false) @JoinColumn(name="donor_id", nullable=false)
    private Account donor;
    @Column(nullable=false) private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getFoodName(){return foodName;} public void setFoodName(String v){foodName=v;}
    public String getCategory(){return category;} public void setCategory(String v){category=v;}
    public Integer getQuantity(){return quantity;} public void setQuantity(Integer v){quantity=v;}
    public String getUnit(){return unit;} public void setUnit(String v){unit=v;}
    public LocalDateTime getPickupDeadline(){return pickupDeadline;} public void setPickupDeadline(LocalDateTime v){pickupDeadline=v;}
    public String getPickupAddress(){return pickupAddress;} public void setPickupAddress(String v){pickupAddress=v;}
    public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public Account getDonor(){return donor;} public void setDonor(Account v){donor=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
}