package hcmus.tetris;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Piece {
    public final int color;
    private int rotation;
    private final int width, height;
    private final Coord coord;
    private final PieceType type;
    Coord[] blockPos;
    List<Coord> downColliders, leftColliders, rightColliders;

    public Piece(PieceType type) {
        this(type, 0);
    }

    public Piece(PieceType type, int columns) {
        rotation = 0;
        this.type = type;
        width = type.width;
        height = type.height;
        color = type.color;
        coord = new Coord(0, columns);
        updateBlocks();
    }

    public void rotate(int i) {
        rotation = (rotation + i) % 4;
        if (rotation < 0)
            rotation += 4;
        updateBlocks();
    }

    public void move(int dx, int dy) {
        coord.translate(dx, dy);
        updateBlocks();
    }

    void updateBlocks() {
        Set<Coord> set = new HashSet<>();

        // Calculate blocks
        blockPos = new Coord[4];
        int[] data = type.getData();
        for (int i = 0; i < 4; ++i) {
            // The column in the piece representation matrix
            int j = data[i] % 4;
            if (rotation > 1)
                j = 4 - j;
            // The row in the piece representation matrix
            int k = ((rotation == 0 || rotation == 3) == data[i] < 4)? 0 : 1;

            if (rotation % 2 == 0)
                blockPos[i] = new Coord(coord.x + k, coord.y + j);
            else
                blockPos[i] = new Coord(coord.x + j, coord.y + k);

            if (rotation == 2)
                blockPos[i].translate(0, -1);
            if (rotation == 3)
                blockPos[i].translate(-1, 0);
            set.add(blockPos[i]);
        }

        // Calculate colliders
        rightColliders = new ArrayList<>(5);
        leftColliders = new ArrayList<>(5);
        downColliders = new ArrayList<>(4);
        for (Coord coord : blockPos) {
            updateCollider(set, downColliders, coord.x + 1, coord.y);
            updateCollider(set, rightColliders, coord.x, coord.y + 1);
            updateCollider(set, leftColliders, coord.x, coord.y - 1);
        }
    }

    void updateCollider(Set<Coord> set, List<Coord> colliders, int x, int y) {
        Coord c = new Coord(x, y);
        if (!set.contains(c))
            colliders.add(c);
    }

    public int[] getModel() {
        return type.getData();
    }

    public PieceType getType() {
        return type;
    }

    public Coord[] getBlockPositions() {
        return blockPos;
    }

    public Coord[] getDownColliders() {
        return downColliders.toArray(new Coord[0]);
    }

    public Coord[] getLeftColliders() {
        return leftColliders.toArray(new Coord[0]);
    }

    public Coord[] getRightColliders() {
        return rightColliders.toArray(new Coord[0]);
    }

    public Coord getCoord() {
        return coord;
    }

    public int getWidth() {
        return (rotation % 2 == 0)? width : height;
    }

    public int getHeight() {
        return (rotation % 2 == 0)? height : width;
    }

    @NonNull
    @Override
    public String toString() {
        return type.name() + "_PIECE";
    }
}
