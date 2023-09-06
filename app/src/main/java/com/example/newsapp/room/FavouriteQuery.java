package com.example.newsapp.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavouriteQuery {

    @Insert
    void insertData(FavouriteModel favouriteModel);

    @Query("SELECT * FROM FavouriteModel ORDER BY id DESC")
    List<FavouriteModel> allwallpapers();

    @Query("SELECT EXISTS(SELECT * FROM FavouriteModel WHERE title=:title)")
    Boolean is_exist(String title);

    @Query("DELETE FROM FavouriteModel WHERE title=:title")
    void deleteByUrl(String title);


}
