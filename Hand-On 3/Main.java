package HandsOn3;

import java.util.Scanner;

public class Main {
	public static void main(String[] args ){
            
            double[] x = {23, 26, 30, 34, 43, 48, 52, 57, 58};
            double[] y = {651, 762, 856, 1063, 1190, 1298, 1421, 1440, 1518};
                
            GradientDescent gradientDescent = new GradientDescent(0, 0, x, y);//Parameters are beta0, beta1, dataset x axis and y axis
            gradientDescent.calculate();
            System.out.println("Enter 'X' value to predict 'Y': ");
            Scanner input = new Scanner(System.in);
            double xValue = input.nextDouble();
            System.out.println(gradientDescent.hypothesisFunction(xValue));
            System.out.println("\nDone, exiting program");
            System.exit(0);
	}
}
