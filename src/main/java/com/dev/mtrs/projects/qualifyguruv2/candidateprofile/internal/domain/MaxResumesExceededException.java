package com.dev.mtrs.projects.qualifyguruv2.candidateprofile.internal.domain;

public class MaxResumesExceededException extends RuntimeException {
    public MaxResumesExceededException(String message) {
        super(message);
    }
}
