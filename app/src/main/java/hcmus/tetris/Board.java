package hcmus.tetris;

import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Board {
    int rows, columns;
    Random rng = new Random();
    Timer timer = new Timer();

    int[][] pile;
    Queue<Piece> queue = new LinkedList<>();

    PieceType[] pieceTypes = PieceType.values();

    public Board(int rows, int columns) {
        if (columns < 4)
            throw new IllegalArgumentException("Board needs at least 4 columns");

        this.rows = rows;
        this.columns = columns;
        pile = new int[rows][columns];
        generateNewPiece();
        generateNewPiece();
    }

    public void startGame() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                tick();
            }
        }, 0, 1000);
    }

    void generateNewPiece() {
        PieceType type = pieceTypes[rng.nextInt(pieceTypes.length)];
        queue.add(new Piece(type));
    }

    void tick() {
        Piece piece = queue.peek();
        if (piece == null) {
            generateNewPiece();
            return;
        }
        if (piece.getCoord().x + piece.getHeight() >= rows) {
            generateNewPiece();
            return;
        }
        piece.drop();
    }

    public Piece getCurrentPiece() {
        return queue.peek();
    }

    public int[][] getPile() {
        return pile;
    }
}
