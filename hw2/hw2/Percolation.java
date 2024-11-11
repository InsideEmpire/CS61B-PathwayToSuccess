package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // Main union-find structure for checking percolation
    private WeightedQuickUnionUF unionUF;
    // Secondary union-find structure for checking fullness (prevent backwash)
    private WeightedQuickUnionUF fullUF;
    // Grid size (N x N)
    private int n;
    // Total number of grid sites (N * N)
    private int totalNum;
    // Counter for the number of open sites
    private int openNum;
    // Array to track if each site is open
    private boolean[] isOpen;


    /**
     * Represents the top and bottom of the grid, where:
     *          0         1        2 ... n-1    col
     *    0     0         1        2     n-1
     *    1     n        n+1      n+2   2n-1
     *    2    2n       2n+1     2n+2   3n-1
     *         ...      ...      ...    ...
     *   n-1 (n-1)n  (n-1)n+1 (n-1)n+2  n*n-1
     *   row                                (total totalSize)
     *   bottom
     *
     * unionUF[n * n] is the virtual top site
     * unionUF[n * n + 1] is the virtual bottom site
     * @param N creates an N-by-N grid, with all sites initially blocked
     */
    public Percolation(int N) {
        if (N < 0) {
            throw new IllegalArgumentException();
        }

        unionUF = new WeightedQuickUnionUF(N * N + 2);
        fullUF = new WeightedQuickUnionUF(N * N + 1);
        isOpen = new boolean[N * N + 2];

        // Initialize all sites as closed
        for (int i = 0; i < N * N + 2; ++i) {
            isOpen[i] = false;
        }

        n = N;
        totalNum = N * N;
        openNum = 0;

        // Connect top row to virtual top site and bottom row to virtual bottom site
        for (int i = 0; i < n; ++i) {
            unionUF.union(i, n * n);
            fullUF.union(i, n * n);
            unionUF.union(i + (n - 1) * n, n * n + 1);
        }
    }

    /**
     * Converts a 2D grid coordinate (row, col) to a 1D index for use with union-find structures.
     * @param row Row in the grid
     * @param col Column in the grid
     * @return 1D index representation (row * n + col)
     */
    private int xyToOneDimension(int row, int col) {
        return row * n + col;
    }

    /**
     * Opens the site (row, col) if it is not already open.
     * @param row Row of the site to open
     * @param col Column of the site to open
     */
    public void open(int row, int col) {
        if (row < 0 || row > n || col < 0 || col > n) {
            throw new IndexOutOfBoundsException();
        }

        int pos = xyToOneDimension(row, col);
        if (!isOpen[pos]) {
            isOpen[pos] = true;
            openNum++;
            connectOpenSite(row, col);
        }
    }

    /**
     * Connects an open site to its neighboring open sites.
     * @param row Row of the site to connect
     * @param col Column of the site to connect
     */
    private void connectOpenSite(int row, int col) {
        int pos = xyToOneDimension(row, col);

        // Try to connect to the neighboring open site above (row - 1, col)
        if (row - 1 >= 0 && isOpen(row - 1, col)) {
            unionUF.union(pos, xyToOneDimension(row - 1, col));
            fullUF.union(pos, xyToOneDimension(row - 1, col));
        }

        // Try to connect to the neighboring open site below (row + 1, col)
        if (row + 1 < n && isOpen(row + 1, col)) {
            unionUF.union(pos, xyToOneDimension(row + 1, col));
            fullUF.union(pos, xyToOneDimension(row + 1, col));
        }

        // Try to connect to the neighboring open site to the left (row, col - 1)
        if (col - 1 >= 0 && isOpen(row, col - 1)) {
            unionUF.union(pos, xyToOneDimension(row, col - 1));
            fullUF.union(pos, xyToOneDimension(row, col - 1));
        }

        // Try to connect to the neighboring open site to the right (row, col + 1)
        if (col + 1 < n && isOpen(row, col + 1)) {
            unionUF.union(pos, xyToOneDimension(row, col + 1));
            fullUF.union(pos, xyToOneDimension(row, col + 1));
        }
    }

    /**
     * Checks if the site (row, col) is open.
     * @param row Row of the site to check
     * @param col Column of the site to check
     * @return true if the site is open, false otherwise
     */
    public boolean isOpen(int row, int col) {
        if (row < 0 || row > n || col < 0 || col > n) {
            throw new IndexOutOfBoundsException();
        }

        return isOpen[xyToOneDimension(row, col)];
    }

    /**
     * Checks if the site (row, col) is full (connected to the top row).
     * @param row Row of the site to check
     * @param col Column of the site to check
     * @return true if the site is full, false otherwise
     */
    public boolean isFull(int row, int col) {
        if (row < 0 || row > n || col < 0 || col > n) {
            throw new IndexOutOfBoundsException();
        }

        return unionUF.connected(n * n, xyToOneDimension(row, col))
                && isOpen(row, col)
                && fullUF.connected(n * n, xyToOneDimension(row, col));
    }

    /**
     * Returns the number of open sites in the grid.
     * @return Number of open sites
     */
    public int numberOfOpenSites() {
        return openNum;
    }

    /**
     * Checks if the grid percolates (there is a path from the top to the bottom).
     * @return true if the grid percolates, false otherwise
     */
    public boolean percolates() {
        if (openNum == 0) {  // if no sites are open, it cannot percolate
            return false;
        }
        return unionUF.connected(n * n, n * n + 1);
    }

    public static void main(String[] args) {
        // Test client (optional, can be used for debugging or unit testing)
    }
}
