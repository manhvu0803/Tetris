package hcmus.tetris;

public class HighScore {
    String name, dateTime;
    long score;

    public HighScore(String name, String dateTime, long score){
        this.name = name;
        this.dateTime = dateTime;
        this.score = score;
    }
}
