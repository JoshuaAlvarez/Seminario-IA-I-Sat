package AISeminar;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javafx.util.Pair;

public class GeneticAgent extends Agent {

  protected void setup() {
    System.out.println("Agent "+getLocalName()+" started.");
    addBehaviour(new Main());
  } 
  
  public class GradientDescent implements GeneticAplication {
    private double beta0;
    private double beta1;
    private float  alpha = 0.0003f;   // Learning rate

    private double[] x;
    private double[] y;
    
    public GradientDescent(double intercept, double slope, double[] x, double[] y){
        //Starting values for beta0 and beta1 
    	beta0 = intercept;
    	beta1 = slope;
        //X and Y axis parameters are the DataSet
        this.x = x;
        this.y = y;
    }

    @Override
    public ArrayList<State> createInitialPopulation(int size) {
        ArrayList<State> result = new ArrayList<>();
        Random rnd = new Random();
        for (int i = 0; i < size; i++) {
            GDState gds = new GDState();
            for (int j = 0; j < 2; j++) {
                gds.coefficient[j] = rnd.nextDouble() * 200;
            }
            result.add(gds);
        }
        return result;
    }

    @Override
    public double getFitness(State s) {
        GDState GDGA = (GDState)s;
        
        beta0 -= alpha * deriveBeta0();
        beta1 -= alpha * deriveBeta1();
        GDGA.coefficient[0] = beta0;
        GDGA.coefficient[1] = beta1;
        
        //The loss function
        double sum = 0;    
        for(int i=0; i<x.length; i++) {
            sum += Math.pow((y[i] - (GDGA.coefficient[1] + GDGA.coefficient[0]*x[i])),2);
                
    	}
        
//        System.out.println("Fitness = " + sum / x.length);
//        System.out.println("Beta0 = " + GDGA.coefficient[0] + "\nBeta1 = " + GDGA.coefficient[1]);

        return sum / x.length;
    }
    
    // y = Beta0 + Beta1 * x
    public double hypothesis(double x){
    	return beta0 + beta1*x;
    }

    // Partial derivative wrt Beta0
    public double deriveBeta0(){
    	double sum = 0;

    	for (int j=0; j<x.length; j++) {
    		sum += y[j] - hypothesis(x[j]);
    	}
    	return -2 * sum / x.length;

    }
    
    // Partial derivative wrt Beta1
    public double deriveBeta1(){
    	double sum = 0;

    	for (int j=0; j<x.length; j++){
    		sum += (y[j] - hypothesis(x[j])) * x[j];
    	}
    	return -2 * sum / x.length;
    }

    @Override
    public State crossover(State s1, State s2) {
        GDState gds1 = (GDState)s1;
        GDState gds2 = (GDState)s2;
        GDState result = new GDState();

        Random rnd = new Random();
        int coffIndex = rnd.nextInt(2) + 1;

        for (int i = 0; i < coffIndex; i++) {
            result.coefficient[i] = gds1.coefficient[i];
        }
        for (int i = coffIndex; i < coffIndex ; i++) {
            result.coefficient[i] = gds2.coefficient[i];
        }

        return result;
    }

    @Override
    public State mutate(State s) {
        GDState mes = (GDState)s;
        GDState result = new GDState();

        Random rnd = new Random();
        int mindex = rnd.nextInt(2);

        for (int i = 0; i < 2; i++ ) {
            if(i != mindex)
                result.coefficient[i] = mes.coefficient[i];
            else
                result.coefficient[i] = rnd.nextInt(200);
        }

        return result;
    }
}

class GDState implements State {

    public double[] coefficient;

    public GDState(){
        coefficient = new double[2];
    }

    @Override
    public boolean isEquals(State s) {
        return false;
    }

    @Override
    public String toString() {
        return "Beta0 = " + coefficient[0] + "\nBeta1 = " + coefficient[1];
    }
}

  public interface State {
    public boolean isEquals(State s);
    
}

  public interface GeneticAplication {
    ArrayList<State> createInitialPopulation(int size);

    double getFitness(State s);

    State crossover(State s1,State s2);

    State mutate(State s);
    
}
  
  public class GeneticAlghorithm {
    private int populationSize;
    private double crossoverRate;
    private double mutationRate;

    public GeneticAlghorithm(int populationSize, double crossoverRate , double mutationRate){
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
    }

    public State finalState;

