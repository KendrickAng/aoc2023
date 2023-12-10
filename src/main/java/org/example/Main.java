package org.example;

import org.example.fileutils.FileUtils;
import org.example.one.One;
import org.example.two.Two;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;

public class Main {
    private static final int DAY = 2;
    private static final Map<Integer, Solver> SOLVERS = Map.of(
            1, One.SINGLETON,
            2, Two.SINGLETON
    );

    public static void main(String[] args) {
        start();
    }

    private static void start() {
        for (int i = 1; i <= DAY; i++) {

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
