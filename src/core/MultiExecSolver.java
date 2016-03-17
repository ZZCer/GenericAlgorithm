package core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * Project: GenericAlgorithm
 * Created by guoli on 2015/11/22.
 */
public class MultiExecSolver implements Solver {

    private GeneticSolver mainSolver;

    private Set<GeneticSolver> solverPool;
    private int mergePoint;
    private int poolSize;

    public MultiExecSolver(GeneticSolver geneticSolver, int poolSize, int mergePoint) {
        this.mainSolver = geneticSolver;
        this.mergePoint = mergePoint;
        this.solverPool = new HashSet<>(poolSize);
        this.poolSize = poolSize;
    }

    @Override
    public void initiate() {
        IntStream.range(0, poolSize).forEach(i -> {
            try {
                solverPool.add(mainSolver.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public GeneticSolver.Candidate solve() {
        return solveUntil(Integer.MAX_VALUE);
    }

    @Override
    public GeneticSolver.Candidate solveUntil(int generation) {
        List<GeneticSolver.Candidate> candidates = new ArrayList<>();
        solverPool.parallelStream().map(s -> {
            s.initiate();
            s.solveUntil(mergePoint);
            return s.getCandidatesSorted().subList(0, s.candidatesSize / solverPool.size());
        }).forEach(candidates::addAll);
        mainSolver.setCandidates(candidates);
        return mainSolver.solveUntil(generation);
    }

}
