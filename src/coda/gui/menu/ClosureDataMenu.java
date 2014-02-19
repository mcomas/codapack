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
public class ClosureDataMenu extends AbstractMenuDialog{
    public static final long serialVersionUID = 1L;
    String selected[];
    JTextField closureTo;
    JLabel text1 = new JLabel("Closure");
    public ClosureDataMenu(final CoDaPackMain mainApp){
        super(mainApp, "Closure Data Menu", false);
        closureTo =  new JTextField(5);
        closureTo.setText("1.0");
        optionsPanel.add(text1);
        optionsPanel.add(closureTo);
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

            double closure = Double.parseDouble(closureTo.getText());
            if(closure < 0){
                JOptionPane.showMessageDialog(this, "Closure value must be positive");
                return;
            }
            String[] new_names = new String[selectedNames.length];
            for(int i=0;i<selectedNames.length;i++) new_names[i] = "clo(" + selectedNames[i] + ")";

            // = CoDaPack.center(df.getNumericalDataZeroFree(sel_names));
            double[][] pdata = CoDaStats.closure(vdata,closure);

            dataFrame.addData(new_names, coda.Utils.recoverData(pdata, selection));
            mainApplication.updateDataFrame(dataFrame);
            setVisible(false);
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Closure value must be a real number");
        }
    }
}

