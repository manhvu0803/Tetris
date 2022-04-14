package hcmus.tetris;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity implements Board.OnLineClearListener {
    BoardView boardView;
    TextView scoreView;
    int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        boardView = this.findViewById(R.id.boardView);
        NextPieceView nextPieceView = this.findViewById(R.id.nextPieceView);
        boardView.setOnNextPieceListener(nextPieceView::setNext);

        boolean createNewGame = this.getIntent().getBooleanExtra("newGame", true);
    }

    @Override
    protected void onPause() {
        boardView.pause();
        super.onPause();
    }

    @Override
    public void onLineClear(int row, int addScore) {
        score += addScore;
        scoreView.setText(score);
    }
}