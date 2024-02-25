package com.travel.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.dto.ActivityDto;
import com.travel.dto.DestinationDto;
import com.travel.dto.PackageDto;
import com.travel.dto.PackagePassengersDto;
import com.travel.entity.Activity;
import com.travel.entity.Destination;
import com.travel.entity.Package;
import com.travel.repository.ActivityRepository;
import com.travel.repository.DestinationRepository;
import com.travel.repository.PackageRepository;
import com.travel.repository.PassengerRepository;
import com.travel.service.ItineraryService;
import com.travel.service.PassengerOpsService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItineraryServiceImpl implements ItineraryService {
    private final ActivityRepository activityRepository;

    private final DestinationRepository destinationRepository;

    private final PackageRepository packageRepository;

    private final ObjectMapper objectMapper;

    private final PassengerOpsService passengerOpsService;

    private final PassengerRepository passengerRepository;

    private static PackageDto buildPackageDto(Package tourPackage) {
        return PackageDto.builder()
                .packageName(tourPackage.getPackageName())
                .passengerCapacity(tourPackage.getPassengerCapacity())
                .destinations(tourPackage.getDestinations().stream()
                        .map(ItineraryServiceImpl::buildDestinationDto)
                        .toList())
                .build();
    }

    private static DestinationDto buildDestinationDto(Destination destination) {
        return DestinationDto.builder()
                .destinationName(destination.getDestinationName())
                .activities(destination.getActivities().stream()
                        .map(ItineraryServiceImpl::buildActivityDto)
                        .toList())
                .build();
    }

    private static ActivityDto buildActivityDto(Activity activity) {
        return ActivityDto.builder()
                .activityName(activity.getActivityName())
                .description(activity.getDescription())
                .cost(activity.getCost())
                .capacityAvailable(activity.getCapacityAvailable())
                .build();
    }

    @Override
    public List<PackageDto> getPackages() {
        List<Package> packages = packageRepository.findAll();
        return packages.stream()
                .map(ItineraryServiceImpl::buildPackageDto)
                .collect(Collectors.toList());
    }

    @Override
    public PackageDto getPackageItinerary(String packageName) {
        Package tourPackage = packageRepository.findByPackageName(packageName)
                .orElseThrow(() -> new NotFoundException("Did not found any Package with packageName: " + packageName));

        return buildPackageDto(tourPackage);
    }

    @Override
    public PackagePassengersDto getPackagePassengers(String packageName) {
        val tourPackage = packageRepository.findByPackageName(packageName)
                .orElseThrow(() -> new NotFoundException("Did not found any Package with packageName: " + packageName));

        return PackagePassengersDto.builder()
                .packageName(tourPackage.getPackageName())
                .passengerCapacity(tourPackage.getPassengerCapacity())
                .passengers(passengerRepository.findByPassengerNumberIn(tourPackage.getPassengersIds())
                        .stream()
                        .map(passengerOpsService::buildPassengerDto)
                        .toList())
                .build();
    }

    @Override
    public List<ActivityDto> getAvailableAcitivities() {
        return activityRepository.findByCapacityAvailableGreaterThan(0).stream()
                .map(activity -> objectMapper.convertValue(activity, ActivityDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public PackageDto createPackage(PackageDto packageDto) {
        Package pack = packageRepository.findByPackageName(packageDto.getPackageName())
                        .map(p -> {
                            p.setPassengerCapacity(packageDto.getPassengerCapacity());
                            return p;
                        })
                        .orElseGet(() -> {
                            Package package1 = new Package();
                            package1.setPackageName(packageDto.getPackageName());
                            package1.setPassengerCapacity(packageDto.getPassengerCapacity());
                            return package1;
                        });
        pack = packageRepository.save(pack);
        return buildPackageDto(pack);
    }

    @Override
    public DestinationDto addDestination(DestinationDto destinationDto) {
        Optional<Destination> destinationOptional = destinationRepository.findByDestinationName(destinationDto.getDestinationName());
        if (destinationOptional.isPresent()) {
            return DestinationDto.builder()
                    .destinationName(destinationOptional.get().getDestinationName())
                    .packageName(destinationOptional.get().getPackageDestination().getPackageName())
                    .build();
        }
        Destination destination1 = new Destination();
        destination1.setDestinationName(destinationDto.getDestinationName());
        destination1.setPackageDestination(packageRepository.findByPackageName(destinationDto.getPackageName())
                .orElseThrow(() -> new NotFoundException("Did not found any Package with packageName: " + destinationDto.getPackageName())));

        Destination destination = destinationRepository.save(destination1);
        return DestinationDto.builder()
                .destinationName(destination.getDestinationName())
                .packageName(destination.getPackageDestination().getPackageName())
                .build();
    }

    @Override
    public ActivityDto addActivity(ActivityDto activityDto) {
        Activity activity = activityRepository.findByActivityName(activityDto.getActivityName())
                .map(activity1 -> {
                    activity1.setCost(activityDto.getCost());
                    activity1.setDescription(activityDto.getDescription());
                    activity1.setCapacityAvailable(activityDto.getCapacityAvailable());
                    return activity1;
                })
                .orElseGet(() -> {
                    Activity activity1 = new Activity();

                    activity1.setActivityName(activityDto.getActivityName());
                    activity1.setDescription(activityDto.getDescription());
                    activity1.setCost(activityDto.getCost());
                    activity1.setCapacityAvailable(activityDto.getCapacityAvailable());
                    activity1.setActivityDestination(destinationRepository.findByDestinationName(activityDto.getDestinationName())
                            .orElseThrow(() -> new NotFoundException("Did not found any Destination with destinationName: " + activityDto.getDestinationName())));

                    return activity1;
                });

        activity = activityRepository.save(activity);
        return ActivityDto.builder()
                .activityName(activity.getActivityName())
                .description(activity.getDescription())
                .cost(activity.getCost())
                .capacityAvailable(activityDto.getCapacityAvailable())
                .destinationName(activity.getActivityDestination().getDestinationName())
                .build();
    }
}
