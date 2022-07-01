package org.blastoffnetwork;

import java.awt.*;

public class Sphere {
    double radius;
    Color color;
    double[] center;
    double specular;
    double reflectiveness;

    public Sphere(double[] center, double radius, Color color, double specular, double reflectiveness) {
        this.center = center;
        this.radius = radius;
        this.color = color;
        this.specular = specular;
        this.reflectiveness = reflectiveness;
    }
}
