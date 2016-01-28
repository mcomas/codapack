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

package coda.gui.output;

import coda.BasicStats;
import coda.CoDaStats;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.TDistribution;
import org.apache.commons.math.distribution.TDistributionImpl;

/**
 *
 * @author marc
 */
public class OutputVariationArray implements OutputElement{
    String names[];
    boolean withTotalVariance = true;
    double var_array[][];
    double clr_var[];
    short var_color[][];

    double totalVariance = 0;
    
    public OutputVariationArray(String n[], double va[][], double[] clr_var, boolean withTotalVariance){
        names = new String[n.length];
        this.clr_var = clr_var;
        this.withTotalVariance = withTotalVariance;
        totalVariance = CoDaStats.totalVariance(va);
        System.arraycopy(n, 0, names, 0, n.length);

        var_array = new double[va.length][va[0].length];
        var_color = new short[va.length][va[0].length];
        //clr_var = new double[va.length];
        //Arrays.fill(clr_var, 0);

        for(int i=0;i<va.length;i++)
            System.arraycopy(va[i], 0, var_array[i], 0, va[i].length);
        int m = names.length*(names.length-1) / 2;
        double ln_var[] = new double[m];
        for(int i=0, k=0;i<names.length;i++){
            for(int j=i+1;j<names.length;j++){
                //clr_var[i] += var_array[i][j];
                //clr_var[j] += var_array[i][j];
                ln_var[k++]= Math.log(var_array[i][j]);
            }
        }
        //for(int i=0, k=0;i<names.length;i++){
        //    clr_var[i] /= ( 2*names.length);
        //}
        double mean = BasicStats.mean(ln_var);
        double sd = BasicStats.sd(ln_var);
        TDistribution t = new TDistributionImpl(m-1);
        double a1 = 0.25;
        double a2 = 0.05;
        try {
            for(int i=0, k=0;i<names.length;i++){
                for(int j=i+1;j<names.length;j++){
                    double lnvar = ln_var[k++];
                    var_color[i][j] = 0;
                    if (lnvar < mean + t.inverseCumulativeProbability(a1) * sd) {
                        // 25%
                        var_color[i][j] = -1;
                    }

                    if(lnvar < mean + t.inverseCumulativeProbability(a2) * sd){ //5%
                        var_color[i][j] = -2;
                    }
                    if(lnvar > mean + t.inverseCumulativeProbability(1-a1) * sd){ //75%
                        var_color[i][j] = 1;
                    }
                    if(lnvar > mean + t.inverseCumulativeProbability(1-a2) * sd){ //95%
                        var_color[i][j] = 2;
                    }
                }
            }
        } catch (MathException ex) {
                    Logger.getLogger(OutputVariationArray.class.getName()).log(Level.SEVERE, null, ex);
                }
    }

    public String printHTML(String html) {
        html += "<b>Variation array:</b><br>";
        html += "<table>";
        html += "<tr><td></td><td colspan='" + names.length + "' align='right'><b>Variance ln(Xi/Xj)</b></td></tr>";
        html += "<tr><td><b>Xi\\Xj</b></td>";
        for(int i=0;i<names.length;i++){
            html += "<th>" + names[i] + "</th>";
        }
        html += "<th align='center'>clr <br>variances</th><td></td></tr>";
        for(int i=0;i<var_array.length;i++){
            html += "<tr>" + "<th>" + names[i] + "</th>";
            for(int j=0;j<i;j++){
                html += "<td align='right'>" + decimalFormat.format(var_array[i][j]) + "</td>";
            }
            html += "<td align='right'></td>";
            for(int j=i+1;j<var_array[0].length;j++){
                double percent = var_array[i][j]/totalVariance;
                String color = "normal";
                if(var_color[i][j] == 1) color = "h1";
                if(var_color[i][j] == 2) color = "h2";
                if(var_color[i][j] ==-1) color = "l1";
                if(var_color[i][j] ==-2) color = "l2";
                //if(percent > 0.8) color = "a";
                html += "<td align='right' class='"+ color +"'>" + decimalFormat.format(var_array[i][j]) + "</td>";
            }
            html += "<td class='alt' align='right'>" + decimalFormat.format(clr_var[i]) + "</td><td></td>";
            html += "</tr>";
        }
        if(withTotalVariance)
            html += "<tr><td></td><td colspan='" + names.length + "' align='left'>"
                    + "<b>Mean ln(Xi/Xj)</b></td><td align='right'>" + decimalFormat.format(totalVariance) + "</td>" +
                    "<td><b>Total Variance</b></td><td></td></tr>";
        else
            html += "<tr><td></td><td colspan='" + names.length + "' align='left'><b>Mean ln(Xi/Xj)</b></td></tr>";

        html += "</table>";

        return html;
    }

    public void printText(Writer b) throws IOException{

        b.write("ILR binary partition\n");
        for(int i=0;i<names.length;i++){
            b.write(separator + names[i]);
        }
        b.write("\n");
        for(int i=0;i<var_array.length;i++){
            for(int j=0;j<i;j++){
                b.write(separator + decimalFormat.format(var_array[i][j]));
            }
            b.write(separator);
            for(int j=i+1;j<var_array[0].length;j++){
                b.write(separator + decimalFormat.format(var_array[i][j]));
            }
            b.write("\n");
        }
  
    }

}
