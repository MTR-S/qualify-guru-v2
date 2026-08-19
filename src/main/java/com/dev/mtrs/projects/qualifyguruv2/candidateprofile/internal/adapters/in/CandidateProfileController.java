package com.dev.mtrs.projects.qualifyguruv2.candidateprofile.internal.adapters.in;

import com.dev.mtrs.projects.qualifyguruv2.candidateprofile.internal.adapters.out.SavedResumeWebMapper;
import com.dev.mtrs.projects.qualifyguruv2.candidateprofile.internal.domain.MaxResumesExceededException;
import com.dev.mtrs.projects.qualifyguruv2.candidateprofile.internal.domain.SavedResume;
import com.dev.mtrs.projects.qualifyguruv2.candidateprofile.internal.domain.SavedResumeResponse;
import com.dev.mtrs.projects.qualifyguruv2.candidateprofile.internal.ports.in.CandidateProfilePort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/candidates/resumes")
public class CandidateProfileController {

    private final CandidateProfilePort candidateProfilePort;
    private final SavedResumeWebMapper savedResumeMapper;

    public CandidateProfileController(CandidateProfilePort candidateProfilePort,
                                      SavedResumeWebMapper savedResumeMapper) {
        this.candidateProfilePort = candidateProfilePort;
        this.savedResumeMapper = savedResumeMapper;
    }

    @PostMapping
    public ResponseEntity<?> saveResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title) {

        try {
            String userIdString = (String) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();

            if(userIdString == null) {
                throw new IOException();
            }

            Long userId = Long.valueOf(userIdString);

            SavedResume savedResume = candidateProfilePort.saveOptimizedResume(
                    userId,
                    file.getBytes(),
                    file.getOriginalFilename(),
                    title
            );

            SavedResumeResponse response = savedResumeMapper.toResponse(savedResume);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (MaxResumesExceededException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process the uploaded file.");
        }
    }
}
