package hcmus.tetris;

public class Piece {
    int rotation;
    int width, height;
    int[] data;
    Coord coord;

    public Piece(PieceType type) {
        rotation = 0;
        data = type.getData();
        width = type.getWidth();
        height = type.getHeight();
        coord = new Coord(0, 0);
    }

    public Piece(PieceType type, int columns) {
        rotation = 0;
        data = type.getData();
        width = type.getWidth();
        height = type.getHeight();
        coord = new Coord(0, Math.max(columns / 2 - width, 0));
    }

    public void rotate(int i) {
        rotation = (rotation + i) % 4;
        if (rotation < 0)
            rotation += 4;
    }

    public Coord[] getBlockPositions() {
        Coord[] res = new Coord[4];

        for (int i = 0; i < 4; ++i) {
            int j = i % 4;
            int k = ((rotation == 0 || rotation == 3) && data[i] < 4)? 0 : 1;
            if (rotation % 2 == 0)
                res[i] = new Coord(coord.x + k, coord.y + j);
            else
                res[i] = new Coord(coord.x + j, coord.y + k);
        }

        return res;
    }
}
