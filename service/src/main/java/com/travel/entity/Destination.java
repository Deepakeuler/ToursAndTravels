package com.travel.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@EqualsAndHashCode
@Getter
@Setter
@RequiredArgsConstructor
public class Destination extends BaseEntity {
    @Column(name = "destination_name")
    private String destinationName;

    @OneToMany(mappedBy = "activityDestination", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Activity> activities;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id")
    private Package packageDestination;
}
