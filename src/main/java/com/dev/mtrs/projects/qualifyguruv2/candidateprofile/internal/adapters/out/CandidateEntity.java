package com.dev.mtrs.projects.qualifyguruv2.candidateprofile.internal.adapters.out;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cand_candidates")
public class CandidateEntity {

    @Id
    private Long id;

    // A candidate can have up to X saved resumes
    @OneToMany(mappedBy = "candidateEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SavedResumeEntity> savedResumes = new ArrayList<>();

    public CandidateEntity(Long id) {
        this.id = id;
    }

    public void addResume(SavedResumeEntity resume) {
        this.savedResumes.add(resume);

        resume.setCandidateEntity(this);
    }
}
