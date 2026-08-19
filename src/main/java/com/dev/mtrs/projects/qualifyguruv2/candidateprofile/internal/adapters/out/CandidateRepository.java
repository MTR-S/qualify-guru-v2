package com.dev.mtrs.projects.qualifyguruv2.candidateprofile.internal.adapters.out;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends JpaRepository<CandidateEntity, Long> {
}
