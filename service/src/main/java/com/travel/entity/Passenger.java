package com.travel.entity;

import com.travel.converters.LongListConverter;
import com.travel.enums.SubscriptionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode
@RequiredArgsConstructor
public class Passenger extends BaseEntity {
    @Column(name = "passenger_name")
    private String name;

    @Column(name = "passenger_number")
    private String passengerNumber;

    @Column(name = "balance")
    private Double availableBalance = 0d;

    @Column(name = "activities")
    @Convert(converter = LongListConverter.class)
    private Set<Long> activities = new HashSet<>();

    @Column(name = "subscription_type")
    @Enumerated(EnumType.STRING)
    private SubscriptionType subscriptionType;
}
