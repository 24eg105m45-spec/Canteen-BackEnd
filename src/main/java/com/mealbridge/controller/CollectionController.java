package com.mealbridge.controller;
import com.mealbridge.dto.CollectionRequestDto;
import com.mealbridge.model.CollectionRequest;
import com.mealbridge.service.CollectionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/collection-requests")
public class CollectionController {
    private final CollectionService service;
    public CollectionController(CollectionService service){this.service=service;}
    @GetMapping public List<CollectionRequest> all(){return service.all();}
    @GetMapping("/organizer/{organizerId}") public List<CollectionRequest> byOrganizer(@PathVariable Long organizerId){return service.byOrganizer(organizerId);}
    @PostMapping("/donation/{donationId}/organizer/{organizerId}") @ResponseStatus(HttpStatus.CREATED)
    public CollectionRequest create(@PathVariable Long donationId,@PathVariable Long organizerId,@Valid @RequestBody(required=false) CollectionRequestDto dto){return service.create(donationId,organizerId,dto);}
    @PatchMapping("/{id}/status") public CollectionRequest status(@PathVariable Long id,@RequestBody Map<String,String> body){return service.updateStatus(id,body.get("status"));}
}