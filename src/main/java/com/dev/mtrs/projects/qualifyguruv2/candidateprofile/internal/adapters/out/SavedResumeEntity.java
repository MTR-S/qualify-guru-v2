package com.dev.mtrs.projects.qualifyguruv2.candidateprofile.internal.adapters.out;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cand_saved_resumes")
public class SavedResumeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String s3FileUrl;

    @Column(nullable = false)
    private LocalDateTime savedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private CandidateEntity candidateEntity;

    public SavedResumeEntity(String title, String s3FileUrl, LocalDateTime savedAt) {
        this.title = title;
        this.s3FileUrl = s3FileUrl;
        this.savedAt = savedAt;
    }
}
