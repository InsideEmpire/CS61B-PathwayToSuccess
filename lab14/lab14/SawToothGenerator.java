package lab14;

import lab14lib.Generator;

public class SawToothGenerator implements Generator {
    private int period;
    private int state;

    public SawToothGenerator(int period) {
        state = 0;
        this.period = period;
    }


    @Override
    public double next() {
        state = (state + 1) % period;
        return normalize(state);
    }

    private double normalize(int state) {
        double k = (double) 2 / period;
        double b = -1.0;
        return (state * k) + b;
    }
}
