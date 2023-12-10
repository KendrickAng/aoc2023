package org.example.four;

import com.google.common.collect.Sets;
import org.example.Solver;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Four implements Solver {
    public static final Four SINGLETON = new Four();

    private static final Pattern CARD_NUMBERS_REGEX = Pattern.compile("^Card\\s+(?<id>\\d+):\\s+(?<rest>.*)$");

    private List<String> lines;

    @Override
    public String partOneSample(List<String> lines) {
        this.lines = lines;
        return String.valueOf(solve1());
    }

    @Override
    public String partOneInput(List<String> lines) {
        this.lines = lines;
        return String.valueOf(solve1());
    }

    @Override
    public String partTwoSample(List<String> lines) {
        this.lines = lines;
        return String.valueOf(solve2());
    }

    @Override
    public String partTwoInput(List<String> lines) {
        this.lines = lines;
        return String.valueOf(solve2());
    }

    private long solve1() {
        long answer = 0;
        for (String line: lines) {
            Matcher matcher = CARD_NUMBERS_REGEX.matcher(line);
            if (!matcher.find()) throw new AssertionError();
            String rest = matcher.group("rest").strip();

            Set<Integer> winningNumbers = Set.copyOf(
                    Arrays.stream(rest.split("\\s+\\|\\s+")[0]
                                    .strip()
                                    .split("\\s+"))
                            .map(Integer::parseInt)
                            .collect(Collectors.toList()));
            Set<Integer> myNumbers = Set.copyOf(
                    Arrays.stream(rest.split("\\s+\\|\\s+")[1]
                                    .strip()
                                    .split("\\s+"))
                            .map(Integer::parseInt)
                            .collect(Collectors.toList()));
            Sets.SetView<Integer> myWinningNumbers = Sets.intersection(winningNumbers, myNumbers);

            int numWinners = myWinningNumbers.size();

            answer += Math.pow(2, numWinners - 1);
        }
        return answer;
    }

    private long solve2() {
        int[] numCards = new int[lines.size() + 10];

        for (String line: lines) {
            Matcher matcher = CARD_NUMBERS_REGEX.matcher(line);
            if (!matcher.find()) throw new AssertionError();

            int id = Integer.parseInt(matcher.group("id"));
            numCards[id]++;

            String rest = matcher.group("rest").strip();
            Set<Integer> winningNumbers = Set.copyOf(
                    Arrays.stream(rest.split("\\s+\\|\\s+")[0]
                                    .strip()
                                    .split("\\s+"))
                            .map(Integer::parseInt)
                            .collect(Collectors.toList()));
            Set<Integer> myNumbers = Set.copyOf(
                    Arrays.stream(rest.split("\\s+\\|\\s+")[1]
                                    .strip()
                                    .split("\\s+"))
                            .map(Integer::parseInt)
                            .collect(Collectors.toList()));
            Sets.SetView<Integer> myWinningNumbers = Sets.intersection(winningNumbers, myNumbers);

            int numWinners = myWinningNumbers.size();
            int cardMultiplier = numCards[id];
            for (int offset = 1; offset <= numWinners; offset++) {
                numCards[id + offset] += cardMultiplier;
            }
        }
        return Arrays.stream(numCards).sum();
    }
}
