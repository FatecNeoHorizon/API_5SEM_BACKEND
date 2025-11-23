package com.neohorizon.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.neohorizon.api.exception.ValidationException;

public enum RoleType {

    ETL,
    ADMIN,
    MANAGER,
    DEVELOPER;

    @JsonCreator
    public static RoleType fromString(String value) {
        if (value == null) throw new ValidationException("Cargo não pode ser nulo");
        String v = value.trim().toUpperCase();
        switch (v) {
            case "ETL" -> {
                return ETL;
            }
            case "ADMIN" -> {
                return ADMIN;
            }
            case "MANAGER" -> {
                return MANAGER;
            }
            case "DEVELOPER" -> {
                return DEVELOPER;
            }
        }
        throw new ValidationException("Cargo inválido: " + value);
    }

    @JsonValue
    public String toJson() {
        return this.name().toUpperCase();
    }

}
