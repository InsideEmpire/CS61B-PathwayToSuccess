package lab14;

import lab14lib.Generator;

public class SawToothGenerator implements Generator {
    private double period;
    private int state;

    public SawToothGenerator(int period) {
        state = 0;
        this.period = period;
    }


    @Override
    public double next() {
        state = (int) ((state + 1) % period);
        return normalize(state);
    }

    private double normalize(int state) {
        double k = 2 / period;
        double b = -1.0;
        return (state * k) + b;
    }
}
