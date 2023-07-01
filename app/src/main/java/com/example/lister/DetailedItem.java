package com.example.lister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailedItem extends AppCompatActivity {
    private TextView detailGameTitle, detailGameGenre, detailGameReleaseDate;
    private ImageView detailGameImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_item);
        Intent intent = getIntent();
        Game game = intent.getParcelableExtra("key"); //if it's a string you stored.

        detailGameTitle = (TextView) findViewById(R.id.detailTitle);
        detailGameGenre= (TextView) findViewById(R.id.detailGenre);
        detailGameReleaseDate= (TextView) findViewById(R.id.detailReleaseDate);
        detailGameImage= (ImageView) findViewById(R.id.detailImage);

        detailGameTitle.setText(game.getGameTitle());
        detailGameGenre.setText(game.getGameGenre());
        detailGameReleaseDate.setText(game.getGameReleaseDate());
        Picasso.with(this)
                .load(game.getGameImage())
                .resize(80,80)
                .centerCrop()
                .into(detailGameImage);
    }
}