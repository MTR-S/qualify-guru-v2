package com.dev.mtrs.projects.qualifyguruv2.candidateprofile.internal.domain;

import java.time.LocalDateTime;

public class SavedResume {
    private Long id;
    private String title;
    private String s3FileUrl;
    private LocalDateTime savedAt;

    public SavedResume() {
    }

    public SavedResume(Long id, String title, LocalDateTime savedAt, String s3FileUrl) {
        this.id = id;
        this.title = title;
        this.savedAt = savedAt;
        this.s3FileUrl = s3FileUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getS3FileUrl() {
        return s3FileUrl;
    }

    public void setS3FileUrl(String s3FileUrl) {
        this.s3FileUrl = s3FileUrl;
    }

    public LocalDateTime getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(LocalDateTime savedAt) {
        this.savedAt = savedAt;
    }
}
