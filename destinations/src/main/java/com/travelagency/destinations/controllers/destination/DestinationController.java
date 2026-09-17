package com.travelagency.destinations.controllers.destination;

import com.travelagency.destinations.dtos.destination.DestinationDTO;
import com.travelagency.destinations.dtos.destination.DestinationRequestDTO;
import com.travelagency.destinations.services.destination.DestinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/destinations")
public class DestinationController {

    @Autowired
    private DestinationService destinationService;

    @PostMapping
    public ResponseEntity<DestinationDTO> createDestination(@RequestBody DestinationRequestDTO requestDTO) {
        return new ResponseEntity<>(destinationService.createDestination(requestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DestinationDTO>> getAllDestinations() {
        return new ResponseEntity<>(destinationService.getAllDestinations(), HttpStatus.OK);
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<DestinationDTO>> getDestinationByName(@RequestParam String name) {
        return new ResponseEntity<>(destinationService.getDestinationByName(name), HttpStatus.OK);
    }

    @GetMapping("/search/location")
    public ResponseEntity<List<DestinationDTO>> getDestinationByLocation(@RequestParam String location) {
        return new ResponseEntity<>(destinationService.getDestinationByLocation(location), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DestinationDTO> getDestinationById(@PathVariable Long id) {
        return new ResponseEntity<>(destinationService.getDestinationById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DestinationDTO> updateDestination(@PathVariable Long id, @RequestBody DestinationRequestDTO requestDTO) {
        return new ResponseEntity<>(destinationService.updateDestination(id, requestDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDestination(@PathVariable Long id) {
        destinationService.deleteDestination(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
