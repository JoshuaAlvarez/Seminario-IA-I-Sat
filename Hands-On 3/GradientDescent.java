package GradientDescent;

import java.io.IOException;
import java.util.Scanner;

public class GradientDescent {

    static double b0;
    static double b1;

    static double[] setA = {23, 26, 30, 34, 43, 48, 52, 57, 58};
    static double[] setB = {651, 762, 856, 1063, 1190, 1298, 1421, 1440, 1518};

    public static void main(String[] args) throws IOException {
        double tolerance = 1e-6;    // Tolerance 
        int maxIterations = 10000; // Maximum iterations
        double alpha = 0.00001;      // Learning rate

        // Parameters initialization
        b0 = 168;
        b1 = 23;

        double d0, d1;
        int i = 0;

        do {
            // Calculate and update the value of bethas
            d0 = alpha * deBetha0();
            d1 = alpha * deBetha1();

            i++;
            b0 -= d0;
            b1 -= d1;

            if (i > maxIterations) {
                break;
            }
        } while (tolerance < Math.abs(d1) || tolerance < Math.abs(d0));

        System.out.println("Betha0 = " + b0 + ", Betha1 = " + b1);
        System.out.println("Enter x value to predict:");
        Scanner input = new Scanner(System.in);
        double xValue = input.nextDouble();
        System.out.println(GradientDescent.hypothesis(xValue));
        System.out.println("\n Done, press enter.");
        System.in.read();
        System.exit(0);
    }

    // Partial derivative wrt Betha0
    public static double deBetha0() {
        double sum = 0;

        for (int i = 0; i < setA.length; i++) {
            sum += hypothesis(setA[i]) - setB[i];
        }
        return sum / setA.length;
    }

    // Partial derivative wrt Betha1
    public static double deBetha1() {
        double sum = 0;

        for (int i = 0; i < setA.length; i++) {
            sum += (hypothesis(setA[i]) - setB[i]) * setA[i];
        }
        return sum / setA.length;
    }

    // Calculate y = Betha0 + Betha1 * x
    public static double hypothesis(double x) {
        return b0 + b1 * x;
    }

}
