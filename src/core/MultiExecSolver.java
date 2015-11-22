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
public class MultiExecSolver<T> implements Solver {

    private GenericSolver<T> mainSolver;

    private Set<GenericSolver<T>> solverPool;
    private int mergePoint;
    private int poolSize;

    public MultiExecSolver(GenericSolver<T> genericSolver, int poolSize, int mergePoint) {
        this.mainSolver = genericSolver;
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
    public void solve() {
        List<GenericSolver<T>.Candidate> candidates = new ArrayList<>();
        solverPool.parallelStream().map(s -> {
            s.initiate();
            s.solveUntil(mergePoint);
            return s.getCandidatesSorted().subList(0, s.candidatesSize / solverPool.size());
        }).forEach(candidates::addAll);
        mainSolver.setCandidates(candidates);
        mainSolver.eliteLimit = 0.01;
        mainSolver.solve();
    }

}
