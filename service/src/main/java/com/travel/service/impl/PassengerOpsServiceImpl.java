package com.travel.service.impl;

import com.travel.dto.OptActivityDto;
import com.travel.dto.PassengerActivityDto;
import com.travel.dto.PassengerDto;
import com.travel.entity.Activity;
import com.travel.entity.Package;
import com.travel.entity.Passenger;
import com.travel.enums.SubscriptionType;
import com.travel.repository.ActivityRepository;
import com.travel.repository.DestinationRepository;
import com.travel.repository.PackageRepository;
import com.travel.repository.PassengerRepository;
import com.travel.service.PassengerOpsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PassengerOpsServiceImpl implements PassengerOpsService {

    private final PackageRepository packageRepository;
    private final DestinationRepository destinationRepository;
    private final ActivityRepository activityRepository;
    private final PassengerRepository passengerRepository;

    @Override
    public PassengerDto getPassenger(String passengerNumber) {
        Passenger passenger = passengerRepository.findByPassengerNumber(passengerNumber)
                .orElseThrow(() -> new NotFoundException("Did not found any passenger with passengerNumber: " + passengerNumber));

        return buildPassengerDto(passenger);
    }

    public PassengerDto buildPassengerDto(Passenger passenger) {
        return PassengerDto.builder()
                .passengerNumber(passenger.getPassengerNumber())
                .name(passenger.getName())
                .balanceAvailable(passenger.getAvailableBalance())
                .subscriptionType(passenger.getSubscriptionType())
                .passengerActivities(
                        passenger.getActivities().isEmpty() ? null :
                        passenger.getActivities().stream()
                        .map(activityId -> {
                            Activity activity = activityRepository.findById(activityId)
                                    .orElseThrow(() -> new NotFoundException("Did not found any Activity with activityId: " + activityId));;
                            return buildPassengerActivityDto(passenger, activity);
                        })
                        .toList())
                .build();

    }

    private PassengerActivityDto buildPassengerActivityDto(Passenger passenger, Activity activity) {
        return PassengerActivityDto.builder()
                .activityName(activity.getActivityName())
                .destinationName(activity.getActivityDestination().getDestinationName())
                .packageName(destinationRepository.findByDestinationName(activity.getActivityDestination().getDestinationName())
                        .orElseThrow(() -> new NotFoundException("Did not found any Destination with destinationName: " + activity.getActivityDestination().getDestinationName()))
                        .getPackageDestination().getPackageName())
                .amountPaid(getAmountPaid(passenger.getSubscriptionType(), activity.getCost()))
                .build();
    }

    private Double getAmountPaid(SubscriptionType subscriptionType, double activityCost) {
        switch (subscriptionType) {
            case GOLD -> {
                return activityCost * (1 - SubscriptionType.GOLD.getDiscountPercentage());
            }
            case PREMIUM -> {
                return 0d;
            }
        }
        return activityCost;
    }

    @Override
    public PassengerDto addPassenger(PassengerDto passengerDto) {
        Passenger passenger = new Passenger();
        passenger.setPassengerNumber(UUID.randomUUID().toString());
        passenger.setName(passengerDto.getName());
        passenger.setAvailableBalance(passengerDto.getBalanceAvailable());
        passenger.setSubscriptionType(passengerDto.getSubscriptionType());

        passenger = passengerRepository.save(passenger);
        return buildPassengerDto(passenger);
    }

    @Override
    public PassengerDto optActivity(OptActivityDto optActivityDto) {
        Passenger passenger = passengerRepository.findByPassengerNumber(optActivityDto.getPassengerNumber())
                .orElseThrow(() -> new NotFoundException("Did not found any passenger with passengerNumber: " + optActivityDto.getPassengerNumber()));

        Activity activity = activityRepository.findByActivityName(optActivityDto.getActivityName())
                .orElseThrow(() -> new NotFoundException("Did not found any Activity with activityName: " + optActivityDto.getActivityName()));

        if (passenger.getActivities().contains(activity.getId())) {
            log.info("Passenger: {} has already opted for the activity: {}", passenger.getPassengerNumber(), activity.getActivityName());
            return buildPassengerDto(passenger);
        }

        Package tourPackage = packageRepository.findByPackageName(optActivityDto.getPackageName())
                .orElseThrow(() -> new NotFoundException("Did not found any Package with packageName: " + optActivityDto.getPackageName()));


        if (tourPackage.getPassengerCapacity() <= 0) {
            throw new RuntimeException();
        }

        if (activity.getCapacityAvailable() <= 0) {
            throw new RuntimeException();
        }

        tourPackage.setPassengerCapacity(tourPackage.getPassengerCapacity() - 1);
        tourPackage.getPassengersIds().add(passenger.getPassengerNumber());
        packageRepository.save(tourPackage);

        activity.setCapacityAvailable(activity.getCapacityAvailable() - 1);
        activity = activityRepository.save(activity);

        passenger.setAvailableBalance(
                passenger.getAvailableBalance() - getAmountPaid(passenger.getSubscriptionType(), activity.getCost())
        );
        passenger.getActivities().add(activity.getId());
        return buildPassengerDto(passengerRepository.save(passenger));
    }

    @Override
    public PassengerDto addBalance(String passengerNumber, Double amountToBeAdded) {
        Passenger passenger = passengerRepository.findByPassengerNumber(passengerNumber)
                .orElseThrow(() -> new NotFoundException("Did not found any passenger with passengerNumber: " + passengerNumber));

        passenger.setAvailableBalance(passenger.getAvailableBalance() + amountToBeAdded);

        return buildPassengerDto(passengerRepository.save(passenger));
    }
}
