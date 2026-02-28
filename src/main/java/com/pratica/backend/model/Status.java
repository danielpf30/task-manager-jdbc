package com.pratica.backend.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {

    PENDENTE,
    EM_PROGRESSO,
    CONCLUIDA;

    @JsonValue
    public String getStatus(){

        return switch(this) {
            case PENDENTE -> "Pendente";
            case EM_PROGRESSO -> "Em Progresso";
            case CONCLUIDA -> "Concluída";
        };
    }

    @JsonCreator
    public static Status fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        for (Status status : Status.values()) {
            if (status.getStatus().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Status inválido. Use: Pendente, Em Progresso ou Concluída.");
    }
}