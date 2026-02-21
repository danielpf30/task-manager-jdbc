package com.pratica.backend.model;

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
}