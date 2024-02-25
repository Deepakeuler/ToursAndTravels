package com.travel.repository;

import com.travel.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
//    Activity findByDestination(Destination destination);
    Optional<Activity> findByActivityName(String name);

    List<Activity> findByCapacityAvailableGreaterThan(Integer val);
}
