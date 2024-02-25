package com.travel.controller;

import com.travel.dto.OptActivityDto;
import com.travel.dto.PassengerDto;
import com.travel.service.PassengerOpsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PassengerOpsController {
    private final PassengerOpsService passengerOpsService;

    @GetMapping("/passenger")
    public ResponseEntity<PassengerDto> getPassenger(@RequestParam String passengerNumber) {
        return ResponseEntity.ok(passengerOpsService.getPassenger(passengerNumber));
    }

    @PostMapping("/passenger")
    public ResponseEntity<PassengerDto> addPassenger(@RequestBody PassengerDto passengerDto) {
        return ResponseEntity.ok(passengerOpsService.addPassenger(passengerDto));
    }

    @PostMapping("/passenger/activity")
    public ResponseEntity<PassengerDto> optActivity(@RequestBody OptActivityDto optActivityDto) {
        return ResponseEntity.ok(passengerOpsService.optActivity(optActivityDto));
    }

    @PutMapping("/passenger/balance")
    public ResponseEntity<PassengerDto> addBalance(
            @RequestParam String passengerNumber,
            @RequestParam double amountToBeAdded
    ) {
        return ResponseEntity.ok(passengerOpsService.addBalance(passengerNumber, amountToBeAdded));
    }
}
