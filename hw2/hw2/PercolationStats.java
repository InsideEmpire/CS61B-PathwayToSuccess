package hw2;

import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private int n;
    private int t;
    private double mean;
    private double deviation;
    private double[] threshold;  // 改为 double 类型

    /**
     * Constructor to perform T independent experiments on an N-by-N grid.
     * Each experiment uses a Percolation object created by the provided factory (pf),
     * and records the threshold (fraction of open sites) at which percolation occurs.
     *
     * @param N Size of the grid (N-by-N)
     * @param T Number of independent experiments to run
     * @param pf Factory to create new Percolation objects for each experiment
     * @throws IllegalArgumentException if N or T is less than 0
     */
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }

        n = N;
        t = T;
        threshold = new double[T];  // 使用 double 类型来存储每次实验的阈值
        for (int i = 0; i < T; ++i) {
            threshold[i] = MonteCarloSimulation.simulation(pf.make(N), n);  // 返回的是比值
        }
        mean = StdStats.mean(threshold);  // 不再除以 n^2
        deviation = StdStats.stddev(threshold);  // 不再除以 n^2
    }

    /**
     * Returns the sample mean of the percolation threshold from T experiments.
     *
     * @return Mean of percolation thresholds
     */
    public double mean() {
        return mean;
    }

    /**
     * Returns the sample standard deviation of the percolation threshold.
     *
     * @return Standard deviation of percolation thresholds
     */
    public double stddev() {
        return deviation;
    }

    /**
     * Returns the lower endpoint of the 95% confidence interval for the percolation threshold.
     * This is calculated as mean - (1.96 * stddev / sqrt(T)).
     *
     * @return Lower endpoint of 95% confidence interval
     */
    public double confidenceLow() {
        return (mean - (1.96 * deviation / Math.sqrt((double) t)));
    }

    /**
     * Returns the upper endpoint of the 95% confidence interval for the percolation threshold.
     * This is calculated as mean + (1.96 * stddev / sqrt(T)).
     *
     * @return Upper endpoint of 95% confidence interval
     */
    public double confidenceHigh() {
        return (mean + (1.96 * deviation / Math.sqrt((double) t)));
    }
}
