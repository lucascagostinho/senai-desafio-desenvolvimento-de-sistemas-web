package com.travelagency.destinations.dtos.review;

public class ReviewDTO {

    private Long id;
    private Integer rating;
    private Long destinationId;

    public ReviewDTO() {
    }

    public ReviewDTO(Long id, Integer rating, Long destinationId) {
        this.id = id;
        this.rating = rating;
        this.destinationId = destinationId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
