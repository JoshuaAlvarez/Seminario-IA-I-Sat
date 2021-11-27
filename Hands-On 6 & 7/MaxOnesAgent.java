package HandsOn6;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class MaxOnesAgent extends Agent {

    protected void setup() {
        System.out.println("Agent " + getLocalName() + " started.");
        
        addBehaviour(new Main());
    }

    public class GA_MAXONES {
        
    public GA_MAXONES(int popsize, int stringsize){
        geneticAlgorithm(popsize, stringsize);
    }
	
    //Function generates random number
    private int random(int min, int max){
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    //Randomize the chromosomes in the strings
    private ArrayList<Integer> generateChromosomesString(int stringsize){
        ArrayList<Integer> string = new ArrayList<Integer>();
        for(int i = 0; i < stringsize; i++){
            if(random(0,1) == 0){
                string.add(0);
            }
            else{
                string.add(1);
            }
        }
        return string;
    }

    //Generating a random population of binary strings
    private ArrayList<ArrayList<Integer>> generateRandPopulation(int populationSize, int numberOfChromosomes){
        ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();

        for(int i = 0; i < populationSize; i++){
            list.add(generateChromosomesString(numberOfChromosomes));
        }

        return list;
    }

    //Determine fittest of two binary string
    private int individualSelection(ArrayList<ArrayList<Integer>> pop){
        int rand1 = random(0,pop.size()-1);
        System.out.println("Tournament Player 1: " + pop.get(rand1) + " Fitness: " + calculateFitness(pop.get(rand1)));
        int rand2 = random(0, pop.size()-1);
        System.out.println("Tournament Player 2: " + pop.get(rand2) + " Fitness: " + calculateFitness(pop.get(rand2)));

        if(calculateFitness(pop.get(rand1)) >= calculateFitness(pop.get(rand2))){
            System.out.println("\tTournament Winner: " + pop.get(rand1));
            return rand1;
        }
        else{
            System.out.println("\tTournament Winner: " + pop.get(rand2));
            return rand2;
        }
    }

    //Obtains the fitness value of a binary string
    private int calculateFitness(ArrayList<Integer> string){
        int fitness = 0;
        for(int i = 0; i < string.size(); i++){
            if(string.get(i) == 1){
                fitness++;
            }
        }
        return fitness;
    }

    //Evaluate population's fitness
    private boolean evaluateFitness(ArrayList<ArrayList<Integer>> population){
        for(int i = 0; i < population.size(); i++){
            if(calculateFitness(population.get(i)) == population.get(i).size()){
                return true;
            }
        }
        return false;
    }

    //Produces offspring
    private ArrayList<ArrayList<Integer>> produceOffspring(ArrayList<Integer> x, ArrayList<Integer> y){
        System.out.println("\t\tParent 1: " + x);
        System.out.println("\t\tParent 2: " + y);
        ArrayList<ArrayList<Integer>> children = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> child1 = new ArrayList<Integer>();
        ArrayList<Integer> child2 = new ArrayList<Integer>();
        
        int crossoverRate = 60; //Set to 60% chance
        
        if(random(0,100) <= crossoverRate){
            //Creates offspring from selection of parents 1 and 2
            for(int i = 0; i < x.size(); i++){
                if(random(0,1) == 1){
                    child1.add(x.get(i));
                    child2.add(y.get(i));
                }
                else{
                    child1.add(y.get(i));
                    child2.add(x.get(i));
                }
            }
        }
        //Or else the individual gets to the next generation
        else{
            child1 = x;
            child2 = y;
        }

        children.add(child1);
        children.add(child2);

        System.out.println("\t\t\tChild 1: " + child1);
        System.out.println("\t\t\tChild 2: " + child2);
        for(int i = 0; i < children.size(); i++){
            System.out.print("\t\tChild " + i + ": ");
            for(int j = 0; j < children.get(i).size(); j++){
                System.out.print(children.get(i).get(j));
                if(random(0,100) <= (1/child1.size()*100)){
                    if(children.get(i).get(j) == 1){
                        children.get(i).set(j, 0);
                    }
                    else{
                        children.get(i).set(j, 1);
                    }

                }
            }
            System.out.print("\n");
        }
        return children;
    }

    private void displayStatistics(ArrayList<ArrayList<Integer>> pop){
        double average = 0;
        double best = 0;
        double worst = calculateFitness(pop.get(0));

        for(int i = 0; i < pop.size(); i++){
            int fitness = calculateFitness(pop.get(i));
            average += fitness;
            if(fitness > best){
                best = fitness;
            }
            if(fitness < worst){
                worst = fitness;
            }
        }
        average = average/pop.size();

        System.out.println("\tGeneration Statistics: ");
        System.out.println("\tFittest: " + best);
        System.out.println("\tUnfittest: " + worst);
        System.out.println("\tAverage Fitness: " + average);
    }

    //Keeps fittest individuals
    private ArrayList<ArrayList<Integer>> keepMostFit(ArrayList<ArrayList<Integer>> oldpop, ArrayList<ArrayList<Integer>> newpop){
        int mostfit = 0;
        int worst = 0;

        for(int i = 0; i < oldpop.size(); i++){
            if(calculateFitness(oldpop.get(i)) > calculateFitness(oldpop.get(mostfit))){
                mostfit = i;
            }
        }

        for(int i = 0; i < newpop.size(); i++){
            if(calculateFitness(newpop.get(i)) < calculateFitness(newpop.get(worst))){
                worst = i;
            }
        }
        System.out.println("Fittest Parent: " + oldpop.get(mostfit));
        newpop.set(worst, oldpop.get(mostfit));
        return newpop;
    }

    //Genetic Algorithm
    private boolean geneticAlgorithm(int popsize, int stringsize){
        int num = 0;
        
        ArrayList<ArrayList<Integer>> pop = generateRandPopulation(popsize, stringsize);
        displayStatistics(pop);
        
        while(evaluateFitness(pop) != true){
            
            System.out.println("\nStarting Generation: " + num);
            ArrayList<ArrayList<Integer>> newpop = new ArrayList<ArrayList<Integer>>();
            
            for(int i = 0; i < pop.size()/2; i++){
                int x = individualSelection(pop);
                int y = individualSelection(pop);

                ArrayList<ArrayList<Integer>> children = produceOffspring(pop.get(x), pop.get(y));
                
                for(int j = 0; j < children.size(); j++){
                    newpop.add(children.get(j));
                }
            }

            pop = keepMostFit(pop,newpop);
            displayStatistics(pop);
            num++;
        }
        return true;
    }
    
}
    
    private class Main extends OneShotBehaviour {
        
        public void action() {
            Scanner input = new Scanner(System.in);
            //Input population size and number of chromosomes
            int populationSize;
            int numberOfChromosomes;
            System.out.println("The MAXONE Problem");
            System.out.println("Population Size: ");
            populationSize = input.nextInt();
            input.nextLine();

            System.out.println("Number of chromosomes per individual: ");
            numberOfChromosomes = input.nextInt();
            input.nextLine();
            input.close();

            new GA_MAXONES(populationSize,numberOfChromosomes);

        } 

        public int onEnd() {
            myAgent.doDelete();   
            return super.onEnd();
        } 
    }
  }   
