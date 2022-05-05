package hcmus.tetris;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hcmus.tetris.dao.SaveGamesDAO;
import hcmus.tetris.dto.SaveGame;

public class LoadGamePlayActivity extends AppCompatActivity {
    //widget variables
    Button btnBackToMain;
    RecyclerView saveGameRecyclerView;

    //other variables
    final SaveGamesDAO saveGamesDAO = SaveGamesDAO.getInstance();
    CustomSaveGameAdapter adapter;
    ArrayList<SaveGame> saveGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get current high score list in database
        saveGames = saveGamesDAO.getSaveGames(getApplicationContext());

        //reverse highScores list
        saveGames.sort((obj1, obj2) -> obj2.getDateTime().compareTo(obj1.getDateTime()));

        setContentView(R.layout.activity_load_game_play);

        //get widgets
        btnBackToMain = findViewById(R.id.btnBackToMain);
        saveGameRecyclerView = findViewById(R.id.saveGameRecyclerView);

        //set OnClickListener
        btnBackToMain.setOnClickListener(view -> onBackPressed());

        //set adapter for save games recyclerview
        adapter = new CustomSaveGameAdapter(this, saveGames);
        saveGameRecyclerView.setAdapter(adapter);
        saveGameRecyclerView.setVisibility(View.VISIBLE);
    }
}