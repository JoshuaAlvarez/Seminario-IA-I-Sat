package handson7;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Usuario
 */
public class GeneticAlghorithm {
    private int populationSize;
    private double elitismValue;
    private double crossoverRate;
    private double mutationRate;

    public GeneticAlghorithm(int populationSize, double elitismValue, double crossoverRate , double mutationRate){
        this.populationSize = populationSize;
        this.elitismValue = elitismValue;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
    }

    public State finalState;

    public void solve(GeneticProblem gp,int generations){

        //initialization
        ArrayList<State> population = gp.initialPopulation(populationSize);

        int k = 0;
        while(k < generations) {

            //evaluate
            double sumFitness = 0;
            double bestFitness = 0;
            double worstFitness = Double.MAX_VALUE;
            ArrayList<Pair<State, Double>> populationFitness = new ArrayList<>();
            for (State p : population) {
                double fitness = gp.fitness(p);
                populationFitness.add(new Pair<>(p, fitness));
                sumFitness += fitness;
                if(fitness < worstFitness) worstFitness = fitness;
                if(fitness > bestFitness) bestFitness = fitness;
            }
            double avgFitness = sumFitness / population.size();

            //Printing generations data
            System.out.println("Generation N°: " + k);
            System.out.println("Best Fitness: " + bestFitness + " AverageFitness: " + avgFitness + " Worst Fitness: " + worstFitness);

            //Calculate  probability for Roulette Wheel Selection
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

            //Add Parents
            for (State s : selectedPopulation) newPopulation.add(s);

            //Selection for crossover
            ArrayList<State> selectedForCrossover = new ArrayList<>();
            for (int i = 0; i < populationSize; i++) {
                if (rnd.nextDouble() < crossoverRate) {
                    selectedForCrossover.add(selectedPopulation.get(i));
                }
            }
            //Crossover
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

            //update population
            population = newPopulation;
            k++;
        }

        //after k iterations return answer
        double bestFitness = Double.MIN_VALUE;
        State bestState = null;

        for(State s : population){
            if(bestState == null || gp.fitness(s) < bestFitness){
                bestState = s;
                bestFitness = gp.fitness(s);
            }
        }

        finalState = bestState;
        System.out.println("[GA] Final State Reached !");
    }
    
}
