package hcmus.tetris;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hcmus.tetris.databinding.CustomHighScoreRowBinding;
import hcmus.tetris.dto.HighScore;

public class CustomHighScoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<HighScore> highScores;

    CustomHighScoreAdapter(Context context, ArrayList<HighScore> highScores){
        this.context = context;
        this.highScores = highScores;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HighScoreViewHolder(
                CustomHighScoreRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((HighScoreViewHolder) holder).setData(highScores.get(position), position, highScores.size());
    }

    @Override
    public int getItemCount() {
        return highScores.size();
    }

    private static class HighScoreViewHolder extends RecyclerView.ViewHolder{
        private final CustomHighScoreRowBinding binding;
        public HighScoreViewHolder(CustomHighScoreRowBinding customHighScoreRowBinding) {
            super(customHighScoreRowBinding.getRoot());
            binding = customHighScoreRowBinding;
        }
        @SuppressLint("SetTextI18n")
        public void setData(HighScore highScore, int position, int rowNum)
        {
            binding.txtTime.setText(highScore.getDateTime());
            binding.txtName.setText(highScore.getName());
            binding.txtHighScore.setText(Long.toString(highScore.getScore()));
            if (position == rowNum - 1) {
                binding.viewDivider.setVisibility(View.INVISIBLE);
            }
            else binding.viewDivider.setVisibility(View.VISIBLE);
        }
    }
}
