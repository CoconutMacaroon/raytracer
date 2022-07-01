package org.blastoffnetwork;

public class IntersectionData {
    public IntersectionData(double closest_t, Sphere sphere) {
        this.closest_t = closest_t;
        this.sphere = sphere;
    }
    double closest_t;
    Sphere sphere;
}
