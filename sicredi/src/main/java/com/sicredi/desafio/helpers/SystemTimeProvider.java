package com.sicredi.desafio.helpers;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
public class SystemTimeProvider {

    private final Clock clock = Clock.systemUTC();

    public LocalDateTime nowUtc() {
        return LocalDateTime.now(clock);
    }
}
