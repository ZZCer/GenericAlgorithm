import impl.HamiltonSolver;
import impl.ThreeSatSolver;
import wrapper.HamiltonWrapper;
import wrapper.ThreeSatWrapper;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Project: GenericAlgorithm
 * Created by guoli on 2015/11/28.
 */
public class Benchmark {

    public static void main(String[] args) throws FileNotFoundException {
        // ThreeSatSolver.ThreeSat threeSat = ThreeSatWrapper.readFromFile(new File("3sat_input.txt"));
        HamiltonSolver.Hamilton hamilton = HamiltonWrapper.readFromFile(new File("travel_input.txt"));
        int candidateSize, generationLimit;
        double mutationChance, crossoverChance;
        int[] sizes = {10, 50, 100, 500, 1000, 2000, 3000, 5000, 10000};
        for (int size : sizes) {
            System.out.println("size = " + size);
            testHamilton(hamilton, size, 200, 0.2, 0.8);
        }

    }

    private static void test3Sat(ThreeSatSolver.ThreeSat threeSat, int candidateSize, int generationLimit, double mutationChance, double crossoverChance) {
        ThreeSatSolver solver = new ThreeSatSolver(threeSat, candidateSize, 0.1, mutationChance, crossoverChance);
        solver.initiate();
        solver.solveUntil(generationLimit);
    }

    private static void testHamilton(HamiltonSolver.Hamilton hamilton,int candidateSize, int generationLimit, double mutationChance, double crossoverChance) {
        HamiltonSolver solver = new HamiltonSolver(hamilton, candidateSize, 0.1, mutationChance, crossoverChance);
        solver.initiate();
        solver.solveUntil(generationLimit);
    }
}
