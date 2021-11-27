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
public class MathematicalEqualityProblem implements GeneticProblem {
    private int a , b , c , d , e, f, r;
    private int limits;
    private int chromosomeLength = 6;

    public MathematicalEqualityProblem(int a,int b,int c,int d, int e, int f, int r,int limits){
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.r = r;
        this.limits = limits;
    }

    @Override
    public ArrayList<State> initialPopulation(int size) {
        ArrayList<State> result = new ArrayList<>();
        Random rnd = new Random();
        for (int i = 0; i < size; i++) {
            MathematicalEqualityState mes = new MathematicalEqualityState();
            for (int j = 0; j < chromosomeLength; j++) {
                mes.C[j] = rnd.nextInt(limits);
            }
            result.add(mes);
        }
        return result;
    }

    @Override
    public double fitness(State s) {
        MathematicalEqualityState mes = (MathematicalEqualityState)s;
        double val = (a*mes.C[0] + b*mes.C[1] + c*mes.C[2] + d*mes.C[3] + e*mes.C[4] + f*mes.C[5]) - r;
        return 1 / (1 + val);
    }

    @Override
    public State crossover(State s1, State s2) {
        MathematicalEqualityState mes1 = (MathematicalEqualityState)s1;
        MathematicalEqualityState mes2 = (MathematicalEqualityState)s2;
        MathematicalEqualityState result = new MathematicalEqualityState();

        Random rnd = new Random();
        int cindex = rnd.nextInt(3) + 1;

        for (int i = 0; i < cindex; i++) {
            result.C[i] = mes1.C[i];
        }
        for (int i = cindex; i < 4 ; i++) {
            result.C[i] = mes2.C[i];
        }

        return result;
    }

    @Override
    public State mutate(State s) {
        MathematicalEqualityState mes = (MathematicalEqualityState)s;
        MathematicalEqualityState result = new MathematicalEqualityState();

        Random rnd = new Random();
        int mindex = rnd.nextInt(chromosomeLength);

        for (int i = 0; i < chromosomeLength; i++ ) {
            if(i != mindex)
                result.C[i] = mes.C[i];
            else
                result.C[i] = rnd.nextInt(limits);
        }

        return result;
    }
}

class MathematicalEqualityState implements State {

    public int C[];

    public MathematicalEqualityState(){
        C = new int[6];
    }

    @Override
    public boolean isEquals(State s) {
        return false;
    }

    @Override
    public String toString() {
        return C[0] + " | " + C[1] +  " | " + C[2] +  " | "  + C[3] +  " | "  + C[4] +  " | "  + C[5] ;
    }
}