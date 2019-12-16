package com.opipo.codechallenge.repository.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.opipo.codechallenge.exception.InvalidRequestException;

/**
 * Gets or Sets sortType
 */
public enum SortType {
    ASC("asc"), DESC("desc");

    private String value;

    SortType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static SortType fromValue(String text) {
        for (SortType b : SortType.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        throw new InvalidRequestException();
    }
}
