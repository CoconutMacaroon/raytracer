package org.blastoffnetwork;

public class Light {
    public static final byte LIGHT_TYPE_AMBIENT = 1;
    public static final byte LIGHT_TYPE_POINT = 2;
    public static final byte LIGHT_TYPE_DIRECTIONAL = 3;

    byte lightType;
    double intensity;
    double[] position;
    double[] direction;

    public Light(byte lightType, double intensity, double[] position, double[] direction) {
        if (lightType < 1 || lightType > 3) {
            throw new IllegalArgumentException();
        }
        this.lightType = lightType;
        this.intensity = intensity;
        this.position = position;
        this.direction = direction;
    }
}
