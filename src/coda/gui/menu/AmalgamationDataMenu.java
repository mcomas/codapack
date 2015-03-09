package coda.gui.menu;

import coda.CoDaStats;
import coda.DataFrame;
import coda.Variable;
import coda.Zero;
import coda.gui.CoDaPackMain;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


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

            boolean selection[] = dataFrame.getValidCompositionsWithZeros(selectedNames);
            
            double dlevel[][] = dataFrame.getDetectionLevel(selectedNames);
            double[][] data = dataFrame.getNumericalData(selectedNames);
            
            double[][] vdata = coda.Utils.reduceData(data, selection);

            String[] new_name = new String[1];
            new_name[0] = "amalg";
            for(String v : selectedNames){
                new_name[0] += '_' + v;
            }

            double[][] d = CoDaStats.amalgamateData(data,combination);
 
            Variable var = new Variable(new_name[0], d[0]);
            for(int j=0;j<data[0].length;j++){
                if(data[0][j] == 0){
                    boolean dl_valid = false;
                    double dl = 0;
                    for(int i=0;i<data.length;i++){
                        dl_valid = (dlevel[i][j] > 0);
                        dl += dlevel[i][j];
                    }
                    if(dl_valid){
                        var.set(j, new Zero(dl));
                    }
                }
            }
            dataFrame.addData(new_name[0], var);

            mainApplication.updateDataFrame(dataFrame);
            setVisible(false);
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Combination value must be a double");
        }
        
    }
}

