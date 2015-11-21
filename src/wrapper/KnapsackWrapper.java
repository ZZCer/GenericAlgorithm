package wrapper;

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

    private KnapsackSolver solver;

    public KnapsackWrapper(File file) throws IOException {
        KnapsackSolver.Knapsack knapsack = readFromFile(file);
        solver = new KnapsackSolver(knapsack, 5000, knapsack.getObjectCount(), 0.1, 0.3, 0.9);
    }

    private KnapsackSolver.Knapsack readFromFile(File file) throws IOException {
        Scanner scanner = new Scanner(file);
        double maxWeight = scanner.nextDouble();
        int objCount = scanner.nextInt();
        KnapsackSolver.Knapsack knapsack = new KnapsackSolver.Knapsack(maxWeight);
        IntStream.range(0, objCount).forEach(i -> knapsack.add(i, scanner.nextDouble(), scanner.nextDouble()));
        return knapsack;
    }

    public void solve() {
        solver.solve();
    }
}
