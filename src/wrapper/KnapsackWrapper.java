package wrapper;

import core.GenericSolver;
import core.MultiExecSolver;
import core.Solver;
import impl.KnapsackSolver;

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
        solver = new MultiExecSolver(new KnapsackSolver(knapsack, 5000, knapsack.getObjectCount(), 0.1, 0.3, 0.9), 4, 200);
    }

    public static KnapsackSolver.Knapsack readFromFile(File file) throws IOException {
        Scanner scanner = new Scanner(file);
        double maxWeight = scanner.nextDouble();
        int objCount = scanner.nextInt();
        KnapsackSolver.Knapsack knapsack = new KnapsackSolver.Knapsack(maxWeight);
        IntStream.range(0, objCount).forEachOrdered(i -> knapsack.add(i, scanner.nextDouble(), scanner.nextDouble()));
        return knapsack;
    }

    public GenericSolver.Candidate solve() {
        solver.initiate();
        return solver.solve();
    }
}
