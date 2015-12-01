package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Project: GenericAlgorithm
 * Created by guoli on 2015/11/15.
 */
public abstract class GenericSolver<T> implements Solver, Cloneable {

    protected final int candidatesSize;
    protected final int candidateLength;

    protected int crossoverLimit;
    protected double eliteLimit;
    protected double mutationChance;
    protected double crossoverChance;

    private List<Candidate> candidates;

    protected GenericSolver(int candidatesSize, int candidateLength, int crossoverLimit, double eliteLimit, double mutationChance, double crossoverChance) {
        this.candidatesSize = candidatesSize;
        this.candidateLength = candidateLength;
        this.crossoverLimit = crossoverLimit;
        this.eliteLimit = eliteLimit;
        this.mutationChance = mutationChance;
        this.crossoverChance = crossoverChance;
    }

    @Override
    public GenericSolver clone() throws CloneNotSupportedException {
        Object solver = super.clone();
        if (solver instanceof GenericSolver) {
            return (GenericSolver) solver;
        }
        return null;
    }

    public abstract double getFitness(Candidate candidate);

    protected abstract List<Candidate> crossover(Candidate a, Candidate b);

    protected abstract void randomFlip(Candidate a);

    protected abstract boolean canTerminate();

    protected abstract Candidate randomValidCandidate();

    protected List<Double> getCurrentFitness(List<Candidate> candidates) {
        List<Double> fitness = new ArrayList<>();
        candidates.forEach(e -> fitness.add(e.getFitness()));
        return fitness;
    }

    void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }

    List<Candidate> getCandidatesSorted() {
        List<Candidate> list = new ArrayList<>(candidates);
        Collections.sort(list, (a, b) -> (int) (b.getFitness() - a.getFitness()));
        return list;
    }

    @Override
    public Candidate solveUntil(int generation) {
        Candidate candidate = null;
        int gen = 0;
        while (!canTerminate() && gen < generation) {
            evolve();
            candidate = getBestCandidate();
            System.out.println("[" + Thread.currentThread().getId() + "] \t" + ++gen + "\t " + candidate.getFitness());
        }
        return candidate;
    }

    public Candidate solve() {
        return solveUntil(Integer.MAX_VALUE);
    }

    protected Candidate getBestCandidate() {
        List<Double> fitness = getCurrentFitness(candidates);
        int index = fitness.indexOf(Collections.max(fitness));
        return candidates.get(index);
    }

    protected void mutation(Candidate a) {
        while (Math.random() < mutationChance) {
            randomFlip(a);
        }
    }

    public void initiate() {
        List<Candidate> candidates = new ArrayList<>();
        IntStream.range(0, candidatesSize).forEach(i -> candidates.add(i, randomValidCandidate()));
        this.candidates = candidates;
    }

    protected List<Candidate> nextGeneration() {
        return roulette(candidates, getCurrentFitness(candidates), candidatesSize);
    }

    protected void crossoverGeneration(List<Candidate> nextGeneration) {
        int size = candidatesSize;
        List<Candidate> afterCross = new ArrayList<>();
        List<Double> fitness = getCurrentFitness(nextGeneration);
        while (afterCross.size() < size) {
            List<Candidate> cross = roulette(nextGeneration, fitness, 2);
            if (Math.random() > crossoverChance) {
                afterCross.addAll(cross);
            } else {
                int times = 0;
                while (true) {
                    List<Candidate> crossoverResult = crossover(cross.get(0), cross.get(1));
                    if (crossoverResult != null) {
                        crossoverResult.forEach(this::mutation);
                        afterCross.addAll(crossoverResult);
                        break;
                    } else {
                        times++;
                    }
                    if (times > crossoverLimit) {
                        afterCross.addAll(cross);
                        break;
                    }
                }
            }
        }
        nextGeneration.clear();
        nextGeneration.addAll(afterCross.subList(0, size));
    }

    protected List<Candidate> eliteSelect(List<Candidate> nextGeneration) {
        List<Candidate> tmpList = new ArrayList<>(nextGeneration);
        Collections.sort(tmpList, Comparator.reverseOrder());
        List<Candidate> elite = new ArrayList<>();
        IntStream.range(0, (int) (candidatesSize * eliteLimit)).forEach(i -> {
            Candidate candidate = new Candidate(tmpList.get(i));
            elite.add(i, candidate);
        });
        return elite;
    }

    protected void addElite(List<Candidate> newGeneration, List<Candidate> elite) {
        Collections.sort(newGeneration);
        IntStream.range(0, (int) (candidatesSize * eliteLimit)).forEach(i -> newGeneration.set(i, elite.get(i)));
        Collections.shuffle(newGeneration);
    }

    protected void evolve() {
        // System.out.println("Selecting elites");
        List<Candidate> elite = eliteSelect(candidates);
        // System.out.println("Generating new generation");
        List<Candidate> newGeneration = nextGeneration();
        // System.out.println("Crossover...");
        crossoverGeneration(newGeneration);
        // System.out.println("Adding elites...");
        addElite(newGeneration, elite);
        this.candidates = newGeneration;
    }

    protected List<Candidate> roulette(List<Candidate> current, List<Double> fitness, int size) {
        List<Candidate> chosen = new ArrayList<>();
        double max = Collections.max(fitness);
        IntStream.range(0, size).forEach(i -> {
            boolean accepted = false;
            int index = 0;
            while (!accepted) {
                index = (int) (current.size() * Math.random());
                if (Math.random() < fitness.get(index) / max) {
                    accepted = true;
                }
            }
            chosen.add(new Candidate(current.get(index)));
        });
        return chosen;
    }


    public class Candidate extends ArrayList<T> implements Comparable<Candidate> {

        private double fitness;

        public Candidate() {

        }

        public Candidate(Candidate candidate) {
            super(candidate);
            this.fitness = candidate.fitness;
        }

        public void refreshFitness() {
            fitness = GenericSolver.this.getFitness(this);
        }

        public double getFitness() {
            return fitness;
        }

        @Override
        public String toString() {
            String result = "";
            for (T t : this) {
                result += t + "\r\n";
            }
            result += Math.abs(fitness);
            return result;
        }

        @Override
        public int compareTo(Candidate o) {
            return compareDouble(this.fitness, o.fitness);
        }

        private int compareDouble(double a, double b) {
            if (a == b) {
                return 0;
            } else {
                return a > b ? 1 : -1;
            }
        }

    }

}
