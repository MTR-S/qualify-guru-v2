package com.dev.mtrs.projects.qualifyguruv2.candidateprofile.internal.adapters.out;

import com.dev.mtrs.projects.qualifyguruv2.candidateprofile.internal.domain.SavedResume;
import com.dev.mtrs.projects.qualifyguruv2.candidateprofile.internal.domain.SavedResumeResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SavedResumeWebMapper {

    SavedResumeResponse toResponse(SavedResume savedResume);

    SavedResume toDomain(SavedResumeEntity savedResume);
}
