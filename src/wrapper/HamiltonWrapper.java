package wrapper;

import impl.HamiltonSolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Project: GenericAlgorithm
 * Created by guoli on 2015/11/22.
 */
public class HamiltonWrapper {

    private HamiltonSolver solver;

    public HamiltonWrapper(File file) throws FileNotFoundException {
        HamiltonSolver.Hamilton hamilton = readFromFile(file);
        solver = new HamiltonSolver(hamilton, 5000, 0.02, 0.3, 0.9);
    }

    private HamiltonSolver.Hamilton readFromFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        int totalCities = scanner.nextInt();
        HamiltonSolver.Hamilton hamilton = new HamiltonSolver.Hamilton(0, totalCities, true);
        while (scanner.hasNext()) {
            int startIndex = scanner.nextInt();
            int endIndex = scanner.nextInt();
            double value = scanner.nextDouble();
            hamilton.addPath(startIndex, endIndex, value);
        }
        return hamilton;
    }

    public void solve() {
        solver.solve();
    }
}
