package hcmus.tetris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PlayOptionActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnPlayNew, btnLoadGame, btnBackToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_option);
        btnPlayNew = (Button) findViewById(R.id.btnPlayNew);
        btnLoadGame = (Button) findViewById(R.id.btnLoadGame);
        btnBackToMain = (Button) findViewById(R.id.btnBackToMain);

        btnPlayNew.setOnClickListener(this);
        btnLoadGame.setOnClickListener(this);
        btnBackToMain.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.btnPlayNew):{
                startActivity(new Intent(getApplicationContext(), GameActivity.class));
                break;
            }
            case (R.id.btnLoadGame):{
                //go to load screen
                break;
            }
            case (R.id.btnBackToMain):{
                onBackPressed();
                break;
            }
        }
    }
}