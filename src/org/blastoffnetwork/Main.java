package org.blastoffnetwork;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static org.blastoffnetwork.ArrMath.*;
import static org.blastoffnetwork.Util.*;


public class Main {
    static final Color BACKGROUND_COLOR = new Color(0, 0, 0);
    static final int CANVAS_WIDTH = 1024;
    static final int CANVAS_HEIGHT = 1024;
    static final double D = 1;
    static final double VIEWPORT_WIDTH = 1;
    static final double VIEWPORT_HEIGHT = 1;
    static final double inf = Double.MAX_VALUE;
    static final int RECURSION_DEPTH_FOR_REFLECTIONS = 4;
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
        Color color = traceRay(cameraPosition, D, 1, inf, RECURSION_DEPTH_FOR_REFLECTIONS);
        putPixel(x, y, color, frame);
    }

    public static void main(String[] args) {
        BufferedImage image = new BufferedImage(CANVAS_WIDTH, CANVAS_HEIGHT, BufferedImage.TYPE_INT_RGB);
        long startTime = System.nanoTime();

        Pixel[] pixelsToRender = new Pixel[2 * CANVAS_WIDTH * 2 * CANVAS_HEIGHT];

        int counter = 0;
        for (int x = -CANVAS_WIDTH; x < CANVAS_WIDTH; ++x) {
            for (int y = -CANVAS_HEIGHT; y < CANVAS_HEIGHT; ++y) {
                pixelsToRender[counter++] = new Pixel(x, y);
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
        displayImage(image);
    }

    private static Color traceRay(double[] cameraPosition,
                                  double[] d,
                                  double min_t,
                                  double max_t, int recursion_depth) {
        IntersectionData intersectionData = closestIntersection(cameraPosition, d, min_t, max_t);
        if (intersectionData.sphere == null) {
            return BACKGROUND_COLOR;
        }

        // return closestSphere.color;
        double[] P = add(cameraPosition, multiply(intersectionData.closest_t, d));
        double[] N = subtract(P, intersectionData.sphere.center);
        N = multiply(1.0 / length(N), N);
        double lighting = computeLighting(P, N, multiply(-1.0, d), intersectionData.sphere.specular);
        Color localColor = new Color(
                roundColor(intersectionData.sphere.color.getRed() * lighting),
                roundColor(intersectionData.sphere.color.getGreen() * lighting),
                roundColor(intersectionData.sphere.color.getBlue() * lighting)
        );

        double r = intersectionData.sphere.reflectiveness;
        if (recursion_depth <= 0 || r <= 0) {
            return localColor;
        }
        double[] R = reflectRay(multiply(-1.0, d), N);

        Color reflectedColor = traceRay(P, R, 0.001, inf, recursion_depth - 1);

        return new Color(
                roundColor(localColor.getRed() * (1 - r) + reflectedColor.getRed() * r),
                roundColor(localColor.getGreen() * (1 - r) + reflectedColor.getGreen() * r),
                roundColor(localColor.getBlue() * (1 - r) + reflectedColor.getBlue() * r)
        );
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

    static IntersectionData closestIntersection(double[] cameraPosition, double[] d, double t_min, double t_max) {
        double closest_t = inf;
        Sphere closestSphere = null;
        for (Sphere sphere : Scene.spheres) {
            double[] t = intersectRaySphere(cameraPosition, d, sphere);
            if (t[0] < closest_t && t_min < t[0] && t[0] < t_max) {
                closest_t = t[0];
                closestSphere = sphere;
            }
            if (t[1] < closest_t && t_min < t[1] && t[1] < t_max) {
                closest_t = t[1];
                closestSphere = sphere;
            }

        }
        return new IntersectionData(closest_t, closestSphere);
    }

    static double computeLighting(double[] P, double[] N, double[] V, double s) {
        double intensity = 0.0;
        for (Light light : Scene.lights) {
            if (light.lightType == Light.LIGHT_TYPE_AMBIENT) {
                intensity += light.intensity;
            } else {
                double[] L;
                double t_max;
                if (light.lightType == Light.LIGHT_TYPE_POINT) {
                    L = subtract(light.position, P);
                    t_max = 1.0;
                } else {
                    L = light.direction;
                    t_max = inf;
                }

                // shadow check
                IntersectionData intersectionData = closestIntersection(P, L, 0.001, t_max);

                if (intersectionData.sphere != null) {
                    continue;
                }

                // diffuse
                double n_dot_l = dot(N, L);

                if (n_dot_l > 0) {
                    intensity += light.intensity * n_dot_l / (length(N) * length(L));
                }

                // specular
                if (s != -1) {
                    // 2 * N * dot(N, L) - L
                    double[] R = reflectRay(L, N);
                    double r_dot_v = dot(R, V);

                    if (r_dot_v > 0) {
                        intensity += light.intensity * pow(r_dot_v / (length(R) * length(V)), s);
                    }
                }
            }
        }
        return intensity;
    }

    static double[] reflectRay(double[] R, double[] N) {
        return subtract(
                multiply(
                        dot(N, R),
                        multiply(2, N)),
                R);
    }
}
