package com.travelagency.destinations.services.destination;

import com.travelagency.destinations.entities.destination.DestinationEntity;
import com.travelagency.destinations.repositories.destination.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DestinationService {

    @Autowired
    private DestinationRepository destinationRepository;

    public DestinationEntity createDestination(DestinationEntity destinationEntity) {
        return destinationRepository.save(destinationEntity);
    }

    public List<DestinationEntity> getAllDestinations() {
        return destinationRepository.findAll();
    }

    public DestinationEntity getDestinationById(Long id) {
        return destinationRepository.getReferenceById(id);
    }

    public List<DestinationEntity> getDestinationByName(String name) {
        return destinationRepository.findAllByNameContainingIgnoreCase(name);
    }

    public List<DestinationEntity> getDestinationByLocation(String location) {
        return destinationRepository.findAllByLocationContainingIgnoreCase(location);
    }

    public DestinationEntity updateDestination(Long id, DestinationEntity updatedData) {
        DestinationEntity destinationEntity = destinationRepository.getReferenceById(id);
        destinationEntity.setName(updatedData.getName());
        destinationEntity.setLocation(updatedData.getLocation());
        destinationEntity.setDescription(updatedData.getDescription());
        return destinationRepository.save(destinationEntity);
    }

    public void deleteDestination(Long id) {
        destinationRepository.deleteById(id);
    }
}
