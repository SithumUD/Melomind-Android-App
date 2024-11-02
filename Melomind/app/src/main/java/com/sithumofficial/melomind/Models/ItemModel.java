package com.sithumofficial.melomind.Models;

public class ItemModel {

    private String fileId; // Add the itemId field

    private String title;
    private String speakers;
    private String selectedValue;
    private String duration;
    private String description;
    private String cover_image_url;
    private String audio_url;

    private String timestamp;

    public ItemModel() {
        // Default constructor required for Firebase
    }

    public ItemModel(String fileId, String title, String speakers, String selectedValue, String timestamp, String duration, String description, String cover_image_url, String audio_url) {
        this.fileId = fileId; // Initialize itemId
        this.title = title;
        this.speakers = speakers;
        this.selectedValue = selectedValue;
        this.duration = duration;
        this.description = description;
        this.cover_image_url = cover_image_url;
        this.audio_url = audio_url;
        this.timestamp = timestamp;
    }

    public String getItemId() {
        return fileId;
    }

    public void setItemId(String itemId) {
        this.fileId = itemId; // Assign the provided itemId to the fileId field
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
