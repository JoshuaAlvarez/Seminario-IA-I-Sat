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
