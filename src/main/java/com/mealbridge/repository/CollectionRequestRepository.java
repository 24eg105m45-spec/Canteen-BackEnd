package com.mealbridge.repository;
import com.mealbridge.model.CollectionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface CollectionRequestRepository extends JpaRepository<CollectionRequest, Long> {
    List<CollectionRequest> findByOrganizerIdOrderByCreatedAtDesc(Long organizerId);
    List<CollectionRequest> findAllByOrderByCreatedAtDesc();
    boolean existsByDonationIdAndOrganizerId(Long donationId, Long organizerId);
}