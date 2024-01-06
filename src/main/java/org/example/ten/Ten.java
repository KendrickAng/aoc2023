package org.example.ten;

import org.example.Solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Ten implements Solver {
    public static final Ten SINGLETON = new Ten();

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
        List<String> lines = new ArrayList<>(this.lines);
        int n = lines.get(0).length();
        for (int i = 0; i < lines.size(); i++) {
            lines.set(i, "." + lines.get(i) + ".");
        }
        lines.add(0, ".".repeat(n + 2));
        lines.add(".".repeat(n + 2));

        char[][] grid = new char[lines.size()][lines.get(0).length()];
        for (int i = 0; i < lines.size(); i++) {
            grid[i] = lines.get(i).toCharArray();
        }

        int startRow = -1;
        int startCol = -1;
        outer:
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                char c = grid[row][col];
                if (c == 'S') {
                    startRow = row;
                    startCol = col;
                    break outer;
                }
            }
        }

        int loopLength = Integer.MIN_VALUE;
        List<int[]> correctPts = null;
        for (char c : new char[]{'|', '-', 'L', 'J', '7', 'F'}) {
            grid[startRow][startCol] = c;
            List<int[]> pts = new ArrayList<>();
            boolean[][] explored = new boolean[grid.length][grid[0].length];
            int tilesCovered = iterative2(grid, startRow, startCol, explored, pts);
            if (tilesCovered > loopLength) {
                loopLength = tilesCovered;
                correctPts = pts;
            }
        }

        int[][] ptsArr = new int[correctPts.size()][2];
        for (int i = 0; i < correctPts.size(); i++) {
            ptsArr[i] = correctPts.get(i);
        }
        double ans = shoelace(ptsArr);
//        System.out.println("A: " + ans);
//        System.out.println("B: " + ptsArr.length);

        return String.valueOf((int) Math.ceil(ans + 1 - (ptsArr.length / 2.0)));
    }

    private double shoelace(int[][] arr)
    {
        int n = arr.length;
        /** copy initial point to last row **/
        arr[n - 1][0] = arr[0][0];
        arr[n - 1][1] = arr[0][1];

        double det = 0.0;
        /** add product of x coordinate of ith point with y coordinate of (i + 1)th point **/
        for (int i = 0; i < n - 1; i++)
            det += (double)(arr[i][0] * arr[i + 1][1]);
        /** subtract product of y coordinate of ith point with x coordinate of (i + 1)th point **/
        for (int i = 0; i < n - 1; i++)
            det -= (double)(arr[i][1] * arr[i + 1][0]);

        /** find absolute value and divide by 2 **/
        det = Math.abs(det);
        det /= 2;
        return det;
    }

    private String solve1() {
        List<String> lines = new ArrayList<>(this.lines);
        int n = lines.get(0).length();
        for (int i = 0; i < lines.size(); i++) {
            lines.set(i, "." + lines.get(i) + ".");
        }
        lines.add(0, ".".repeat(n + 2));
        lines.add(".".repeat(n + 2));

        char[][] grid = new char[lines.size()][lines.get(0).length()];
        for (int i = 0; i < lines.size(); i++) {
            grid[i] = lines.get(i).toCharArray();
        }

        int startRow = -1;
        int startCol = -1;
        outer:
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                char c = grid[row][col];
                if (c == 'S') {
                    startRow = row;
                    startCol = col;
                    break outer;
                }
            }
        }

        int ans = Integer.MIN_VALUE;
        for (char c : new char[]{'|', '-', 'L', 'J', '7', 'F'}) {
            grid[startRow][startCol] = c;
            boolean[][] explored = new boolean[grid.length][grid[0].length];
            ans = Math.max(ans, iterative(grid, startRow, startCol, explored));
        }

        return String.valueOf((ans + 1) / 2);
    }

    int iterative2(char[][] grid, int currRow, int currCol, boolean[][] explored, List<int[]> pts) {
        int steps = 0;
        while (true) {
            explored[currRow][currCol] = true;
            pts.add(new int[]{currRow, currCol});
            char c = grid[currRow][currCol];
            int[][] next = null;
            if (c == '|') {
                next = new int[][]{{-1, 0}, {1, 0}};
            } else if (c == '-') {
                next = new int[][]{{0, -1}, {0, 1}};
            } else if (c == 'L') {
                next = new int[][]{{-1, 0}, {0, 1}};
            } else if (c == 'J') {
                next = new int[][]{{-1, 0}, {0, -1}};
            } else if (c == '7') {
                next = new int[][]{{0, -1}, {1, 0}};
            } else if (c == 'F') {
                next = new int[][]{{1, 0}, {0, 1}};
            } else {
                throw new AssertionError();
            }
            boolean hasNext = false;
            for (int[] nn : next) {
                int nextRow = currRow + nn[0];
                int nextCol = currCol + nn[1];
                if (explored[nextRow][nextCol] || grid[nextRow][nextCol] == '.') continue;
                currRow = nextRow;
                currCol = nextCol;
                steps++;
                hasNext = true;
                break;
            }
            if (!hasNext) break;
        }
        return steps;
    }

    int iterative(char[][] grid, int currRow, int currCol, boolean[][] explored) {
        int steps = 0;
        while (true) {
            explored[currRow][currCol] = true;
            char c = grid[currRow][currCol];
            int[][] next = null;
            if (c == '|') {
                next = new int[][]{{-1, 0}, {1, 0}};
            } else if (c == '-') {
                next = new int[][]{{0, -1}, {0, 1}};
            } else if (c == 'L') {
                next = new int[][]{{-1, 0}, {0, 1}};
            } else if (c == 'J') {
                next = new int[][]{{-1, 0}, {0, -1}};
            } else if (c == '7') {
                next = new int[][]{{0, -1}, {1, 0}};
            } else if (c == 'F') {
                next = new int[][]{{1, 0}, {0, 1}};
            } else {
                throw new AssertionError();
            }
            boolean hasNext = false;
            for (int[] nn : next) {
                int nextRow = currRow + nn[0];
                int nextCol = currCol + nn[1];
                if (explored[nextRow][nextCol] || grid[nextRow][nextCol] == '.') continue;
                currRow = nextRow;
                currCol = nextCol;
                steps++;
                hasNext = true;
                break;
            }
            if (!hasNext) break;
        }
        return steps;
    }

    int dfs(char[][] grid, int row, int col, boolean[][] explored) {
        char c = grid[row][col];
        if (c == '.') return 0;
        if (explored[row][col]) return 0;

        explored[row][col] = true;

        int[][] next = null;
        switch (c) {
            case '|':
                next = new int[][]{{-1, 0}, {1, 0}};
                break;
            case '-':
                next = new int[][]{{0, -1}, {0, 1}};
                break;
            case 'L':
                next = new int[][]{{-1, 0}, {0, 1}};
                break;
            case 'J':
                next = new int[][]{{-1, 0}, {0, -1}};
                break;
            case '7':
                next = new int[][]{{0, -1}, {1, 0}};
                break;
            case 'F':
                next = new int[][]{{1, 0}, {0, 1}};
                break;
            case 'S':
                next = new int[][]{{0, -1}, {0, 1}, {1, 0}, {-1, 0}};
                break;
            default:
                throw new AssertionError();
        }

        int max = Integer.MIN_VALUE;
        for (int[] n : next) {
            int nextRow = row + n[0];
            int nextCol = col + n[1];

            int val = 1 + dfs(grid, nextRow, nextCol, explored);

            max = Math.max(max, val);
        }

        return max;
    }
}
