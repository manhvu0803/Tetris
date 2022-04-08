package hcmus.tetris;

import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Board {
    static PieceType[] pieceTypes = PieceType.values();
    static Random rng = new Random();

    int rows, columns;
    int[][] pile;

    Timer timer = new Timer();
    public Runnable tickListener = null;

    Queue<Piece> queue = new LinkedList<>();

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
        }, 0, 500);
    }

    void generateNewPiece() {
        PieceType type = pieceTypes[rng.nextInt(pieceTypes.length)];
        queue.add(new Piece(type, rng.nextInt(columns / 2)));
    }

    void tick() {
        Piece piece = queue.peek();
        if (piece == null) {
            generateNewPiece();
            return;
        }

        Coord[] colliders = piece.getDownColliders();
        for (Coord coll : colliders)
            if (coll.x >= rows || pile[coll.x][coll.y] != 0) {
                for (Coord coord : piece.getBlockPositions())
                    pile[coord.x][coord.y] = piece.color;
                generateNewPiece();
                queue.poll();
                return;
            }
        piece.drop();

        if (tickListener != null)
            tickListener.run();
    }

    public void steer(int dir) {
        Piece piece = queue.peek();
        if (piece == null)
            return;

        Coord[] colliders = (dir < 0)? piece.getLeftColliders() : piece.getRightColliders();
        for (Coord coll : colliders)
            if (coll.y >= columns || coll.y < 0 || pile[coll.x][coll.y] != 0)
                return;
        piece.steer(dir);
        Log.d("Steer", "Steer: " + dir);
    }

    public Piece getCurrentPiece() {
        return queue.peek();
    }

    public int[][] getPile() {
        return pile;
    }
}
