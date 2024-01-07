package org.example.eleven;

import org.example.Solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Eleven implements Solver {
    public static final Eleven SINGLETON = new Eleven();

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
        List<Integer> expandedRows = new ArrayList<>();
        List<Integer> expandedCols = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (!line.contains("#")) expandedRows.add(i);
        }

        List<String> arrT = transpose(lines);
        for (int i = 0; i < arrT.size(); i++) {
            String line = arrT.get(i);
            if (!line.contains("#")) expandedCols.add(i);
        }

        char[][] grid = new char[lines.size()][lines.get(0).length()];
        for (int i = 0; i < lines.size(); i++) {
            grid[i] = lines.get(i).toCharArray();
        }

        List<int[]> galaxyPositions = new ArrayList<>();
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == '#') {
                    galaxyPositions.add(new int[]{row, col});
                }
            }
        }

        long ans = 0;
        for (int i = 0; i < galaxyPositions.size(); i++) {
            for (int j = i + 1; j < galaxyPositions.size(); j++) {
                int[] start = galaxyPositions.get(i);
                int[] end = galaxyPositions.get(j);
                int startRow = Math.min(start[0], end[0]);
                int startCol = Math.min(start[1], end[1]);
                int endRow = Math.max(start[0], end[0]);
                int endCol = Math.max(start[1], end[1]);
                int rowExpansions = 0;
                int colExpansions = 0;
                for (int row : expandedRows) {
                    if (startRow <= row && row <= endRow) rowExpansions++;
                }
                for (int col : expandedCols) {
                    if (startCol <= col && col <= endCol) colExpansions++;
                }
                long distance = Math.abs(start[0] - end[0]) - rowExpansions + (rowExpansions * 1_000_000L)
                        + Math.abs(start[1] - end[1]) - colExpansions + (colExpansions * 1_000_000L);
                ans += distance;
            }
        }

        return String.valueOf(ans);
    }

    private String solve1() {
        List<String> arr = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            arr.add(line);
            if (!line.contains("#")) arr.add(line);
        }

        List<String> arrT = transpose(arr);
        List<String> finalArr = new ArrayList<>();
        for (int i = 0; i < arrT.size(); i++) {
            String line = arrT.get(i);
            finalArr.add(line);
            if (!line.contains("#")) finalArr.add(line);
        }

        char[][] grid = new char[finalArr.size()][finalArr.get(0).length()];
        for (int i = 0; i < finalArr.size(); i++) {
            grid[i] = finalArr.get(i).toCharArray();
        }

        List<int[]> galaxyPositions = new ArrayList<>();
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == '#') {
                    galaxyPositions.add(new int[]{row, col});
                }
            }
        }

        long ans = 0;
        for (int i = 0; i < galaxyPositions.size(); i++) {
            for (int j = i + 1; j < galaxyPositions.size(); j++) {
                int[] a = galaxyPositions.get(i);
                int[] b = galaxyPositions.get(j);
                long distance = Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]);
                ans += distance;
            }
        }

        return String.valueOf(ans);
    }

    private List<String> transpose(List<String> input) {
        int rows = input.size();
        int cols = input.get(0).length();

        List<String> output = new ArrayList<>(cols);
        for (int col = 0; col < cols; col++) {
            StringBuilder sb = new StringBuilder();
            for (int row = 0; row < rows; row++) {
                sb.append(input.get(row).charAt(col));
            }
            output.add(sb.toString());
        }

        return output;
    }
}
