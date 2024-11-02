package com.sithumofficial.melomind.Models;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {LocalItemModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ItemDao itemDao();
}
