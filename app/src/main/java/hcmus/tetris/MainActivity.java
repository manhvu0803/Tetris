package hcmus.tetris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnPlay, btnSetting, btnViewScore, btnExitApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnPlay = findViewById(R.id.btnPlay);
        btnSetting = findViewById(R.id.btnSetting);
        btnViewScore = findViewById(R.id.btnViewScore);
        btnExitApp = findViewById(R.id.btnExitApp);

        btnPlay.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        btnViewScore.setOnClickListener(this);
        btnExitApp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.btnPlay):{
                startActivity(new Intent(getApplicationContext(), PlayOptionActivity.class));
                break;
            }
            case (R.id.btnSetting):{
                //move to setting screen
                break;
            }
            case (R.id.btnViewScore):{
                startActivity(new Intent(getApplicationContext(), ViewHighScoreActivity.class));
                break;
            }
            case (R.id.btnExitApp):{
                finish();
                break;
            }
        }
    }
}