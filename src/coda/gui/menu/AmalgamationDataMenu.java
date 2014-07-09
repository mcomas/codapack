package coda.gui.menu;

import coda.CoDaStats;
import coda.DataFrame;
import coda.gui.CoDaPackMain;
import java.util.Arrays;
import javax.swing.JOptionPane;


/**
 *
 * @author mcomas
 */
public class AmalgamationDataMenu extends AbstractMenuDialog{
    public static final long serialVersionUID = 1L;
    String selected[];
    //JTextField amalgameWith;
    //JLabel text1 = new JLabel("Combination");
    public AmalgamationDataMenu(final CoDaPackMain mainApp){
        super(mainApp, "Amalgamation Menu", false);
        //amalgameWith =  new JTextField("1.0 1.0 1.0", 14);
        //perturbateWith.setText();
        //optionsPanel.add(text1);
        //optionsPanel.add(amalgameWith);
    }

    @Override
    public void acceptButtonActionPerformed() {
        //Comprobation that closuredTo is a double value is needed
        try{
            String selectedNames[] = ds.getSelectedData();
            DataFrame dataFrame = mainApplication.getActiveDataFrame();

            //String[] v = amalgameWith.getText().split(" ");

            double [] combination = new double[selectedNames.length];

            Arrays.fill(combination,1);            

            // = CoDaPack.center(df.getNumericalDataZeroFree(sel_names));

            //boolean selection[] = dataFrame.getValidCompositions(selectedNames);
            double[][] data = dataFrame.getNumericalData(selectedNames);
            //double[][] vdata = coda.Utils.reduceData(data, selection);

            String[] new_name = new String[1];
            new_name[0] = "amalg";
            for(String v : selectedNames){
                new_name[0] += '_' + v;
            }
            //double[][] pData = CoDaStats.amalgamateData(vdata,combination);
            //dataFrame.addData(new_name, coda.Utils.recoverData(pData,selection));
            double[][] pData = CoDaStats.amalgamateData(data,combination);
            dataFrame.addData(new_name, pData); //coda.Utils.recoverData(pData,selection));

            mainApplication.updateDataFrame(dataFrame);
            setVisible(false);
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Combination value must be a double");
        }
        
    }
}

