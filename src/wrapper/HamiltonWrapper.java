package wrapper;

import core.GenericSolver;
import core.MultiExecSolver;
import core.Solver;
import impl.HamiltonSolver;
import ui.StopButtonFrame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Project: GenericAlgorithm
 * Created by guoli on 2015/11/22.
 */
public class HamiltonWrapper {

    private Solver solver;

    public HamiltonWrapper(File file) throws FileNotFoundException {
        HamiltonSolver.Hamilton hamilton = readFromFile(file);
        HamiltonSolver mainSolver = new HamiltonSolver(hamilton, 4000, 0.3, 0.3, 0.9);
        solver = new MultiExecSolver(mainSolver, 4, 200);
        new StopButtonFrame(mainSolver);
    }

    public static HamiltonSolver.Hamilton readFromFile(File file) throws FileNotFoundException {
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

    public GenericSolver.Candidate solve() {
        solver.initiate();
        return solver.solve();
    }
}
