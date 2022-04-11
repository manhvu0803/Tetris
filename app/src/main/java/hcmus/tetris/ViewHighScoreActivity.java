package hcmus.tetris;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewHighScoreActivity extends AppCompatActivity {
    Button btnBackToMain;
    RecyclerView highScoreRecyclerView;
    final ArrayList<HighScore> highScores = new ArrayList<>();
    CustomHighScoreRowAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_high_score);

        btnBackToMain = findViewById(R.id.btnBackToMain);
        highScoreRecyclerView = findViewById(R.id.highScoreRecyclerView);

        btnBackToMain.setOnClickListener(view -> onBackPressed());

        for (int i = 0; i < 10; i++){
            highScores.add(new HighScore("A" + i, "11/04/2022 12:12:00", i * 1000));
        }

        adapter = new CustomHighScoreRowAdapter(this, highScores);
        highScoreRecyclerView.setAdapter(adapter);
        highScoreRecyclerView.setVisibility(View.VISIBLE);
    }
}