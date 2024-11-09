package byog.Core;

public enum Toward {
    W(0, 1), S(0, -1), A(-1, 0), D(1, 0), STAY(0, 0);

    Toward(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public final int x;
    public final int y;
}
