package com.sicredi.desafio.external.service;

import com.sicredi.desafio.external.dto.VoterStatusResponse;
import com.sicredi.desafio.external.enumerations.VoterStatus;
import com.sicredi.desafio.external.exception.CpfNotFoundException;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.random.RandomGenerator;

import static com.sicredi.desafio.external.helpers.CpfValidator.isValid;

@Component
public class FakeVoterRegistryClient implements VoterRegistryClient {

    private final RandomGenerator rng = new SecureRandom();

    @Override
    public VoterStatusResponse checkCpf(String cpf) {
        if (!isValid(cpf))
            throw new CpfNotFoundException(cpf);

        boolean able = rng.nextBoolean();
        return new VoterStatusResponse(
                able ? VoterStatus.ABLE_TO_VOTE : VoterStatus.UNABLE_TO_VOTE
        );
    }
}
