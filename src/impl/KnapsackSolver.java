package impl;

import core.GeneticSolver;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Project: GenericAlgorithm
 * Created by guoli on 2015/11/16.
 */
public class KnapsackSolver extends GeneticSolver<Boolean> {

    private Knapsack knapsack;

    public KnapsackSolver(Knapsack knapsack, int candidatesSize, int crossoverLimit, double eliteLimit, double mutationChance, double crossoverChance) {
        super(candidatesSize, knapsack.getObjectCount(), crossoverLimit, eliteLimit, mutationChance, crossoverChance);
        this.knapsack = knapsack;
    }

    public boolean validCandidate(Candidate candidate) {
        return knapsack.valid(candidate);
    }

    @Override
    public double getFitness(Candidate candidate) {
        return knapsack.fitness(candidate);
    }

    @Override
    public List<Candidate> crossover(Candidate a, Candidate b) {
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
        if (validCandidate(newA) && validCandidate(newB)) {
            refreshFitness(newA);
            refreshFitness(newB);
            return Arrays.asList(newA, newB);
        } else {
            return null;
        }
    }

    @Override
    public void randomFlip(Candidate a) {
        while (true) {
            int flipIndex = (int) (Math.random() * a.size());
            a.set(flipIndex, !a.get(flipIndex));
            if (validCandidate(a)) {
                refreshFitness(a);
                break;
            } else {
                a.set(flipIndex, !a.get(flipIndex));
            }
        }
    }

    @Override
    public boolean canTerminate() {
        return false;
    }

    @Override
    public Candidate randomValidCandidate() {
        Random random = new Random();
        while (true) {
            Candidate candidate = new Candidate();
            IntStream.range(0, candidateLength).forEach(i -> candidate.add(i, random.nextBoolean()));
            if (validCandidate(candidate)) {
                refreshFitness(candidate);
                return candidate;
            }
        }
    }

    public static class Knapsack {
        private final double maxWeight;
        private List<Pair<Double, Double>> objects;

        public Knapsack(double maxWeight) {
            objects = new ArrayList<>();
            this.maxWeight = maxWeight;
        }

        public void add(int index, double weight, double value) {
            objects.add(index, new Pair<>(weight, value));
        }

        public boolean valid(Candidate candidate) {
            return IntStream.range(0, candidate.size())
                    .filter(i -> candidate.get(i).equals(true))
                    .mapToDouble(i -> objects.get(i).getKey())
                    .sum() <= maxWeight;
        }

        public double fitness(Candidate candidate) {
            return IntStream.range(0, candidate.size())
                    .filter(i -> candidate.get(i).equals(true))
                    .mapToDouble(i -> objects.get(i).getValue()).sum();
        }

        public int getObjectCount() {
            return objects.size();
        }
    }
}
