package coda.gui.menu;

import coda.CoDaStats;
import coda.DataFrame;
import coda.gui.CoDaPackMain;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author mcomas
 */
public class TransformationCLRMenu extends AbstractMenuDialog{
    public static final long serialVersionUID = 1L;
    JRadioButton rc;
    JRadioButton cr;
    JTextField closure = new JTextField("1.0");

    public TransformationCLRMenu(final CoDaPackMain mainApp){
        super(mainApp, "CLR Transform Menu", false);

        JPanel opt = new JPanel();
        rc = new JRadioButton("Raw-CLR");
        rc.setSelected(true);
        cr = new JRadioButton("CLR-Raw");
        ButtonGroup group = new ButtonGroup();
        group.add(cr);
        group.add(rc);

        opt.add(rc);
        opt.add(cr);
        optionsPanel.add(opt);
    }

    @Override
    public void acceptButtonActionPerformed() {
        if(rc.isSelected()){
            DataFrame df = mainApplication.getActiveDataFrame();
            String[] sel_names = ds.getSelectedData();
            int m = sel_names.length;
            String[] new_names = new String[m];
            for(int i=0;i<m;i++) new_names[i] = "clr." + sel_names[i];
            boolean selection[] = df.getValidCompositions(sel_names);
            double data[][] = df.getNumericalData(sel_names);
            double vdata[][] = coda.Utils.reduceData(data, selection);

            double clr[][] = CoDaStats.transformRawCLR(vdata);
            df.addData(new_names, coda.Utils.recoverData(clr, selection));
            mainApplication.updateDataFrame(df);
        }else{
            DataFrame df = mainApplication.getActiveDataFrame();
            String[] sel_names = ds.getSelectedData();
            int m = sel_names.length;
            String[] new_names = new String[m];
            for(int i=0;i<m;i++) new_names[i] = "inv.clr." + (i+1);
            df.addData(new_names, CoDaStats.closure(
                    CoDaStats.transformCLRRaw(df.getNumericalData(sel_names)), 1));
            mainApplication.updateDataFrame(df);
        }
        setVisible(false);
    }
}

