package org.blastoffnetwork;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InvalidClassException;

import static java.lang.Math.sqrt;

public class Util {
    private Util() throws InvalidClassException {
        throw new InvalidClassException("Don't make an instance of this");
    }

    static int roundColor(double colorValue) {
        return Math.round(colorValue) > 255 ? 255 : (int) Math.round(colorValue);
    }

    static double length(double[] vec) {
        return (double) sqrt(dot(vec, vec));
    }

    static void displayImage(BufferedImage image) {
        JFrame frame = new JFrame("Raytracer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(
                new JLabel(
                        new ImageIcon(
                                image
                        )
                ),
                BorderLayout.CENTER
        );
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
    
    static double dot(double[] a, double[] b) {
        double result = 0.0f;
        for (int i = 0; i < a.length; i++)
            result += a[i] * b[i];
        return result;
    }
}
