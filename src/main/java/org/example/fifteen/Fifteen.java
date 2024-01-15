package org.example.fifteen;

import org.example.Solver;

import java.util.ArrayList;
import java.util.List;

public class Fifteen implements Solver {
    public static final Fifteen SINGLETON = new Fifteen();

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
        String[] tokens = lines.get(0).split(",");
        long sum = 0;
        for (String token : tokens) {
            sum += hash(token);
        }
        return String.valueOf(sum);
    }

    private long hash(String token) {
        long sum = 0;
        for (char c : token.toCharArray()) {
            sum += (int) c;
            sum *= 17;
            sum %= 256;
        }
        return sum;
    }

    private String solve2() {
        String[] tokens = lines.get(0).split(",");

        List<List<String[]>> hashmap = new ArrayList<>();
        for (int i = 0; i < 256; i++) {
            hashmap.add(new ArrayList<>());
        }

        for (String token : tokens) {
            hash2(token, hashmap);
        }

        long ans = getFocusingPower(hashmap);

        return String.valueOf(ans);
    }

    private long getFocusingPower(List<List<String[]>> hashmap) {
        long sum = 0;
        for (int i = 0; i < 256; i++) {
            List<String[]> box = hashmap.get(i);
            int boxNumPlusOne = i + 1;
            for (int j = 0; j < box.size(); j++) {
                int slotNum = j + 1;
                int focalLength = Integer.parseInt(box.get(j)[1]);
                sum += (long) boxNumPlusOne * slotNum * focalLength;
            }
        }
        return sum;
    }

    private void hash2(String step, List<List<String[]>> hashmap) {
        String[] tokens = null;
        char operation = ' ';
        if (step.contains("=")) {
            tokens = step.split("=");
            operation = '=';
        } else if (step.contains("-")) {
            tokens = step.split("-");
            operation = '-';
        }

        int label = (int) hash(tokens[0]);

        if (operation == '-') {
            List<String[]> box = hashmap.get(label);
            String labelToFind = tokens[0];
            int idxToDelete = findLensWithLabel(box, labelToFind);
            if (idxToDelete != -1) {
                box.remove(idxToDelete);
            }
        }

        if (operation == '=') {
            List<String[]> box = hashmap.get(label);
            String labelToFind = tokens[0];
            int idxToChange = findLensWithLabel(box, labelToFind);
            if (idxToChange != -1) {
                box.get(idxToChange)[1] = tokens[1];
            } else {
                box.add(new String[]{tokens[0], tokens[1]});
            }
        }
    }

    private int findLensWithLabel(List<String[]> box, String label) {
        for (int i = 0; i < box.size(); i++) {
            String[] lens = box.get(i);
            String boxLabel = lens[0];
            if (boxLabel.equals(label)) {
                return i;
            }
        }
        return -1;
    }
}
