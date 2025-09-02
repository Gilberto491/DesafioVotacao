package com.sicredi.desafio.service.enumerations;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum VoteChoice {
    SIM,
    NAO;

    @JsonCreator
    public static VoteChoice from(String raw) {
        if (raw == null) return null;
        String v = raw.trim().toUpperCase(java.util.Locale.ROOT);
        if (v.equals("SIM")) return SIM;
        if (v.equals("NAO") || v.equals("NÃO")) return NAO;
        throw new IllegalArgumentException("choice inválido. Aceitos: SIM, NAO");
    }
}
