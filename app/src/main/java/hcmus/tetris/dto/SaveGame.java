package hcmus.tetris.dto;

public class SaveGame {
    String dateTime, otherContent;
    long score;

    public SaveGame(String dateTime, long score, String otherContent){
        this.dateTime = dateTime;
        this.score = score;
        this.otherContent = otherContent;
    }

    public String getDateTime() { return dateTime; }
    public Long getScore() { return score; }
    public String getOtherContent() { return otherContent; }
}
