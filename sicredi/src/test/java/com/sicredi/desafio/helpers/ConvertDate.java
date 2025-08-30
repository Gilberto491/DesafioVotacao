package com.sicredi.desafio.helpers;

import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@UtilityClass
public class ConvertDate {

    public Instant instantNow() {
        return LocalDateTime.now().atZone(ZoneOffset.UTC).toInstant();
    }

    public Instant instantPlusMinutes(long minutes) {
        return LocalDateTime.now().plusMinutes(minutes).atZone(ZoneOffset.UTC).toInstant();
    }
}
