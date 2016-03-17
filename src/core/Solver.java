package core;

/**
 * Project: GenericAlgorithm
 * Created by guoli on 2015/11/22.
 */
public interface Solver {

    void initiate();

    GeneticSolver.Candidate solve();

    GeneticSolver.Candidate solveUntil(int generation);

}
