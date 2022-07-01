package org.blastoffnetwork;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import static java.lang.Math.sqrt;
import static org.blastoffnetwork.Util.dot;
import static org.blastoffnetwork.Util.subtract;

public class Main {
    static final Color BACKGROUND_COLOR = new Color(255, 255, 255);
    static final int CANVAS_WIDTH = 2048;
    static final int CANVAS_HEIGHT = 2048;
    static final double D = 1;
    static final double VIEWPORT_WIDTH = 1;
    static final double VIEWPORT_HEIGHT = 1;
    static final int inf = Integer.MAX_VALUE;
    static double[] cameraPosition = new double[]{0, 0, 0};

    static void putPixel(int x, int y, Color color, BufferedImage image) {
        x = CANVAS_WIDTH / 2 + x;
        y = CANVAS_HEIGHT / 2 - y - 1;
        if (x < 0 || x >= CANVAS_WIDTH || y < 0 || y >= CANVAS_HEIGHT) {
            return;
        }
        image.setRGB(x, y, color.getRGB());
    }
    public static double[] canvasToViewport(int x, int y) {
        return new double[]{
            x * VIEWPORT_WIDTH / CANVAS_WIDTH,
            y * VIEWPORT_HEIGHT / CANVAS_HEIGHT,
            D
        };
    }

    static void renderPixel(int x, int y, BufferedImage frame) {
        double[] D = canvasToViewport(x, y);
        Color color = traceRay(cameraPosition, D, 1, inf);
        putPixel(x, y, color, frame);
    }

    public static void main(String[] args) {
        BufferedImage image = new BufferedImage(CANVAS_WIDTH, CANVAS_HEIGHT, BufferedImage.TYPE_INT_RGB);
        long startTime = System.nanoTime();

        Pixel[] pixelsToRender = new Pixel[2 * CANVAS_WIDTH * 2 * CANVAS_HEIGHT];

        int counter = 0;
        for (int x = -CANVAS_WIDTH; x < CANVAS_WIDTH; ++x) {
            for (int y = -CANVAS_HEIGHT; y < CANVAS_HEIGHT; ++y) {
                pixelsToRender[counter] = new Pixel(x, y);
                counter++;
            }
        }
        Arrays
            .stream(pixelsToRender)
            .parallel()
            .forEach(
                pixel -> renderPixel(pixel.x, pixel.y, image)
            );
        long duration = (System.nanoTime() - startTime);

        System.out.printf("Execution time (MS) %f", duration / 1000000.0);
        //displayImage(image);
    }

    private static Color traceRay(double[] cameraPosition,
                                  double[] d,
                                  double min_t,
                                  double max_t) {
        double closest_t = inf;
        Sphere closestSphere = null;
        for (Sphere sphere : Scene.spheres) {
            double[] t = intersectRaySphere(cameraPosition, d, sphere);
            if (t[0] < closest_t && min_t < t[0] && t[0] < max_t) {
                closest_t = t[0];
                closestSphere = sphere;
            }
            if (t[1] < closest_t && min_t < t[1] && t[1] < max_t) {
                closest_t = t[1];
                closestSphere = sphere;
            }

        }
        if (closestSphere == null) {
            return BACKGROUND_COLOR;
        }
        return closestSphere.color;
    }

    private static double[] intersectRaySphere(double[] cameraPosition, double[] d, Sphere sphere) {
        double r = sphere.radius;
        double[] CO = subtract(cameraPosition, sphere.center);

        double a = dot(d, d);
        double b = 2 * dot(CO, d);
        double c = dot(CO, CO) - r * r;

        double discriminant = b * b - 4 * a * c;

        if (discriminant < 0) {
            return new double[]{inf, inf};
        }

        // don't calculate this twice
        double discriminantSqrt = sqrt(discriminant);

        return new double[]{
            (-b + discriminantSqrt) / (2 * a),
            (-b - discriminantSqrt) / (2 * a)
        };
    }
}