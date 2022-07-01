package org.blastoffnetwork;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InvalidClassException;

public class Util {
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
    private Util() throws InvalidClassException {
        throw new InvalidClassException("Don't make an instance of this");
    }

    static boolean inRange(double n, double a, double b) {
        return a < n && n < b;
    }

    static double[] subtract(double[] a, double[] b) {
        assert a.length == b.length;
        double[] result = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] - b[i];
        }
        return result;
    }

    static double dot(double[] a, double[] b) {
        double result = 0.0;
        for (int i = 0; i < a.length; i++)
            result += a[i] * b[i];
        return result;
    }
}
