package coda.gui.menu;

import coda.CoDaStats;
import coda.DataFrame;
import coda.gui.CoDaPackMain;
import javax.swing.JTextField;

/**
 *
 * @author mcomas
 */
public class ZeroReplacementMenu extends AbstractMenuDialog{
    public static final long serialVersionUID = 1L;
    JTextField closure = new JTextField("1.0");
    double percentatgeDL = 0.65;
    public ZeroReplacementMenu(final CoDaPackMain mainApp){
        super(mainApp, "Zero Replacement Menu", false);
    }
    @Override
    public void acceptButtonActionPerformed() {
        
        DataFrame df = mainApplication.getActiveDataFrame();
        String[] sel_names = ds.getSelectedData();
        int m = sel_names.length;
        String[] new_names = new String[m];
        for(int i=0;i<m;i++) new_names[i] = "z_" + sel_names[i];        
        double data[][] = df.getNumericalData(sel_names);
        double dlevel[][] = df.getDetectionLevel(sel_names);
        df.addData(new_names, CoDaStats.roundedZeroReplacement(data, dlevel, percentatgeDL));
        mainApplication.updateDataFrame(df);
       
        setVisible(false);
    }
}

