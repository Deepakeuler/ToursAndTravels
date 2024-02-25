package com.travel.entity;

import com.travel.converters.StringListConverter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode
public class Package extends BaseEntity {
    @Column(name = "package_name", unique = true)
    private String packageName;

    @Column(name = "passenger_capacity")
    private Integer passengerCapacity;

    @OneToMany(mappedBy = "packageDestination", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Destination> destinations;

    @Column(name = "passengers")
    @Convert(converter = StringListConverter.class)
    private Set<String> passengersIds = new HashSet<>();

}
