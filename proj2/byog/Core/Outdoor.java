package byog.Core;

import java.io.Serial;
import java.io.Serializable;

public class Outdoor implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    static int x;
    static int y;
    Outdoor(int x, int y) {
        Outdoor.x = x;
        Outdoor.y = y;
    }
}
