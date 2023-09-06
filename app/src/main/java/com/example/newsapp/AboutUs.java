package com.example.newsapp;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
//import android.provider.CalendarContract;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Element adsElement = new Element();
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.infoo)
                .setDescription("Hello, I'm Parhtib Mitra, the developer of this app. As a student of B.Sc Computer Science at Sammilani Mahavidyalaya, I have put my effort and passion into creating an app that provides efficient and insightful news summaries. \n" +
                        "I have always been fascinated by technology and its potential to simplify our lives. With this app, my aim is to provide a seamless and convenient way for users to stay informed about current events. By leveraging my knowledge in programming and data analysis, I have incorporated features like article summarization and personalized favorites to enhance the user experience. I am committed to continuously improving the app to enhance your news reading experience.")
                .addItem(new Element().setTitle("Version 1.0"))
                .addGroup("CONNECT WITH US!")
                .addEmail("parthibmitra1278@gmail.com")
//                .addWebsite("Your website/")
//                .addYoutube("UCbekhhidkzkGryM7mi5Ys_w")   //Enter your youtube link here (replace with my channel link)
//                .addPlayStore("com.example.yourprojectname")   //Replace all this with your package name
//                .addInstagram("jarves.usaram")    //Your instagram id
                .addItem(createCopyright())
                .create();
        setContentView(aboutPage);
    }
    private Element createCopyright()
    {
        Element copyright = new Element();
        @SuppressLint("DefaultLocale") final String copyrightString = String.format("Copyright %d by Parthib Mitra", Calendar.getInstance().get(Calendar.YEAR));
        copyright.setTitle(copyrightString);
        // copyright.setIcon(R.mipmap.ic_launcher);
        copyright.setGravity(Gravity.CENTER);
        copyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutUs.this,copyrightString,Toast.LENGTH_SHORT).show();
            }
        });
        return copyright;
    }
}