package wrapper;

import impl.ThreeSatSolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * Project: GenericAlgorithm
 * Created by guoli on 2015/11/21.
 */
public class ThreeSatWrapper {

    private final int HOW_MANY_SAT = 3;
    private ThreeSatSolver solver;

    public ThreeSatWrapper(File file) throws FileNotFoundException {
        ThreeSatSolver.ThreeSat threeSat = readFromFile(file);
        solver = new ThreeSatSolver(threeSat, 5000, 0.1, 0.1, 0.8);
    }

    private ThreeSatSolver.ThreeSat readFromFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        int totalLetters = scanner.nextInt();
        int totalStatements = scanner.nextInt();
        ThreeSatSolver.ThreeSat threeSat = new ThreeSatSolver.ThreeSat(1, totalLetters);
        IntStream.range(0, totalStatements).forEachOrdered(i -> {
            Set<Integer> statement = new HashSet<>();
            for (int k = 0; k < HOW_MANY_SAT; k++) {
                int number = scanner.nextInt();
                statement.add(number);
            }
            threeSat.addStatement(statement);
        });
        return threeSat;
    }

    public void solve() {
        solver.solve();
    }
}
