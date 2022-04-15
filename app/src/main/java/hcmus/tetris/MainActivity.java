package hcmus.tetris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import hcmus.tetris.ults.DAOHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnPlayNew, btnLoadGame, btnSetting, btnViewScore, btnExitApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //create database if needed
        DAOHelper.createDatabaseIfNotExists(getApplicationContext());

        setContentView(R.layout.activity_main);

        //get widgets
        btnPlayNew = findViewById(R.id.btnPlayNew);
        btnLoadGame = findViewById(R.id.btnLoadGame);
        btnSetting = findViewById(R.id.btnSetting);
        btnViewScore = findViewById(R.id.btnViewScore);
        btnExitApp = findViewById(R.id.btnExitApp);

        //set OnClickListener
        btnPlayNew.setOnClickListener(this);
        btnLoadGame.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        btnViewScore.setOnClickListener(this);
        btnExitApp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.btnPlayNew):{
                startActivity(new Intent(getApplicationContext(), GameActivity.class));
                break;
            }
            case (R.id.btnLoadGame):{
                startActivity(new Intent(getApplicationContext(), LoadGamePlayActivity.class));
                break;
            }
            case (R.id.btnSetting):{
                startActivity(new Intent(getApplicationContext(), SettingActivity.class));
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