    public void solve(GeneticAplication gp,int generations){

        //Initialize population
        ArrayList<State> population = gp.createInitialPopulation(populationSize);

        int k = 0;
        while(k < generations) {

            //Evaluate getFitness
            double sumFitness = 0;
            double bestFitness = 0;
            double worstFitness = Double.MAX_VALUE;
            ArrayList<Pair<State, Double>> populationFitness = new ArrayList<>();
            for (State p : population) {
                double fitness = gp.getFitness(p);
                populationFitness.add(new Pair<>(p, fitness));
                sumFitness += fitness;
                if(fitness < worstFitness) worstFitness = fitness;
                if(fitness > bestFitness) bestFitness = fitness;
            }
            double avgFitness = sumFitness / population.size();

            //Show info on generations
            //System.out.println("Generation N°: " + k);
            //System.out.println("Best Fitness: " + bestFitness + " AverageFitness: " + avgFitness + " Worst Fitness: " + worstFitness);

            //Calculate probability for Roulette Wheel Selection
            double cumulativeP[] = new double[population.size() + 1];
            cumulativeP[0] = 0;
            double cumulativeS = 0;
            for (int i = 1; i <= populationFitness.size(); i++) {
                double p = populationFitness.get(i - 1).getValue() / sumFitness;
                cumulativeS += p;
                cumulativeP[i] = cumulativeS;
            }


            //Roulette Wheel Selection
            ArrayList<State> selectedPopulation = new ArrayList<>();
            Random rnd = new Random();
            while (selectedPopulation.size() < populationSize) {
                double r = rnd.nextDouble();
                for (int i = 0; i <= populationSize; i++) {
                    if (r < cumulativeP[i]) {
                        //Select element
                        selectedPopulation.add(populationFitness.get(i - 1).getKey());
                        break;
                    }
                }
            }

            ArrayList<State> newPopulation = new ArrayList<>();

            //Parents selection
            for (State s : selectedPopulation) newPopulation.add(s);

            //Selection for crossover
            ArrayList<State> selectedForCrossover = new ArrayList<>();
            for (int i = 0; i < populationSize; i++) {
                if (rnd.nextDouble() < crossoverRate) {
                    selectedForCrossover.add(selectedPopulation.get(i));
                }
            }
            //Recombination
            if (selectedForCrossover.size() >= 2) {
                for (int i = 0; i < selectedForCrossover.size(); i++) {
                    for (int j = i + 1; j < selectedForCrossover.size(); j++) {
                        newPopulation.add(gp.crossover(selectedForCrossover.get(i), selectedForCrossover.get(j)));
                    }
                }
            }

            int mutationsNumber = (int) Math.floor(mutationRate * populationSize);
            for (int i = 0; i < mutationsNumber; i++) {
                int mut_index = rnd.nextInt(populationSize);
                newPopulation.add(gp.mutate(selectedPopulation.get(mut_index)));
            }

            //Update population
            population = newPopulation;
            k++;//termination condition
        }

        double bestFitness = Double.MIN_VALUE;
        State bestState = null;

        for(State s : population){
            if(bestState == null || gp.getFitness(s) < bestFitness){
                bestState = s;
                bestFitness = gp.getFitness(s);
            }
        }

        finalState = bestState;
        System.out.println("Final result: ");
    }
    
}

  private class Main extends OneShotBehaviour {

    public void action() {
        double[] x = {23, 26, 30, 34, 43, 48, 52, 57, 58};
        double[] y = {651, 762, 856, 1063, 1190, 1298, 1421, 1440, 1518};
        
        //Parameters are beta0, beta1, dataset x axis and y axis
        GradientDescent gradient = new GradientDescent(0, 0, x, y);
        
        //Arguments are GeneticAlgorithm(populationSize, crossoverRate, mutationRate)
        GeneticAlghorithm ga = new GeneticAlghorithm(25,0.05,0.01);
        
        //Arguments are GA.solve(EqS, MaxIterations)
        ga.solve(gradient,6000);
        
        System.out.println(ga.finalState.toString());
        System.out.println("Final Fitness : " + gradient.getFitness(ga.finalState));


        System.out.println("Enter 'x' value to predict Sales: ");
        Scanner input = new Scanner(System.in);
        double X = input.nextDouble();
        System.out.println(gradient.hypothesis(X));
        System.out.println("Done.");
        System.exit(0);
    } 
    
    public int onEnd() {
      myAgent.doDelete();   
      return super.onEnd();
    } 
  }
}
