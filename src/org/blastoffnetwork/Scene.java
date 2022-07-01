package org.blastoffnetwork;

import java.awt.*;

public class Scene {
    static Sphere[] spheres = new Sphere[]{
        new Sphere(new double[]{0, -1, 3}, 1.0, new Color(255, 0, 0)),
        new Sphere(new double[]{2, 0, 4}, 1.0, new Color(0, 0, 255)),
        new Sphere(new double[]{-2, 0, 4}, 1.0, new Color(0, 255, 0))

    };
}
