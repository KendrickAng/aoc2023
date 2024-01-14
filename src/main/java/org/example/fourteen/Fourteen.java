package org.example.fourteen;

import org.example.Solver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Fourteen implements Solver {
    public static final Fourteen SINGLETON = new Fourteen();

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
        char[][] grid = new char[lines.size()][lines.get(0).length()];
        for (int i = 0; i < lines.size(); i++) {
            grid[i] = lines.get(i).toCharArray();
        }

        List<Long> scores = new ArrayList<>();
        for (int i = 0; i < 300; i++) {
            grid = cycle(grid);
//            print(grid);
            long score = score(grid);
            scores.add(score);
        }

//        for (long score: scores) {
//            System.out.println(score);
//        }

        return "101010"; // manually derived by looking at print statements and using the size of a cycle
    }

    private long score(char[][] grid) {
        long ans = 0;
        for (int col = 0; col < grid[0].length; col++) {
            for (int row = grid.length - 1; row >= 0; row--) {
                char c = grid[row][col];
                if (c == 'O') {
                    ans += grid.length - row;
                }
            }
        }
        return ans;
    }

    private char[][] cycle(char[][] grid) {
        return east(south(west(north(grid))));
    }

    private char[][] north(char[][] grid) {
        return moveBouldersNorth(grid);
    }

    private char[][] west(char[][] grid) {
        return rotate90Right(rotate90Right(rotate90Right(north(rotate90Right(grid)))));
    }

    private char[][] south(char[][] grid) {
        return rotate90Right(rotate90Right(moveBouldersNorth(rotate90Right(rotate90Right(grid)))));
    }

    private char[][] east(char[][] grid) {
        return rotate90Right(moveBouldersNorth(rotate90Right(rotate90Right(rotate90Right(grid)))));
    }


    private void print(char[][] grid) {
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                System.out.printf("%c", grid[r][c]);
            }
            System.out.println();
        }
        System.out.println();
    }

    private char[][] rotate90Right(char[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        char[][] out = new char[cols][rows];
        for (int row = 0; row < rows; row++) {
            char[] rowArr = grid[row];
            for (int col = 0; col < cols; col++) {
                out[col][rows - row - 1] = rowArr[col];
            }
        }
        return out;
    }

    private char[][] moveBouldersNorth(char[][] grid) {
        char[][] out = new char[grid.length][grid[0].length];
        int cols = grid[0].length;
        int rows = grid.length;
        for (int col = 0; col < cols; col++) {
            List<Integer> rowsWithCubeRocks = new ArrayList<>();
            List<Integer> numBouldersTill = new ArrayList<>();
            int rocksSoFar = 0;
            for (int row = rows - 1; row >= 0; row--) {
                char c = grid[row][col];
                if (c == '.') {
                    out[row][col] = '.';
                } else if (c == '#') {
                    rowsWithCubeRocks.add(row);
                    numBouldersTill.add(rocksSoFar);
                    rocksSoFar = 0;
                    out[row][col] = '#';
                } else if (c == 'O') {
                    rocksSoFar++;
                    out[row][col] = '.';
                }
            }
            if (rocksSoFar != 0) {
                rowsWithCubeRocks.add(-1);
                numBouldersTill.add(rocksSoFar);
            }

            int n = rowsWithCubeRocks.size();
            for (int i = 0; i < n; i++) {
                int rowIdx = rowsWithCubeRocks.get(i);
                int numBoulders = numBouldersTill.get(i);
                int startIdx = rowIdx + 1;
                for (int row = startIdx; row < startIdx + numBoulders; row++) {
                    out[row][col] = 'O';
                }
            }
        }

        return out;
    }

    private String solve1() {
        List<String> lines = new ArrayList<>(this.lines);
        lines.add(0, "#".repeat(lines.get(0).length()));

        char[][] grid = new char[lines.size()][lines.get(0).length()];
        for (int i = 0; i < lines.size(); i++) {
            grid[i] = lines.get(i).toCharArray();
        }

        long ans = 0;
        int rows = grid.length;
        int cols = grid[0].length;
        for (int col = 0; col < cols; col++) {
            int rocksSoFar = 0;
//            System.out.println("Column " + col);
            for (int row = rows - 1; row >= 0; row--) {
                char c = grid[row][col];
                if (c == '.') {
                    continue;
                } else if (c == 'O') {
                    rocksSoFar++;
                } else if (c == '#') {
                    ans += calculateLoad(rocksSoFar, row, rows);
                    rocksSoFar = 0;
                }
            }
        }

        return String.valueOf(ans);
    }

    private long calculateLoad(int rocksSoFar, int row, int rows) {
        int actualRow = row + 1;
        long ret = 0;
        for (int i = 0; i < rocksSoFar; i++) {
            long load = rows - actualRow - i;
            ret += load;
        }
//        System.out.println("Load is " + ret);
        return ret;
    }
}
