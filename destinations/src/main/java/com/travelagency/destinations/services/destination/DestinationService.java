package com.travelagency.destinations.services.destination;

import com.travelagency.destinations.dtos.destination.DestinationDTO;
import com.travelagency.destinations.dtos.destination.DestinationRequestDTO;
import com.travelagency.destinations.entities.destination.DestinationEntity;
import com.travelagency.destinations.repositories.destination.DestinationRepository;
import com.travelagency.destinations.services.review.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DestinationService {

    @Autowired
    private DestinationRepository destinationRepository;

    // ReviewService injetado em vez do ReviewRepository — o cálculo do rating
    // é responsabilidade do domínio de reviews, não do domínio de destinations
    @Autowired
    private ReviewService reviewService;

    // Converte entidade + rating calculado via ReviewService para DTO de saída
    private DestinationDTO toDTO(DestinationEntity entity) {
        Double rating = reviewService.getAverageRatingByDestinationId(entity.getId());
        return new DestinationDTO(
                entity.getId(),
                entity.getName(),
                entity.getLocation(),
                entity.getDescription(),
                rating
        );
    }

    @Transactional
    public DestinationDTO createDestination(DestinationRequestDTO requestDTO) {
        DestinationEntity entity = new DestinationEntity();
        entity.setName(requestDTO.getName());
        entity.setLocation(requestDTO.getLocation());
        entity.setDescription(requestDTO.getDescription());
        DestinationEntity saved = destinationRepository.save(entity);
        return toDTO(saved);
    }

    public List<DestinationDTO> getAllDestinations() {
        return destinationRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public DestinationDTO getDestinationById(Long id) {
        DestinationEntity entity = destinationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Destination not found with id: " + id));
        return toDTO(entity);
    }

    public List<DestinationDTO> getDestinationByName(String name) {
        return destinationRepository.findAllByNameContainingIgnoreCase(name)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<DestinationDTO> getDestinationByLocation(String location) {
        return destinationRepository.findAllByLocationContainingIgnoreCase(location)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public DestinationDTO updateDestination(Long id, DestinationRequestDTO requestDTO) {
        DestinationEntity entity = destinationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Destination not found with id: " + id));
        entity.setName(requestDTO.getName());
        entity.setLocation(requestDTO.getLocation());
        entity.setDescription(requestDTO.getDescription());
        DestinationEntity saved = destinationRepository.save(entity);
        return toDTO(saved);
    }

    @Transactional
    public void deleteDestination(Long id) {
        destinationRepository.deleteById(id);
    }
}
