package hcmus.tetris;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hcmus.tetris.databinding.CustomHighScoreRowBinding;

public class CustomHighScoreRowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    //String[] times, names, highscores;
    ArrayList<HighScore> highScores;

//    CustomHighScoreRowAdapter(Context context, int layoutToBeInflated,
//                              String[] times, String[] names, String[] highscores){
//        super(context, layoutToBeInflated, names);
//        this.context = context;
//        this.times = times;
//        this.names = names;
//        this.highscores = highscores;
//    }
    CustomHighScoreRowAdapter(Context context, ArrayList<HighScore> highScores){
        this.context = context;
        this.highScores = highScores;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        System.out.println("OK");
        return new HighScoreViewHolder(
                CustomHighScoreRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((HighScoreViewHolder) holder).setData(highScores.get(position));
        System.out.println("OK2");
    }

    @Override
    public int getItemCount() {
        return highScores.size();
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
//        View row = inflater.inflate(R.layout.custom_high_score_row, null);
//        TextView txtTime = (TextView) row.findViewById(R.id.txtTime);
//        TextView txtName = (TextView) row.findViewById(R.id.txtName);
//        TextView txtHighScore = (TextView) row.findViewById(R.id.txtHighScore);
//
//        txtTime.setText(times[position]);
//        txtName.setText(names[position]);
//        txtHighScore.setText(names[position]);
//
//        return (row);
//    }

    private static class HighScoreViewHolder extends RecyclerView.ViewHolder{
        private final CustomHighScoreRowBinding binding;
        public HighScoreViewHolder(CustomHighScoreRowBinding customHighScoreRowBinding) {
            super(customHighScoreRowBinding.getRoot());
            binding = customHighScoreRowBinding;
        }
        @SuppressLint("SetTextI18n")
        public void setData(HighScore highScore)
        {
            binding.txtTime.setText(highScore.dateTime);
            binding.txtName.setText(highScore.name);
            binding.txtHighScore.setText(Long.toString(highScore.score));
        }
    }
}
