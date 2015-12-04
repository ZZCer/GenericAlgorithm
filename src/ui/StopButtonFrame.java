package ui;

import com.sun.scenario.effect.impl.sw.java.JSWBlend_COLOR_BURNPeer;
import core.GenericSolver;

import javax.swing.*;

/**
 * Project: GenericAlgorithm
 * Created by guoli on 2015/12/2.
 */
public class StopButtonFrame extends JFrame {

    public StopButtonFrame(GenericSolver solver) {
        JButton button = new JButton("Stop!");
        button.addActionListener(e -> solver.setStopFlag(true));
        this.add(button);
        this.setSize(200, 200);
        this.setVisible(true);
    }

}
