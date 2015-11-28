import impl.HamiltonSolver;
import impl.ThreeSatSolver;
import wrapper.ThreeSatWrapper;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Project: GenericAlgorithm
 * Created by guoli on 2015/11/28.
 */
public class Benchmark {

    public static void main(String[] args) throws FileNotFoundException {
        ThreeSatSolver.ThreeSat threeSat = ThreeSatWrapper.readFromFile(new File("3sat_input.txt"));
        int candidateSize, generationLimit;
        double mutationChance, crossoverChance;
        for (mutationChance = 0; mutationChance < 1; mutationChance += 0.1) {
            System.out.println("mutation chance = " + mutationChance);
            test3Sat(threeSat, 5000, 100, mutationChance, 0.8);
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
