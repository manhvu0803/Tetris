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
    ArrayList<HighScore> highScores;

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
