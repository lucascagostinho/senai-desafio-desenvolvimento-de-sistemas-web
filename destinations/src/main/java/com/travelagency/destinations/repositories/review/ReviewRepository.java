package com.travelagency.destinations.repositories.review;

import com.travelagency.destinations.entities.review.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    public List<ReviewEntity> getAllByDestinationId(Long destinationId);
}
