package hcmus.tetris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import hcmus.tetris.dao.SettingDAO;
import hcmus.tetris.dto.Setting;
import hcmus.tetris.gameplay.Board;
import hcmus.tetris.gameplay.BoardView;
import hcmus.tetris.gameplay.PieceView;

public class GameActivity extends AppCompatActivity implements Board.OnLineClearListener, Board.OnGameOverListener {
    BoardView boardView;
    TextView scoreView;
    int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Setting setting = SettingDAO.getInstance().getSetting(getApplicationContext());

        boardView = this.findViewById(R.id.boardView);

        PieceView holdView = this.findViewById(R.id.holdPieceView);
        holdView.setOnClickListener((View) -> holdView.setPiece(boardView.hold()));

        PieceView nextPieceView = this.findViewById(R.id.nextPieceView);
        boardView.setOnNextPieceListener(nextPieceView::setPiece);

        boardView.setOnLineClearListener(this);
        boardView.setOnGameOverListener(this);

        boardView.setLineClearScore((int)setting.getLineScore());

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
        String scoreString = score + "";
        scoreView.setText(scoreString);
    }

    @Override
    public void onGameOver() {
        Intent intent = new Intent(this, SaveScoreActivity.class);
        intent.putExtra("score", score);
        startActivity(intent);
        finish();
    }
}