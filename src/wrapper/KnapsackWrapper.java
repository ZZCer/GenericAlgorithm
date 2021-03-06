package wrapper;

import core.GeneticSolver;
import core.MultiExecSolver;
import core.Solver;
import impl.KnapsackSolver;
import ui.StopButtonFrame;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.stream.IntStream;

/**
 * Project: GenericAlgorithm
 * Created by guoli on 2015/11/16.
 */
public class KnapsackWrapper {

    private Solver solver;

    public KnapsackWrapper(File file) throws IOException {
        KnapsackSolver.Knapsack knapsack = readFromFile(file);
        KnapsackSolver mainSolver = new KnapsackSolver(knapsack, 5000, knapsack.getObjectCount(), 0.1, 0.2, 0.9);
        solver = new MultiExecSolver(mainSolver, 4, 200);
        new StopButtonFrame(mainSolver);
    }

    public static KnapsackSolver.Knapsack readFromFile(File file) throws IOException {
        Scanner scanner = new Scanner(file);
        double maxWeight = scanner.nextDouble();
        int objCount = scanner.nextInt();
        KnapsackSolver.Knapsack knapsack = new KnapsackSolver.Knapsack(maxWeight);
        IntStream.range(0, objCount).forEachOrdered(i -> knapsack.add(i, scanner.nextDouble(), scanner.nextDouble()));
        return knapsack;
    }

    public GeneticSolver.Candidate solve() {
        solver.initiate();
        return solver.solve();
    }
}
