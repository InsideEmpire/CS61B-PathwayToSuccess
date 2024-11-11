package hw2;

import edu.princeton.cs.introcs.StdRandom;

public class MonteCarloSimulation {
    public static double simulation(Percolation percolation, int n) {
        int openedSites = 0;
        while (!percolation.percolates()) {
            randomlyOpen(percolation, n);
            openedSites++;
        }
        // 返回的是打开站点数与总站点数的比值
        return (double) openedSites / (n * n);
    }

    private static void randomlyOpen(Percolation percolation, int n) {
        int row = StdRandom.uniform(n);
        int col = StdRandom.uniform(n);
        while (percolation.isOpen(row, col)) {
            row = StdRandom.uniform(n);
            col = StdRandom.uniform(n);
        }
        percolation.open(row, col);
    }
}
