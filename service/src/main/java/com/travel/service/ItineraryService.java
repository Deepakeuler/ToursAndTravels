package com.travel.service;

import com.travel.dto.ActivityDto;
import com.travel.dto.DestinationDto;
import com.travel.dto.PackageDto;
import com.travel.dto.PackagePassengersDto;

import java.util.List;

public interface ItineraryService {
    List<PackageDto> getPackages();

    PackageDto getPackageItinerary(String packageName);

    PackagePassengersDto getPackagePassengers(String packageName);

    List<ActivityDto> getAvailableAcitivities();

    PackageDto createPackage(PackageDto packageDto);

    DestinationDto addDestination(DestinationDto destination);

    ActivityDto addActivity(ActivityDto activityDto);
}
