package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.util.Locale;

public class Summarizer extends AppCompatActivity {

    Button submit, reset;
    TextView content, sentiment_txt;
    EditText pasteLink;
    String url, title;
    String res, sentiment;
    ProgressDialog dialog;
    ImageView sentiment_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summarizer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        submit = findViewById(R.id.submit);
        pasteLink = findViewById(R.id.pasteLink);
        reset = findViewById(R.id.reset);
        content = findViewById(R.id.content);

        sentiment_txt = findViewById(R.id.sentiment_txt);
        sentiment_icon = findViewById(R.id.sentiment_icon);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = pasteLink.getText().toString();
                Log.i("hello", url);
                new multi().execute();

            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pasteLink.getText().clear();
            }
        });

    }

    public class multi extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... strings) {
            if (!Python.isStarted()) {
                Python.start(new AndroidPlatform(Summarizer.this));
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

                res = "Sorry! failed to generate a summary";

            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            dialog = new ProgressDialog(Summarizer.this);
            dialog.setTitle("Generating summary please wait...");
            dialog.show();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            content.setText(res + "\n");
            sentiment_txt.setText(sentiment.toUpperCase(Locale.ROOT));
            if (sentiment.toUpperCase(Locale.ROOT).equals("POSITIVE")) {
                sentiment_icon.setImageResource(R.drawable.ic_baseline_circle_24_green);
            } else if (sentiment.toUpperCase(Locale.ROOT).equals("NEGATIVE")) {
                sentiment_icon.setImageResource(R.drawable.ic_baseline_circle_24_red);
            } else {
                sentiment_icon.setImageResource(R.drawable.ic_baseline_circle_24_yellow);
            }

        }
    }
}