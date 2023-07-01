package com.example.lister;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

    //String sampleText[]={"Hello World","Lorem Corpus", "Banana"};

    //ELEMENTS
    private Button buttonAddGame;
    private EditText gameTitle, gameGenre, gameReleaseDate, gameImage;

    private ListView mainList;
    private MainListAdapter adapter;

    public static ArrayList<Game> games;
    //private ProductAdapter productAdapter;

    //FIREBASE-FIRESTORE
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
        buttonAddGame.setOnClickListener(this);

        //mainList = (ListView) findViewById(R.id.mainList);
        //adapter = new MainListAdapter(getApplicationContext(),games);
        //mainList.setAdapter(adapter);
    }
    @Override
    public void onClick(View view) {
        Log.d("CREATE", "Entered onClick()");
        switch (view.getId()){
            case R.id.btnAddGame:
                createNewGame();
                break;
            //case R.id.btnDeleteGame:
                //deleteGameById("1Pa16uOY5KrX4NHALa4w");
                //break;
            /*case R.id.get_all_products_btn:
                getAllProducts();
                break;
            case R.id.mainList:
                getProductById("1Pa16uOY5KrX4NHALa4w");
                break;
            case R.id.get_product_by_para_btn:
                getProductByValue("iPad pro 12.9");
                break;
            case R.id.edit_product_btn:
                editProductById("WPcoBu3jSH2dwFMYVl6B");
                break;*/
        }
    }

    private void editGameById(String gid) {
        DocumentReference document = database.collection("games").document(gid);
        document
                .update(
                        "gameReleaseDate", "5900"
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
                        Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_LONG).show();
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
                                System.out.println(documentSnapshot.getId() + " ---> " + documentSnapshot.getData());
                            } else {
                                Toast.makeText(getApplicationContext(), "No doc for you", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error: " + task.getException(), Toast.LENGTH_LONG).show();
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
                            Toast.makeText(getApplicationContext(), "Error: " + task.getException(), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_LONG).show();
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
                            //Intent intent = new Intent(getApplicationContext(), mainList.class);
                            //intent.putParcelableArrayListExtra("list", games);
                            //startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error: " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}