package coda.gui.menu;

import coda.CoDaStats;
import coda.DataFrame;
import coda.gui.CoDaPackMain;
import coda.gui.output.OutputVector;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;


/**
 *
 * @author mcomas
 */
public class CenterDataMenu extends AbstractMenuDialog{
    public static final long serialVersionUID = 1L;
    String selected[];
    JCheckBox centerCheck;
    public CenterDataMenu(final CoDaPackMain mainApp){
        super(mainApp, "Center Data Menu", false);
        centerCheck = new JCheckBox("Show Center", true);
        optionsPanel.add(centerCheck);
    }

    @Override
    public void acceptButtonActionPerformed() {
        //Comprobation that closuredTo is a double value is needed
        try{
            
            String selectedNames[] = ds.getSelectedData();
            DataFrame dataFrame = mainApplication.getActiveDataFrame();

            boolean selection[] = dataFrame.getValidCompositions(selectedNames);
            double[][] data = dataFrame.getNumericalData(selectedNames);
            double[][] vData = coda.Utils.reduceData(data, selection);
            
            double [] center = CoDaStats.center(vData);
                         
            if(centerCheck.isSelected()){
                //mainApplication.outputPanel.writeCenter(selectedNames, center);
                //CoDaPackMain.outputPanel.writeCenter(selectedNames, center);
                CoDaPackMain.outputPanel.addOutput(
                        new OutputVector("Center", selectedNames, center));
            }
            for(int i=0;i<center.length;i++) center[i] = 1 / center[i];

            String[] new_names = new String[selectedNames.length];
            for(int i=0; i<selectedNames.length;i++)
                new_names[i] = "c_" + selectedNames[i];

            double [][] cData = CoDaStats.closure(
                    CoDaStats.perturbateData(vData, center),1);
            dataFrame.addData(new_names, 
                    coda.Utils.recoverData(cData, selection));
            mainApplication.updateDataFrame(dataFrame);
            setVisible(false);
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Closured value must be a double");
        }
        
    }
}

