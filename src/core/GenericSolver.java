package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Project: GenericAlgorithm
 * Created by guoli on 2015/11/15.
 */
public abstract class GenericSolver<T> {

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

    public Candidate solve() {
        initiate();
        Candidate candidate = null;
        int gen = 0;
        while (!canTerminate()) {
            evolve();
            candidate = getBestCandidate();
            System.out.println(++gen + " " + candidate.getFitness());
        }
        return candidate;
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

    protected void initiate() {
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
        IntStream.range(0, size / 2 + 1).forEach(i ->
                {
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
        );
        nextGeneration.clear();
        nextGeneration.addAll(afterCross.subList(0, size));
    }

    protected List<Candidate> eliteSelect(List<Candidate> nextGeneration) {
        List<Candidate> tmpList = new ArrayList<>(nextGeneration);
        Collections.sort(tmpList, (a, b) -> (int) (b.getFitness() - a.getFitness()));
        List<Candidate> elite = new ArrayList<>();
        IntStream.range(0, (int) (candidatesSize * eliteLimit)).forEach(i -> {
            Candidate candidate = new Candidate(tmpList.get(i));
            elite.add(i, candidate);
        });
        return elite;
    }

    protected void addElite(List<Candidate> newGeneration, List<Candidate> elite) {
        Collections.sort(newGeneration, (a, b) -> (int) (a.getFitness() - b.getFitness()));
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


    public class Candidate extends ArrayList<T> {

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
    }

}