package org.example;

import com.google.common.base.Functions;
import org.example.eight.Eight;
import org.example.eleven.Eleven;
import org.example.fileutils.FileUtils;
import org.example.five.Five;
import org.example.four.Four;
import org.example.nine.Nine;
import org.example.one.One;
import org.example.seven.Seven;
import org.example.six.Six;
import org.example.ten.Ten;
import org.example.thirteen.Thirteen;
import org.example.three.Three;
import org.example.twelve.Twelve;
import org.example.two.Two;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.Set;

public class Main {
    private static final Map<Integer, Solver> SOLVERS = Map.ofEntries(
            Map.entry(1, One.SINGLETON),
            Map.entry(2, Two.SINGLETON),
            Map.entry(3, Three.SINGLETON),
            Map.entry(4, Four.SINGLETON),
            Map.entry(5, Five.SINGLETON),
            Map.entry(6, Six.SINGLETON),
            Map.entry(7, Seven.SINGLETON),
            Map.entry(8, Eight.SINGLETON),
            Map.entry(9, Nine.SINGLETON),
            Map.entry(10, Ten.SINGLETON),
            Map.entry(11, Eleven.SINGLETON),
            Map.entry(12, Twelve.SINGLETON),
            Map.entry(13, Thirteen.SINGLETON)
    );

    private static final Set<Integer> IGNORE = Set.of(5);

    private static final int DAY = SOLVERS.keySet().stream().mapToInt(x -> x).max().orElseThrow();

    public static void main(String[] args) {
        start();
    }

    private static void start() {
        for (int i = 1; i <= DAY; i++) {
            if (IGNORE.contains(i)) {
                System.out.println("Skipping day " + i);
                continue;
            }

            System.out.printf("########## DAY #%d ##########\n", i);

            String num = String.valueOf(i);
            String part1Sample = Paths.get("/", num, "sample.part1.txt").toString();
            String part2Sample = Paths.get("/", num, "sample.part2.txt").toString();

            Path inputPath = Paths.get("/", num, "input.txt");
            String part1Input = inputPath.toString();
            String part2Input = inputPath.toString();

            Solver solver = SOLVERS.get(i);

            try {
                System.out.printf("Part 1 (Sample): %s\n", solver.partOneSample(FileUtils.readResourceAsStream(part1Sample)));
                System.out.printf("Part 1 (Input): %s\n", solver.partOneInput(FileUtils.readResourceAsStream(part1Input)));
                System.out.printf("Part 2 (Sample): %s\n", solver.partTwoSample(FileUtils.readResourceAsStream(part2Sample)));
                System.out.printf("Part 2 (Input): %s\n", solver.partTwoInput(FileUtils.readResourceAsStream(part2Input)));
            } catch (IOException e) {
                System.out.printf("Error reading file: %s\n", e.getMessage());
            }

            System.out.print("#############################\n");
        }
    }
}
