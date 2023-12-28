package org.example.eight;

import com.google.common.math.LongMath;
import org.example.Solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Eight implements Solver {
    public static final Eight SINGLETON = new Eight();

    private static final Pattern PATTERN = Pattern
            .compile("(?<source>[A-Z0-9]+) = \\((?<left>[A-Z0-9]+), (?<right>[A-Z0-9]+)\\)");

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
        String instructions = lines.get(0);

        Map<String, String[]> adjList = new HashMap<>();
        for (String line : lines.subList(2, lines.size())) {
            Matcher m = PATTERN.matcher(line);
            if (!m.matches()) throw new AssertionError();
            String source = m.group("source");
            String left = m.group("left");
            String right = m.group("right");

            adjList.putIfAbsent(source, new String[2]);
            adjList.get(source)[0] = left;
            adjList.get(source)[1] = right;
        }

        long steps = 0;
        String currentElement = "AAA";
        int currentStepIdx = 0;

        while (!currentElement.equals("ZZZ")) {
            char instruction = instructions.charAt(currentStepIdx++ % instructions.length());
            if (instruction == 'L') {
                currentElement = adjList.get(currentElement)[0];
            } else if (instruction == 'R') {
                currentElement = adjList.get(currentElement)[1];
            } else {
                throw new AssertionError();
            }
            steps++;
        }

        return String.valueOf(steps);
    }

    private String solve2() {
        String instructions = lines.get(0);

        List<String> sources = new ArrayList<>();
        Map<String, String[]> adjList = new HashMap<>();
        for (String line : lines.subList(2, lines.size())) {
            Matcher m = PATTERN.matcher(line);
            if (!m.matches()) throw new AssertionError();
            String source = m.group("source");
            String left = m.group("left");
            String right = m.group("right");

            adjList.putIfAbsent(source, new String[2]);
            adjList.get(source)[0] = left;
            adjList.get(source)[1] = right;

            if (source.charAt(2) == 'A') sources.add(source);
        }

        if (sources.size() <= 1) throw new AssertionError();

        List<Long> stepsToSolve = new ArrayList<>();
        for (String source : sources) {
            long steps = 0;
            String currentElement = source;
            int currentStepIdx = 0;

            while (!(currentElement.charAt(2) == 'Z')) {
                char instruction = instructions.charAt(currentStepIdx++ % instructions.length());
                if (instruction == 'L') {
                    currentElement = adjList.get(currentElement)[0];
                } else if (instruction == 'R') {
                    currentElement = adjList.get(currentElement)[1];
                } else {
                    throw new AssertionError();
                }
                steps++;
            }

            stepsToSolve.add(steps);
        }

        long ans = lcm(stepsToSolve);

        return String.valueOf(ans);
    }

    private long lcm(List<Long> numbers) {
        if (numbers.size() < 2) throw new AssertionError();
        long ans = lcm(numbers.get(0), numbers.get(1));
        for (int i = 2; i < numbers.size(); i++) {
            ans = lcm(ans, numbers.get(i));
        }
        return ans;
    }

    private long lcm(long a, long b) {
        return a * b / LongMath.gcd(a, b);
    }
}
