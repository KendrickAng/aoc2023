package org.example.seven;

import org.example.Solver;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Seven implements Solver {
    public static final Seven SINGLETON = new Seven();

    private List<String> lines;

    private class Hand implements Comparable<Hand> {
        private static final Map<Character, Integer> STRENGTHS1 = Map.ofEntries(
                Map.entry('2', 2),
                Map.entry('3', 3),
                Map.entry('4', 4),
                Map.entry('5', 5),
                Map.entry('6', 6),
                Map.entry('7', 7),
                Map.entry('8', 8),
                Map.entry('9', 9),
                Map.entry('T', 10),
                Map.entry('J', 11),
                Map.entry('Q', 12),
                Map.entry('K', 13),
                Map.entry('A', 14)
        );

        private static final Map<Character, Integer> STRENGTHS2 = Map.ofEntries(
                Map.entry('J', 2),
                Map.entry('2', 3),
                Map.entry('3', 4),
                Map.entry('4', 5),
                Map.entry('5', 6),
                Map.entry('6', 7),
                Map.entry('7', 8),
                Map.entry('8', 9),
                Map.entry('9', 10),
                Map.entry('T', 11),
                Map.entry('Q', 12),
                Map.entry('K', 13),
                Map.entry('A', 14)
        );

        private final String cards;

        public Hand(String cards) {
            this.cards = cards;
        }

        @Override
        public int compareTo(Hand o) {
            if (this.score2() == o.score2()) {
                for (int i = 0; i < cards.length(); i++) {
                    char myCard = cards.charAt(i);
                    char otherCard = o.cards.charAt(i);
                    if (!Objects.equals(STRENGTHS2.get(myCard), STRENGTHS2.get(otherCard))) {
                        return STRENGTHS2.get(myCard) - STRENGTHS2.get(otherCard);
                    }
                }
            }
            return this.score2() - o.score2();
        }

        public int score1() {
            Map<Character, Long> m = cards.chars().mapToObj(x -> (char) x)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            if (m.size() == 1) {
                return 7; // five of a kind
            } else if (m.size() == 2 && new HashSet<>(m.values()).contains(4L)) {
                return 6; // four of a kind
            } else if (m.size() == 2 && new HashSet<>(m.values()).contains(3L)) {
                return 5; // full house
            } else if (m.size() == 3 && new HashSet<>(m.values()).contains(3L)) {
                return 4; // three of a kind
            } else if (m.size() == 3 && new HashSet<>(m.values()).contains(2L)) {
                return 3; // two pair
            } else if (m.size() == 4) {
                return 2; // one pair
            } else {
                return 1; // high card
            }
        }

        public int score2() {
            Map<Character, Long> m = cards.chars().mapToObj(x -> (char) x)
                    .filter(x -> x != 'J')
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            long js = cards.chars().mapToObj(x -> (char) x).filter(x -> x == 'J').count();

            int fiveOfAKind = 7;
            int fourOfAKind = 6;
            int fullHouse = 5;
            int threeOfAKind = 4;
            int twoPair = 3;
            int onePair = 2;
            int highCard = 1;

            if (m.size() <= 1) {
                return fiveOfAKind; // always five of a kind, regardless of number of Js
            } else if (m.size() == 2 && js == 3) { // JJJAB
                return fourOfAKind;
            } else if (m.size() == 2 && js == 2) { // JJAAB
                return fourOfAKind;
            } else if (m.size() == 2 && js == 1) { // JAABB, JAAAB
                long max = m.values().stream().max(Long::compareTo).orElseThrow();
                long min = m.values().stream().min(Long::compareTo).orElseThrow();
                if (max == min) {
                    return fullHouse;
                } else {
                    return fourOfAKind;
                }
            } else if (m.size() == 2 && js == 0) { // AAAAB, AAABB
                long max = m.values().stream().max(Long::compareTo).orElseThrow();
                long min = m.values().stream().min(Long::compareTo).orElseThrow();
                if (max == 4 && min == 1) {
                    return fourOfAKind;
                } else if (max == 3 && min == 2) {
                    return fullHouse;
                } else {
                    throw new AssertionError();
                }
            } else if (m.size() == 3 && js == 2) { // ABCJJ
                return threeOfAKind;
            } else if (m.size() == 3 && js == 1) { // AABCJ
                return threeOfAKind;
            } else if (m.size() == 3 && js == 0) { // AAABC, AABBC
                long max = m.values().stream().max(Long::compareTo).orElseThrow();
                if (max == 3) {
                    return threeOfAKind;
                } else if (max == 2) {
                    return twoPair;
                } else {
                    throw new AssertionError();
                }
            } else if (m.size() == 4) { // ABCDJ, AABCD
                return onePair;
            } else if (m.size() == 5) {
                return highCard;
            } else {
                throw new AssertionError();
            }
        }
    }

    @Override
    public String partOneSample(List<String> lines) {
//        this.lines = List.copyOf(lines);
//        return solve1();
        return null;
    }

    @Override
    public String partOneInput(List<String> lines) {
//        this.lines = List.copyOf(lines);
//        return solve1();
        return null;
    }

    @Override
    public String partTwoSample(List<String> lines) {
        this.lines = List.copyOf(lines);
        return solve1();
    }

    @Override
    public String partTwoInput(List<String> lines) {
        this.lines = List.copyOf(lines);
        return solve1();
    }

    private String solve1() {
        TreeMap<Hand, Long> hands = new TreeMap<>();
        for (String line : lines) {
            String hand = line.split(" ")[0];
            String score = line.split(" ")[1];
            hands.put(new Hand(hand), Long.parseLong(score));
        }

        long answer = 0;
        long multiplier = 1;
        for (Map.Entry<Hand, Long> e : hands.entrySet()) {
            answer += e.getValue() * multiplier++;
        }

        return String.valueOf(answer);
    }
}
