package hcmus.tetris;

public class Piece {
    public final int color;
    private int rotation;
    private int width, height;
    private Coord coord;
    private PieceType type;

    public Piece(PieceType type) {
        this(type, 0);
    }

    public Piece(PieceType type, int columns) {
        rotation = 0;
        this.type = type;
        width = type.width;
        height = type.height;
        color = type.color;
        coord = new Coord(0, Math.max(columns / 2 - width, 0));
    }

    public void rotate(int i) {
        rotation = (rotation + i) % 4;
        if (rotation < 0)
            rotation += 4;
    }

    public void drop() {
        ++coord.x;
    }

    public void steer(int dir) {
        coord.y += dir / dir;
    }

    public Coord[] getBlockPositions() {
        Coord[] res = new Coord[4];
        int[] data = type.getData();

        for (int i = 0; i < 4; ++i) {
            int j = data[i] % 4;
            int k = ((rotation == 0 || rotation == 3) && data[i] < 4)? 0 : 1;
            if (rotation % 2 == 0)
                res[i] = new Coord(coord.x + k, coord.y + j);
            else
                res[i] = new Coord(coord.x + j, coord.y + k);
        }

        return res;
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

    @Override
    public String toString() {
        return type.name() + "_PIECE";
    }
}
