package hcmus.tetris;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hcmus.tetris.dao.HighScoreDAO;
import hcmus.tetris.dto.HighScore;

public class ViewHighScoreActivity extends AppCompatActivity {
    //widget variables
    Button btnBackToMain;
    RecyclerView highScoreRecyclerView;

    //other variables
    HighScoreDAO highScoreDAO;
    CustomHighScoreAdapter adapter;
    ArrayList<HighScore> highScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get current high score list in database
        highScoreDAO = HighScoreDAO.getInstance();
        highScores = highScoreDAO.getHighScores(getApplicationContext());

        //reverse highScores list
        highScores.sort((obj1, obj2) -> obj2.getDateTime().compareTo(obj1.getDateTime()));

        setContentView(R.layout.activity_view_high_score);

        //get widgets
        btnBackToMain = findViewById(R.id.btnBackToMain);
        highScoreRecyclerView = findViewById(R.id.highScoreRecyclerView);

        //set OnClickListener
        btnBackToMain.setOnClickListener(view -> onBackPressed());

        //set adapter for high score recyclerview
        adapter = new CustomHighScoreAdapter(this, highScores);
        highScoreRecyclerView.setAdapter(adapter);
        highScoreRecyclerView.setVisibility(View.VISIBLE);
    }
}