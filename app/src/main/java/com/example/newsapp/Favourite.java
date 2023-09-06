package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsapp.Models.FavouriteData;
import com.example.newsapp.Models.NewsHeadlines;
import com.example.newsapp.room.FavouriteFire;
import com.example.newsapp.room.FavouriteModel;
import com.example.newsapp.room.FavouriteQuery;

import java.util.ArrayList;
import java.util.List;

public class Favourite extends AppCompatActivity {

    RecyclerView recyclerView;
    FavAdapter favAdapter;
    ArrayList<FavouriteData> favlist = new ArrayList<>();
    public static String dbName = "favDb";
    ImageView favImage;
    TextView favtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        favImage = findViewById(R.id.favIcon);
        favtext = findViewById(R.id.favtext);


        loadFav();


    }

    private void loadFav() {

        initList();
        recyclerView = findViewById(R.id.recycler_fav);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        favAdapter = new FavAdapter(this, favlist);
        recyclerView.setAdapter(favAdapter);

        favAdapter.notifyDataSetChanged();


    }

    private void initList() {

        FavouriteFire db = Room.databaseBuilder(Favourite.this, FavouriteFire.class, dbName).allowMainThreadQueries().build();
        FavouriteQuery userDao = db.favouriteQuery();

        List<FavouriteModel> list = userDao.allwallpapers();
        for (FavouriteModel data : list) {
            favlist.add(new FavouriteData(data.title, data.content));


            //favAdapter.notifyDataSetChanged();
        }
    }

    public void removeFav(String title, String content, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete..?")
                .setMessage("Are you want to remove this article from Favourite List")
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FavouriteFire db = Room.databaseBuilder(context, FavouriteFire.class, dbName).allowMainThreadQueries().build();
                        FavouriteQuery userDao = db.favouriteQuery();

                        try {
                            userDao.deleteByUrl(title);
                            userDao.deleteByUrl(content);
                            Toast.makeText(context, "Removed..", Toast.LENGTH_SHORT).show();


                        } catch (Exception e) {

                            Toast.makeText(context, "Failed to delete..", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            loadFav();
        } catch (Exception e) {
            Log.i("check", "onResume: " + e.toString());
        }

    }
}