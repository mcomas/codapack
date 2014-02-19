/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.gui.output;

import coda.CoDaStats;
import java.io.IOException;
import java.io.Writer;

/**
 *
 * @author mcomas
 */
public class OutputNormalityTest implements OutputElement{
    String[] names;
    double[][] marginals;
    double[][][] bivariates;
    double[] radius;

    public OutputNormalityTest(String names[], double[][] marginals, double[][][] bivariates, double[] radius){
        this.names = names;
        this.marginals = marginals;
        this.bivariates = bivariates;
        this.radius = radius;
    }
    public String printHTML(String html) {
        html += "<b>Normality Tests:</b><br><table>" +
                "<tr><th></th><th colspan='2' align='center'>Anderson-Darling</th>" +
                "<th colspan='2' align='center'>Cramer-von Mises</th>" +
                "<th colspan='2' align='center'>Watson</th></tr>"+
                "<tr><th></th><th align='center'>A<sup>2</sup>*</th><th align='center'>p</th>" +
                "<th align='center'>W<sup>2</sup>*</th><th align='center'>p</th>" +
                "<th align='center'>U<sup>2</sup>*</th><th align='center'>p</th></tr>"+
                "<tr><td class='l1'></td><td colspan='6' align='center' class='l1'>Marginals</td></tr>";
        String pValues[] = null;
        for(int i=0;i<names.length;i++){
            pValues = CoDaStats.pValueUniformEmpirical(marginals[i]);
            html += "<tr><th align='right'>" + names[i] + "</th>";
            html += "<td>" + decimalFormat.format(marginals[i][0]) + "</td>";
            html += "<td class='alt' align='center' > " + pValues[0] + "</td>";
            html += "<td>" + decimalFormat.format(marginals[i][1]) + "</td>";
            html += "<td class='alt' align='center' > " + pValues[1] + "</td>";
            html += "<td>" + decimalFormat.format(marginals[i][2]) + "</td>";
            html += "<td class='alt' align='center' > " + pValues[2] + "</td>";
            html += "</tr>";
        }
        html += "<tr><td class='l1'></td><td colspan='6' align='center' class='l1'>Bivariate angle</td></tr>";
        for(int i=0;i<names.length;i++){
            for(int j=i+1;j<names.length;j++){
                pValues = CoDaStats.pValueChiSquareEmmpirical(bivariates[i][j]);
                html += "<tr><th align='right'>" + names[i] + "<br>" + names[j] + "</th>";
                html += "<td>" + decimalFormat.format(bivariates[i][j][0]) + "</td>";
                html += "<td class='alt' align='center' > " + pValues[0] + "</td>";
                html += "<td>" + decimalFormat.format(bivariates[i][j][1]) + "</td>";
                html += "<td class='alt' align='center' > " + pValues[1] + "</td>";
                html += "<td>" + decimalFormat.format(bivariates[i][j][2]) + "</td>";
                html += "<td class='alt' align='center' > " + pValues[2] + "</td>";
                html += "</tr>";
            }
        }
        html += "<tr><td class='l1'></td><td colspan='6' align='center' class='l1'>Radius</td></tr>";
        html += "<tr><th></th>";
        pValues = CoDaStats.pValueChiSquareEmmpirical(radius);
        html += "<td>" + decimalFormat.format(radius[0]) + "</td>";
        html += "<td class='alt' align='center' > " + pValues[0] + "</td>";
        html += "<td>" + decimalFormat.format(radius[1]) + "</td>";
        html += "<td class='alt' align='center' > " + pValues[1] + "</td>";
        html += "<td>" + decimalFormat.format(radius[2]) + "</td>";
        html += "<td class='alt' align='center' > " + pValues[2] + "</td>";
        html += "</tr>";
        html += "</table>";

        return html;
    }

    public void printText(Writer b) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
