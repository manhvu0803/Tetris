package hcmus.tetris;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class SaveScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_score);
        int score = getIntent().getIntExtra("score", -1);

        TextView scoreView = this.findViewById(R.id.reachedScoreTextView);
        scoreView.setText(score + "!");
    }
}