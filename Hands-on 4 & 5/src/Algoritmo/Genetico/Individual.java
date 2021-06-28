package Algoritmo.Genetico;

import java.util.Arrays;
import java.util.Random;

public class Individual implements Cloneable{

	private int geneLength;
	private int fitness = 0;
	private int[] genes;

	public Individual(int geneLength) {
		
		this.geneLength = geneLength;
		this.genes = new int[geneLength];
		
		Random rn = new Random();
	
		for (int i = 0; i < genes.length; i++) {
			genes[i] = Math.abs(rn.nextInt() % 2);
		}
	
		fitness = 0;
	}

	public void calcFitness() {
		fitness = 0;
		for (int i = 0; i < genes.length; i++) {
			if (genes[i] == 1) {
				++fitness;
			}
		}
	}
  
	@Override
	protected Object clone() throws CloneNotSupportedException {
		Individual individual = (Individual)super.clone();
		individual.genes = new int[geneLength];
		for(int i = 0; i < individual.genes.length; i++){
			individual.genes[i] = this.genes[i];
		}
		return individual;
	}

	@Override
	public String toString() {
		return "[genes=" + Arrays.toString(genes) + "]";
	}
  
	public int getGeneLength() {
		return geneLength;
	}

	public void setGeneLength(int geneLength) {
		this.geneLength = geneLength;
	}

	public int getFitness() {
		return fitness;
	}

	public void setFitness(int fitness) {
		this.fitness = fitness;
	}

	public int[] getGenes() {
		return genes;
	}

	public void setGenes(int[] genes) {
		this.genes = genes;
	}

}