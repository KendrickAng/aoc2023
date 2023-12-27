package org.example.six;

import org.example.Solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Six implements Solver {
    public static final Six SINGLETON = new Six();
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

    private String solve1() {
        if (lines.size() != 2) throw new AssertionError();

        int[] times = Arrays.stream(lines.get(0).split(" +")).skip(1).mapToInt(Integer::parseInt).toArray();
        int[] distances = Arrays.stream(lines.get(1).split(" +")).skip(1).mapToInt(Integer::parseInt).toArray();

        if (times.length != distances.length) throw new AssertionError();

        long answer = 0;

        for (int i = 0; i < times.length; i++) {
            long recordsBeat = 0;
            int time = times[i];
            int distance = distances[i];

            for (int held = 0; held <= time; held++) {
                int moving = time - held;
                int distanceCovered = held * moving; // speed = held
                if (distanceCovered > distance) {
                    recordsBeat++;
                }
            }

            if (recordsBeat > 0 && answer == 0) {
                answer = recordsBeat;
            } else if (recordsBeat > 0) {
                answer *= recordsBeat;
            }
        }

        return String.valueOf(answer);
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
        if (lines.size() != 2) throw new AssertionError();

        long time = Long.parseLong(Arrays.stream(lines.get(0).split(" +")).skip(1).collect(Collectors.joining("")));
        long distance = Long.parseLong(Arrays.stream(lines.get(1).split(" +")).skip(1).collect(Collectors.joining("")));

        long recordsBeat = 0;
        for (long held = 0; held <= time; held++) {
            if (held * (time - held) > distance) {
                recordsBeat++;
            }
        }

        return String.valueOf(recordsBeat);
    }
}
