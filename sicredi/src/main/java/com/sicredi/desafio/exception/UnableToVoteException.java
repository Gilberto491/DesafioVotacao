package com.sicredi.desafio.exception;

public class UnableToVoteException extends RuntimeException {
    public UnableToVoteException() {
        super("error.unableToVote");
    }
}