package org.example.nine;

import org.example.Solver;

import java.util.*;
import java.util.stream.Collectors;

public class Nine implements Solver {
    public static final Nine SINGLETON = new Nine();

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

    private String solve1() {
        long extrapolatedValueSum = 0;
        for(String line: lines) {
            List<Integer> nums = Arrays.stream(line.split(" ")).map(Integer::parseInt).toList();
            List<List<Integer>> processed = new ArrayList<>();
            processed.add(nums);

            while (true) {
                List<Integer> last = processed.get(processed.size() - 1);
                int lastNum = last.get(last.size() - 1);
                if (lastNum == 0) break;

                List<Integer> next = new ArrayList<>();
                for (int i = 1; i < last.size(); i++) {
                    next.add(last.get(i) - last.get(i - 1));
                }
                processed.add(next);
            }

            long extrapolatedValue = 0;
            for (List<Integer> l: processed) {
                int lastNum = l.get(l.size() - 1);
                extrapolatedValue += lastNum;
            }

            extrapolatedValueSum += extrapolatedValue;
        }

        return String.valueOf(extrapolatedValueSum);
    }

    private String solve2() {
        long extrapolatedValueSum = 0;
        for(String line: lines) {
            List<Integer> nums = Arrays.stream(line.split(" ")).map(Integer::parseInt).toList();
            List<List<Integer>> processed = new ArrayList<>();
            processed.add(nums);

            while (true) {
                List<Integer> last = processed.get(processed.size() - 1);
                int lastNum = last.get(last.size() - 1);
                if (lastNum == 0) break;

                List<Integer> next = new ArrayList<>();
                for (int i = 1; i < last.size(); i++) {
                    next.add(last.get(i) - last.get(i - 1));
                }
                processed.add(next);
            }

            Collections.reverse(processed);
            long inferred = 0;
            for (List<Integer> l: processed.subList(1, processed.size())) {
                int firstNum = l.get(0);
                inferred = firstNum - inferred;
            }

            extrapolatedValueSum += inferred;
        }

        return String.valueOf(extrapolatedValueSum);
    }
}
