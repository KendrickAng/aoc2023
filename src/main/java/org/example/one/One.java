package org.example.one;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import org.example.Solver;

public class One implements Solver {
    public static final One SINGLETON = new One();


    private List<String> lines;
    private int part;

    private One() {
        lines = List.of();
        part = 1;
    }

    public String partOneSample(List<String> lines) {
        this.setPart(1);
        this.setLines(lines);

        return this.solve();
    }

    public String partOneInput(List<String> lines) {
        this.setPart(1);
        this.setLines(lines);

        return this.solve();
    }

    public String partTwoSample(List<String> lines) {
        this.setPart(2);
        this.setLines(lines);

        return this.solve();
    }

    public String partTwoInput(List<String> lines) {
        this.setPart(2);
        this.setLines(lines);

        return this.solve();
    }


    private void setLines(List<String> lines) {
        this.lines = lines;
    }

    private void setPart(int part) {
        this.part = part;
    }

    private String solve() {
        long ans = lines.stream().filter(x -> !x.isBlank())
                .map(this::getCalibrationValue).mapToLong(Long::longValue).sum();

        return String.valueOf(ans);
    }

    private long getCalibrationValue(String line) {
        Function<String, Integer> findFirstDigit =
                part == 1 ? One::findFirstDigitP1 : One::findFirstDigitP2;
        int firstDigit = findFirstDigit.apply(line);

        Function<String, Integer> findLastDigit =
                part == 1 ? One::findLastDigitP1 : One::findLastDigitP2;
        int lastDigit = findLastDigit.apply(line);

        if (firstDigit == -1 || lastDigit == -1) {
            throw new RuntimeException("Could not find digits in line: " + line);
        }

        return Long.parseLong(String.valueOf(firstDigit) + lastDigit);
    }

    private static int findFirstDigitP1(String line) {
        return line.chars().filter(Character::isDigit).findFirst().orElseThrow() - '0';
    }

    private static int findLastDigitP1(String line) {
        String reversed = new StringBuilder(line).reverse().toString();
        return reversed.chars().filter(Character::isDigit).findFirst().orElseThrow() - '0';
    }

    private static int findFirstDigitP2(String line) {
        int i = Stream.of(
                line.indexOf("0"),
                line.indexOf("1"),
                line.indexOf("2"),
                line.indexOf("3"),
                line.indexOf("4"),
                line.indexOf("5"),
                line.indexOf("6"),
                line.indexOf("7"),
                line.indexOf("8"),
                line.indexOf("9"),
                line.indexOf("one"),
                line.indexOf("two"),
                line.indexOf("three"),
                line.indexOf("four"),
                line.indexOf("five"),
                line.indexOf("six"),
                line.indexOf("seven"),
                line.indexOf("eight"),
                line.indexOf("nine")
        ).filter(x -> x >= 0).min(Integer::compareTo).orElse(-1);

        if (i == -1) return -1;

        return resolve(line, i);
    }

    private static int findLastDigitP2(String line) {
        int i = Stream.of(
                line.lastIndexOf("0"),
                line.lastIndexOf("1"),
                line.lastIndexOf("2"),
                line.lastIndexOf("3"),
                line.lastIndexOf("4"),
                line.lastIndexOf("5"),
                line.lastIndexOf("6"),
                line.lastIndexOf("7"),
                line.lastIndexOf("8"),
                line.lastIndexOf("9"),
                line.lastIndexOf("one"),
                line.lastIndexOf("two"),
                line.lastIndexOf("three"),
                line.lastIndexOf("four"),
                line.lastIndexOf("five"),
                line.lastIndexOf("six"),
                line.lastIndexOf("seven"),
                line.lastIndexOf("eight"),
                line.lastIndexOf("nine")
        ).filter(x -> x < line.length()).max(Integer::compareTo).orElse(-1);

        if (i == -1) return -1;

        return resolve(line, i);
    }

    private static int resolve(String line, int i) {
        if (Character.isDigit(line.charAt(i))) {
            return line.charAt(i) - '0';
        }

        String word = line.substring(i);
        if (word.startsWith("one")) return 1;
        if (word.startsWith("two")) return 2;
        if (word.startsWith("three")) return 3;
        if (word.startsWith("four")) return 4;
        if (word.startsWith("five")) return 5;
        if (word.startsWith("six")) return 6;
        if (word.startsWith("seven")) return 7;
        if (word.startsWith("eight")) return 8;
        if (word.startsWith("nine")) return 9;

        return 0;
    }
}
