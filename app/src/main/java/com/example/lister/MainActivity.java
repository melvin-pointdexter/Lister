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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonAddGame, buttonDeleteGame, buttonEditGame;
    private EditText gameTitle, gameGenre, gameReleaseDate, gameImage;

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

        buttonAddGame = (Button)findViewById(R.id.btnAddGame);
        buttonDeleteGame = (Button)findViewById(R.id.btnDeleteGame);
        buttonEditGame = (Button)findViewById(R.id.btnEditGame);

        buttonAddGame.setOnClickListener(this);
        buttonDeleteGame.setOnClickListener(this);
        buttonEditGame.setOnClickListener(this);
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
                editGameById(gameTitle.getText().toString());
                break;
        }
    }

    private void editGameById(String gid) {
        DocumentReference document = database.collection("games").document(gid);
        document
                .update(
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
        database.collection("games").document(gid)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Game deleted", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "ERROR: " + e, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void getGameById(String gid) {
        DocumentReference document = database
                .collection("games")
                .document(gid);

        document
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot.exists()){
                                System.out.println(documentSnapshot.getId() + " -> " + documentSnapshot.getData());
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Doc does not exist", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "ERROR: " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void getGameByValue(String para) {
        database.collection("games")
                .whereEqualTo("gameTitle", para)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                System.out.println(document.getId() + " ---> " + document.getData());
                            }
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
                                    Toast.makeText(MainActivity.this, "Test", Toast.LENGTH_LONG).show();
                                    Intent myIntent = new Intent(MainActivity.this, DetailedItem.class);
                                    myIntent.putExtra("key", games.get(position)); //Optional parameters
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