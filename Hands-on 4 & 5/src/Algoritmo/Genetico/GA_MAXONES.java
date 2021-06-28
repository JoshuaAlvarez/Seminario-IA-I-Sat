package Algoritmo.Genetico;

import java.util.Random;

public class GA_MAXONES {

	private Population population;
	private Individual fittest;
	private Individual secondFittest;
	private int generationCount;
	private static int numberOfGenes;
	private static int numberOfIndividuals;
 
	public GA_MAXONES() {
		this.population = new Population(numberOfIndividuals, numberOfGenes);
		this.generationCount = 0;
	}

	public static void main(String[] args) {
    	
		Random rn = new Random();

		numberOfGenes = 20;

		numberOfIndividuals = 5;

		GA_MAXONES demo = new GA_MAXONES();
        
		demo.population = new Population(numberOfIndividuals, numberOfGenes);
        
		System.out.println("Population of "+demo.population.getPopSize()+" individual(s).");

		demo.population.calculateFitness();

		System.out.println("\nGeneration: " + demo.generationCount + " Fittest: " + demo.population.getFittestScore());
                
		showGeneticPool(demo.population.getIndividuals());

		while (demo.population.getFittestScore() < numberOfGenes) {
			++demo.generationCount;

			demo.selection();

			demo.crossover();
                        
			if (rn.nextInt()%7 < 5) {
				demo.mutation();
			}

			demo.addFittestOffspring();

			demo.population.calculateFitness();

			System.out.println("\nGeneration: " + demo.generationCount + " Fittest score: " + demo.population.getFittestScore());
            
			showGeneticPool(demo.population.getIndividuals());
		}

		System.out.println("\nSolution found in generation " + demo.generationCount);
		System.out.println("Index of winner Individual: "+demo.population.getFittestIndex());
		System.out.println("Fitness: "+demo.population.getFittestScore());
		System.out.print("Genes: ");
		for (int i = 0; i < numberOfGenes; i++) {
			System.out.print(demo.population.selectFittest().getGenes()[i]);
		}

		System.out.println("");

	}

	void selection() {

		fittest = population.selectFittest();

		secondFittest = population.selectSecondFittest();
	}

	void crossover() {
		Random rn = new Random();

		int crossOverPoint = rn.nextInt(this.numberOfGenes);

		for (int i = 0; i < crossOverPoint; i++) {
			int temp = fittest.getGenes()[i];
			fittest.getGenes()[i] = secondFittest.getGenes()[i];
			secondFittest.getGenes()[i] = temp;

		}

	}

	void mutation() {
		Random rn = new Random();

		int mutationPoint = rn.nextInt(this.numberOfGenes);

		if (fittest.getGenes()[mutationPoint] == 0) {
			fittest.getGenes()[mutationPoint] = 1;
		} else {
			fittest.getGenes()[mutationPoint] = 0;
		}

		mutationPoint = rn.nextInt(this.numberOfGenes);

		if (secondFittest.getGenes()[mutationPoint] == 0) {
			secondFittest.getGenes()[mutationPoint] = 1;
		} else {
			secondFittest.getGenes()[mutationPoint] = 0;
		}
	}

	Individual getFittestOffspring() {
		if (fittest.getFitness() > secondFittest.getFitness()) {
			return fittest;
		}
		return secondFittest;
	}


	void addFittestOffspring() {

		fittest.calcFitness();
		secondFittest.calcFitness();

		int leastFittestIndex = population.getLeastFittestIndex();

		population.getIndividuals()[leastFittestIndex] = getFittestOffspring();
	}
    
	static void showGeneticPool(Individual[] individuals) {
            
		System.out.println("==Genetic Pool==");
		int increment=0;
		for (Individual individual:individuals) {
			System.out.println("> Individual  "+increment+" || "+(individual.toString())+" ||");
			increment++;
		}
		System.out.println("================");
	}

}