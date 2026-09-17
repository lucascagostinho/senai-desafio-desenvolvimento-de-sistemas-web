package com.travelagency.destinations.dtos.review;

public class ReviewRequestDTO {

    private Integer rating;
    private Long destinationId;

    public ReviewRequestDTO() {
    }

    public ReviewRequestDTO(Integer rating, Long destinationId) {
        this.rating = rating;
        this.destinationId = destinationId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }
}
