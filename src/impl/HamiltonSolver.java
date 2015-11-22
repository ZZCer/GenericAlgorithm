package impl;

import core.GenericSolver;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Project: GenericAlgorithm
 * Created by guoli on 2015/11/21.
 */
public class HamiltonSolver extends GenericSolver<Integer> {

    private Hamilton hamilton;

    static {
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
    }

    public HamiltonSolver(Hamilton hamilton, int candidatesSize, double eliteLimit, double mutationChance, double crossoverChance) {
        super(candidatesSize, hamilton.TOTAL_VERTEXES, 1, eliteLimit, mutationChance, crossoverChance);
        this.hamilton = hamilton;
    }

    @Override
    public double getFitness(Candidate candidate) {
        return hamilton.fitness(candidate);
    }

    @Override
    protected List<Candidate> crossover(Candidate a, Candidate b) {
        Candidate newA = new Candidate(a);
        Candidate newB = new Candidate(b);
        int offsetX = (int) (Math.random() * candidateLength);
        int offsetY = (int) (Math.random() * candidateLength);
        offsetX = offsetX == 0 ? 1 : offsetX;
        offsetY = offsetY == 0 ? 1 : offsetY;
        IntStream.range(offsetX, offsetY)
                .forEach(i -> {
                    int s = newA.get(i);
                    int e = newB.get(i);
                    if (s != e) {
                        Collections.swap(newA, i, newA.indexOf(e));
                        Collections.swap(newB, i, newB.indexOf(s));
                    }
                });
        newA.refreshFitness();
        newB.refreshFitness();
        return Arrays.asList(newA, newB);
    }

    @Override
    protected void randomFlip(Candidate a) {
        int flipIndexA = (int) (Math.random() * a.size());
        int flipIndexB = (int) (Math.random() * a.size());
        flipIndexA = flipIndexA == 0 ? 1 : flipIndexA;
        flipIndexB = flipIndexB == 0 ? 1 : flipIndexB;
        Collections.swap(a, flipIndexA, flipIndexB);
        a.refreshFitness();
    }

    @Override
    protected boolean canTerminate() {
        return false;
    }

    @Override
    protected Candidate randomValidCandidate() {
        Candidate candidate = new Candidate();
        IntStream.range(hamilton.START_INDEX + 1, hamilton.TOTAL_VERTEXES + hamilton.START_INDEX).forEach(candidate::add);
        Collections.shuffle(candidate);
        candidate.add(0, hamilton.START_INDEX);
        candidate.refreshFitness();
        return candidate;
    }

    public static class Hamilton {

        public final int START_INDEX;
        public final int TOTAL_VERTEXES;
        public final boolean SINGLE_DIRECTION;

        private double[][] values;

        public Hamilton(int start_index, int total_vertexes, boolean single_direction) {
            START_INDEX = start_index;
            TOTAL_VERTEXES = total_vertexes;
            SINGLE_DIRECTION = single_direction;
            values = new double[TOTAL_VERTEXES][TOTAL_VERTEXES];
        }

        public void addPath(int start, int end, double value) {
            values[start - START_INDEX][end - START_INDEX] = value;
            if (SINGLE_DIRECTION) {
                values[end - START_INDEX][start - START_INDEX] = value;
            }
        }

        public double getPath(int start, int end) {
            return values[start - START_INDEX][end - START_INDEX];
        }

        public double fitness(Candidate candidate) {
            return -IntStream.range(0, TOTAL_VERTEXES - 1)
                    .mapToDouble(i -> getPath(candidate.get(i), candidate.get(i + 1)))
                    .sum();
        }
    }
}
