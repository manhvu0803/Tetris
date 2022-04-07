package hcmus.tetris;

public enum PieceType {
    SQUARE      (0, 1, 4, 5, 2, 2),
    L           (2, 4, 5, 6, 3, 2),
    LR          (0, 1, 2, 6, 3, 2),
    STRAIGHT    (0, 1, 2, 3, 4, 1),
    Z           (0, 1, 5, 6, 3, 2),
    ZR          (1, 2, 4, 5, 3, 2),
    T           (1, 4, 5, 6, 3, 2);

    private final int[] data = new int[4];
    private int w, h;

    PieceType(int b1, int b2, int b3, int b4, int w, int h) {
        data[1] = b1;
        data[2] = b2;
        data[3] = b3;
        data[4] = b4;
        this.w = w;
        this.h = h;
    }

    public int[] getData() {
        return data;
    }

    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }
}
