package org.example.eighteen;

import org.example.Solver;

import java.util.ArrayList;
import java.util.List;

public class Eighteen implements Solver {
    public static final Eighteen SINGLETON = new Eighteen();

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
        List<long[]> coords = new ArrayList<>();
        long[] currPos = new long[]{0, 0};
        coords.add(currPos);

        long perimeter = 0;
        for (String line : lines) {
            String[] tokens = line.split(" ");
            String instructions = tokens[2].substring(2, tokens[2].length() - 1);
            if (instructions.length() != 6) throw new AssertionError();

            long distance = Long.parseLong(instructions.substring(0, 5), 16);
            String dir = instructions.substring(5);

            currPos = move2(currPos, dir, distance);
            coords.add(new long[]{currPos[0], currPos[1]});
            if (dir.equals("2") || dir.equals("3")) perimeter += distance;
        }

//        System.out.println(shoelaceArea(coords));
        return String.valueOf((long) shoelaceArea(coords) + perimeter + 1);
    }

    long[] move2(long[] pos, String dir, long times) {
        long[] newPos = new long[]{pos[0], pos[1]};
        if (dir.equals("3")) { // U
            newPos[0] -= times;
        } else if (dir.equals("1")) { // D
            newPos[0] += times;
        } else if (dir.equals("2")) { // L
            newPos[1] -= times;
        } else if (dir.equals("0")) { // R
            newPos[1] += times;
        }
        return newPos;
    }

    private String solve1() {
        int n = lines.size();

        int idx = 0;
        int[][] coords = new int[n + 1][2];
        int[] currPos = new int[]{0, 0};
        coords[idx++] = currPos;

        int perimeter = 0;
        for (String line : lines) {
            String[] tokens = line.split(" ");
            String dir = tokens[0];
            int times = Integer.parseInt(tokens[1]);
            String color = tokens[2];

            currPos = move(currPos, dir, times);
            coords[idx++] = currPos;

            perimeter += times;
        }

        return String.valueOf(shoelace(coords) + perimeter / 2 + 1);
    }

    int[] move(int[] pos, String dir, int times) {
        int[] newPos = new int[]{pos[0], pos[1]};
        if (dir.equals("U")) {
            newPos[0] -= times;
        } else if (dir.equals("D")) {
            newPos[0] += times;
        } else if (dir.equals("L")) {
            newPos[1] -= times;
        } else if (dir.equals("R")) {
            newPos[1] += times;
        }
        return newPos;
    }

    private static double shoelaceArea(List<long[]> v) {
        int n = v.size();
        double a = 0.0;
        for (int i = 0; i < n - 1; i++) {
            a += v.get(i)[0] * v.get(i + 1)[1] - v.get(i + 1)[0] * v.get(i)[1];
        }
        return Math.abs(a + v.get(n - 1)[0] * v.get(0)[1] - v.get(0)[0] * v.get(n - 1)[1]) / 2.0;
    }

    // WARNING: This shoelace method is not correct and gives off-by-one errors for large inputs
    private double shoelace(int[][] arr) {
        int n = arr.length;
        /** copy initial point to last row **/
        arr[n - 1][0] = arr[0][0];
        arr[n - 1][1] = arr[0][1];

        double det = 0.0;
        /** add product of x coordinate of ith point with y coordinate of (i + 1)th point **/
        for (int i = 0; i < n - 1; i++)
            det += (double) (arr[i][0] * arr[i + 1][1]);
        /** subtract product of y coordinate of ith point with x coordinate of (i + 1)th point **/
        for (int i = 0; i < n - 1; i++)
            det -= (double) (arr[i][1] * arr[i + 1][0]);

        /** find absolute value and divide by 2 **/
        det = Math.abs(det);
        det /= 2;
        return det;
    }
}
