package JadeAgent;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import java.util.ArrayList;
import java.util.Random;
import javafx.util.Pair;


public class SolutionAgent extends Agent {

    protected void setup() {
        System.out.println("Agent " + getLocalName() + " started.");
        
        addBehaviour(new Main());
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
    //            double avgFitness = sumFitness / population.size();

                //Show info on generations
    //            System.out.println("Generation N°: " + k);
    //            System.out.println("Best Fitness: " + bestFitness + " AverageFitness: " + avgFitness + " Worst Fitness: " + worstFitness);

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

                //Mutation
                int number_of_mutations = (int) Math.floor(mutationRate * populationSize);
                for (int i = 0; i < number_of_mutations; i++) {
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
    
    public class EquationSolver implements GeneticAplication {
    private int a , b , c , d , e, f, r;
    private int limit;
    private int chromosomeLength = 6;
    
    //r = equation result, limit = max value for the variables (0 -> limit-1)
    public EquationSolver(int a,int b,int c,int d, int e, int f, int r,int limit){
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.r = r;
        this.limit = limit;
    }

    @Override
    public ArrayList<State> createInitialPopulation(int size) {
        ArrayList<State> result = new ArrayList<>();
        Random rnd = new Random();
        for (int i = 0; i < size; i++) {
            EquationSolverState res = new EquationSolverState();
            for (int j = 0; j < chromosomeLength; j++) {
                res.cof[j] = rnd.nextInt(limit);
            }
            result.add(res);
        }
        return result;
    }

    @Override
    public double getFitness(State s) {
        EquationSolverState mes = (EquationSolverState)s;
        double val = (a*mes.cof[0] + b*mes.cof[1] + c*mes.cof[2] + d*mes.cof[3] + e*mes.cof[4] + f*mes.cof[5]);
//        System.out.println("val = " + val);
        return 100 - Math.abs(val - 30);
    }

    @Override
    public State crossover(State s1, State s2) {
        EquationSolverState mes1 = (EquationSolverState)s1;
        EquationSolverState mes2 = (EquationSolverState)s2;
        EquationSolverState result = new EquationSolverState();

        Random rnd = new Random();
        int coffIndex = rnd.nextInt(5) + 1;

        for (int i = 0; i < coffIndex; i++) {
            result.cof[i] = mes1.cof[i];
        }
        for (int i = coffIndex; i < 4 ; i++) {
            result.cof[i] = mes2.cof[i];
        }

        return result;
    }

    @Override
    public State mutate(State s) {
        EquationSolverState mes = (EquationSolverState)s;
        EquationSolverState result = new EquationSolverState();

        Random rnd = new Random();
        int mindex = rnd.nextInt(chromosomeLength);

        for (int i = 0; i < chromosomeLength; i++ ) {
            if(i != mindex)
                result.cof[i] = mes.cof[i];
            else
                result.cof[i] = rnd.nextInt(limit);
        }

        return result;
    }
}

class EquationSolverState implements State {

    public int cof[];

    public EquationSolverState(){
        cof = new int[6];
    }

    @Override
    public boolean isEquals(State s) {
        return false;
    }

    @Override
    public String toString() {
        return "a = " + cof[0] + "\nb = " + cof[1] +  "\nc = " + cof[2] +  "\nd = "  + cof[3] +  "\ne = "  + cof[4] +  "\nf = "  + cof[5] ;
    }
}

public interface GeneticAplication {
    ArrayList<State> createInitialPopulation(int size);

    double getFitness(State s);

    State crossover(State s1,State s2);

    State mutate(State s);
    
}

public interface State {
    public boolean isEquals(State s);
    
}
    
    private class Main extends OneShotBehaviour {
        
        public void action() {
            //EquationSolver(x1, x2, x3, x4, x5, x6, r, limit) arguments are x = coefficients, r = equality, 
        //limit = max value for the variables (0 -> limit-1)
        EquationSolver EqS = new EquationSolver(1,2,-3,1,4,1,30,11);
        //Arguments are GeneticAlgorithm(populationSize, crossoverRate, mutationRate)
        GeneticAlghorithm GA = new GeneticAlghorithm(20,0.05,0.01);
        
        System.out.println("\nEquation is a + 2b - 3c + d + 4e + f = 30\n" );
        
        //Arguments are GA.solve(EqS, MaxIterations)
        GA.solve(EqS,10000);
        
        System.out.println(GA.finalState.toString());
        System.out.println("Final Fitness : " + EqS.getFitness(GA.finalState));

        } 

        public int onEnd() {
            myAgent.doDelete();   
            return super.onEnd();
        } 
    }
  }   
