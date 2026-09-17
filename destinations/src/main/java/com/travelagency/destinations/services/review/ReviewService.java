package com.travelagency.destinations.services.review;

import com.travelagency.destinations.entities.review.ReviewEntity;
import com.travelagency.destinations.repositories.review.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    
    @Autowired
    private ReviewRepository reviewRepository;

    public ReviewEntity createReview(ReviewEntity reviewEntity) {
        return reviewRepository.save(reviewEntity);
    }

    public List<ReviewEntity> getAllReviews() {
        return reviewRepository.findAll();
    }

    public ReviewEntity getReviewById(Long id) {
        return reviewRepository.getReferenceById(id);
    }

    public List<ReviewEntity> getAllByDestinationId(Long destinationId) {
        return reviewRepository.getAllByDestinationId(destinationId);
    }

    public ReviewEntity updateReview(Long id, ReviewEntity updatedData) {
        ReviewEntity ReviewEntity = reviewRepository.getReferenceById(id);
        ReviewEntity.setDestination(updatedData.getDestination());
        ReviewEntity.setRating(updatedData.getRating());
        return reviewRepository.save(ReviewEntity);
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}
