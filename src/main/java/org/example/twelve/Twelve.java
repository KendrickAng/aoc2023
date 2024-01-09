package org.example.twelve;

import org.example.Solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Twelve implements Solver {
    public static final Twelve SINGLETON = new Twelve();

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
        long sum = 0;
        for (String line : lines) {
            String[] tokens = line.split(" ");

            String withSentinel = String.join("?", List.of(tokens[0], tokens[0], tokens[0], tokens[0], tokens[0])) + ".";
            char[] springs = withSentinel.toCharArray();
            int[] groups = Arrays.stream(String.join(",", List.of(tokens[1], tokens[1], tokens[1], tokens[1], tokens[1]))
                            .split(","))
                    .mapToInt(Integer::parseInt)
                    .toArray();

            long[][][] memo = new long[springs.length + 5][groups.length + 5][springs.length + 5];
            for (int i = 0; i < memo.length; i++) {
                for (int j = 0; j < memo[0].length; j++) {
                    for (int k = 0; k < memo[0][0].length; k++) {
                        memo[i][j][k] = -1;
                    }
                }
            }

            long arrangements = dp(springs, 0, groups, 0, 0, memo);
//            System.out.printf("%s %s %s\n", Arrays.toString(springs), Arrays.toString(groups), arrangements);

            sum += arrangements;
        }
        return String.valueOf(sum);
    }

    private long dp(char[] springs, int sIdx, int[] groups, int gIdx, int contiguousBroken, long[][][] memo) {
        if (sIdx == springs.length) {
            if (gIdx == groups.length && contiguousBroken == 0) {
                return 1;
            } else {
                return 0;
            }
        } else if (gIdx >= groups.length) {
            if (String.valueOf(Arrays.copyOfRange(springs, sIdx, springs.length)).contains("#")) {
                return 0;
            } else {
                return 1;
            }
        }

        if (memo[sIdx][gIdx][contiguousBroken] == -1) {
            char c = springs[sIdx];
            long ans = -1;

            if (c == '.') {
                if (contiguousBroken == 0) {
                    ans = dp(springs, sIdx + 1, groups, gIdx, 0, memo);
                } else if (contiguousBroken == groups[gIdx]) {
                    ans = dp(springs, sIdx + 1, groups, gIdx + 1, 0, memo);
                } else {
                    ans = 0;
                }
            } else if (c == '#') {
                ans = dp(springs, sIdx + 1, groups, gIdx, contiguousBroken + 1, memo);
            } else if (c == '?') {
                long unbroken = -1;
                if (contiguousBroken == 0) {
                    unbroken = dp(springs, sIdx + 1, groups, gIdx, 0, memo);
                } else if (contiguousBroken == groups[gIdx]) {
                    unbroken = dp(springs, sIdx + 1, groups, gIdx + 1, 0, memo);
                } else {
                    unbroken = 0;
                }

                long broken = dp(springs, sIdx + 1, groups, gIdx, contiguousBroken + 1, memo);

                ans = unbroken + broken;
            }

            memo[sIdx][gIdx][contiguousBroken] = ans;
        }

        return memo[sIdx][gIdx][contiguousBroken];
    }

    private String solve1() {
        long sum = 0;
        for (String line : lines) {
            String[] tokens = line.split(" ");
            String springs = tokens[0];
            String groups = tokens[1];

            int expectedBroken = Arrays.stream(groups.split(",")).mapToInt(Integer::parseInt).sum();
            List<String> candidates = new ArrayList<>();
            generate(springs, 0, new StringBuilder(), candidates, 0, expectedBroken);

            for (String candidate : candidates) {
                int[] parsedGroups = Arrays.stream(groups.trim().split(",")).mapToInt(Integer::parseInt).toArray();
                if (isCorrect(candidate, parsedGroups)) {
                    sum++;
                }
            }
        }
        return String.valueOf(sum);
    }

    private boolean isCorrect(String candidate, int[] groups) {
        candidate = candidate + "."; // sentinel value
        List<Integer> found = new ArrayList<>();

        int numContiguous = 0;
        for (char c : candidate.toCharArray()) {
            if (c == '.' && numContiguous > 0) {
                found.add(numContiguous);
                numContiguous = 0;
            } else if (c == '#') {
                numContiguous++;
            }
        }

        int[] foundArr = found.stream().mapToInt(Integer::intValue).toArray();
        if (Arrays.equals(foundArr, groups)) {
            return true;
        }

        return false;
    }

    private void generate(String s, int idx, StringBuilder sb, List<String> candidates, int broken, int expectedBroken) {
        if (idx == s.length()) {
            if (broken == expectedBroken) candidates.add(sb.toString());
            return;
        }
        if (s.charAt(idx) == '?') {
            StringBuilder sb2 = sb.append('.');
            generate(s, idx + 1, sb2, candidates, broken, expectedBroken);
            sb2.deleteCharAt(sb2.length() - 1);

            StringBuilder sb3 = sb.append('#');
            generate(s, idx + 1, sb3, candidates, broken + 1, expectedBroken);
            sb3.deleteCharAt(sb3.length() - 1);
        } else {
            char c = s.charAt(idx);
            StringBuilder sb2 = sb.append(c);
            generate(s, idx + 1, sb2, candidates, c == '#' ? broken + 1 : broken, expectedBroken);
            sb2.deleteCharAt(sb2.length() - 1);
        }
    }
}
