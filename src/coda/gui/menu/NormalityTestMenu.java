/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.gui.menu;

import coda.CoDaStats;
import coda.DataFrame;
import coda.gui.CoDaPackMain;
import coda.gui.output.OutputElement;
import coda.gui.output.OutputNormalityTest;
import coda.gui.output.OutputText;
import java.util.ArrayList;

/**
 *
 * @author mcomas
 */
public class NormalityTestMenu extends AbstractMenuDialog{

    public NormalityTestMenu(final CoDaPackMain mainApp){
        super(mainApp, "Additive Logistic Normality Test", false);

        

    }
    @Override
    public void acceptButtonActionPerformed() {
        String selectedNames[] = ds.getSelectedData();
        DataFrame df = mainApplication.getActiveDataFrame();

        boolean[] selection = getValidComposition(df, selectedNames);
        int [] mapping = df.getMapingToData(selectedNames, selection);
        double[][] data = df.getNumericalData(selectedNames, mapping);

        double[][] aln = CoDaStats.transformRawALR(data);
        String names[] = new String[selectedNames.length-1];
        for(int i=0;i<names.length;i++)
            names[i] = "alr(" + selectedNames[i] + "," + selectedNames[names.length] + ")";

        int d = aln.length;
        int n = aln[0].length;
        double marginal[][] = new double[d][3];
        double bivariate[][][] = new double[d][d][3];


        for(int i=0;i<d;i++)
            marginal[i] = CoDaStats.marginalUnivariateTest(aln[i]);

        for(int i=0;i<d;i++)
            for(int j=i+1;j<d;j++)
                bivariate[i][j] = CoDaStats.bivariateAngleTest(aln[i], aln[j]);

        double radius[] = CoDaStats.radiusTest(aln);

        ArrayList<OutputElement> outputs = new ArrayList<OutputElement>();
        outputs.add(new OutputNormalityTest(names, marginal, bivariate, radius));

        CoDaPackMain.outputPanel.addOutput(outputs);
        setVisible(false);
    }

}
