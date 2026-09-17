package com.travelagency.destinations.repositories.review;

import com.travelagency.destinations.entities.review.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    List<ReviewEntity> getAllByDestinationId(Long destinationId);

    @Query("SELECT AVG(r.rating) FROM ReviewEntity r WHERE r.destination.id = :destinationId")
    Double findAverageRatingByDestinationId(@Param("destinationId") Long destinationId);
}
