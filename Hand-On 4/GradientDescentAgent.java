package HandsOn4;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import java.io.IOException;
import java.util.Scanner;

public class GradientDescentAgent extends Agent {

    protected void setup() {
        System.out.println("Agent " + getLocalName() + " started.");
        
        addBehaviour(new Main());
    }

    public class GradientDescent {
        
        private double beta0;
    private double beta1;

    // Initialize variables
    private float alpha = 0.0003f;   // Learning rate
    private double tolerance = 1e-6; // If convergence is reached
    private int maxIter = 600000;   //  Maximum number of iterations 

    private double[] x;
    private double[] y;

    public GradientDescent(double intercept, double slope, double[] x, double[] y){
	//Starting values for coefficients set in this constructor
    	this.beta0 = intercept;
    	this.beta1 = slope;
        //X and Y axis parameters as DataSet
        this.x = x;
        this.y = y;
    }

    public void calculate(){
        int i = 0;
    	do {
            // Calculate and update the value of betas
            this.beta1 -= alpha * deriveBeta1();
            this.beta0 -= alpha * deriveBeta0();

            i++;
            
//            if (i % 10000 == 0){
//                System.out.println("Iteration n° " + i + "\nBeta0 = " + this.beta0 + " and Beta1 = " + this.beta1);
//            }

            if (i >= maxIter) break;
        } while (Math.abs(beta1) > tolerance || Math.abs(beta0) > tolerance);
//        System.out.println(i + " iterations made.");
        System.out.println("Beta0 = " + this.beta0 + ".   Beta1 = " + this.beta1);
    }

    // Calculate y = Beta0 + Beta1 * x
    public double hypothesisFunction(double x){
    	return this.beta1*x + beta0;
    }
    
    // Partial derivative wrt Beta0
    public double deriveBeta0(){
    	double sum = 0;

    	for (int j=0; j<x.length; j++) {
    		sum += y[j] - hypothesisFunction(x[j]);
    	}
    	return -2 * sum / x.length;

    }
    
    // Partial derivative wrt Beta1
    public double deriveBeta1(){
    	double sum = 0;

    	for (int j=0; j<x.length; j++){
    		sum += (y[j] - hypothesisFunction(x[j])) * x[j];
    	}
    	return -2 * sum / x.length;
    }
    
}
    
    private class Main extends OneShotBehaviour {
        
        public void action() {
            double[] x = {23, 26, 30, 34, 43, 48, 52, 57, 58};
            double[] y = {651, 762, 856, 1063, 1190, 1298, 1421, 1440, 1518};
            //Parameters are beta0, beta1, dataset x axis and y axis
            GradientDescent gradientDescent = new GradientDescent(0, 0, x, y);
            gradientDescent.calculate();
            System.out.println("Enter 'X' value to predict 'Y': ");
            Scanner input = new Scanner(System.in);
            double xValue = input.nextDouble();
            System.out.println(gradientDescent.hypothesisFunction(xValue));
            System.out.println("\nDone, exiting program");
            System.exit(0);

        } 

        public int onEnd() {
            myAgent.doDelete();   
            return super.onEnd();
        } 
    }
  }   
