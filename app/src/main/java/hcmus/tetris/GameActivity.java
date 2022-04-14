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
        PieceView nextPieceView = this.findViewById(R.id.nextPieceView);
        PieceView holdView = this.findViewById(R.id.holdPieceView);
        holdView.setOnClickListener((View) -> holdView.setPiece(boardView.hold()));
        boardView.setOnNextPieceListener(nextPieceView::setPiece);
        boardView.setOnLineClearListener(this);

        scoreView = this.findViewById(R.id.scoreTextView);
        scoreView.setText("0");

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
        scoreView.setText(score + "");
    }
}