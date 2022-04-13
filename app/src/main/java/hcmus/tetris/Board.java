package hcmus.tetris;

import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.os.Handler;

import androidx.annotation.NonNull;

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

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            tick();
        }
    };

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
                handler.sendMessage(handler.obtainMessage());
            }
        }, 0, 750);
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
                addToPile(piece);
                generateNewPiece();
                queue.poll();
                return;
            }
        piece.move(1, 0);

        if (tickListener != null)
            tickListener.run();
    }

    void addToPile(Piece piece) {
        for (Coord coord : piece.getBlockPositions())
            pile[coord.x][coord.y] = piece.color;
        int diff = 0;
        for (int i = rows - 1; i >= 0; --i) {
            int cnt = 0;
            for (int j = 0; j < columns; ++j)
                if (pile[i][j] != 0)
                    ++cnt;
            if (cnt >= columns)
                ++diff;
            if (i - diff >= 0)
                pile[i] = pile[i - diff];
            else
                pile[i] = new int[columns];
        }
    }

    public void rotate(int dir) {
        Piece p = getCurrentPiece();
        p.rotate(dir);
        // Check if the rotation is valid
        for (Coord cr : p.getBlockPositions()) {
            int dx = 0, dy = 0;
            if (cr.x >= rows)
                dx = cr.x - rows + 1;
            if (cr.y >= columns)
                dy = cr.y - columns + 1;
            p.move(dx, dy);
            if (pile[cr.x][cr.y] != 0) {
                p.rotate(-dir);
                break;
            }
        }
    }

    public void steer(int dir) {
        Piece piece = getCurrentPiece();
        if (piece == null)
            return;

        Coord[] colliders = (dir < 0)? piece.getLeftColliders() : piece.getRightColliders();
        for (Coord coll : colliders)
            if (coll.y >= columns || coll.y < 0 || pile[coll.x][coll.y] != 0)
                return;
        piece.move(0, dir);
    }

    public Piece getCurrentPiece() {
        return queue.peek();
    }

    public int[][] getPile() {
        return pile;
    }

    public void pause() {
        timer.cancel();
    }
}
