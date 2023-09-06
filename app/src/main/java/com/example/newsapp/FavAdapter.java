package com.example.newsapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.newsapp.Models.FavouriteData;
import com.example.newsapp.room.FavouriteFire;
import com.example.newsapp.room.FavouriteQuery;

import java.util.ArrayList;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.FavViewHolder> {

    private Context context;
    private ArrayList<FavouriteData> list;
    public static String dbName = "favDb";

    public FavAdapter(Context context, ArrayList<FavouriteData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favourite, parent, false);
        return new FavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.content.setText(list.get(position).getContent() + "\n");
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Favourite f = new Favourite();
                f.removeFav(list.get(position).getTitle(), list.get(position).getContent() + "\n", context);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size() - list.size() / 2;
    }

    public class FavViewHolder extends RecyclerView.ViewHolder {

        private TextView title, content;
        CardView cardView;

        public FavViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.fav_title);
            content = itemView.findViewById(R.id.fav_content);
            cardView = itemView.findViewById(R.id.card_fav);


        }
    }


}
