package org.example.sixteen;

import org.example.Solver;

import java.util.*;

public class Sixteen implements Solver {
    public static final Sixteen SINGLETON = new Sixteen();

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

    enum DIR {
        UP, DOWN, LEFT, RIGHT
    }

    private String solve2() {
        int rows = lines.size();
        int cols = lines.get(0).length();
        char[][] grid = new char[rows][cols];
        for (int i = 0; i < lines.size(); i++) {
            grid[i] = lines.get(i).toCharArray();
        }

        int ans = Integer.MIN_VALUE;

        int row = 0;
        for (int col = 0; col < cols; col++) {
            boolean[][] energized = new boolean[rows][cols];
            stepIterative2(grid, energized, row, col, DIR.DOWN);
            ans = Math.max(ans, countEnergy(energized));
        }

        row = rows - 1;
        for (int col = 0; col < cols; col++) {
            boolean[][] energized = new boolean[rows][cols];
            stepIterative2(grid, energized, row, col, DIR.UP);
            ans = Math.max(ans, countEnergy(energized));
        }

        int col = 0;
        for (row = 0; row < rows; row++) {
            boolean[][] energized = new boolean[rows][cols];
            stepIterative2(grid, energized, row, col, DIR.RIGHT);
            ans = Math.max(ans, countEnergy(energized));
        }

        col = cols - 1;
        for (row = 0; row < rows; row++) {
            boolean[][] energized = new boolean[rows][cols];
            stepIterative2(grid, energized, row, col, DIR.LEFT);
            ans = Math.max(ans, countEnergy(energized));
        }

        return String.valueOf(ans);
    }
    private void stepIterative2(char[][] grid, boolean[][] energized, int startRow, int startCol, DIR startDir) {
        Queue<Position> q = new LinkedList<>();
        Position start = new Position(startRow, startCol, startDir);
        Set<Position> seen = new HashSet<>();

        q.add(start);

        while (!q.isEmpty()) {
            Position curr = q.poll();
            int row = curr.row;
            int col = curr.col;
            DIR dir = curr.dir;

            if (row < 0 || row >= grid.length || col < 0 || col >= grid[0].length) {
                continue;
            }
            if (seen.contains(curr)) {
                continue;
            }

            seen.add(curr);
            char c = grid[row][col];
            energized[row][col] = true;

            if (c == '.') {
                q.add(new Position(bounceRow(row, dir), bounceCol(col, dir), dir));
            } else if (c == '|' && dir.equals(DIR.UP)) {
                q.add(new Position(bounceRow(row, dir), bounceCol(col, dir), dir));
            } else if (c == '|' && dir.equals(DIR.DOWN)) {
                q.add(new Position(bounceRow(row, dir), bounceCol(col, dir), dir));
            } else if (c == '|' && dir.equals(DIR.LEFT)) {
                q.add(new Position(bounceRow(row, DIR.UP), bounceCol(col, DIR.UP), DIR.UP));
                q.add(new Position(bounceRow(row, DIR.DOWN), bounceCol(col, DIR.DOWN), DIR.DOWN));
            } else if (c == '|' && dir.equals(DIR.RIGHT)) {
                q.add(new Position(bounceRow(row, DIR.UP), bounceCol(col, DIR.UP), DIR.UP));
                q.add(new Position(bounceRow(row, DIR.DOWN), bounceCol(col, DIR.DOWN), DIR.DOWN));
            } else if (c == '-' && dir.equals(DIR.UP)) {
                q.add(new Position(bounceRow(row, DIR.LEFT), bounceCol(col, DIR.LEFT), DIR.LEFT));
                q.add(new Position(bounceRow(row, DIR.RIGHT), bounceCol(col, DIR.RIGHT), DIR.RIGHT));
            } else if (c == '-' && dir.equals(DIR.DOWN)) {
                q.add(new Position(bounceRow(row, DIR.LEFT), bounceCol(col, DIR.LEFT), DIR.LEFT));
                q.add(new Position(bounceRow(row, DIR.RIGHT), bounceCol(col, DIR.RIGHT), DIR.RIGHT));
            } else if (c == '-' && dir.equals(DIR.LEFT)) {
                q.add(new Position(bounceRow(row, dir), bounceCol(col, dir), dir));
            } else if (c == '-' && dir.equals(DIR.RIGHT)) {
                q.add(new Position(bounceRow(row, dir), bounceCol(col, dir), dir));
            } else if (c == '\\' && dir.equals(DIR.UP)) {
                q.add(new Position(bounceRow(row, DIR.LEFT), bounceCol(col, DIR.LEFT), DIR.LEFT));
            } else if (c == '\\' && dir.equals(DIR.DOWN)) {
                q.add(new Position(bounceRow(row, DIR.RIGHT), bounceCol(col, DIR.RIGHT), DIR.RIGHT));
            } else if (c == '\\' && dir.equals(DIR.LEFT)) {
                q.add(new Position(bounceRow(row, DIR.UP), bounceCol(col, DIR.UP), DIR.UP));
            } else if (c == '\\' && dir.equals(DIR.RIGHT)) {
                q.add(new Position(bounceRow(row, DIR.DOWN), bounceCol(col, DIR.DOWN), DIR.DOWN));
            } else if (c == '/' && dir.equals(DIR.UP)) {
                q.add(new Position(bounceRow(row, DIR.RIGHT), bounceCol(col, DIR.RIGHT), DIR.RIGHT));
            } else if (c == '/' && dir.equals(DIR.DOWN)) {
                q.add(new Position(bounceRow(row, DIR.LEFT), bounceCol(col, DIR.LEFT), DIR.LEFT));
            } else if (c == '/' && dir.equals(DIR.LEFT)) {
                q.add(new Position(bounceRow(row, DIR.DOWN), bounceCol(col, DIR.DOWN), DIR.DOWN));
            } else if (c == '/' && dir.equals(DIR.RIGHT)) {
                q.add(new Position(bounceRow(row, DIR.UP), bounceCol(col, DIR.UP), DIR.UP));
            } else {
                throw new AssertionError();
            }
        }
    }

