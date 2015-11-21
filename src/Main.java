import wrapper.KnapsackWrapper;
import wrapper.ThreeSatWrapper;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        // KnapsackWrapper wrapper = new KnapsackWrapper(new File("backpack_input.txt"));
        ThreeSatWrapper wrapper = new ThreeSatWrapper(new File("3sat_input.txt"));
        wrapper.solve();
    }
}
