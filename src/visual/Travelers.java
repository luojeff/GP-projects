package visual;

import processing.core.*;
import controlP5.*;

import java.util.ArrayList;
import java.util.Collections;

public class Travelers extends PApplet {
    private static double STRICTNESS = 0.25; // Top percentile for which the population can breed the next generation
    private static double MUTATION_RATE = 0.01; // Chance for a mutation to develop in the next generation (out of 1)
    private static int POPULATION_SIZE = 1000;
    private static int NUM_GENERATIONS = 10000;
    //private static double INITIAL_VARIATION = 100; // Variation of beginning candidate xVals
    //private static double MUTATION_VARIATION = 1;

    private int currentGeneration = 0;

    private Endpoint endpoint;
    private ArrayList<Traveler> population;
    private ArrayList<Barricade> barricades;

    private int generationCount = 0;
    private int iterationCount = 0;

    private ControlP5 gui;
    private Textlabel generationLabel, mutationLabel, fitnessLabel;

    public static void main(String[] args) {
        PApplet.main("visual.Travelers");
    }

    public void setup() {
        frameRate(120);
        background(255, 255, 255);
        createGUI();

        endpoint = new Endpoint(9 * width / 10, height / 2, 50, 50, this);

        population = new ArrayList<Traveler>();
        barricades = new ArrayList<Barricade>();
        createInitialTravelers();
    }

    private void createInitialTravelers() {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            Traveler traveler = new Traveler(width / 10, height / 2, 5, 5, this);
            traveler.initializeRandom();
            population.add(traveler);
        }
    }

    private void displayTravelers() {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            Traveler traveler = population.get(i);
            traveler.stop = collides(traveler);
            traveler.display();

            if (!traveler.stop) {
                traveler.move();
            }
        }
    }

    private void displayBarriers() {
        for (Barricade barrier : barricades) {
            barrier.display();
        }
    }

    /* Not optimized */
    private double getFitness(Traveler traveler) {
        double rawDistance = Math.sqrt(Math.pow(Math.abs(endpoint.posX - traveler.posX), 2) +
                Math.pow(Math.abs(endpoint.posY - traveler.posY), 2));


        double fitness = 100.0 / rawDistance;

        if (traveler.stop) {
            fitness *= 3.0 / 5;
        }

        return fitness;
    }

    private void evaluateFitnesses() {
        for (Traveler traveler : population) {
            traveler.fitness = getFitness(traveler);
        }

        Collections.sort(population);
    }

    /* NOT OPTIMIZED */
    public void breed() {
        Traveler first;
        Traveler second;
        ArrayList<Traveler> newPopulation = new ArrayList<Traveler>();
        int top = (int) (STRICTNESS * population.size());

        for (int i = 0; i < POPULATION_SIZE; i++) {
            first = population.get((int) (Math.random() * top));
            second = population.get((int) (Math.random() * top));

            Traveler child = intermix(first, second);
            newPopulation.add(child);
        }

        population = newPopulation;
    }

    /* NOT OPTIMIZED */
    private Traveler intermix(Traveler first, Traveler second) {
        Traveler child = new Traveler(Traveler.START_X, Traveler.START_Y, first.width, first.height, this);

        for (int i = 0; i < Traveler.SEQUENCE_LENGTH; i++) {
            if (i % 2 == 0) {
                child.sequence[i] = first.sequence[i];
            } else {
                child.sequence[i] = second.sequence[i];
            }

            if (Math.random() < MUTATION_RATE) {
                child.sequence[i] = Math.random() * 2.0 * Math.PI;
            }
        }

        return child;
    }

    public static double distBetween(Traveler traveler, Barricade barricade) {
        return Math.sqrt(Math.pow(traveler.posX - barricade.posX, 2) + Math.pow(traveler.posY - barricade.posY, 2));
    }

    public boolean collides(Traveler traveler) {
        for (Barricade barricade : barricades) {
            if (distBetween(traveler, barricade) < barricade.size / 2) {
                return true;
            }
        }
        return false;
    }

    public void draw() {
        background(255, 255, 255);
        endpoint.display();

        if (mousePressed) {
            barricades.add(new Barricade(mouseX, mouseY, 50, this));
        }

        if (barricades != null) {
            displayBarriers();
        }

        evaluateFitnesses();
        generationLabel.setText("Generation: " + generationCount);
        mutationLabel.setText("Mutation Rate: " + MUTATION_RATE);
        fitnessLabel.setText("Best Fitness: " + String.format("%.2f", population.get(0).fitness));

        /*
        Create initial population of candidates
        - - - - - - - - - - -
        Evaluation (for fitness)
        Selection (of the fittest)
        Breed & variation (mutation)
        Repeat
         */
        if (iterationCount++ < Traveler.SEQUENCE_LENGTH) {
            displayTravelers();
        } else if (generationCount < NUM_GENERATIONS) {
            breed(); // satisfies selection, breed, and variation
            iterationCount = 0;
            generationCount++;
        }
    }

    public void settings() {
        size(1200, 600);
    }

    public void createGUI() {
        gui = new ControlP5(this);

        generationLabel = gui.addTextlabel("generation").setText("Generation: " + 0).setPosition(50, 50).setSize(200,
                60).setFont(new ControlFont(createFont("Arial", 20))).setColor(0);

        mutationLabel = gui.addTextlabel("mutation").setText("Mutation Rate: " + MUTATION_RATE).setPosition(225, 50).setSize(200,
                60).setFont(new ControlFont(createFont("Arial", 20))).setColor(0);

        fitnessLabel = gui.addTextlabel("fitness").setText("Best Fitness: N/A").setPosition(450, 50).setSize(200,
                60).setFont(new ControlFont(createFont("Arial", 20))).setColor(0);
    }
}
