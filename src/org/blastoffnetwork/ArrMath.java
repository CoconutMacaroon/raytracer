package org.blastoffnetwork;

import java.io.InvalidClassException;

public class ArrMath {
    private ArrMath() throws InvalidClassException {
        throw new InvalidClassException("Don't make an instance of this");
    }

    // arr and arr
    static double[] subtract(double[] a, double[] b) {
        assert a.length == b.length;
        double[] result = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] - b[i];
        }
        return result;
    }

    static double[] add(double[] a, double[] b) {
        assert a.length == b.length;
        double[] result = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] + b[i];
        }
        return result;
    }

    // double and arr
    static double[] multiply(double a, double[] b) {
        double[] result = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            result[i] = a * b[i];
        }
        return result;
    }

    static double[] divide(double a, double[] b) {
        /// A is in the denominator
        double[] result = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            result[i] = b[i] / a;
        }
        return result;
    }
}
