package com.sithumofficial.melomind.Models;

import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

