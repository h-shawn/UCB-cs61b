package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int N;
    private final boolean[][] grids;
    private final WeightedQuickUnionUF uni;
    private int numOpen;

    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        this.N = N;
        numOpen = 0;
        grids = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                grids[i][j] = false;
            }
        }

        uni = new WeightedQuickUnionUF(N * N + 2);
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (isOpen(row, col)) {
            return;
        }
        grids[row][col] = true;
        numOpen++;

        unionInRange(row, col);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkRange(row, col);
        return grids[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!isOpen(row, col)) {
            return false;
        }
        return uni.connected(N * N, row * N + col);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return numOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return uni.connected(N * N, N * N + 1);
    }

    private void checkRange(int row, int col) {
        if (row < 0 || col < 0 || row >= N || col >= N) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void unionInRange(int row, int col) {
        int A = row * N + col;
        // Top = N * N, bottom = N * N + 1
        if (row == 0) {
            uni.union(N * N, A);
        }
        if (row == N - 1) {
            uni.union(N * N + 1, A);
        }

        if (row - 1 >= 0 && grids[row - 1][col]) {
            uni.union(A, A - N);
        }
        if (row + 1 < N && grids[row + 1][col]) {
            uni.union(A, A + N);
        }
        if (col - 1 >= 0 && grids[row][col - 1]) {
            uni.union(A, A - 1);
        }
        if (col + 1 < N && grids[row][col + 1]) {
            uni.union(A, A + 1);
        }
    }

}
