package com.travel.service;

import com.travel.dto.OptActivityDto;
import com.travel.dto.PassengerDto;
import com.travel.entity.Passenger;

public interface PassengerOpsService {
    PassengerDto getPassenger(String passengerNumber);

    PassengerDto addPassenger(PassengerDto passengerDto);

    PassengerDto optActivity(OptActivityDto optActivityDto);

    PassengerDto addBalance(String passengerNumber, Double amountToBeAdded);

    PassengerDto buildPassengerDto(Passenger passenger);
}
