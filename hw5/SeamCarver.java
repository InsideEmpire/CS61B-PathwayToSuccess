import edu.princeton.cs.algs4.Picture;

import java.awt.*;
//import edu.princeton.cs.introcs.Picture;

public class SeamCarver {
    final private Picture picture;
    final private int[][] color;
    final private double[][] energy;
    private boolean flag = false;

    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
        this.color = new int[this.picture.width()][this.picture.height()];
        this.energy = new double[this.picture.width()][this.picture.height()];
        for (int x = 0; x < this.picture.width(); x++) {
            for (int y = 0; y < this.picture.height(); y++) {
                Color c = this.picture.get(x, y);
                this.color[x][y] = c.getRGB();  // Store the full RGB value
            }
        }
        for (int x = 0; x < this.picture.width(); x++) {
            for (int y = 0; y < this.picture.height(); y++) {
                this.energy[x][y] = this.energy(x, y);
            }
        }
        this.flag = true;
    }
    public Picture picture() {
        return new Picture(this.picture);
    }                       // current picture

    public int width() {
        return this.picture.width();
    }                         // width of current picture

    public int height() {
        return this.picture.height();
    }                        // height of current picture

    public double energy(int x, int y) {
        if (x < 0 || x >= this.picture.width() || y < 0 || y >= this.picture.height()) {
            throw new IndexOutOfBoundsException();
        }

        if (flag) {
            return this.energy[x][y];
        }

        int Rx, Gx, Bx;
        int Ry, Gy, By;

        Rx = Math.abs(this.color((x - 1 + this.picture.width()) % this.picture.width(),y, 'R') - this.color((x + 1) % this.picture.width(), y, 'R'));
        Gx = Math.abs(this.color((x - 1 + this.picture.width()) % this.picture.width(),y, 'G') - this.color((x + 1) % this.picture.width(), y, 'G'));
        Bx = Math.abs(this.color((x - 1 + this.picture.width()) % this.picture.width(),y, 'B') - this.color((x + 1) % this.picture.width(), y, 'B'));

        Ry = Math.abs(this.color(x,(y - 1 + this.picture.height()) % this.picture.height(), 'R') - this.color(x, (y + 1) % this.picture.height(), 'R'));
        Gy = Math.abs(this.color(x,(y - 1 + this.picture.height()) % this.picture.height(), 'G') - this.color(x, (y + 1) % this.picture.height(), 'G'));
        By = Math.abs(this.color(x,(y - 1 + this.picture.height()) % this.picture.height(), 'B') - this.color(x, (y + 1) % this.picture.height(), 'B'));

        return Math.pow(Rx, 2) + Math.pow(Ry, 2) + Math.pow(Gx, 2) + Math.pow(Gy, 2) + Math.pow(Bx, 2) + Math.pow(By, 2);
    }            // energy of pixel at column x and row y

    private int color(int x, int y, char type) {
        switch (type) {
            case 'R':
                return (this.color[x][y] & 0xff0000) >> 16;
            case 'G':
                return (this.color[x][y] & 0xff00) >> 8;
            case 'B':
                return (this.color[x][y] & 0xff);
            default:
                throw new RuntimeException("parameter is not RGB type");
        }
    }

    public int[] findHorizontalSeam() {
        double[][] minEnergy = new double[this.picture.width()][this.picture.height()];

        for (int x = 0; x < this.picture.width(); x++) {
            for (int y = 0; y < this.picture.height(); y++) {
                if (x == 0) {
                    minEnergy[x][y] = this.energy(x, y);
                } else {
                    if (y == 0) {
                        minEnergy[x][y] = this.energy[x][y] + Math.min(minEnergy[x - 1][y], minEnergy[x - 1][(y + 1) % this.picture.height()]);
                    } else if (y == this.picture.height() - 1) {
                        minEnergy[x][y] = this.energy[x][y] + Math.min(minEnergy[x - 1][y], minEnergy[x - 1][(y - 1 + this.picture.height()) % this.picture.height()]);
                    } else {
                        minEnergy[x][y] = this.energy[x][y] + Math.min(minEnergy[x - 1][(y - 1 + this.picture.height()) % this.picture.height()], Math.min(minEnergy[x - 1][y], minEnergy[x - 1][(y + 1) % this.picture.height()]));
                    }
                }
            }
        }

        int[] seam = new int[this.picture.width()];
        int upper = this.picture.height() - 1, lower = 0;
        for (int x = this.picture.width() - 1; x >= 0; x--) {
            int row = -1;
            double min = Double.MAX_VALUE;
            for (int y = lower; y <= upper; y++) {
                if (minEnergy[x][y] < min) {
                    row = y;
                    min = minEnergy[x][y];
                }
            }
            seam[x] = row;
            lower = row == 0 ? 0 : row - 1;
            upper = row == this.picture.height() - 1 ? this.picture.height() - 1 : row + 1;
        }
        return seam;
    }              // sequence of indices for horizontal seam

    public int[] findVerticalSeam() {
        double[][] minEnergy = new double[this.picture.width()][this.picture.height()];
        for (int y = 0; y < this.picture.height(); y++) {
            for (int x = 0; x < this.picture.width(); x++) {
                if (y == 0) {
                    minEnergy[x][y] = this.energy(x, y);
                } else {
                    if (x == 0) {
                        minEnergy[x][y] = this.energy[x][y] + Math.min(minEnergy[x][y - 1], minEnergy[(x + 1) % this.picture.width()][y - 1]);
                    } else if (x == this.picture.width() - 1) {
                        minEnergy[x][y] = this.energy[x][y] + Math.min(minEnergy[x][y - 1], minEnergy[(x - 1 + this.picture.width()) % this.picture.width()][y - 1]);
                    } else {
                        minEnergy[x][y] = this.energy[x][y] + Math.min(minEnergy[x][y - 1], Math.min(minEnergy[(x - 1 + this.picture.width()) % this.picture.width()][y - 1], minEnergy[(x + 1) % this.picture.width()][y - 1]));
                    }
                }
            }
        }
        int[] seam = new int[this.picture.height()];
        int right = this.picture.width() - 1, left = 0;
        for (int y = this.picture.height() - 1; y >= 0; y--) {
            int column = -1;
            double min = Double.MAX_VALUE;
            for (int x = left; x <= right; x++) {
                if (minEnergy[x][y] < min) {
                    column = x;
                    min = minEnergy[x][y];
                }
            }
            seam[y] = column;
            left = column == 0 ? 0 : column - 1;
            right = column == this.picture.width() - 1 ? this.picture.width() - 1 : column + 1;
        }
        return seam;
    }              // sequence of indices for vertical seam

    public void removeHorizontalSeam(int[] seam) {
        if (seam.length != picture().width()) {
            throw new IllegalArgumentException();
        }

        SeamRemover.removeHorizontalSeam(this.picture, seam);
    }   // remove horizontal seam from picture
    public void removeVerticalSeam(int[] seam) {
        if (seam.length != picture().height()) {
            throw new IllegalArgumentException();
        }

        SeamRemover.removeVerticalSeam(this.picture, seam);
    }     // remove vertical seam from picture
}