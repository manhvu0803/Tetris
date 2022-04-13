package hcmus.tetris;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hcmus.tetris.databinding.CustomSaveGameRowBinding;

public class CustomSaveGameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<SaveGame> saveGames;

    CustomSaveGameAdapter(Context context, ArrayList<SaveGame> saveGames){
        this.context = context;
        this.saveGames = saveGames;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        System.out.println("OK");
        return new CustomSaveGameAdapter.SaveGameViewHolder(
                CustomSaveGameRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((CustomSaveGameAdapter.SaveGameViewHolder) holder).setData(saveGames.get(position));
        System.out.println("OK2");
    }

    @Override
    public int getItemCount() {
        return saveGames.size();
    }

    private static class SaveGameViewHolder extends RecyclerView.ViewHolder{
        private final CustomSaveGameRowBinding binding;
        public SaveGameViewHolder(CustomSaveGameRowBinding customSaveGameBinding) {
            super(customSaveGameBinding.getRoot());
            binding = customSaveGameBinding;
        }
        @SuppressLint("SetTextI18n")
        public void setData(SaveGame saveGame)
        {
            binding.txtTime.setText(saveGame.dateTime);
            binding.txtHighScore.setText(Long.toString(saveGame.score));
        }
    }
}
