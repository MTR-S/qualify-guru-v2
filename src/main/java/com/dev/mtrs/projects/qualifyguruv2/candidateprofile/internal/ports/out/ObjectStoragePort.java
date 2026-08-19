package com.dev.mtrs.projects.qualifyguruv2.candidateprofile.internal.ports.out;

public interface ObjectStoragePort {
    String putObjectAndGetURl(byte[] fileBytes, String originalFilename);
}
