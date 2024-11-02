package com.sithumofficial.melomind.Models;

import java.util.List;

public class Users {
    String profilepic,username,mail,userid,password;
    List<String> activityHistory;
    List<String> downloadList;


    public Users(String profilepic, String username, String mail, String userid, String password, List<String> activityHistory, List<String> downloadList) {
        this.profilepic = profilepic;
        this.username = username;
        this.mail = mail;
        this.userid = userid;
        this.password = password;
        this.activityHistory = activityHistory;
        this.downloadList = downloadList;
    }

    public  Users(){}

    //SIGNUP CONSTRUCTOR
    public Users(String username, String mail, String password) {
        this.username = username;
        this.mail = mail;
        this.password = password;
    }

    public List<String> getDownloadList() {
        return downloadList;
    }

    public void setDownloadList(List<String> downloadList) {
        this.downloadList = downloadList;
    }

    public List<String> getActivityHistory() {
        return activityHistory;
    }

    public void setActivityHistory(List<String> activityHistory) {
        this.activityHistory = activityHistory;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
