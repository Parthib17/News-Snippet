package com.example.newsapp;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.newsapp.Models.NewsHeadlines;
import com.example.newsapp.room.FavouriteFire;
import com.example.newsapp.room.FavouriteModel;
import com.example.newsapp.room.FavouriteQuery;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Locale;


public class detailsView extends AppCompatActivity {
    NewsHeadlines headlines;
    TextView txt_title, txt_author, txt_time, txt_content, sentiment_txt;
    Button read_more;
    ImageView img_news, sentiment_icon, fav;
    String url;
    String res, imgUrl, sentiment;
    ProgressDialog dialog;
    public static String dbName = "favDb";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));

        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_title = findViewById(R.id.text_detail_title);
        txt_author = findViewById(R.id.text_detail_author);
        txt_time = findViewById(R.id.text_detail_time);
        txt_content = findViewById(R.id.text_detail_content);
        read_more = findViewById(R.id.read_more);
        img_news = findViewById(R.id.img_detail_news);
        fav = findViewById(R.id.addToFav);

        sentiment_txt = findViewById(R.id.sentiment_txt);
        sentiment_icon = findViewById(R.id.sentiment_icon);


        headlines = (NewsHeadlines) getIntent().getSerializableExtra("data");

        txt_title.setText(headlines.getTitle());
        txt_author.setText(headlines.getAuthor());
        txt_time.setText(headlines.getPublishedAt());
        imgUrl = headlines.getUrlToImage();
//        txt_content.setText(headlines.getContent());

        // Log.i("hello",headlines.getUrlToImage());

        url = headlines.getUrl();

//        str=headlines.getContent();

        new multi().execute();

        //python implementation
        read_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(detailsView.this, webView.class);
                intent.putExtra("url",url);
                startActivity(intent);
            }
        });


//        Log.i("ok",headlines.getContent());
    }


    public class multi extends AsyncTask<String,String,String>{


        @Override
        protected String doInBackground(String... strings) {
            if (!Python.isStarted()) {
                Python.start(new AndroidPlatform(detailsView.this));
            }
            Python py = Python.getInstance();
            Python py1 = Python.getInstance();
            PyObject pyobj = py.getModule("myfile");
            PyObject obj = pyobj.callAttr("main", url);
            String result = obj.toString();
            String[] arr = result.split("999");
            res = arr[0];
            sentiment = arr[1];
            if (res.isEmpty()) {
                try {
                    res = headlines.getDescription();
                } catch (Exception e) {
                    res = "Sorry! failed to generate a summary";
                }

            }
//            PyObject pyobj1 = py1.getModule("imdown");
//            PyObject obj1 = pyobj1.callAttr("main1",url);
//            imgUrl = obj1.toString();
//            if(imgUrl.isEmpty()){
//                imgUrl="https://.com/full/2112588.jpg";
//            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            dialog = new ProgressDialog(detailsView.this);
            dialog.setTitle("Generating summary please wait...");
            dialog.show();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            dialog.dismiss();
            //Picasso.get().load(imgUrl).into(img_news);
            Glide.with(detailsView.this).load(imgUrl).into(img_news);
            txt_content.setText(res);

            if (checkFav(headlines.getTitle(), detailsView.this)) {
                fav.setImageResource(R.drawable.hear);
            } else {
                fav.setImageResource(R.drawable.ic_baseline_favorite_24);
            }


            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkFav(headlines.getTitle(), detailsView.this)) {
                        removeFav(headlines.getTitle(), res, detailsView.this, fav);
                    } else {
                        addFav(headlines.getTitle(), res, fav);
                    }
                }
            });


            sentiment_txt.setText(sentiment.toUpperCase(Locale.ROOT));
            if (sentiment.toUpperCase(Locale.ROOT).equals("POSITIVE")) {
                sentiment_icon.setImageResource(R.drawable.ic_baseline_circle_24_green);
            } else if (sentiment.toUpperCase(Locale.ROOT).equals("NEGATIVE")) {
                sentiment_icon.setImageResource(R.drawable.ic_baseline_circle_24_red);
            } else {
                sentiment_icon.setImageResource(R.drawable.ic_baseline_circle_24_yellow);
            }

            //System.out.println(headlines.getDescription());
        }
    }

    public void addFav(String title, String content, ImageView imageView) {
        imageView.setImageResource(R.drawable.hear);
        new AddFav(title, content).start();
    }

    class AddFav extends Thread {
        FavouriteFire db = Room.databaseBuilder(detailsView.this, FavouriteFire.class, dbName).allowMainThreadQueries().build();
        FavouriteQuery query = db.favouriteQuery();

        String title;
        String content;

        AddFav(String title, String content) {
            this.title = title;
            this.content = content;
        }

        @Override
        public void run() {
            super.run();

            try {
                query.insertData(new FavouriteModel(title, content));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(detailsView.this, "Added to Favourite", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(detailsView.this, title, Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(detailsView.this, "Failed to add", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void removeFav(String title, String content, Context context, ImageView imageView) {
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
                            imageView.setImageResource(R.drawable.ic_baseline_favorite_24);
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

    private boolean checkFav(String title, Context context) {
        FavouriteFire db = Room.databaseBuilder(context, FavouriteFire.class, dbName).allowMainThreadQueries().build();
        FavouriteQuery userDao = db.favouriteQuery();
        if (userDao.is_exist(title)) {
            return true;
        } else {
            return false;
        }
    }

}