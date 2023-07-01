package com.example.lister;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

//import org.checkerframework.checker.nullness.qual.NonNull;

public class MainListAdapter extends BaseAdapter {

    Context context;
    //String listNames[];
    //String listGenres[];
    //String listImages[];
    ArrayList<Game> gameList;
    LayoutInflater inflater;

    public MainListAdapter(Context context, ArrayList<Game> data){
        //String[] listNames, String[] listGenres, String[] listImages
        this.context=context;
        //this.listNames=listNames;
        //this.listGenres=listGenres;
        //this.listImages=listImages;
        this.gameList=data;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return gameList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_main_list,null);
        TextView txtTitle = (TextView) view.findViewById(R.id.itemTitle);
        TextView txtGenre = (TextView) view.findViewById(R.id.itemGenre);
        ImageView imgView = (ImageView) view.findViewById(R.id.itemImage);
        txtTitle.setText(gameList.get(position).getGameTitle());
        txtGenre.setText(gameList.get(position).getGameGenre());
        //imgView.setImageResource(R.drawable.ic_launcher_foreground);
        Picasso.with(context)
                .load(gameList.get(position).getGameImage())
                .resize(80,80)
                .centerCrop()
                .into(imgView);
        return view;
    }
}
