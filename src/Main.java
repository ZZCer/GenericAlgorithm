import core.GenericSolver;
import wrapper.HamiltonWrapper;
import wrapper.KnapsackWrapper;
import wrapper.ThreeSatWrapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        // KnapsackWrapper wrapper = new KnapsackWrapper(new File("data/TSP/TSP_0.txt"));
        // ThreeSatWrapper wrapper = new ThreeSatWrapper(new File("data/SAT3/SAT3_2.txt"));
        HamiltonWrapper wrapper = new HamiltonWrapper(new File("data/TSP/TSP_2.txt"));

        GenericSolver.Candidate result = wrapper.solve();
        FileWriter writer = new FileWriter(new File("TSP_2.txt"));
        writer.write(result.toString());
        writer.flush();
        writer.close();
        System.exit(0);
    }
}
