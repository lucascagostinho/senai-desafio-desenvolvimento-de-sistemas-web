package com.travelagency.destinations.controllers.review;

import com.travelagency.destinations.entities.review.ReviewEntity;
import com.travelagency.destinations.services.review.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewEntity> createReview(@RequestBody ReviewEntity reviewEntity) {
        return new ResponseEntity<>(reviewService.createReview(reviewEntity), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ReviewEntity>> getAllReviews() {
        return new ResponseEntity<>(reviewService.getAllReviews(), HttpStatus.OK);
    }

    @GetMapping("/search/destination")
    public ResponseEntity<List<ReviewEntity>> getAllReviewsByDestinationId(@RequestParam Long id) {
        return new ResponseEntity<>(reviewService.getAllByDestinationId(id), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewEntity> getReviewById(@PathVariable Long id) {
        return new ResponseEntity<>(reviewService.getReviewById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewEntity> updateReview(@PathVariable Long id, @RequestBody ReviewEntity reviewEntity) {
        return new ResponseEntity<>(reviewService.updateReview(id, reviewEntity), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
