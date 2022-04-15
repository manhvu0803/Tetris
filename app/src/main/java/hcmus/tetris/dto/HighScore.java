package hcmus.tetris.dto;

public class HighScore {
    String name, dateTime;
    long score;

    public HighScore(String name, String dateTime, long score){
        this.name = name;
        this.dateTime = dateTime;
        this.score = score;
    }

    public String getName() { return name; }
    public String getDateTime() { return dateTime; }
    public long getScore() { return score; }
}
