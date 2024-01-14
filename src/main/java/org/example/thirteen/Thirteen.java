package org.example.thirteen;

import org.example.Solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Thirteen implements Solver {
    public static final Thirteen SINGLETON = new Thirteen();

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
        List<String> lines = new ArrayList<>(this.lines);
        if (!lines.get(lines.size()-1).isEmpty()) lines.add(""); // sentinel

        long ans = 0;
        List<String> mirror = new ArrayList<>();
        for (String line : lines) {
            if (!line.isEmpty()) {
                mirror.add(line);
            } else {
                ans += calculateScore2(mirror);
                mirror.clear();
            }
        }
        return String.valueOf(ans);
    }

    private long calculateScore2(List<String> lines) {
        for (int i = 1; i < lines.size(); i++) {
            // if odd number of rows, skip
            if ((i+1) % 2 != 0) continue;
            if (verify2(lines, 0, i)) {
                long score = (i + 1) / 2L * 100L;
//                System.out.printf("Row 0 to %d: %d\n", i, score);
                return score;
            }
        }

        for (int i = lines.size()-2; i >= 0; i--) {
            // if odd number of rows, skip
            if ((lines.size() - i) % 2 != 0) continue;
            if (verify2(lines, i, lines.size()-1)) {
                long score = (i + lines.size()) / 2L * 100L;
//                System.out.printf("Row %d to %d: %d\n", i, lines.size()-1, score);
                return score;
            }
        }

        List<String> mirrorT = transpose(lines);

        for (int i = 1; i < mirrorT.size(); i++) {
            // if odd number of cols, skip
            if ((i+1) % 2 != 0) continue;
            if (verify2(mirrorT, 0, i)) {
                long score = (i + 1) / 2L;
//                System.out.printf("Col 0 to %d: %d\n", i, score);
                return score;
            }
        }

        for (int i = mirrorT.size()-2; i >= 0; i--) {
            // if odd number of cols, skip
            if ((mirrorT.size() - i) % 2 != 0) continue;
            if (verify2(mirrorT, i, mirrorT.size()-1)) {
                long score = (i + mirrorT.size()) / 2L;
//                System.out.printf("Col %d to %d: %d\n", i, mirrorT.size()-1, score);
                return score;
            }
        }

        return 0;
    }

    private boolean verify2(List<String> lines, int start, int end) {
        int smudges = 0;
        for (int i = 0; i <= (end - start) / 2; i++) {
            String a = lines.get(start + i);
            String b = lines.get(end - i);
            if (a.length() != b.length()) throw new AssertionError();
            for (int j = 0; j < a.length(); j++) {
                if (a.charAt(j) != b.charAt(j)) {
                    smudges++;
                    if (smudges > 1) return false;
                }
            }
        }
        return smudges == 1;
    }

    private String solve1() {
        List<String> lines = new ArrayList<>(this.lines);
        if (!lines.get(lines.size()-1).isEmpty()) lines.add(""); // sentinel

        long ans = 0;
        List<String> mirror = new ArrayList<>();
        for (String line : lines) {
            if (!line.isEmpty()) {
                mirror.add(line);
            } else {
                ans += calculateScore(mirror);
                mirror.clear();
            }
        }
        return String.valueOf(ans);
    }

    private long calculateScore(List<String> lines) {
        for (int i = 1; i < lines.size(); i++) {
            if (verify(lines, 0, i)) {
                return (i + 1) / 2L * 100L;
            }
        }

        for (int i = lines.size()-2; i >= 0; i--) {
            if (verify(lines, i, lines.size()-1)) {
                return (i + lines.size()) / 2L * 100L;
            }
        }

        List<String> mirrorT = transpose(lines);

        for (int i = 1; i < mirrorT.size(); i++) {
            if (verify(mirrorT, 0, i)) {
                return (i + 1) / 2L;
            }
        }

        for (int i = mirrorT.size()-2; i >= 0; i--) {
            if (verify(mirrorT, i, mirrorT.size()-1)) {
                return (i + mirrorT.size()) / 2L;
            }
        }

        return 0;
    }

    private boolean verify(List<String> lines, int start, int end) {
        for (int i = 0; i <= (end - start) / 2; i++) {
            if (!lines.get(start + i).equals(lines.get(end - i))) {
                return false;
            }
        }
        return true;
    }

    private List<String> transpose(List<String> lines) {
        List<String> ans = new ArrayList<>();
        for (int i = 0; i < lines.get(0).length(); i++) {
            StringBuilder sb = new StringBuilder();
            for (String line : lines) {
                sb.append(line.charAt(i));
            }
            ans.add(sb.toString());
        }
        return ans;
    }
}
