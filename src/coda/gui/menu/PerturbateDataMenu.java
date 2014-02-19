package coda.gui.menu;

import coda.CoDaStats;
import coda.DataFrame;
import coda.gui.CoDaPackMain;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


/**
 *
 * @author mcomas
 */
public class PerturbateDataMenu extends AbstractMenuDialog{
    public static final long serialVersionUID = 1L;
    String selected[];
    JTextField perturbateWith;
    JLabel text1 = new JLabel("Perturbation");
    public PerturbateDataMenu(final CoDaPackMain mainApp){
        super(mainApp, "Perturbate Data Menu", false);
        perturbateWith =  new JTextField("1.0 1.0 1.0", 14);
        //perturbateWith.setText();
        optionsPanel.add(text1);
        optionsPanel.add(perturbateWith);
    }

    @Override
    public void acceptButtonActionPerformed() {
        //Comprobation that closuredTo is a double value is needed
        try{
            String selectedNames[] = ds.getSelectedData();
            DataFrame dataFrame = mainApplication.getActiveDataFrame();

            String[] v = perturbateWith.getText().split(" ");

            double [] perturbation = new double[v.length];

            for(int i=0;i<v.length;i++) perturbation[i] = Double.parseDouble(v[i]);
            

            // = CoDaPack.center(df.getNumericalDataZeroFree(sel_names));

            boolean selection[] = dataFrame.getValidCompositions(selectedNames);
            double[][] data = dataFrame.getNumericalData(selectedNames);
            double[][] vdata = coda.Utils.reduceData(data, selection);

            String[] new_names = new String[selectedNames.length];
            for(int i=0;i<selectedNames.length;i++) new_names[i] = Double.toString(perturbation[i]) + ".x." + selectedNames[i];

            double[][] pData = CoDaStats.closure(
                    CoDaStats.perturbateData(vdata,perturbation), 1);
            dataFrame.addData(new_names, coda.Utils.recoverData(pData,selection));
            
            mainApplication.updateDataFrame(dataFrame);
            setVisible(false);
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Closured value must be a double");
        }
        
    }
}

