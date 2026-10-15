package com.mealbridge.controller;
import com.mealbridge.repository.AccountRepository;
import com.mealbridge.repository.CollectionRequestRepository;
import com.mealbridge.repository.FoodDonationRepository;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final AccountRepository accounts; private final FoodDonationRepository donations; private final CollectionRequestRepository requests;
    public DashboardController(AccountRepository a,FoodDonationRepository d,CollectionRequestRepository r){accounts=a;donations=d;requests=r;}
    @GetMapping("/summary")
    public Map<String,Object> summary(){
        return Map.of("accounts",accounts.count(),"donations",donations.count(),"availableDonations",donations.findByStatusIgnoreCaseOrderByCreatedAtDesc("AVAILABLE").size(),"collectionRequests",requests.count());
    }
}