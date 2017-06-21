package main;

import java.util.ArrayList;
import java.util.Collections;

public class Maximizer {
    private int A;
    private int B;
    private int C;
    private int D;
    private int E;

    private ArrayList<Candidate> initialPopulation;

    private static double STRICTNESS = 0.1;
    private static double MUTATION_RATE = 0.01;
    private static int POPULATION_SIZE = 100;
    private static int NUM_GENERATIONS = 1000;

    private static double INITIAL_VARIATION = 100; // Variation of beginning candidate xVals
    private static double MUTATION_VARIATION = 1;

    public Maximizer(int A, int B, int C, int D, int E) {
        initialPopulation = new ArrayList<Candidate>();

        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
        this.E = E;
    }

    public void createInitialCandidates() {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            initialPopulation.add(new Candidate(Math.random() * INITIAL_VARIATION - INITIAL_VARIATION / 2));
        }
    }

    public void evaluateFitness() {
        for (Candidate candidate : initialPopulation) {
            candidate.fitness = getFitness(candidate.xVal);
        }

        Collections.sort(initialPopulation);
    }

    public double getFitness(double xVal) {
        return A * Math.pow(xVal, 4) + B * Math.pow(xVal, 3) + C * Math.pow(xVal, 2) + D * xVal + E;
    }

    /* NOT OPTIMIZED */
    public void breed() {
        Candidate first;
        Candidate second;
        double newVal;
        ArrayList<Candidate> newPopulation = new ArrayList<Candidate>();

        for (int i = 0; i < POPULATION_SIZE; i++) {
            first = initialPopulation.get((int) (Math.random() * POPULATION_SIZE * STRICTNESS));
            second = initialPopulation.get((int) (Math.random() * POPULATION_SIZE * STRICTNESS));

            if (Math.random() < MUTATION_RATE) {
                newVal = ((first.xVal + second.xVal) / 2) + (Math.random() * MUTATION_VARIATION - MUTATION_VARIATION / 2);
            } else {
                newVal = (first.xVal + second.xVal) / 2;
            }

            newPopulation.add(new Candidate(newVal));
        }

        initialPopulation = newPopulation;

        Collections.sort(initialPopulation);
    }

    public double getAverageOfBest() {
        int top = (int) (POPULATION_SIZE * STRICTNESS);
        double sum = 0;

        for (int i = 0; i < top; i++) {
            sum += initialPopulation.get(i).xVal;
        }

        return sum / top;
    }

    public void calculateMaximum() {
        createInitialCandidates();

        for (int i = 1; i <= NUM_GENERATIONS; i++) {
            evaluateFitness();
            breed();

            double average = getAverageOfBest();
            System.out.println("----------");
            System.out.println("Generation: " + i);
            System.out.println("Average x-val: " + average);
            System.out.println("Maximum: " + getFitness(average));
        }
    }

    public static void main(String[] args) {
        /*
        Create initial population of candidates
        ----- (calculateMaximum) -----
        Evaluation (for fitness)
        Selection & breed
        Variation (per mutation)
        Repeat
         */

        Maximizer approx = new Maximizer(-3, 6, -1, -1, 4); // FITNESS will be value plugged into quartic

        approx.calculateMaximum();
    }
}