package com.sithumofficial.melomind.Models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ItemDao {
    @Insert
    void insertItem(LocalItemModel item);

    @Query("SELECT * FROM items WHERE fileId = :fileId")
    LocalItemModel getItemById(String fileId);
}

