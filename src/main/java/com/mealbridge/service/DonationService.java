package com.mealbridge.service;

import com.mealbridge.dto.FoodDonationRequest;
import com.mealbridge.model.Account;
import com.mealbridge.model.FoodDonation;
import com.mealbridge.repository.FoodDonationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
public class DonationService {
    private final FoodDonationRepository donations;
    private final AccountService accountService;
    public DonationService(FoodDonationRepository donations,AccountService accountService){this.donations=donations;this.accountService=accountService;}

    public List<FoodDonation> all(){return donations.findAll().stream().sorted((a,b)->b.getCreatedAt().compareTo(a.getCreatedAt())).toList();}
    public List<FoodDonation> available(){return donations.findByStatusIgnoreCaseOrderByCreatedAtDesc("AVAILABLE");}
    public List<FoodDonation> byDonor(Long donorId){accountService.requireAccount(donorId,"DONOR");return donations.findByDonorIdOrderByCreatedAtDesc(donorId);}

    @Transactional
    public FoodDonation create(Long donorId,FoodDonationRequest req){
        Account donor=accountService.requireAccount(donorId,"DONOR");
        if(req.getPickupDeadline().isBefore(LocalDateTime.now()))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Pickup deadline must be in the future.");
        FoodDonation d=new FoodDonation();d.setFoodName(req.getFoodName().trim());d.setCategory(req.getCategory().trim());d.setQuantity(req.getQuantity());d.setUnit(req.getUnit().trim());d.setPickupDeadline(req.getPickupDeadline());d.setPickupAddress(req.getPickupAddress().trim());d.setNotes(req.getNotes());d.setDonor(donor);d.setStatus("AVAILABLE");return donations.save(d);
    }

    @Transactional
    public FoodDonation updateStatus(Long id,String status,Long actingAccountId,boolean admin){
        String s=status==null?"":status.toUpperCase(Locale.ROOT);
        if(!List.of("AVAILABLE","RESERVED","COLLECTED","CANCELLED").contains(s))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid donation status.");
        FoodDonation d=donations.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Food listing not found."));
        if(!admin&&!d.getDonor().getId().equals(actingAccountId))throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You can only update your own listings.");
        d.setStatus(s);return donations.save(d);
    }
}