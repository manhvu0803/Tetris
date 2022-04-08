package hcmus.tetris;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomHighScoreRowAdapter extends ArrayAdapter<String> {
    Context context;
    String[] times, names, highscores;

    CustomHighScoreRowAdapter(Context context, int layoutToBeInflated,
                              String[] times, String[] names, String[] highscores){
        super(context, layoutToBeInflated, names);
        this.context = context;
        this.times = times;
        this.names = names;
        this.highscores = highscores;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(R.layout.custom_high_score_row, null);
        TextView txtTime = (TextView) row.findViewById(R.id.txtTime);
        TextView txtName = (TextView) row.findViewById(R.id.txtName);
        TextView txtHighScore = (TextView) row.findViewById(R.id.txtHighScore);

        txtTime.setText(times[position]);
        txtName.setText(names[position]);
        txtHighScore.setText(names[position]);

        return (row);
    }
}
