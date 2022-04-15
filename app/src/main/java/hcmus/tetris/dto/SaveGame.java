package hcmus.tetris.dto;

public class SaveGame {
    String dateTime;
    long score;

    public SaveGame(String dateTime, long score){
        this.dateTime = dateTime;
        this.score = score;
    }

    public String getDateTime() { return dateTime;}
    public Long getScore() { return score;}
}
