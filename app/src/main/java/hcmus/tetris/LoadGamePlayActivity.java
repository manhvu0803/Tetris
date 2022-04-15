package hcmus.tetris;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import hcmus.tetris.dto.SaveGame;

public class LoadGamePlayActivity extends AppCompatActivity {
    Button btnBackToMain;
    RecyclerView saveGameRecyclerView;
    final ArrayList<SaveGame> saveGames = new ArrayList<>();
    CustomSaveGameAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_game_play);

        btnBackToMain = findViewById(R.id.btnBackToMain);
        saveGameRecyclerView = findViewById(R.id.saveGameRecyclerView);

        btnBackToMain.setOnClickListener(view -> onBackPressed());

        for (int i = 0; i < 10; i++){
            saveGames.add(new SaveGame("11/04/2022 12:12:00", i * 1000));
        }

        adapter = new CustomSaveGameAdapter(this, saveGames);
        saveGameRecyclerView.setAdapter(adapter);
        saveGameRecyclerView.setVisibility(View.VISIBLE);
    }
}