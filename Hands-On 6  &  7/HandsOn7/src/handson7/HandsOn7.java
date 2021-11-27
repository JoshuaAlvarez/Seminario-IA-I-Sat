/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handson7;

/**
 *
 * @author Usuario
 */
public class HandsOn7 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MathematicalEqualityProblem ME = new MathematicalEqualityProblem(1,2,3,1,4,1,30,30);
        GeneticAlghorithm GA = new GeneticAlghorithm(25,0.0,0.95,0.01);
        GA.solve(ME,10000);
        System.out.println(GA.finalState.toString());
        System.out.println("Fitness : " + ME.fitness(GA.finalState));
    }
    
}
