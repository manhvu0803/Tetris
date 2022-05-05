package hcmus.tetris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;

import hcmus.tetris.dao.HighScoreDAO;
import hcmus.tetris.dto.HighScore;
import hcmus.tetris.ults.DAOHelper;

public class SaveScoreActivity extends AppCompatActivity implements View.OnClickListener{
    //widget variables
    TextView reachedScoreTextView;
    EditText nameEditText;
    Button saveScoreButton, cancelSaveScoreButton;

    //other variables
    final HighScoreDAO highScoreDAO = HighScoreDAO.getInstance();
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_score);
        score = getIntent().getIntExtra("score", -1);

        //get widgets
        reachedScoreTextView = this.findViewById(R.id.reachedScoreTextView);
        nameEditText = this.findViewById(R.id.nameEditText);
        saveScoreButton = this.findViewById(R.id.saveScoreButton);
        cancelSaveScoreButton = this.findViewById(R.id.cancelSaveScoreButton);

        //set data
        String text = score + "!";
        reachedScoreTextView.setText(text);

        //set OnClickListener
        cancelSaveScoreButton.setOnClickListener(this);
        saveScoreButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(SaveScoreActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        switch (view.getId()) {
            case (R.id.saveScoreButton): {
                String name = nameEditText.getText().toString();
                String message;
                if (name.isEmpty()) {
                    message = "Vui lòng điền tên";
                }
                else {
                    int saveResult = highScoreDAO.saveHighScoreToDTB(getApplicationContext(),
                            new HighScore(name, DAOHelper.formatDateTime(LocalDateTime.now()), score));
                    if (saveResult == 1) {
                        message = "Lưu điểm cao thành công";
                        startActivity(intent);
                    }
                    else message = "Có lỗi xảy ra trong quá trình lưu";
                }
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                break;
            }
            case (R.id.cancelSaveScoreButton): {
                startActivity(intent);
                break;
            }
        }
    }
}