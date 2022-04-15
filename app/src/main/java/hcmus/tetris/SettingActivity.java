package hcmus.tetris;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import hcmus.tetris.dao.SettingDAO;
import hcmus.tetris.dto.Setting;

public class SettingActivity extends AppCompatActivity {
    //widget variables
    EditText editWinScore, editRowScore, editBoardWidth, editBoardHeight;
    EditText editSpeedDrop1, editTime1, editSpeedDrop2, editTime2;
    EditText editSpeedDrop3, editTime3, editSpeedDrop4, editTime4;
    EditText editSpeedDrop5, editTime5, editSpeedDrop6, editTime6;
    EditText editSpeedDrop7, editTime7, editSpeedDrop8, editTime8;
    EditText editSpeedDrop9, editTime9, editSpeedDrop10, editTime10;
    RadioGroup rgControlBlock;
    RadioButton dragDrop, tap;
    Button btnSave, btnCancel;

    //other variables
    SettingDAO settingDAO;
    final ArrayList<EditText> timeLevelEditTextList = new ArrayList<>();
    Setting setting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get current setting in database
        settingDAO = SettingDAO.getInstance();
        setting = settingDAO.getSetting(getApplicationContext());

        setContentView(R.layout.activity_setting);

        //get widgets
        getWidgets();

        //set data for widgets
        setWidgetsData();

        //set OnClickListener
        setOnClickListener();
    }

    private void getWidgets(){
        editWinScore = findViewById(R.id.editWinScore);
        editRowScore = findViewById(R.id.editRowScore);
        editBoardWidth = findViewById(R.id.editBoardWidth);
        editBoardHeight = findViewById(R.id.editBoardHeight);

        editSpeedDrop1 = findViewById(R.id.editSpeedDrop1);
        editTime1 = findViewById(R.id.editTime1);
        timeLevelEditTextList.add(editTime1);
        timeLevelEditTextList.add(editSpeedDrop1);

        editSpeedDrop2 = findViewById(R.id.editSpeedDrop2);
        editTime2 = findViewById(R.id.editTime2);
        timeLevelEditTextList.add(editTime2);
        timeLevelEditTextList.add(editSpeedDrop2);

        editSpeedDrop3 = findViewById(R.id.editSpeedDrop3);
        editTime3 = findViewById(R.id.editTime3);
        timeLevelEditTextList.add(editTime3);
        timeLevelEditTextList.add(editSpeedDrop3);

        editSpeedDrop4 = findViewById(R.id.editSpeedDrop4);
        editTime4 = findViewById(R.id.editTime4);
        timeLevelEditTextList.add(editTime4);
        timeLevelEditTextList.add(editSpeedDrop4);

        editSpeedDrop5 = findViewById(R.id.editSpeedDrop5);
        editTime5 = findViewById(R.id.editTime5);
        timeLevelEditTextList.add(editTime5);
        timeLevelEditTextList.add(editSpeedDrop5);

        editSpeedDrop6 = findViewById(R.id.editSpeedDrop6);
        editTime6 = findViewById(R.id.editTime6);
        timeLevelEditTextList.add(editTime6);
        timeLevelEditTextList.add(editSpeedDrop6);

        editSpeedDrop7 = findViewById(R.id.editSpeedDrop7);
        editTime7 = findViewById(R.id.editTime7);
        timeLevelEditTextList.add(editTime7);
        timeLevelEditTextList.add(editSpeedDrop7);

        editSpeedDrop8 = findViewById(R.id.editSpeedDrop8);
        editTime8 = findViewById(R.id.editTime8);
        timeLevelEditTextList.add(editTime8);
        timeLevelEditTextList.add(editSpeedDrop8);

        editSpeedDrop9 = findViewById(R.id.editSpeedDrop9);
        editTime9 = findViewById(R.id.editTime9);
        timeLevelEditTextList.add(editTime9);
        timeLevelEditTextList.add(editSpeedDrop9);

        editSpeedDrop10 = findViewById(R.id.editSpeedDrop10);
        editTime10 = findViewById(R.id.editTime10);
        timeLevelEditTextList.add(editTime10);
        timeLevelEditTextList.add(editSpeedDrop10);

        rgControlBlock = findViewById(R.id.rgControlBlock);
        dragDrop = findViewById(R.id.dragDrop);
        tap = findViewById(R.id.tap);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
    }

    @SuppressLint("SetTextI18n")
    private void setWidgetsData(){
        //scores
        editWinScore.setText(Long.toString(setting.getScoreLimit()));
        editRowScore.setText(Long.toString(setting.getLineScore()));

        //levels
        int levelNum = setting.getTimeLevels().size();
        ArrayList<ArrayList<Integer>> timeLevels = setting.getTimeLevels();
        int j = 0;
        for (int i = 0; i < levelNum; i++){
            timeLevelEditTextList.get(j).setText(timeLevels.get(i).get(0).toString());
            timeLevelEditTextList.get(j + 1).setText(timeLevels.get(i).get(1).toString());
            j += 2;
        }

        //control scheme
        String controlScheme = setting.getControlScheme();
        if (controlScheme.equals("tap")) tap.setChecked(true);
        else dragDrop.setChecked(true);

        //board's size
        editBoardWidth.setText(Integer.toString(setting.getWidth()));
        editBoardHeight.setText(Integer.toString(setting.getHeight()));
    }

    private void setOnClickListener(){
        btnSave.setOnClickListener(view -> saveSetting());
        btnCancel.setOnClickListener(view -> onBackPressed());
    }

    private void saveSetting(){
        ArrayList<String> otherValuesToSave = new ArrayList<>();
        otherValuesToSave.add(editWinScore.getText().toString());
        otherValuesToSave.add(editRowScore.getText().toString());
        if (rgControlBlock.getCheckedRadioButtonId() == tap.getId()) otherValuesToSave.add("tap");
        else otherValuesToSave.add("drag_drop");
        otherValuesToSave.add(editBoardWidth.getText().toString());
        otherValuesToSave.add(editBoardHeight.getText().toString());

        int saveResult = settingDAO.saveSettingToDTB(getApplicationContext(), otherValuesToSave,
                                                    timeLevelEditTextList);
        String message = "Lưu cài đặt thành công";
        if (saveResult < 1) message = "Có lỗi trong quá trình lưu (mã lỗi: " + saveResult + ")";
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}