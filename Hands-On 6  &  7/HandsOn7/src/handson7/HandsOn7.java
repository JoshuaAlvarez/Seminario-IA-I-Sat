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
    
}
