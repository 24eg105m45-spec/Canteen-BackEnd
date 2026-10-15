package com.mealbridge.repository;
import com.mealbridge.model.FoodDonation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface FoodDonationRepository extends JpaRepository<FoodDonation, Long> {
    List<FoodDonation> findByDonorIdOrderByCreatedAtDesc(Long donorId);
    List<FoodDonation> findByStatusIgnoreCaseOrderByCreatedAtDesc(String status);
}