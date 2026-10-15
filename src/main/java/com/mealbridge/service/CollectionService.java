package com.mealbridge.service;

import com.mealbridge.dto.CollectionRequestDto;
import com.mealbridge.model.Account;
import com.mealbridge.model.CollectionRequest;
import com.mealbridge.model.FoodDonation;
import com.mealbridge.repository.CollectionRequestRepository;
import com.mealbridge.repository.FoodDonationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
public class CollectionService {
    private final CollectionRequestRepository requests;
    private final FoodDonationRepository donations;
    private final AccountService accounts;
    public CollectionService(CollectionRequestRepository requests,FoodDonationRepository donations,AccountService accounts){this.requests=requests;this.donations=donations;this.accounts=accounts;}
    public List<CollectionRequest> all(){return requests.findAllByOrderByCreatedAtDesc();}
    public List<CollectionRequest> byOrganizer(Long organizerId){accounts.requireAccount(organizerId,"ORGANIZER");return requests.findByOrganizerIdOrderByCreatedAtDesc(organizerId);}
    @Transactional
    public CollectionRequest create(Long donationId,Long organizerId,CollectionRequestDto dto){
        Account organizer=accounts.requireAccount(organizerId,"ORGANIZER");
        FoodDonation donation=donations.findById(donationId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Food listing not found."));
        if(!"AVAILABLE".equals(donation.getStatus()))throw new ResponseStatusException(HttpStatus.CONFLICT,"This food listing is no longer available.");
        if(donation.getPickupDeadline().isBefore(LocalDateTime.now()))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"The pickup deadline has passed.");
        if(requests.existsByDonationIdAndOrganizerId(donationId,organizerId))throw new ResponseStatusException(HttpStatus.CONFLICT,"You already requested this listing.");
        CollectionRequest r=new CollectionRequest();r.setDonation(donation);r.setOrganizer(organizer);r.setMessage(dto==null?null:dto.getMessage());r.setStatus("REQUESTED");
        return requests.save(r);
    }
    @Transactional
    public CollectionRequest updateStatus(Long id,String status){
        String s=status==null?"":status.toUpperCase(Locale.ROOT);
        if(!List.of("REQUESTED","APPROVED","REJECTED","COLLECTED","CANCELLED").contains(s))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid collection request status.");
        CollectionRequest r=requests.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Collection request not found."));
        r.setStatus(s);r.setUpdatedAt(LocalDateTime.now());
        if("APPROVED".equals(s))r.getDonation().setStatus("RESERVED");
        if("COLLECTED".equals(s))r.getDonation().setStatus("COLLECTED");
        if("REJECTED".equals(s)||"CANCELLED".equals(s))r.getDonation().setStatus("AVAILABLE");
        return requests.save(r);
    }
}