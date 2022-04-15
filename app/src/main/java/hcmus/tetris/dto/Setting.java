package hcmus.tetris.dto;

import java.util.ArrayList;

public class Setting {
    long scoreLimit, lineScore;
    String controlScheme;
    ArrayList<ArrayList<Integer>> timeLevels;
    int width, height;

    public Setting(long scoreLimit, long lineScore, String controlScheme,
                   ArrayList<ArrayList<Integer>> timeLevels, int width, int height){
        this.scoreLimit = scoreLimit;
        this.lineScore = lineScore;
        this.controlScheme = controlScheme;
        this.timeLevels = timeLevels;
        this.width = width;
        this.height = height;
    }

    public long getScoreLimit() { return scoreLimit; }
    public long getLineScore() { return lineScore; }
    public String getControlScheme() { return controlScheme; }
    public ArrayList<ArrayList<Integer>> getTimeLevels() { return timeLevels; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
