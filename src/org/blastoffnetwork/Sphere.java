package org.blastoffnetwork;

import java.awt.*;

public class Sphere {
    double radius;
    Color color;
    double[] center;
    double specular;

    public Sphere(double[] center, double radius, Color color, double specular) {
        this.center = center;
        this.radius = radius;
        this.color = color;
        this.specular = specular;
    }
}
