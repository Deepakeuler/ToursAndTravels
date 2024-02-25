package com.travel.controller;

import com.travel.dto.ActivityDto;
import com.travel.dto.DestinationDto;
import com.travel.dto.PackageDto;
import com.travel.dto.PackagePassengersDto;
import com.travel.service.ItineraryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class ItineraryController {

    private final ItineraryService itineraryService;

    public ItineraryController(ItineraryService itineraryService) {
        this.itineraryService = itineraryService;
    }

    @GetMapping("/packages/list")
    public ResponseEntity<List<PackageDto>> getPackages() {
        return ResponseEntity.ok(itineraryService.getPackages());
    }

    @GetMapping("/packages")
    public ResponseEntity<PackageDto> getPackageItinerary(@RequestParam String packageName) {
        return ResponseEntity.ok(itineraryService.getPackageItinerary(packageName));
    }

    @PostMapping("/packages")
    public ResponseEntity<PackageDto> createPackageItinerary(@RequestBody PackageDto packageDto) {
        return ResponseEntity.ok(itineraryService.createPackage(packageDto));
    }

    @PostMapping("/packages/destinations/")
    public ResponseEntity<DestinationDto> addDestination(@RequestBody DestinationDto destinationDto) {
        return ResponseEntity.ok(itineraryService.addDestination(destinationDto));
    }

    @PostMapping("/packages/destinations/activities")
    public ResponseEntity<ActivityDto> addActivity(@RequestBody ActivityDto activityDto) {
        return ResponseEntity.ok(itineraryService.addActivity(activityDto));
    }
    
    @GetMapping("/packages/passengers") 
    public ResponseEntity<PackagePassengersDto> getPackagePassengers(@RequestParam String packageName) {
        return ResponseEntity.ok(itineraryService.getPackagePassengers(packageName));
    }
}
