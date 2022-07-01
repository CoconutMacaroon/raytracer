package org.blastoffnetwork;

import java.awt.*;

public class Sphere {
    double radius;
    Color color;
    double[] center;

    public Sphere(double[] center, double radius, Color color) {
        this.center = center;
        this.radius = radius;
        this.color = color;
    }
}
