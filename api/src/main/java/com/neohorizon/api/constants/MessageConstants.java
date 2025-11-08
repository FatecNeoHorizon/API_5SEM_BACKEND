package com.neohorizon.api.constants;

public final class MessageConstants {
    
    private MessageConstants() {
        // Construtor privado para evitar instanciação
    }
    
    // Mensagens base
    public static final String NOT_FOUND_MASCULINE = " não encontrado";
    public static final String NOT_FOUND_FEMININE = " não encontrada";
    
    // Entidades específicas
    public static final String DEVELOPER_PREFIX = "Desenvolvedor com ID ";
    public static final String ACTIVITY_PREFIX = "Atividade com ID ";
    public static final String PROJECT_PREFIX = "Projeto com ID ";
    public static final String APPOINTMENT_PREFIX = "Apontamento de horas com ID ";
    public static final String DIMDEV_PREFIX = "DimDev com ID ";
    public static final String USER_PREFIX = "Usuário com ID : ";
    
    // Validações
    public static final String DEVELOPER_ID_REQUIRED = "ID do desenvolvedor é obrigatório";
    public static final String ACTIVITY_ID_REQUIRED = "ID da atividade é obrigatório";
    public static final String PROJECT_ID_REQUIRED = "ID do projeto é obrigatório";
    public static final String USER_ID_REQUIRED = "ID do usuário é obrigatório";
    public static final String DATE_REQUIRED = "Data do apontamento é obrigatória";
    public static final String HOURS_INVALID = "Horas trabalhadas deve ser maior que zero";
    
    // Métodos utilitários para gerar mensagens completas
    public static String developerNotFound(Long id) {
        return DEVELOPER_PREFIX + id + NOT_FOUND_MASCULINE;
    }
    
    public static String activityNotFound(Long id) {
        return ACTIVITY_PREFIX + id + NOT_FOUND_FEMININE;
    }
    
    public static String projectNotFound(Long id) {
        return PROJECT_PREFIX + id + NOT_FOUND_MASCULINE;
    }
    
    public static String appointmentNotFound(Long id) {
        return APPOINTMENT_PREFIX + id + NOT_FOUND_MASCULINE;
    }

    public static String dimDevNotFound(Long id) {
        return DIMDEV_PREFIX + id + NOT_FOUND_MASCULINE;
    }

    public static String userIdRequired() {
        return USER_ID_REQUIRED;
    }
}