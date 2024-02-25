package com.travel.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class LongListConverter implements AttributeConverter<Set<Long>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(Set<Long> integerList) {
        if (integerList == null || integerList.isEmpty()) {
            return null;
        }
        return integerList.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public Set<Long> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(Arrays.stream(dbData.split(DELIMITER))
                .map(Long::valueOf)
                .collect(Collectors.toList()));
    }
}