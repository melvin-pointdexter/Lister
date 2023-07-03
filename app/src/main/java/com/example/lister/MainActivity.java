package com.example.lister;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListView;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* IDs to change around:
* G1mAnt6XF2S0RV4IFz4E - Worms Armageddon
* QN9zfqu44rIwbQAkILhY - Resident Evil
* SuHIcTFj9xtZXMS0TENE - Counter Strike Condition Zero
* WN9uowmX5MxjWXKySPkG - Just Cause 3
* jEYFSkTalk1NTQxbNiWj - Deus Ex
* xNF3M0OMLhMJXjPFTNX7 - Fallout 3
*  */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonAddGame, buttonDeleteGame, buttonEditGame, buttonGetByTitle, buttonGetByGid;
    private EditText gameTitle, gameGenre, gameReleaseDate, gameImage, gameId;

    private ListView mainList;
    private MainListAdapter adapter;
    public static ArrayList<Game> games;

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    public static final String KEY_GAME_TITLE = "gameTitle";
    public static final String KEY_GAME_GENRE = "gameGenre";
    public static final String KEY_GAME_RELEASE_DATE = "gameReleaseDate";
    public static final String KEY_GAME_IMAGE = "gameImage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getAllGames();      //initialize list

        gameTitle = (EditText)findViewById(R.id.inputTitle);
        gameGenre = (EditText)findViewById(R.id.inputGenre);
        gameReleaseDate = (EditText)findViewById(R.id.inputReleaseDate);
        gameImage = (EditText)findViewById(R.id.inputImage);
        gameId = (EditText)findViewById(R.id.inputGid);

        buttonAddGame = (Button)findViewById(R.id.btnAddGame);
        buttonDeleteGame = (Button)findViewById(R.id.btnDeleteGame);
        buttonEditGame = (Button)findViewById(R.id.btnEditGame);
        buttonGetByTitle = (Button)findViewById(R.id.btnGetByTitle);
        buttonGetByGid = (Button)findViewById(R.id.btnGetByGid);

        buttonAddGame.setOnClickListener(this);
        buttonDeleteGame.setOnClickListener(this);
        buttonEditGame.setOnClickListener(this);
        buttonGetByTitle.setOnClickListener(this);
        buttonGetByGid.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        Log.d("CREATE", "Entered onClick()");
        switch (view.getId()){
            case R.id.btnAddGame:
                createNewGame();
                break;
            case R.id.btnDeleteGame:
                deleteGameById(gameTitle.getText().toString());
                break;
            case R.id.btnEditGame:
                editGameById(gameId.getText().toString());
                break;
            case R.id.btnGetByTitle:
                getGameByTitle(gameId.getText().toString());
                break;
            case R.id.btnGetByGid:
                getGameByGid(gameId.getText().toString());
                break;
        }
    }

    private void editGameById(String gid) {
        if (gid.contains("/") || gid.contentEquals("")){
            Toast.makeText(getApplicationContext(), "Invalid gid", Toast.LENGTH_LONG).show();
            return;
        }
        DocumentReference document = database.collection("games").document(gid);
        document
                .update(
                        "gameTitle", gameTitle.getText().toString(),
                        "gameImage", gameImage.getText().toString(),
                        "gameGenre", gameGenre.getText().toString(),
                        "gameReleaseDate", gameReleaseDate.getText().toString()
                )
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Game updated", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "ERROR: " + e, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void deleteGameById(String gid) {
        if (gid.contains("/") || gid.contentEquals("")){
            Toast.makeText(getApplicationContext(), "Invalid gid", Toast.LENGTH_LONG).show();
            return;
        }
        database.collection("games").document(gid)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Game deleted (if gid exists)", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "ERROR: " + e, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void getGameByGid(String gid) {
        if (gid.contains("/") || gid.contentEquals("")){
                Toast.makeText(getApplicationContext(), "Invalid gid", Toast.LENGTH_LONG).show();
            return;
        }
        database
                .collection("games")
                .document(gid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot.exists()){
                                Game game = documentSnapshot.toObject(Game.class);

                                Intent myIntent = new Intent(MainActivity.this, DetailedItem.class);
                                myIntent.putExtra("key", game);
                                MainActivity.this.startActivity(myIntent);
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Game does not exist/ Invalid gid", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "ERROR: " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void getGameByTitle(String title) {
        database.collection("games")
                .whereEqualTo("gameTitle", title)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            //If there are many games by the same Title, show one of them (instead of opening an activity (using intent) for every game)
                            Game game=null;
                            //= new Game("Error","Something went wrong","01/01/1970","https://upload.wikimedia.org/wikipedia/commons/7/7d/Question_opening-closing.svg");
                            for (QueryDocumentSnapshot document : task.getResult()){
                                System.out.println(document.getId() + " ---> " + document.getData());
                                game = document.toObject(Game.class);
                            }
                            if (game==null){
                                Toast.makeText(getApplicationContext(), "Game does not exist", Toast.LENGTH_LONG).show();
                                return;
                            }
                            Intent myIntent = new Intent(MainActivity.this, DetailedItem.class);
                            myIntent.putExtra("key", game);
                            MainActivity.this.startActivity(myIntent);

                        }   else {
                            Toast.makeText(getApplicationContext(), "ERROR: " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void createNewGame() {
        Toast.makeText(getApplicationContext(), "Entered createNewGame()", Toast.LENGTH_LONG);
        Log.d("CREATE", "Entered createNewGame()");

        //GET PARAMETERS
        String gTitle = gameTitle.getText().toString();
        String gGenre = gameGenre.getText().toString();
        String gReleaseDate = gameReleaseDate.getText().toString();
        String gImage =gameImage.getText().toString();
        // "https://static.wikia.nocookie.net/fallout/images/c/c0/Fallout_3_cover_art.png"

        //MAPPING
        Map<String, Object> data = new HashMap<>();
        data.put(KEY_GAME_TITLE, gTitle);
        data.put(KEY_GAME_GENRE, gGenre);
        data.put(KEY_GAME_RELEASE_DATE, gReleaseDate);
        data.put(KEY_GAME_IMAGE, gImage);

        database
                .collection("games")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Game created", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "ERROR: " + e, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void getAllGames() {
        database
                .collection("games")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        games = new ArrayList<Game>();

                        if (task.isSuccessful()) {
                            games = new ArrayList<Game>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Game game = document.toObject(Game.class);
                                games.add(game);
                            }
                            mainList = findViewById(R.id.mainList);
                            adapter = new MainListAdapter(getApplicationContext(),games);
                            mainList.setAdapter(adapter);
                            mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                public void onItemClick(AdapterView<?> adpView, View v, int position, long id){
                                    Intent myIntent = new Intent(MainActivity.this, DetailedItem.class);
                                    myIntent.putExtra("key", games.get(position));
                                    MainActivity.this.startActivity(myIntent);
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "ERROR: " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}