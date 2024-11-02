package com.sithum.melomindadmin.Models;

public class UploadFile {
    public String fileCategory,fileTitle,speakers,coverimage,description,fileDuration,fileLink,mkey;

    public UploadFile(String fileCategory, String fileTitle, String speakers, String fileDuration, String description, String fileLink) {

        if (fileTitle.trim().equals("")){
            fileTitle = "No title";
        }


        this.fileCategory = fileCategory;
        this.fileTitle = fileTitle;
        this.speakers = speakers;
        this.fileDuration = fileDuration;
        this.fileLink = fileLink;
        this.description = description;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UploadFile() {
    }

    public String getFileCategory() {
        return fileCategory;
    }

    public void setFileCategory(String fileCategory) {
        this.fileCategory = fileCategory;
    }

    public String getFileTitle() {
        return fileTitle;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public String getSpeakers() {
        return speakers;
    }

    public void setSpeakers(String speakers) {
        this.speakers = speakers;
    }

    public String getCoverimage() {
        return coverimage;
    }

    public void setCoverimage(String coverimage) {
        this.coverimage = coverimage;
    }

    public String getFileDuration() {
        return fileDuration;
    }

    public void setFileDuration(String fileDuration) {
        this.fileDuration = fileDuration;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public String getMkey() {
        return mkey;
    }

    public void setMkey(String mkey) {
        this.mkey = mkey;
    }
}
