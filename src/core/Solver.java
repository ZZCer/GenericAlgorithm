package core;

/**
 * Project: GenericAlgorithm
 * Created by guoli on 2015/11/22.
 */
public interface Solver {

    void initiate();

    GenericSolver.Candidate solve();

    GenericSolver.Candidate solveUntil(int generation);

}
