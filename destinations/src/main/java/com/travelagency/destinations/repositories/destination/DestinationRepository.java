package com.travelagency.destinations.repositories.destination;

import com.travelagency.destinations.entities.destination.DestinationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinationRepository extends JpaRepository<DestinationEntity, Long> {

    public List<DestinationEntity> findAllByNameContainingIgnoreCase(String name);

    public List<DestinationEntity> findAllByLocationContainingIgnoreCase(String name);

}