    private int countEnergy(boolean[][] energized) {
        int ans = 0;
        for (int i = 0; i < energized.length; i++) {
            for (int j = 0; j < energized[0].length; j++) {
                if (energized[i][j]) {
                    ans++;
                }
            }
        }
        return ans;
    }

    private String solve1() {
        int rows = lines.size();
        int cols = lines.get(0).length();
        char[][] grid = new char[rows][cols];
        for (int i = 0; i < lines.size(); i++) {
            grid[i] = lines.get(i).toCharArray();
        }

        boolean[][] energized = new boolean[rows][cols];

//        stepRecursive(grid, energized, 0, 0, DIR.RIGHT);
        stepIterative(grid, energized);

        int ans = 0;
        for (int i = 0; i < energized.length; i++) {
            for (int j = 0; j < energized[0].length; j++) {
                if (energized[i][j]) {
                    ans++;
                }
            }
        }

        return String.valueOf(ans);
    }

    static class Position {
        int row;
        int col;
        DIR dir;

        public Position(int row, int col, DIR dir) {
            this.row = row;
            this.col = col;
            this.dir = dir;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col, dir);
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }

            if (!(o instanceof Position)) {
                return false;
            }

            Position other = (Position) o;
            return row == other.row && col == other.col && dir == other.dir;
        }
    }


    private void stepIterative(char[][] grid, boolean[][] energized) {
        Queue<Position> q = new LinkedList<>();
        Position start = new Position(0, 0, DIR.RIGHT);
        Set<Position> seen = new HashSet<>();

        q.add(start);

        while (!q.isEmpty()) {
            Position curr = q.poll();
            int row = curr.row;
            int col = curr.col;
            DIR dir = curr.dir;

            if (row < 0 || row >= grid.length || col < 0 || col >= grid[0].length) {
                continue;
            }
            if (seen.contains(curr)) {
                continue;
            }

            seen.add(curr);
            char c = grid[row][col];
            energized[row][col] = true;
//            System.out.println(row + " " + col + " " + dir);

            if (c == '.') {
                q.add(new Position(bounceRow(row, dir), bounceCol(col, dir), dir));
            } else if (c == '|' && dir.equals(DIR.UP)) {
                q.add(new Position(bounceRow(row, dir), bounceCol(col, dir), dir));
            } else if (c == '|' && dir.equals(DIR.DOWN)) {
                q.add(new Position(bounceRow(row, dir), bounceCol(col, dir), dir));
            } else if (c == '|' && dir.equals(DIR.LEFT)) {
                q.add(new Position(bounceRow(row, DIR.UP), bounceCol(col, DIR.UP), DIR.UP));
                q.add(new Position(bounceRow(row, DIR.DOWN), bounceCol(col, DIR.DOWN), DIR.DOWN));
            } else if (c == '|' && dir.equals(DIR.RIGHT)) {
                q.add(new Position(bounceRow(row, DIR.UP), bounceCol(col, DIR.UP), DIR.UP));
                q.add(new Position(bounceRow(row, DIR.DOWN), bounceCol(col, DIR.DOWN), DIR.DOWN));
            } else if (c == '-' && dir.equals(DIR.UP)) {
                q.add(new Position(bounceRow(row, DIR.LEFT), bounceCol(col, DIR.LEFT), DIR.LEFT));
                q.add(new Position(bounceRow(row, DIR.RIGHT), bounceCol(col, DIR.RIGHT), DIR.RIGHT));
            } else if (c == '-' && dir.equals(DIR.DOWN)) {
                q.add(new Position(bounceRow(row, DIR.LEFT), bounceCol(col, DIR.LEFT), DIR.LEFT));
                q.add(new Position(bounceRow(row, DIR.RIGHT), bounceCol(col, DIR.RIGHT), DIR.RIGHT));
            } else if (c == '-' && dir.equals(DIR.LEFT)) {
                q.add(new Position(bounceRow(row, dir), bounceCol(col, dir), dir));
            } else if (c == '-' && dir.equals(DIR.RIGHT)) {
                q.add(new Position(bounceRow(row, dir), bounceCol(col, dir), dir));
            } else if (c == '\\' && dir.equals(DIR.UP)) {
                q.add(new Position(bounceRow(row, DIR.LEFT), bounceCol(col, DIR.LEFT), DIR.LEFT));
            } else if (c == '\\' && dir.equals(DIR.DOWN)) {
                q.add(new Position(bounceRow(row, DIR.RIGHT), bounceCol(col, DIR.RIGHT), DIR.RIGHT));
            } else if (c == '\\' && dir.equals(DIR.LEFT)) {
                q.add(new Position(bounceRow(row, DIR.UP), bounceCol(col, DIR.UP), DIR.UP));
            } else if (c == '\\' && dir.equals(DIR.RIGHT)) {
                q.add(new Position(bounceRow(row, DIR.DOWN), bounceCol(col, DIR.DOWN), DIR.DOWN));
            } else if (c == '/' && dir.equals(DIR.UP)) {
                q.add(new Position(bounceRow(row, DIR.RIGHT), bounceCol(col, DIR.RIGHT), DIR.RIGHT));
            } else if (c == '/' && dir.equals(DIR.DOWN)) {
                q.add(new Position(bounceRow(row, DIR.LEFT), bounceCol(col, DIR.LEFT), DIR.LEFT));
            } else if (c == '/' && dir.equals(DIR.LEFT)) {
                q.add(new Position(bounceRow(row, DIR.DOWN), bounceCol(col, DIR.DOWN), DIR.DOWN));
            } else if (c == '/' && dir.equals(DIR.RIGHT)) {
                q.add(new Position(bounceRow(row, DIR.UP), bounceCol(col, DIR.UP), DIR.UP));
            } else {
                throw new AssertionError();
            }
        }
    }

    private int bounceRow(int row, DIR dir) {
        if (dir.equals(DIR.UP)) {
            return row - 1;
        } else if (dir.equals(DIR.DOWN)) {
            return row + 1;
        } else {
            return row;
        }
    }

    private int bounceCol(int col, DIR dir) {
        if (dir.equals(DIR.LEFT)) {
            return col - 1;
        } else if (dir.equals(DIR.RIGHT)) {
            return col + 1;
        } else {
            return col;
        }
    }

    private void stepRecursive(char[][] grid, boolean[][] energized, int row, int col, DIR dir) {
        if (row < 0 || row >= grid.length || col < 0 || col >= grid[0].length) {
            return;
        }

        char c = grid[row][col];
        energized[row][col] = true;

        if (c == '.') {
            stepRecursive(grid, energized, bounceRow(row, dir), bounceCol(col, dir), dir);
        } else if (c == '|' && dir.equals(DIR.UP)) {
            stepRecursive(grid, energized, bounceRow(row, dir), bounceCol(col, dir), dir);
        } else if (c == '|' && dir.equals(DIR.DOWN)) {
            stepRecursive(grid, energized, bounceRow(row, dir), bounceCol(col, dir), dir);
        } else if (c == '|' && dir.equals(DIR.LEFT)) {
            stepRecursive(grid, energized, bounceRow(row, DIR.UP), bounceCol(col, DIR.UP), DIR.UP);
            stepRecursive(grid, energized, bounceRow(row, DIR.DOWN), bounceCol(col, DIR.DOWN), DIR.DOWN);
        } else if (c == '|' && dir.equals(DIR.RIGHT)) {
            stepRecursive(grid, energized, bounceRow(row, DIR.UP), bounceCol(col, DIR.UP), DIR.UP);
            stepRecursive(grid, energized, bounceRow(row, DIR.DOWN), bounceCol(col, DIR.DOWN), DIR.DOWN);
        } else if (c == '-' && dir.equals(DIR.UP)) {
            stepRecursive(grid, energized, bounceRow(row, DIR.LEFT), bounceCol(col, DIR.LEFT), DIR.LEFT);
            stepRecursive(grid, energized, bounceRow(row, DIR.RIGHT), bounceCol(col, DIR.RIGHT), DIR.RIGHT);
        } else if (c == '-' && dir.equals(DIR.DOWN)) {
            stepRecursive(grid, energized, bounceRow(row, DIR.LEFT), bounceCol(col, DIR.LEFT), DIR.LEFT);
            stepRecursive(grid, energized, bounceRow(row, DIR.RIGHT), bounceCol(col, DIR.RIGHT), DIR.RIGHT);
        } else if (c == '-' && dir.equals(DIR.LEFT)) {
            stepRecursive(grid, energized, bounceRow(row, dir), bounceCol(col, dir), dir);
        } else if (c == '-' && dir.equals(DIR.RIGHT)) {
            stepRecursive(grid, energized, bounceRow(row, dir), bounceCol(col, dir), dir);
        } else if (c == '\\' && dir.equals(DIR.UP)) {
            stepRecursive(grid, energized, bounceRow(row, DIR.LEFT), bounceCol(col, DIR.LEFT), DIR.LEFT);
        } else if (c == '\\' && dir.equals(DIR.DOWN)) {
            stepRecursive(grid, energized, bounceRow(row, DIR.RIGHT), bounceCol(col, DIR.RIGHT), DIR.RIGHT);
        } else if (c == '\\' && dir.equals(DIR.LEFT)) {
            stepRecursive(grid, energized, bounceRow(row, DIR.UP), bounceCol(col, DIR.UP), DIR.UP);
        } else if (c == '\\' && dir.equals(DIR.RIGHT)) {
            stepRecursive(grid, energized, bounceRow(row, DIR.DOWN), bounceCol(col, DIR.DOWN), DIR.DOWN);
        } else if (c == '/' && dir.equals(DIR.UP)) {
            stepRecursive(grid, energized, bounceRow(row, DIR.RIGHT), bounceCol(col, DIR.RIGHT), DIR.RIGHT);
        } else if (c == '/' && dir.equals(DIR.DOWN)) {
            stepRecursive(grid, energized, bounceRow(row, DIR.LEFT), bounceCol(col, DIR.LEFT), DIR.LEFT);
        } else if (c == '/' && dir.equals(DIR.LEFT)) {
            stepRecursive(grid, energized, bounceRow(row, DIR.DOWN), bounceCol(col, DIR.DOWN), DIR.DOWN);
        } else if (c == '/' && dir.equals(DIR.RIGHT)) {
            stepRecursive(grid, energized, bounceRow(row, DIR.UP), bounceCol(col, DIR.UP), DIR.UP);
        } else {
            throw new AssertionError();
        }
    }
}
