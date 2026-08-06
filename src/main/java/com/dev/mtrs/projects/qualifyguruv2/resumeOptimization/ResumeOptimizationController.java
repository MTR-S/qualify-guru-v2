package com.dev.mtrs.projects.qualifyguruv2.resumeOptimization;

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

    private final ResumeOptimizationService resumeOptimizationService;

    private ResumeOptimizationController(ResumeOptimizationService resumeOptimizationService) {
        this.resumeOptimizationService = resumeOptimizationService;
    }

    @PostMapping("/extract")
    public ResponseEntity<String> uploadResume(@RequestParam("file")MultipartFile file) {

        if(file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a valid PDF file.");
        }

        try(InputStream inputStream = file.getInputStream()) {
            String extractedText = resumeOptimizationService.processResume(inputStream);

            return ResponseEntity.ok(extractedText);

        } catch(IOException e) {
            return ResponseEntity.internalServerError().body("Failed to read the uploaded file.");

        } catch (ResumeExtractionException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }
}
