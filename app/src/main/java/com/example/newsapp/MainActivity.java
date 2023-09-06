package com.example.newsapp;

import androidx.annotation.GravityInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.newsapp.Models.NewsApiResponse;
import com.example.newsapp.Models.NewsHeadlines;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SelectListener, View.OnClickListener {

    RecyclerView recyclerView;
    CustomAdapter adapter;
    Button b1, b2, b3, b4, b5, b6, b7;
    SearchView searchView;
    String category;
    ShimmerFrameLayout shimmerFrameLayout;
    FirebaseAuth auth;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);
            finish();
        } else {

        }
        searchView = findViewById(R.id.search_view);
        recyclerView = findViewById(R.id.recycler_main);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        shimmerFrameLayout.startShimmer();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                RequestManager manager = new RequestManager(MainActivity.this);
                manager.getNewsHeadlines(listener, category, query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        b1=findViewById(R.id.btn_1);
        b1.setOnClickListener(this);
        b2=findViewById(R.id.btn_2);
        b2.setOnClickListener(this);
        b3=findViewById(R.id.btn_3);
        b3.setOnClickListener(this);
        b4=findViewById(R.id.btn_4);
        b4.setOnClickListener(this);
        b5=findViewById(R.id.btn_5);
        b5.setOnClickListener(this);
        b6=findViewById(R.id.btn_6);
        b6.setOnClickListener(this);
        b7=findViewById(R.id.btn_7);
        b7.setOnClickListener(this);

        RequestManager manager = new RequestManager(this);
        manager.getNewsHeadlines(listener, "general",null);


    }

    private final OnFetchDataListener<NewsApiResponse> listener=new OnFetchDataListener<NewsApiResponse>() {
        @Override
        public void onFetchData(List<NewsHeadlines> list, String message) {
            if(list.isEmpty()){
                Toast.makeText(MainActivity.this, "No data found", Toast.LENGTH_SHORT).show();
            }
            else {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                showNews(list);

            }

        }

        @Override
        public void onError(String message) {
            Toast.makeText(MainActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
    };

    private void showNews(List<NewsHeadlines> list) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        adapter= new CustomAdapter(this, list,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void OnNewsClicked(NewsHeadlines headlines) {
        startActivity(new Intent(MainActivity.this, detailsView.class)
                .putExtra("data",headlines));
    }

    @Override
    public void onClick(View v) {
        Button button=(Button) v;
        category = button.getText().toString();
        searchView.setQueryHint(category);
        RequestManager manager = new RequestManager(this);
        manager.getNewsHeadlines(listener, category,null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.about_us:
                startActivity(new Intent(MainActivity.this, AboutUs.class));
                break;

            case R.id.sign_out:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
                finish();
                break;

            case R.id.summarizer:
                startActivity(new Intent(MainActivity.this, Summarizer.class));
                break;


            case R.id.favourite:
                startActivity(new Intent(MainActivity.this, Favourite.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}