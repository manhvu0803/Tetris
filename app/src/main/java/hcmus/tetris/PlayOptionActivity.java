package hcmus.tetris;

import androidx.appcompat.app.AppCompatActivity;

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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.btnPlayNew):{
                //move to create board screen
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