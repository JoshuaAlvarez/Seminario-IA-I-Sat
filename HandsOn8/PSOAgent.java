package AISeminar;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PSOAgent extends Agent {

  protected void setup() {
    System.out.println("Agent "+getLocalName()+" started.");
    addBehaviour(new PSOAction());
  }
  
  public class ParticleSwarmOptimization {

    private double[] gBest; //Global optimum position 
    private double gBestFitness = Double.MAX_VALUE; //Fitness of the global optimum position
    private int populationSize = 50; //Number of particles
    private int maxIter = 400; //Maximun number of iterations
    private int c1 = 2; //Acceleration factors
    private int c2 = 2;
    private double w = 0.9; //Inertia weight
    private List<Particle> swarm = new ArrayList<>(); //Particle swarm

//  Initialize population
    public void initialParticles() {
        for (int i = 0; i < populationSize; i++) {
            Particle particle = new Particle();
            particle.initialX();
            particle.initialV();
            particle.fitness = particle.calculateFitness();
            swarm.add(particle);
        }
    }

//  Update Global best
    public void updateGbest() {
        double fitness = Double.MAX_VALUE;
        int index = 0;
        for (int i = 0; i < populationSize; i++) {
            if (swarm.get(i).fitness < fitness) {
                index = i;
                fitness = swarm.get(i).fitness;
            }
        }
        if (fitness < gBestFitness) {
            gBest = swarm.get(index).pBest.clone();
            gBestFitness = fitness;
        }
    }
    
//  Update the velocity of each particle
    public void updateSwarmVelocity() {
        swarm.forEach((particle) -> {
            for (int i = 0; i < particle.dimension; i++) {
                double v = w * particle.V[i] + c1 * rand() * (particle.pBest[i] - particle.x[i]) + c2 * rand() * (gBest[i] - particle.x[i]);
                if (v > particle.Vmax)
                    v = particle.Vmax;
                else if (v < -particle.Vmax)
                    v = -particle.Vmax;
                particle.V[i] = v;//Updating Vi
            }
        });
    }
    
//  Update the position and pBest of each particle
    public void updateSwarmPosition() {
        swarm.stream().map((particle) -> {
            for (int i = 0; i < particle.dimension; i++) {
                particle.x[i] = particle.x[i] + particle.V[i];
            }
            return particle;
        }).forEachOrdered((particle) -> {
            double newFitness = particle.calculateFitness(); //New fitness
            //If the new fitness value is smaller than the original, update fitness and pBest
            if (newFitness < particle.fitness) {
                particle.pBest = particle.x.clone();
                particle.fitness = newFitness;
            }
        });
    }

    public void PSOprocess() {
        initialParticles();
        updateGbest();
        int i = 0;
        while(i++<maxIter) {
            updateSwarmVelocity();
            updateSwarmPosition();
            updateGbest();
            System.out.println(i+".gbest:("+gBest[0]+","+gBest[1]+")  fitness="+gBestFitness);
        }
    }

//  Random parameters within [0,1]
    public double rand() {
        return new Random().nextDouble();
    }

}
  
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

  private class PSOAction extends OneShotBehaviour {

    public void action() {
        ParticleSwarmOptimization opt = new ParticleSwarmOptimization();
        System.out.println("Particle Swarm Optimization");
        System.out.println("Estimating solution for SLR equation y = B0 + B1*x");
        opt.PSOprocess();
    } 
    
    public int onEnd() {
      myAgent.doDelete();   
      return super.onEnd();
    } 
  }    // END of inner class ...Behaviour
}
