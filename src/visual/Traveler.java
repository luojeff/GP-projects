package visual;

import processing.core.PApplet;

public class Traveler implements Comparable<Traveler> {
    public static float START_X, START_Y;
    public float posX, posY;
    public int width, height;
    public double[] sequence;
    public double fitness;

    private PApplet pApplet;
    private int currentMove;

    public static int SEQUENCE_LENGTH = 300;
    public static double DISTANCE = 5; // distance traveled per move

    public boolean stop = false;

    public Traveler(float posX, float posY, int width, int height, PApplet pApplet) {
        this.posX = posX;
        this.posY = posY;
        this.START_X = posX;
        this.START_Y = posY;
        this.width = width;
        this.height = height;
        this.pApplet = pApplet;

        currentMove = 0;
        sequence = new double[SEQUENCE_LENGTH]; // arbitrary size
    }

    public void display() {
        pApplet.fill(100, 100, 100);
        pApplet.ellipse(posX, posY, width, height);
    }

    public void move() {
        double xTravel = DISTANCE * Math.cos(sequence[currentMove]);
        double yTravel = DISTANCE * Math.sin(sequence[currentMove]);

        posX += xTravel;
        posY += yTravel;
        currentMove++;
    }

    public void initializeRandom() {
        for (int i = 0; i < sequence.length; i++) {
            sequence[i] = Math.random() * 2.0 * Math.PI;
        }
    }

    public int compareTo(Traveler other) {
        return new Double(other.fitness).compareTo(new Double(this.fitness));
    }
}
