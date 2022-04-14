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
    interface OnNextPieceListener {
        void onNewNextPiece(PieceType nextPiece);
    }

    interface OnLineClearListener {
        void onLineClear(int row, int score);
    }

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
    public OnNextPieceListener nextPieceListener;
    public OnLineClearListener lineClearListener;

    Queue<Piece> queue = new LinkedList<>();
    Piece currentPiece;

    public Board(int rows, int columns) {
        if (columns < 4)
            throw new IllegalArgumentException("Board needs at least 4 columns");

        this.rows = rows;
        this.columns = columns;
        pile = new int[rows][columns];
        generateNewPiece();
        generateNewPiece();
        currentPiece = queue.poll();
        if (nextPieceListener != null && queue.peek() != null)
            nextPieceListener.onNewNextPiece(queue.peek().getType());
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
        Piece piece = getCurrentPiece();
        if (piece == null) {
            generateNewPiece();
            return;
        }

        drop();

        if (tickListener != null)
            tickListener.run();
    }

    void drop() {
        Piece piece = getCurrentPiece();
        Coord[] colliders = piece.getDownColliders();
        for (Coord coll : colliders)
            if (coll.x >= rows || pile[coll.x][coll.y] != 0) {
                addToPile(piece);
                generateNewPiece();
                currentPiece = queue.poll();
                if (nextPieceListener != null && queue.peek() != null)
                    nextPieceListener.onNewNextPiece(queue.peek().getType());
                return;
            }
        piece.move(1, 0);
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
            if (cnt >= columns) {
                ++diff;
                if (lineClearListener != null)
                    lineClearListener.onLineClear(i, 800);
            }
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

    public void hardDrop() {
        Piece piece = queue.peek();
        while (piece == queue.peek())
            drop();
    }

    public Piece getCurrentPiece() {
        return currentPiece;
    }

    public int[][] getPile() {
        return pile;
    }

    public void pause() {
        timer.cancel();
    }
}
