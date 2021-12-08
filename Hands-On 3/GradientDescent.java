package HandsOn3;

public class GradientDescent{
    private double beta0;
    private double beta1;

    // Initialize variables
    private float alpha = 0.0003f;   // Learning rate
    private double tolerance = 1e-6; // If convergence is reached
    private int maxIter = 150000;   //  Maximum number of iterations 

    private double[] x;
    private double[] y;

    public GradientDescent(double intercept, double slope, double[] x, double[] y){
	//Starting values for coefficients set in this constructor
    	beta0 = intercept;
    	beta1 = slope;
        //X and Y axis parameters as DataSet
        this.x = x;
        this.y = y;
    }

    public void calculate(){
        int i = 0;
    	do {
            // Calculate and update the value of betas
            beta1 -= alpha * deriveBeta1();
            beta0 -= alpha * deriveBeta0();

            i++;
            
//            if (i % 10000 == 0){
//                System.out.println("Iteration n° " + i + "\nBeta0 = " + this.beta0 + " and Betha1 = " + this.beta1);
//            }

            if (i >= maxIter) break;
        } while (Math.abs(beta1) > tolerance || Math.abs(beta0) > tolerance);
//        System.out.println(i + " iterations made.");
        System.out.println("Beta0 = " + this.beta0 + ".   Beta1 = " + this.beta1);
        System.out.println("y = " + this.beta0 + "+ " + this.beta1 + "(x1)");
    }

    // Calculate y = Beta0 + Beta1 * x
    public double hypothesisFunction(double x){
    	return beta1*x + beta0;
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

