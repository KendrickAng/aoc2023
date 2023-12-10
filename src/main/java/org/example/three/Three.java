package org.example.three;

import com.google.common.base.Strings;
import org.example.Solver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Three implements Solver {
    public static final Three SINGLETON = new Three();

    private List<String> lines;
    private int n;
    private int oldN;

    @Override
    public String partOneSample(List<String> input) {
        setLines(input);
        return String.valueOf(solve1());
    }

    @Override
    public String partOneInput(List<String> input) {
        setLines(input);
        return String.valueOf(solve1());
    }

    @Override
    public String partTwoSample(List<String> input) {
        setLines(input);
        return String.valueOf(solve2());
    }

    @Override
    public String partTwoInput(List<String> input) {
        setLines(input);
        return String.valueOf(solve2());
    }

    private long solve1() {
        int rows = lines.size();
        int cols = lines.get(0).length();

        List<List<Boolean>> isParsed = IntStream.range(0, rows)
                .mapToObj(x -> Stream.generate(() -> false).limit(cols).collect(Collectors.toCollection(ArrayList::new)))
                .collect(Collectors.toCollection(ArrayList::new));

        List<List<String>> numberAt = IntStream.range(0, rows)
                .mapToObj(x -> Stream.generate(() -> "").limit(cols).collect(Collectors.toCollection(ArrayList::new)))
                .collect(Collectors.toCollection(ArrayList::new));

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char ch = lines.get(r).charAt(c);
                if (Character.isDigit(ch)) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = c; i < cols; i++) {
                        isParsed.get(r).set(c, true);
                        char ch2 = lines.get(r).charAt(i);
                        if (!Character.isDigit(ch2)) {
                            break;
                        }
                        sb.append(ch2);
                    }
                    numberAt.get(r).set(c, sb.toString());
                    c += sb.length();
                }
            }
        }

        List<List<Boolean>> isSelected = IntStream.range(0, rows)
                .mapToObj(x -> Stream.generate(() -> false).limit(cols).collect(Collectors.toCollection(ArrayList::new)))
                .collect(Collectors.toCollection(ArrayList::new));

        for (int r = 1; r < rows - 1; r++) {
            for (int c = 1; c < cols - 1; c++) {
                char ch = lines.get(r).charAt(c);

                if (isSymbol(ch)) {
                    int[] rOffset = {-1, -1, -1, 0, 1, 1, 1, 0};
                    int[] cOffset = {-1, 0, 1, 1, 1, 0, -1, -1};
                    for (int i = 0; i < rOffset.length; i++) {
                        int r2 = r + rOffset[i];
                        int c2 = c + cOffset[i];
                        floodFillDigit(r2, c2, isSelected);
                    }
                }
            }
        }

        long answer = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (isSelected.get(r).get(c) && !Strings.isNullOrEmpty(numberAt.get(r).get(c))) {
                    answer += Long.parseLong(numberAt.get(r).get(c));
                }
            }
        }

        return answer;
    }

    private long solve2() {
        int rows = lines.size();
        int cols = lines.get(0).length();

        List<List<Boolean>> isParsed = IntStream.range(0, rows)
                .mapToObj(x -> Stream.generate(() -> false).limit(cols).collect(Collectors.toCollection(ArrayList::new)))
                .collect(Collectors.toCollection(ArrayList::new));

        List<List<String>> numberAt = IntStream.range(0, rows)
                .mapToObj(x -> Stream.generate(() -> "").limit(cols).collect(Collectors.toCollection(ArrayList::new)))
                .collect(Collectors.toCollection(ArrayList::new));

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char ch = lines.get(r).charAt(c);
                if (Character.isDigit(ch)) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = c; i < cols; i++) {
                        isParsed.get(r).set(c, true);
                        char ch2 = lines.get(r).charAt(i);
                        if (!Character.isDigit(ch2)) {
                            for(int j = c; j < i; j++) {
                                numberAt.get(r).set(j, sb.toString());
                            }
                            break;
                        }
                        sb.append(ch2);
                    }
                    c += sb.length();
                }
            }
        }

        List<List<Boolean>> isSelected = IntStream.range(0, rows)
                .mapToObj(x -> Stream.generate(() -> false).limit(cols).collect(Collectors.toCollection(ArrayList::new)))
                .collect(Collectors.toCollection(ArrayList::new));

        for (int r = 1; r < rows - 1; r++) {
            for (int c = 1; c < cols - 1; c++) {
                char ch = lines.get(r).charAt(c);

                if (isSymbol(ch)) {
                    int[] rOffset = {-1, -1, -1, 0, 1, 1, 1, 0};
                    int[] cOffset = {-1, 0, 1, 1, 1, 0, -1, -1};
                    for (int i = 0; i < rOffset.length; i++) {
                        int r2 = r + rOffset[i];
                        int c2 = c + cOffset[i];
                        floodFillDigit(r2, c2, isSelected);
                    }
                }
            }
        }

        long answer = 0;
        for (int r = 1; r < rows-1; r++) {
            for (int c = 1; c < cols-1; c++) {
                if (lines.get(r).charAt(c) == '*') {
                    int[] rOffset = {-1, -1, -1, 0, 1, 1, 1, 0};
                    int[] cOffset = {-1, 0, 1, 1, 1, 0, -1, -1};
                    List<Long> numbers = new ArrayList<>();
                    Set<Long> seen = new HashSet<>();
                    for (int i = 0; i < rOffset.length; i++) {
                        int r2 = r + rOffset[i];
                        int c2 = c + cOffset[i];
                        if (!Strings.isNullOrEmpty(numberAt.get(r2).get(c2))) {
                            long num = Long.parseLong(numberAt.get(r2).get(c2));
                            if (!seen.contains(num)) {
                                numbers.add(num);
                                seen.add(num);
                            }
                        }
                    }
                    if (numbers.size() == 2) {
                        long add = numbers.get(0) * numbers.get(1);
                        answer += add;
                    }
                }
            }
        }

        return answer;
    }
    private void floodFillDigit(int r, int c, List<List<Boolean>> board) {
        boolean inBounds = r >= 0 && r < board.size() && c >= 0 && c < board.get(0).size();
        if (!inBounds) return;

        boolean traversed = board.get(r).get(c);

        if (!traversed && Character.isDigit(lines.get(r).charAt(c))) {
            board.get(r).set(c, true);

            int[] rOffset = {-1, -1, -1, 0, 1, 1, 1, 0};
            int[] cOffset = {-1, 0, 1, 1, 1, 0, -1, -1};
            for (int i = 0; i < rOffset.length; i++) {
                int r2 = r + rOffset[i];
                int c2 = c + cOffset[i];
                floodFillDigit(r2, c2, board);
            }
        }
    }

    private void setLines(List<String> lines) {
        if (lines.size() == 0) {
            throw new IllegalArgumentException("Lines must not be empty");
        }

        int newColumnLen = lines.get(0).length() + 2;
        List<String> newLines = new ArrayList<>(lines.stream().map(x -> "." + x + ".").toList());
        newLines.add(0, ".".repeat(newColumnLen));
        newLines.add(".".repeat(newColumnLen));

        this.lines = newLines;
    }

    private boolean isSymbol(char ch) {
        return !Character.isDigit(ch) && ch != '.';
    }
}
