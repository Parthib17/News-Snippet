package com.example.newsapp.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {FavouriteModel.class}, version = 1)
public abstract class FavouriteFire extends RoomDatabase {
    public abstract FavouriteQuery favouriteQuery();
}

