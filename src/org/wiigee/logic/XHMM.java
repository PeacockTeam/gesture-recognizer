/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.wiigee.logic;

import java.util.Vector;

/**
 *
 * @author bepo
 */
public class XHMM extends HMM {

    // the temporal values for scaling
    int[] currSequence;
    double[][] currForward;
    double[][] scaledForward;
    double[][] currBackward;
    double[][] scaledBackward;
    double[] currScaling;
    double[][] currHelper;

    public XHMM(int numStates, int numObservations) {
        super(numStates, numObservations);
    }
    
    @Override
    public void train(Vector<int[]> trainsequence) {
        
    }

    private double[][] getScaledForward(int[] sequence) {
        double[][] fwd = this.forwardProc(sequence);
        double[][] retVal = new double[fwd.length][fwd[0].length];
        for(int t=0; t<fwd.length; t++) {
            for(int i=0; i<fwd[0].length; i++) {

                // build sum
                double sum = 0.0;
                for(int n=0; n<this.numStates; n++) {
                    sum += fwd[t][n];
                }

                retVal[t][i] = fwd[t][i] / sum;
            }
        }
        return retVal;
    }

    private double[][] getScaledBackward(int[] sequence) {
        double[][] fwd = this.forwardProc(sequence);
        double[][] bwd = this.backwardProc(sequence);
        double[][] retVal = new double[bwd.length][bwd[0].length];
        for(int t=0; t<bwd.length; t++) {
            for(int i=0; i<bwd[0].length; i++) {

                // build sum
                double sum = 0.0;
                for(int n=0; n<this.numStates; n++) {
                    sum += fwd[t][n];
                }

                retVal[t][i] = bwd[t][i] / sum;
            }
        }
        return retVal;
    }

   private double getScalingDenominator(int t, int[] sequence) {
       double retVal = 0.0;
       double[][] fwd = this.forwardProc(sequence);
       double[][] sfwd = this.getScaledForward(sequence);
       double[][] helper = new double[sfwd.length][sfwd[0].length];

       if(t==0) {
           
       }


       return retVal;
   }

}
