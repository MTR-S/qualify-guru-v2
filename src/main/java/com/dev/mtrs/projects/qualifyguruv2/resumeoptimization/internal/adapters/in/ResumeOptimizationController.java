package com.dev.mtrs.projects.qualifyguruv2.resumeoptimization.internal.adapters.in;

import com.dev.mtrs.projects.qualifyguruv2.resumeoptimization.internal.ports.in.ResumeOptimizationPort;
import com.dev.mtrs.projects.qualifyguruv2.resumeoptimization.internal.domain.AdaptedResumeResponse;
import com.dev.mtrs.projects.qualifyguruv2.resumeoptimization.internal.domain.ResumeExtractionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("api/v1/resumes")
public class ResumeOptimizationController {

    private final ResumeOptimizationPort resumeOptimization;

    private ResumeOptimizationController(ResumeOptimizationPort resumeOptimization) {
        this.resumeOptimization = resumeOptimization;
    }

    @PostMapping("/extract")
    public ResponseEntity<AdaptedResumeResponse> uploadResume(
            @RequestParam("file")MultipartFile file,
            @RequestParam("jobTitle") String jobTitle,
            @RequestParam("jobDescription") String jobDescriptionText) {

        if(file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try(InputStream inputStream = file.getInputStream()) {
            JobDescriptionRequest jobDescription = new JobDescriptionRequest(jobTitle, jobDescriptionText);

            AdaptedResumeResponse response = resumeOptimization.extractAndProcessResume(inputStream, jobDescription);

            return ResponseEntity.ok(response);

        } catch(IOException e) {
            return ResponseEntity.internalServerError().build();

        } catch (ResumeExtractionException e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }
}
