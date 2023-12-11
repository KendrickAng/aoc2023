package org.example.five;

import org.apache.commons.lang3.tuple.Pair;
import org.example.Solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Five implements Solver {
    private static class Range {
        public long sourceStart;
        public long destStart;
        public long width;

        public Range(long destStart, long sourceStart, long width) {
            this.sourceStart = sourceStart;
            this.destStart = destStart;
            this.width = width;
        }

        @Override
        public String toString() {
            return String.format("Range{sourceStart=%d, destStart=%d, width=%d}", sourceStart, destStart, width);
        }
    }

    public static final Five SINGLETON = new Five();

    private List<String> lines;

    @Override
    public String partOneSample(List<String> lines) {
        setLines(lines);
        return String.valueOf(solve1());
    }

    @Override
    public String partOneInput(List<String> lines) {
        setLines(lines);
        return String.valueOf(solve1());
    }

    @Override
    public String partTwoSample(List<String> lines) {
        setLines(lines);
        return String.valueOf(solve2());
    }

    @Override
    public String partTwoInput(List<String> lines) {
        setLines(lines);
        return String.valueOf(solve2());
    }

    private long solve1() {
        if (lines.size() <= 2) throw new AssertionError();

        List<Long> seeds = Arrays.stream(lines.get(0).split(":")[1].strip().split("\\s+"))
                .map(Long::parseLong)
                .toList();

        List<String> massagedLines = lines.subList(2, lines.size());
        if (!massagedLines.get(massagedLines.size()-1).isBlank()) {
            massagedLines.add("");
        }
        List<List<Range>> maps = parseMaps(massagedLines);

        long answer = Long.MAX_VALUE;

        for (long seed: seeds) {
            answer = Math.min(answer, getLowestLocation(seed, maps));
        }

        return answer;
    }

    private long solve2() {
        if (lines.size() <= 2) throw new AssertionError();

        List<Long> seeds = Arrays.stream(lines.get(0).split(":")[1].strip().split("\\s+"))
                .map(Long::parseLong)
                .toList();
        if (seeds.size() % 2 != 0) throw new AssertionError();

        List<String> massagedLines = lines.subList(2, lines.size());
        if (!massagedLines.get(massagedLines.size()-1).isBlank()) {
            massagedLines.add("");
        }
        List<List<Range>> maps = parseMaps(massagedLines);

        long answer = Long.MAX_VALUE;

        for (int i = 0; i < seeds.size(); i += 2) {
            long start = seeds.get(i);
            long range = seeds.get(i+1);
            for (long seed = start; seed < start + range; seed++) {
                answer = Math.min(answer, getLowestLocation(seed, maps));
            }
        }

        return answer;
    }

    private List<List<Range>> parseMaps(List<String> lines) {
        List<List<Range>> ret = new ArrayList<>();
        List<Range> map = new ArrayList<>();
        for (String line : lines) {
            if (line.isBlank()) {
                ret.add(List.copyOf(map));
                map = new ArrayList<>();
            }
            if (!line.isBlank() && Character.isDigit(line.strip().charAt(0))) {
                String[] parts = line.strip().split("\\s+");
                map.add(new Range(Long.parseLong(parts[0]),
                        Long.parseLong(parts[1]),
                        Long.parseLong(parts[2])));
            }
        }
        if (ret.size() != 7) throw new AssertionError();
        return ret;
    }

    private long getLowestLocation(long seed, List<List<Range>> maps) {
        long ans = seed;
        for (List<Range> map: maps) {
            for (Range range: map) {
                if (ans >= range.sourceStart && ans < range.sourceStart + range.width) {
                    ans = ans + (range.destStart - range.sourceStart);
                    break;
                }
            }
        }
        return ans;
    }

    private void setLines(List<String> lines) {
        this.lines = lines;
    }
}
