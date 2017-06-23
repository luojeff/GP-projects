package visual;

import processing.core.PApplet;

public class Endpoint {
    public float posX, posY;
    public int width, height;
    private PApplet pApplet;

    public Endpoint(float posX, float posY, int width, int height, PApplet pApplet) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.pApplet = pApplet;
    }


    public void display() {
        pApplet.fill(0, 150, 50);
        pApplet.ellipse(posX, posY, width, height);
    }
}