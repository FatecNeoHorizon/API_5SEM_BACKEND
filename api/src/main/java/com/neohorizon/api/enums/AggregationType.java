package com.neohorizon.api.enums;

public enum AggregationType {

    DIA,
    SEMANA,
    MES,
    ANO;

    public static AggregationType fromString(String value) {
        if (value == null) return DIA;
        try {
            return AggregationType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return DIA;
        }
    }
}
