package hcmus.tetris;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class GameActivity extends AppCompatActivity {
    BoardView boardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        boardView = this.findViewById(R.id.boardView);

        boolean createNewGame = this.getIntent().getBooleanExtra("newGame", true);
    }

    @Override
    protected void onPause() {
        boardView.pause();
        super.onPause();
    }
}