package com.travel.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@EqualsAndHashCode
public class Activity extends BaseEntity{
    @Column(name = "activity_name")
    private String activityName;

    @Column(name = "description")
    private String description;

    @Column(name = "cost")
    private Double cost = 0d;

    @Column(name = "capacity_available")
    private Integer capacityAvailable = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id")
    private Destination activityDestination;
}
