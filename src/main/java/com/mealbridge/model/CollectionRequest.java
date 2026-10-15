package com.mealbridge.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "collection_requests", uniqueConstraints = @UniqueConstraint(name="uk_request_donation_organizer", columnNames={"donation_id","organizer_id"}))
public class CollectionRequest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.EAGER, optional=false) @JoinColumn(name="donation_id", nullable=false)
    private FoodDonation donation;
    @ManyToOne(fetch=FetchType.EAGER, optional=false) @JoinColumn(name="organizer_id", nullable=false)
    private Account organizer;
    @Column(nullable=false, length=20) private String status = "REQUESTED";
    @Column(length=500) private String message;
    @Column(nullable=false) private LocalDateTime createdAt = LocalDateTime.now();
    @Column(nullable=false) private LocalDateTime updatedAt = LocalDateTime.now();

    public Long getId(){return id;} public void setId(Long v){id=v;}
    public FoodDonation getDonation(){return donation;} public void setDonation(FoodDonation v){donation=v;}
    public Account getOrganizer(){return organizer;} public void setOrganizer(Account v){organizer=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public String getMessage(){return message;} public void setMessage(String v){message=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
    public LocalDateTime getUpdatedAt(){return updatedAt;} public void setUpdatedAt(LocalDateTime v){updatedAt=v;}
}