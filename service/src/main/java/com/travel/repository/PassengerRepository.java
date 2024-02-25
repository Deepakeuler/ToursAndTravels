package com.travel.repository;

import com.travel.entity.Passenger;
import com.travel.enums.SubscriptionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    List<Passenger> findBySubscriptionType(SubscriptionType subscriptionType);
    Optional<Passenger> findByPassengerNumber(String passengerNumber);

    List<Passenger> findByPassengerNumberIn(Set<String> passngersNumber);
}
