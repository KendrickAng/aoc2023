package org.example.seventeen;

import org.example.Solver;

import java.util.*;

public class Seventeen implements Solver {
    public static final Seventeen SINGLETON = new Seventeen();
    private static final int UP = 0;
    private static final int DOWN = 1;
    private static final int LEFT = 2;
    private static final int RIGHT = 3;
    private static final int INF = 5_000_000;

    private static final int UPPER_TURN_LIMIT_2 = 11;

    private List<String> lines;

    @Override
    public String partOneSample(List<String> lines) {
        this.lines = List.copyOf(lines);
        return solve1();
    }

    @Override
    public String partOneInput(List<String> lines) {
        this.lines = List.copyOf(lines);
        return solve1();
    }

    @Override
    public String partTwoSample(List<String> lines) {
        this.lines = List.copyOf(lines);
        return solve2();
    }

    @Override
    public String partTwoInput(List<String> lines) {
        this.lines = List.copyOf(lines);
        return solve2();
    }

    private String solve2() {
        int rows = lines.size();
        int cols = lines.get(0).length();
        int[][] grid = new int[rows][cols];
        for (int row = 0; row < rows; row++) {
            String line = lines.get(row);
            for (int col = 0; col < cols; col++) {
                char digit = line.charAt(col);
                grid[row][col] = digit - '0';
            }
        }

        int[][][][] dist = new int[rows][cols][4][UPPER_TURN_LIMIT_2];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                for (int dir = 0; dir < 4; dir++) {
                    for (int move = 0; move < UPPER_TURN_LIMIT_2; move++) { // 10 moves without turning
                        dist[row][col][dir][move] = INF;
                    }
                }
            }
        }

        // distance to the source is 0
        for (int dir = 0; dir < 4; dir++) {
            for (int move = 0; move < UPPER_TURN_LIMIT_2; move++) { // 10 moves without turning
                dist[0][0][dir][move] = 0;
            }
        }

        // row, col, moves, dir, cost
        int[] start1 = new int[]{0, 0, 0, RIGHT, 0};
        int[] start2 = new int[]{0, 0, 0, DOWN, 0};
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[4] - o2[4];
            }
        });
        pq.add(start1);
        pq.add(start2);

        while (!pq.isEmpty()) {
            int[] node = pq.poll();
            int currRow = node[0];
            int currCol = node[1];
            int currMoves = node[2];
            int currDir = node[3];
            int currCost = node[4];
//            System.out.printf("[%d,%d] moves=%d dir=%d cost=%d\n", currRow, currCol, currMoves, currDir, currCost);

            if (currRow < 0 || currRow >= rows || currCol < 0 || currCol >= cols) {
                continue;
            }

            // explore neighbours
            List<int[]> dirs = getNextDirs2(currRow, currCol, currMoves, currDir, currCost, grid);

            for (int[] next : dirs) {
                int nextRow = next[0];
                int nextCol = next[1];
                int nextMoves = next[2];
                int nextDir = next[3];
                int nextCost = next[4];

                if (dist[nextRow][nextCol][nextDir][nextMoves] == INF &&
                        nextCost <= dist[nextRow][nextCol][nextDir][nextMoves]) {
                    dist[nextRow][nextCol][nextDir][nextMoves] = nextCost;
                    pq.add(new int[]{nextRow, nextCol, nextMoves, nextDir, nextCost});
                }
            }
        }

        int ans = Integer.MAX_VALUE;
        for (int dir = 0; dir < 4; dir++) {
            for (int move = 4; move < UPPER_TURN_LIMIT_2; move++) { // at least 4 blocks before stopping
                ans = Math.min(ans, dist[rows - 1][cols - 1][dir][move]);
            }
        }

        return String.valueOf(ans);
    }

    private List<int[]> getNextDirs2(int row, int col, int moves, int dir, int cost, int[][] grid) {
        if (moves >= UPPER_TURN_LIMIT_2) throw new AssertionError();

        List<int[]> dirs = new ArrayList<>();

        // move front
        int nextAdditionalCost = 0;
        for (int i = 1; i < UPPER_TURN_LIMIT_2; i++) {
            int nextRow = moveRow(row, dir, i);
            int nextCol = moveCol(col, dir, i);
            if (nextRow >= 0 && nextRow < grid.length && nextCol >= 0 && nextCol < grid[0].length
                    && moves + i < UPPER_TURN_LIMIT_2) {
                nextAdditionalCost += grid[nextRow][nextCol];
                dirs.add(new int[]{nextRow, nextCol, moves + i, dir, cost + nextAdditionalCost});
            }
        }

        if (moves >= 4) {
            int leftDir = left(dir);
            int leftAdditionalCost = 0;
            for (int i = 1; i < UPPER_TURN_LIMIT_2; i++) {
                int leftRow = moveRow(row, leftDir, i);
                int leftCol = moveCol(col, leftDir, i);
                if (leftRow >= 0 && leftRow < grid.length && leftCol >= 0 && leftCol < grid[0].length) {
                    leftAdditionalCost += grid[leftRow][leftCol];
                    dirs.add(new int[]{leftRow, leftCol, i, leftDir, cost + leftAdditionalCost});
                }
            }

            int rightDir = right(dir);
            int rightAdditionalCost = 0;
            for (int i = 1; i < UPPER_TURN_LIMIT_2; i++) {
                int rightRow = moveRow(row, rightDir, i);
                int rightCol = moveCol(col, rightDir, i);
                if (rightRow >= 0 && rightRow < grid.length && rightCol >= 0 && rightCol < grid[0].length) {
                    rightAdditionalCost += grid[rightRow][rightCol];
                    dirs.add(new int[]{rightRow, rightCol, i, rightDir, cost + rightAdditionalCost});
                }
            }
        }

        return dirs;
    }


    private String solve1() {
        int rows = lines.size();
        int cols = lines.get(0).length();
        int[][] grid = new int[rows][cols];
        for (int row = 0; row < rows; row++) {
            String line = lines.get(row);
            for (int col = 0; col < cols; col++) {
                char digit = line.charAt(col);
                grid[row][col] = digit - '0';
            }
        }

        int[][][][] dist = new int[rows][cols][4][4];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                for (int d = 0; d < 4; d++) {
                    for (int move = 0; move < 4; move++) {
                        dist[row][col][d][move] = INF;
                    }
                }
            }
        }

        // distance to the source is 0
        for (int d = 0; d < 4; d++) {
            for (int move = 0; move < 4; move++) {
                dist[0][0][d][move] = 0;
            }
        }

        // row, col, moves, dir, cost
        int[] start = new int[]{0, 0, 0, RIGHT, 0};
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[4] - o2[4];
            }
        });
        pq.add(start);

        while (!pq.isEmpty()) {
            int[] node = pq.poll();
            int currRow = node[0];
            int currCol = node[1];
            int currMoves = node[2];
            int currDir = node[3];
            int currCost = node[4];

            if (currRow < 0 || currRow >= rows || currCol < 0 || currCol >= cols || currMoves > 3) {
                continue;
            }

            // explore neighbours
            List<int[]> dirs = getNextDirs(currRow, currCol, currMoves, currDir, currCost, grid);

            for (int[] next : dirs) {
                int nextRow = next[0];
                int nextCol = next[1];
                int nextMoves = next[2];
                int nextDir = next[3];
                int nextCost = next[4];

                if (dist[nextRow][nextCol][nextDir][nextMoves] == INF && nextCost <= dist[nextRow][nextCol][nextDir][nextMoves]) {
//                    System.out.printf("Updated %d,%d,%d to %d\n", nextRow, nextCol, nextMoves, dist[currRow][currCol] + grid[nextRow][nextCol]);
                    dist[nextRow][nextCol][nextDir][nextMoves] = nextCost;
                    pq.add(new int[]{nextRow, nextCol, nextMoves, nextDir, dist[nextRow][nextCol][nextDir][nextMoves]});
                }
            }
        }

        int ans = Integer.MAX_VALUE;
        for (int d = 0; d < 4; d++) {
            for (int move = 0; move < 4; move++) {
                ans = Math.min(ans, dist[rows - 1][cols - 1][d][move]);
            }
        }

        return String.valueOf(ans);
    }

    private List<int[]> getNextDirs(int row, int col, int moves, int dir, int cost, int[][] grid) {
        List<int[]> dirs = new ArrayList<>();
        int leftDir = left(dir);
        int rightDir = right(dir);

        int nextAdditionalCost = 0;
        int leftAdditionalCost = 0;
        int rightAdditionalCost = 0;
        for (int i = 1; i <= 3; i++) {
            int nextRow = moveRow(row, dir, i);
            int nextCol = moveCol(col, dir, i);
            if (nextRow >= 0 && nextRow < grid.length && nextCol >= 0 && nextCol < grid[0].length && moves + i < 4) {
                nextAdditionalCost += grid[nextRow][nextCol];
                dirs.add(new int[]{nextRow, nextCol, moves + i, dir, cost + nextAdditionalCost});
            }

            int leftRow = moveRow(row, leftDir, i);
            int leftCol = moveCol(col, leftDir, i);
            if (leftRow >= 0 && leftRow < grid.length && leftCol >= 0 && leftCol < grid[0].length) {
                leftAdditionalCost += grid[leftRow][leftCol];
                dirs.add(new int[]{leftRow, leftCol, i, leftDir, cost + leftAdditionalCost});
            }

            int rightRow = moveRow(row, rightDir, i);
            int rightCol = moveCol(col, rightDir, i);
            if (rightRow >= 0 && rightRow < grid.length && rightCol >= 0 && rightCol < grid[0].length) {
                rightAdditionalCost += grid[rightRow][rightCol];
                dirs.add(new int[]{rightRow, rightCol, i, rightDir, cost + rightAdditionalCost});
            }
        }

        return dirs;
    }

    private int moveRow(int row, int dir, int steps) {
        if (dir == UP) {
            return row - steps;
        } else if (dir == DOWN) {
            return row + steps;
        } else {
            return row;
        }
    }

    private int moveCol(int col, int dir, int steps) {
        if (dir == LEFT) {
            return col - steps;
        } else if (dir == RIGHT) {
            return col + steps;
        } else {
            return col;
        }
    }

    private int left(int dir) {
        if (dir == UP) {
            return LEFT;
        } else if (dir == DOWN) {
            return RIGHT;
        } else if (dir == LEFT) {
            return DOWN;
        } else {
            return UP;
        }
    }

    private int right(int dir) {
        if (dir == UP) {
            return RIGHT;
        } else if (dir == DOWN) {
            return LEFT;
        } else if (dir == LEFT) {
            return UP;
        } else {
            return DOWN;
        }
    }
}
