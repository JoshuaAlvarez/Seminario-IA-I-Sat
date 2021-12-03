package HandsOn8;

import java.util.Random;

public class Particle {
    
    public int dimension = 2; //No. variables in the equation 

    public double[] x = new double[dimension]; //Particle position

    public double[] pBest = new double[dimension]; //Local best location

    public double[] V = new double[dimension]; //Particle velocity

    public double Vmax = 2;

    public double fitness;

    public double calculateFitness() {

        double b = x[0];
        double m = x[1];
        
        double[] advertising = {23, 26, 30, 34, 43, 48, 52, 57, 58};
        double[] sales = {651, 762, 856, 1063, 1190, 1298, 1421, 1440, 1518};
        
        double sum = 0;
        
        for(int i=0; i<advertising.length; i++) {
            sum += Math.pow((sales[i] - (b + m*advertising[i])),2);              
    	}

        return sum;
    }

    public void initialX() {
        for (int i = 0; i < dimension ; i++) {
            x[i] = new Random().nextInt(100);
            pBest[i] = x[i];
        }
    }

    public void initialV() {
        for (int i = 0; i < dimension ; i++) {
            double tmp = new Random().nextDouble();
            V[i] = tmp*4 + (-2);
        }
    }

}
