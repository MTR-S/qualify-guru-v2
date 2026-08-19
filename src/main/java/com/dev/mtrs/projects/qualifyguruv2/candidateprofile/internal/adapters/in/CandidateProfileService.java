package com.dev.mtrs.projects.qualifyguruv2.candidateprofile.internal.adapters.in;

import com.dev.mtrs.projects.qualifyguruv2.candidateprofile.internal.adapters.out.CandidateEntity;
import com.dev.mtrs.projects.qualifyguruv2.candidateprofile.internal.adapters.out.CandidateRepository;
import com.dev.mtrs.projects.qualifyguruv2.candidateprofile.internal.adapters.out.SavedResumeEntity;
import com.dev.mtrs.projects.qualifyguruv2.candidateprofile.internal.adapters.out.SavedResumeWebMapper;
import com.dev.mtrs.projects.qualifyguruv2.candidateprofile.internal.domain.MaxResumesExceededException;
import com.dev.mtrs.projects.qualifyguruv2.candidateprofile.internal.domain.SavedResume;
import com.dev.mtrs.projects.qualifyguruv2.candidateprofile.internal.ports.in.CandidateProfilePort;
import com.dev.mtrs.projects.qualifyguruv2.candidateprofile.internal.ports.out.ObjectStoragePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CandidateProfileService implements CandidateProfilePort {

    private final CandidateRepository candidateRepository;
    private final ObjectStoragePort objectStoragePort;
    private final SavedResumeWebMapper savedResumeMapper;

    public CandidateProfileService(CandidateRepository candidateRepository,
                                   ObjectStoragePort objectStoragePort,
                                   SavedResumeWebMapper savedResumeMapper) {
        this.candidateRepository = candidateRepository;
        this.objectStoragePort = objectStoragePort;
        this.savedResumeMapper = savedResumeMapper;
    }

    @Override
    @Transactional
    public SavedResume saveOptimizedResume(Long userId, byte[] pdfBytes, String originalFilename, String title) {

        CandidateEntity candidate = createNewCandidate(userId);

        if (candidate.getSavedResumes().size() >= 3) {
            throw new MaxResumesExceededException("You have reached the maximum limit of 3 saved resumes.");
        }

        String s3FileUrl = objectStoragePort.putObjectAndGetURl(pdfBytes, originalFilename);

        SavedResumeEntity newResume = new SavedResumeEntity(title, s3FileUrl, LocalDateTime.now());
        candidate.addResume(newResume);

        candidateRepository.save(candidate);

        return savedResumeMapper.toDomain(newResume);
    }

    private CandidateEntity createNewCandidate(Long userId) {

        return candidateRepository.findById(userId)
                .orElseGet(() -> candidateRepository.save(new CandidateEntity(userId)));
    }
}
