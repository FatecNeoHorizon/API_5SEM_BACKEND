package com.neohorizon.api.enums;

public enum AggregationType {

    DIA,
    SEMANA,
    MES,
    ANO;

    public static AggregationType fromString(String value) {
        if (value == null) return DIA;
        String v = value.trim().toLowerCase();
        switch (v) {
            case "dia" -> {
                return DIA;
            }
            case "semana" -> {
                return SEMANA;
            }
            case "mes", "mês" -> {
                return MES;
            }
            case "ano" -> {
                return ANO;
            }
            default -> {
                try {
                    return AggregationType.valueOf(value.toUpperCase());
                } catch (IllegalArgumentException e) {
                    return DIA;
                }
            }
        }
    }
}
