package com.example.newsapp;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.newsapp.Models.NewsHeadlines;
import com.squareup.picasso.Picasso;


public class detailsView extends AppCompatActivity {
    NewsHeadlines headlines;
    TextView txt_title,txt_author,txt_time,txt_content;
    Button read_more;
    ImageView img_news;
    String url;
    String res;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_title=findViewById(R.id.text_detail_title);
        txt_author=findViewById(R.id.text_detail_author);
        txt_time=findViewById(R.id.text_detail_time);
        txt_content=findViewById(R.id.text_detail_content);
        read_more=findViewById(R.id.read_more);
        img_news= findViewById(R.id.img_detail_news);


        headlines =(NewsHeadlines) getIntent().getSerializableExtra("data");

        txt_title.setText(headlines.getTitle());
        txt_author.setText(headlines.getAuthor());
        txt_time.setText(headlines.getPublishedAt());
//        txt_content.setText(headlines.getContent());
        Picasso.get().load(headlines.getUrlToImage()).into(img_news);

        url = headlines.getUrl();
//        str=headlines.getContent();
        //str="Actor Swara Bhasker is all set to tie the knot with Samajwadi Party\\'s state youth president Fahad Ahmad. After she announced the news of social media, their first engagement pictures surfaced online. The two opted for court marriage and said they submitted their papers on January 6. Also read: Swara Bhasker to marry Samajwadi Party leader Fahad Ahmad\\n\\nIn the first photo, Swara walked hand-in-hand with Fahad. For the D-day, Swara opted for a red saree with a beige embroidered blouse. She elevated her look with minimal jewellery and kept her hair open. Complementing her, Fahad wore beige kurta-pyjama with a red vest on top.\\n\\nIn few more pictures, the two were seen posing with each other. While Fahad held Swara close in all of them, in one, he placed a sweet kiss on her forehead. The actor also gave a close look at her engagement ring and mehendi.\\n\\nSwara Bhasker flaunted her mehendi on her Instagram Stories.\\n\\nAnnouncing the wedding news, Swara posted a video montage featuring a timeline of their relationship. From meeting during a protest to discussing each other\\'s tweets; the couple also adopted a pet cat together which eventually brought them closer. They finally decided to seal the deal last month and made it public on Thursday.\\n\\nSharing the video, Swara wrote in the caption of her post, “Sometimes you search far and wide for something that was right next to you all along. We were looking for love, but we found friendship first. And then we found each other! Welcome to my heart @FahadZirarAhmad It’s chaotic but it’s yours!” \"I never knew chaos can be so beautiful Thank you for holding my hand love,\" re-posted Fahad.\\n\\nReacting to the news, actor Sayani Gupta commented, “Oh wow! Yayyyy! This is just wishing you both the world full of happiness, peace, laughter and only good times! And yayyyyyy! @reallyswara you deserve the best and this is everything! @fahadzirarahmad welcome to the madness!” “Congratulations you both,” added Richa Chadha on Twitter. Maanvi Gagroo wished them, “Yay yay yay Congratulations you two.” Actor Shruti Seth called them ‘didi and jejuuuu’ in the comment section as well.\\n\\nSwara and Fahad met in 2020 during a rally and with time, fell in love. Swara who was last seen in Jahaan Chaar Yaar, will be next seen in Mimamsa and Mrs Falani.\\n\\nSHARE THIS ARTICLE ON";
//        Python.start(new AndroidPlatform(this));

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
            if (! Python.isStarted()) {
                Python.start(new AndroidPlatform(detailsView.this));
            }
            Python py = Python.getInstance();
            PyObject pyobj = py.getModule("myfile");
            PyObject obj = pyobj.callAttr("main",url);
            res= obj.toString();
            if (res.isEmpty()){
                res=headlines.getDescription();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            dialog= new ProgressDialog(detailsView.this);
            dialog.setTitle("Generating summary please wait...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            dialog.dismiss();
            txt_content.setText(res);
            //System.out.println(headlines.getDescription());
        }
    }
}