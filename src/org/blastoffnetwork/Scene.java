package org.blastoffnetwork;

import java.awt.*;

public class Scene {
    static Sphere[] spheres = new Sphere[]{
            new Sphere(new double[]{0, -1, 3}, 1.0f, new Color(255, 0, 0), 500, 0.2f),
            new Sphere(new double[]{2, 0, 4}, 1.0f, new Color(0, 0, 255), 500, 0.3f),
            new Sphere(new double[]{-2, 0, 4}, 1.0f, new Color(0, 255, 0), 10, 0.4f),
            new Sphere(new double[]{0, -5001, 0}, 5000, new Color(255, 255, 0), 1000, 0.5f)
    };
    static Light[] lights = new Light[]{
            new Light(Light.LIGHT_TYPE_AMBIENT, 0.2f, null, null),
            new Light(Light.LIGHT_TYPE_POINT, 0.6f, new double[]{2, 1, 0}, null),
            new Light(Light.LIGHT_TYPE_DIRECTIONAL, 0.2f, null, new double[]{1, 4, 4})
    };
}
