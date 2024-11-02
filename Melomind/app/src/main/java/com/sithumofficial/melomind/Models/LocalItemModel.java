package com.sithumofficial.melomind.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "items")
public class LocalItemModel {
    @PrimaryKey
    @NonNull // Annotate the primary key as non-null
    private String fileId;
    private String title;

    private String speakers;

    private String selectedValue;

    private String duration;

    private String cover_image_url;

    private String audio_url;

    private String timestamp;
    private String description;
    // Add other fields as needed

    public LocalItemModel(String fileId, String title, String speakers, String selectedValue, String timestamp, String duration, String description, String cover_image_url, String audio_url) {
        this.fileId = fileId; // Initialize itemId
        this.title = title;
        this.speakers = speakers;
        this.selectedValue = selectedValue;
        this.duration = duration;
        this.description = description;
        this.cover_image_url = cover_image_url;
        this.audio_url = audio_url;
        this.timestamp = timestamp;
        // Initialize other fields as needed
    }

    public LocalItemModel() {

    }

    // Getters and setters for your fields

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSpeakers() {
        return speakers;
    }

    public void setSpeakers(String speakers) {
        this.speakers = speakers;
    }

    public String getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCover_image_url() {
        return cover_image_url;
    }

    public void setCover_image_url(String cover_image_url) {
        this.cover_image_url = cover_image_url;
    }

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    // Add getters and setters for other fields as needed
}

