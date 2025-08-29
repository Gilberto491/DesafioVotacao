package com.sicredi.desafio.external.service;

import com.sicredi.desafio.external.dto.VoterStatusResponse;
import com.sicredi.desafio.external.exception.CpfNotFoundException;

public interface VoterRegistryClient {
    VoterStatusResponse checkCpf(String cpf) throws CpfNotFoundException;
}
