package impl;

import core.GenericSolver;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Project: GenericAlgorithm
 * Created by guoli on 2015/11/21.
 */
public class HamiltonSolver extends GenericSolver<Integer> {

    private Hamilton hamilton;

    public HamiltonSolver(Hamilton hamilton, int candidatesSize, double eliteLimit, double mutationChance, double crossoverChance) {
        super(candidatesSize, hamilton.TOTAL_VERTEXES, 1, eliteLimit, mutationChance, crossoverChance);
        this.hamilton = hamilton;
    }

    @Override
    public double getFitness(Candidate candidate) {
        return hamilton.fitness(candidate);
    }

    private int findNext(int current, Candidate candidate, boolean[] usedMap) {
        int nxt = current + 1;
        while (true){
            if (nxt >= candidateLength) {
                nxt = 0;
            }
            if (usedMap[candidate.get(nxt)]) {
                nxt++;
            } else  {
                break;
            }
        }
        return nxt;
    }

    @Override
    protected List<Candidate> crossover(Candidate a, Candidate b) {
        Candidate newCandidate = new Candidate();
        newCandidate.add(hamilton.START_INDEX);
        int last = hamilton.START_INDEX;
        boolean[] used = new boolean[candidateLength];
        used[hamilton.START_INDEX] = true;
        int aCur = 0, aNxt, bCur = 0, bNxt;
        while (newCandidate.size() < candidateLength) {
            aNxt = findNext(aCur, a, used);
            bNxt = findNext(bCur, b, used);
            double aCost = hamilton.getPath(last, a.get(aNxt));
            double bCost = hamilton.getPath(last, b.get(bNxt));
            if (aCost < bCost) {
                last = a.get(aNxt);
                bCur = b.indexOf(last);
                aCur = aNxt;
            } else {
                last = b.get(bNxt);
                aCur = a.indexOf(last);
                bCur = bNxt;
            }
            used[last] = true;
            newCandidate.add(last);
        }
        newCandidate.refreshFitness();
        return Collections.singletonList(newCandidate);
    }

    @Override
    protected void randomFlip(Candidate a) {
        Random random = new Random();
        int flipIndexA = random.nextInt(a.size() - 1) + 1;
        int flipIndexB = random.nextInt(a.size() - 1) + 1;
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
            return -IntStream.range(0, TOTAL_VERTEXES)
                    .mapToDouble(i -> {
                        if (i == TOTAL_VERTEXES - 1) {
                            return getPath(candidate.get(i), candidate.get(0));
                        }
                        return getPath(candidate.get(i), candidate.get(i + 1));
                    })
                    .sum();
        }
    }
}
