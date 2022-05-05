package hcmus.tetris;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hcmus.tetris.databinding.CustomSaveGameRowBinding;
import hcmus.tetris.dto.SaveGame;
import hcmus.tetris.listeners.ItemCLickListener;

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
        return new CustomSaveGameAdapter.SaveGameViewHolder(
                CustomSaveGameRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SaveGame saveGame = saveGames.get(position);
        ((CustomSaveGameAdapter.SaveGameViewHolder) holder).setData(saveGame);

        ((SaveGameViewHolder) holder).setItemCLickListener((view, position1) -> {
            String dateTime = saveGame.getDateTime(), otherContent = saveGame.getOtherContent();
            long score = saveGame.getScore();
            Intent playGameIntent = new Intent(context, GameActivity.class);
            playGameIntent.putExtra("newGame", false);
            playGameIntent.putExtra("dateTime", dateTime);
            playGameIntent.putExtra("otherContent", otherContent);
            playGameIntent.putExtra("score", score);
            context.startActivity(playGameIntent);
        });
    }

    @Override
    public int getItemCount() {
        return saveGames.size();
    }

    private static class SaveGameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final CustomSaveGameRowBinding binding;
        private ItemCLickListener itemCLickListener;
        public SaveGameViewHolder(CustomSaveGameRowBinding customSaveGameBinding) {
            super(customSaveGameBinding.getRoot());
            binding = customSaveGameBinding;
            binding.getRoot().setOnClickListener(this);
        }
        @SuppressLint("SetTextI18n")
        public void setData(SaveGame saveGame)
        {
            binding.txtTime.setText(saveGame.getDateTime());
            binding.txtHighScore.setText(Long.toString(saveGame.getScore()));
        }

        public void setItemCLickListener(ItemCLickListener itemCLickListener){
            this.itemCLickListener = itemCLickListener;
        }

        @Override
        public void onClick(View view) {
            itemCLickListener.onCLick(view, getAdapterPosition());
        }
    }
}
