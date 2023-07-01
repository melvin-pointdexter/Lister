package com.example.lister;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.ListView;
import java.util.ArrayList;
import android.os.Bundle;

public class mainList extends AppCompatActivity {
    //ListView gameListView;
    //private MainListAdapter gameAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        /*Intent i = this.getIntent();
        ArrayList<Game> gameList = i.getParcelableArrayListExtra("list");

        gameListView = findViewById(R.id.mainList);
        gameAdapter = new MainListAdapter(getApplicationContext(),gameList);
        gameListView.setAdapter(gameAdapter);*/
    }
}