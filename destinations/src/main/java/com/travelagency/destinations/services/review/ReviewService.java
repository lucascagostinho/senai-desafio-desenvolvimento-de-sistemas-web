package com.travelagency.destinations.services.review;

import com.travelagency.destinations.dtos.review.ReviewDTO;
import com.travelagency.destinations.dtos.review.ReviewRequestDTO;
import com.travelagency.destinations.entities.destination.DestinationEntity;
import com.travelagency.destinations.entities.review.ReviewEntity;
import com.travelagency.destinations.repositories.destination.DestinationRepository;
import com.travelagency.destinations.repositories.review.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private DestinationRepository destinationRepository;

    private ReviewDTO toDTO(ReviewEntity entity) {
        return new ReviewDTO(
                entity.getId(),
                entity.getRating(),
                entity.getDestination().getId()
        );
    }

    private DestinationEntity resolveDestination(Long destinationId) {
        return destinationRepository.findById(destinationId)
                .orElseThrow(() -> new EntityNotFoundException("Destination not found with id: " + destinationId));
    }

    public Double getAverageRatingByDestinationId(Long destinationId) {
        return reviewRepository.findAverageRatingByDestinationId(destinationId);
    }

    @Transactional
    public ReviewDTO createReview(ReviewRequestDTO requestDTO) {
        ReviewEntity entity = new ReviewEntity();
        entity.setRating(requestDTO.getRating());
        entity.setDestination(resolveDestination(requestDTO.getDestinationId()));
        ReviewEntity saved = reviewRepository.save(entity);
        return toDTO(saved);
    }

    public List<ReviewDTO> getAllReviews() {
        return reviewRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public ReviewDTO getReviewById(Long id) {
        ReviewEntity entity = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + id));
        return toDTO(entity);
    }

    public List<ReviewDTO> getAllByDestinationId(Long destinationId) {
        return reviewRepository.getAllByDestinationId(destinationId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public ReviewDTO updateReview(Long id, ReviewRequestDTO requestDTO) {
        ReviewEntity entity = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + id));
        entity.setRating(requestDTO.getRating());
        entity.setDestination(resolveDestination(requestDTO.getDestinationId()));
        ReviewEntity saved = reviewRepository.save(entity);
        return toDTO(saved);
    }

    @Transactional
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}
