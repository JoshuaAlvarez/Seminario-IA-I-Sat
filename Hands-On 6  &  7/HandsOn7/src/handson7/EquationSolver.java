/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handson7;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Usuario
 */
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