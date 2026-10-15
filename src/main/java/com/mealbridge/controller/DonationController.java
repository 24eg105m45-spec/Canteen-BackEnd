package com.mealbridge.controller;
import com.mealbridge.dto.FoodDonationRequest;
import com.mealbridge.model.FoodDonation;
import com.mealbridge.service.DonationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/donations")
public class DonationController {
    private final DonationService service;
    public DonationController(DonationService service){this.service=service;}
    @GetMapping public List<FoodDonation> all(){return service.all();}
    @GetMapping("/available") public List<FoodDonation> available(){return service.available();}
    @GetMapping("/donor/{donorId}") public List<FoodDonation> byDonor(@PathVariable Long donorId){return service.byDonor(donorId);}
    @PostMapping("/donor/{donorId}") @ResponseStatus(HttpStatus.CREATED)
    public FoodDonation create(@PathVariable Long donorId,@Valid @RequestBody FoodDonationRequest req){return service.create(donorId,req);}
    @PatchMapping("/{id}/status") public FoodDonation status(@PathVariable Long id,@RequestParam Long accountId,@RequestParam(defaultValue="false") boolean admin,@RequestBody Map<String,String> body){return service.updateStatus(id,body.get("status"),accountId,admin);}
}