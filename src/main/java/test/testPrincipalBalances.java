/**	
 *	Copyright 2011-2016 Marc Comas - Santiago Thió
 *
 *	This file is part of CoDaPack.
 *
 *  CoDaPack is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CoDaPack is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CoDaPack.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import java.util.HashMap;

/**
 *
 * @author mcomas
 */
public class testPrincipalBalances {
    static HashMap<int[], Double> gmeans = new HashMap<int[], Double>();
    static double f(double val){
        return Math.sin(val);
    }
    static void generate_combos(int n, int k) {
        int com[] = new int[100];
        for (int i = 0; i < k; i++) com[i] = i;
        while (com[k - 1] < n) {
            gmeans.put(com, f(k));
            for (int i = 0; i < k; i++)
                System.out.print(com[i] + " ");
            System.out.print("\n");

            int t = k - 1;
            while (t != 0 && com[t] == n - k + t) t--;
            com[t]++;
            for (int i = t + 1; i < k; i++) com[i] = com[i - 1] + 1;
        }
    }
    public static void main(String args[]){
        
        
        int M = 20;
        int L[] = new int[M+1];
        long start = System.nanoTime();
        
        L[0] = 1;
        for(int i=0;i<M;i++)
            L[i+1] = L[i] * 2;

        for(int i=0;i<L[M];i++){
            String vec = "";
            for(int j=0;j<M;j++){
                vec += i % L[j+1] < L[j] ? "1" : "0";
            }
            //gmeans.put(vec, f(i));
        }
         
        long end = System.nanoTime();

        System.out.println("Time:" + (double)(end-start)/(double)1000000000);
    }
}
