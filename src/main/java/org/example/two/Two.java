package org.example.two;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.example.Solver;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

public class Two implements Solver {
    public static final Two SINGLETON = new Two();

    private static final Pattern GAME_ID_REGEX = Pattern.compile("Game (?<id>\\d+)");

    private static final int REDS = 12;
    private static final int GREENS = 13;
    private static final int BLUES = 14;

    private List<String> lines;

    private Two() {
    }

    public String partOneSample(List<String> input) {
        setLines(input);
        return String.valueOf(solve1());
    }

    public String partOneInput(List<String> input) {
        setLines(input);
        return String.valueOf(solve1());
    }

    public String partTwoSample(List<String> input) {
        setLines(input);
        return String.valueOf(solve2());
    }

    public String partTwoInput(List<String> input) {
        setLines(input);
        return String.valueOf(solve2());
    }

    private long solve1() {
        long answer = 0;
        for (String line : lines) {
            Pair<String, String> gameIdAndGames = Arrays.stream(line.split(":")).collect(
                    /* supplier= */MutablePair::new,
                    /* accumulator= */(pair, str) -> {
                        if (pair.getLeft() == null) {
                            pair.setLeft(str);
                        } else {
                            pair.setRight(str);
                        }
                    },
                    /* combiner= */ (pair1, pair2) -> {
                        List<String> stuff = Stream.of(pair1.getLeft(), pair1.getRight(), pair2.getLeft(), pair2.getRight()).filter(x -> x != null).toList();
                        if (stuff.size() != 2) throw new AssertionError();
                        pair1.setLeft(stuff.get(0));
                        pair1.setRight(stuff.get(1));
                    });

            String gameId = gameIdAndGames.getLeft();
            String games = gameIdAndGames.getRight();
            Matcher matcher = GAME_ID_REGEX.matcher(gameId);
            if (!matcher.matches()) throw new AssertionError();
            long groupId = Long.parseLong(matcher.group("id"));

            long failedGames = Arrays.stream(games.split(";"))
                    .map(String::strip)
                    .map(game -> {
                        // Can a single sub-game fail?
                        boolean canFail = Arrays.stream(game.split(",")).map(String::strip)
                                .map(numberAndColor -> {
                                    String[] split = numberAndColor.split(" ");
                                    if (split.length != 2) throw new AssertionError();
                                    return new ImmutablePair<>(Integer.parseInt(split[0].strip()), split[1].strip());
                                })
                                .map(pair -> {
                                    int number = pair.getLeft();
                                    String color = pair.getRight().strip();
                                    if (color.equals("red") && pair.getLeft() > REDS) {
                                        return false;
                                    }
                                    if (color.equals("green") && pair.getLeft() > GREENS) {
                                        return false;
                                    }
                                    if (color.equals("blue") && pair.getLeft() > BLUES) {
                                        return false;
                                    }
                                    return true;
                                })
                                .anyMatch(x -> !x);
                        return canFail;
                    })
                    // Can any sub-game in a game fail?
                    .filter(x -> x)
                    .count();

            if (failedGames == 0) {
                // If a game is possible, sum the ID of that game.
                answer += groupId;
            }
        }

        return answer;
    }

    private long solve2() {
        long answer = 0;

        for (String line : lines) {
           line = line.split(":")[1].strip();

            int[] fewestCubesOfEachColor = Arrays.stream(line.split(";"))
                    .flatMap(x -> Arrays.stream(x.split(",")))
                    .map(String::strip)
                    .map(numberAndColor -> {
                        String[] split = numberAndColor.split(" ");
                        if (split.length != 2) throw new AssertionError();
                        int left = Integer.parseInt(split[0].strip());
                        String right = split[1].strip();

                        int[] numbers = new int[]{0, 0, 0};
                        if (right.equals("red")) {
                            numbers[0] = left;
                        } else if (right.equals("green")) {
                            numbers[1] = left;
                        } else if (right.equals("blue")) {
                            numbers[2] = left;
                        } else {
                            throw new AssertionError();
                        }
                        return numbers;
                    })
                    .collect(
                            /* supplier= */() -> new int[3],
                            /* accumulator= */(arr, numbers) -> {
                                arr[0] = Math.max(arr[0], numbers[0]);
                                arr[1] = Math.max(arr[1], numbers[1]);
                                arr[2] = Math.max(arr[2], numbers[2]);
                            },
                            /* combiner= */(arr1, arr2) -> {
                                arr1[0] = Math.max(arr1[0], arr2[0]);
                                arr1[1] = Math.max(arr1[1], arr2[1]);
                                arr1[2] = Math.max(arr1[2], arr2[2]);
                            }
                    );

            answer += (long) fewestCubesOfEachColor[0] *
                    fewestCubesOfEachColor[1] *
                    fewestCubesOfEachColor[2];
        }

        return answer;
    }

    private void setLines(List<String> lines) {
        this.lines = List.copyOf(lines);
    }

    private String addSemiColonToBack(String line) {
        return line.endsWith(";") ? line : line + ";";
    }
}
