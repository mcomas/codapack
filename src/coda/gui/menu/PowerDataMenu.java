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
public class PowerDataMenu extends AbstractMenuDialog{
    public static final long serialVersionUID = 1L;
    String selected[];
    JTextField powerWith;
    JLabel text1 = new JLabel("Power");
    public PowerDataMenu(final CoDaPackMain mainApp){
        super(mainApp, "Power Data Menu", false);
        powerWith =  new JTextField(5);
        powerWith.setText("1.0");
        optionsPanel.add(text1);
        optionsPanel.add(powerWith);
    }

    @Override
    public void acceptButtonActionPerformed() {
        //Comprobation that closuredTo is a double value is needed
        try{

            String selectedNames[] = ds.getSelectedData();
            DataFrame dataFrame = mainApplication.getActiveDataFrame();

            boolean selection[] = dataFrame.getValidCompositions(selectedNames);
            double[][]data = dataFrame.getNumericalData(selectedNames);
            double[][]vdata = coda.Utils.reduceData(data, selection);

            double power = Double.parseDouble(powerWith.getText());

            String[] new_names = new String[selectedNames.length];
            for(int i=0;i<selectedNames.length;i++) new_names[i] = selectedNames[i] + "^" + Double.toString(power);

            // = CoDaPack.center(df.getNumericalDataZeroFree(sel_names));
            double[][] pdata = CoDaStats.closure(
                    CoDaStats.powerData(vdata,power), 1);

            dataFrame.addData(new_names, coda.Utils.recoverData(pdata, selection));
            mainApplication.updateDataFrame(dataFrame);
            setVisible(false);
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Power value must be a double");
        }
        
    }
}

