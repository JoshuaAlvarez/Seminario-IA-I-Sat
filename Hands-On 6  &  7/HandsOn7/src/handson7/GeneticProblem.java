/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handson7;

import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public interface GeneticProblem {
    ArrayList<State> initialPopulation(int size);

    double fitness(State s);

    State crossover(State s1,State s2);

    State mutate(State s);
    
}
