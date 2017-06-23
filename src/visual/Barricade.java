package visual;

import processing.core.PApplet;

public class Barricade {
    public int posX, posY;
    public int size;
    private PApplet pApplet;

    public Barricade(int posX, int posY, int size, PApplet pApplet) {
        this.posX = posX;
        this.posY = posY;
        this.size = size;
        this.pApplet = pApplet;
    }

    public void display() {
        pApplet.fill(120,120,120);
        pApplet.ellipse(posX, posY, size, size);
    }
}