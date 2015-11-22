package impl;

import core.GenericSolver;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Project: GenericAlgorithm
 * Created by guoli on 2015/11/21.
 */
public class ThreeSatSolver extends GenericSolver<Boolean> {

    private ThreeSat threeSat;

    public ThreeSatSolver(ThreeSat threeSat, int candidatesSize, double eliteLimit, double mutationChance, double crossoverChance) {
        super(candidatesSize, threeSat.TOTAL_LETTERS, 1, eliteLimit, mutationChance, crossoverChance);
        this.threeSat = threeSat;
    }

    @Override
    public double getFitness(Candidate candidate) {
        return threeSat.fitness(candidate);
    }

    @Override
    protected List<Candidate> crossover(Candidate a, Candidate b) {
        int offsetX = (int) (Math.random() * candidateLength);
        int offsetY = (int) (Math.random() * candidateLength);
        Candidate newA = new Candidate(a);
        Candidate newB = new Candidate(b);
        IntStream.range(offsetX, offsetY)
                .forEach(i -> {
                    boolean s = newA.get(i);
                    newA.set(i, b.get(i));
                    newB.set(i, s);
                });
        newA.refreshFitness();
        newB.refreshFitness();
        return Arrays.asList(newA, newB);
    }

    @Override
    protected void randomFlip(Candidate a) {
        int flipIndex = (int) (Math.random() * a.size());
        a.set(flipIndex, !a.get(flipIndex));
        a.refreshFitness();
    }

    @Override
    protected boolean canTerminate() {
        return (int) getBestCandidate().getFitness() == threeSat.getNumOfStatements();
    }

    @Override
    protected Candidate randomValidCandidate() {
        Random random = new Random();
        Candidate candidate = new Candidate();
        IntStream.range(0, candidateLength).forEach(i -> candidate.add(i, random.nextBoolean()));
        candidate.refreshFitness();
        return candidate;
    }

    public static class ThreeSat {
        public final int START_INDEX;
        public final int TOTAL_LETTERS;
        private List<Set<Integer>> statements;

        public ThreeSat(int start_index, int total_letters) {
            START_INDEX = start_index;
            this.TOTAL_LETTERS = total_letters;
            statements = new ArrayList<>();
        }

        public void addStatement(Set<Integer> statement) {
            statements.add(statement);
        }

        public double fitness(Candidate candidate) {
            return statements.parallelStream()
                    .filter(s -> s.stream()
                            .anyMatch(i -> candidate.get(Math.abs(i) - START_INDEX) == i > 0))
                    .count();
        }

        public int getNumOfStatements() {
            return statements.size();
        }
    }
}
