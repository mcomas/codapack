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
public class TransformationALRMenu extends AbstractMenuDialog{
    public static final long serialVersionUID = 1L;
    JRadioButton ra;
    JRadioButton ar;
    JTextField closure = new JTextField("1.0");

    public TransformationALRMenu(final CoDaPackMain mainApp){
        super(mainApp, "ALR Transform Menu", false);

        JPanel opt = new JPanel();
        ra = new JRadioButton("Raw-ALR");
        ra.setSelected(true);
        ar = new JRadioButton("ALR-Raw");
        ButtonGroup group = new ButtonGroup();
        group.add(ar);
        group.add(ra);

        opt.add(ra);
        opt.add(ar);
        optionsPanel.add(opt);
    }
    @Override
    public void acceptButtonActionPerformed() {
        if(ra.isSelected()){
            DataFrame df = mainApplication.getActiveDataFrame();
            String[] sel_names = ds.getSelectedData();
            int m = sel_names.length-1;
            String[] new_names = new String[m];
            for(int i=0;i<m;i++) new_names[i] = "alr." + sel_names[i] + "_" + sel_names[m];
            boolean selection[] = df.getValidCompositions(sel_names);
            double data[][] = df.getNumericalData(sel_names);
            double vdata[][] = coda.Utils.reduceData(data, selection);

            double alr[][] = CoDaStats.transformRawALR(vdata);
            df.addData(new_names, coda.Utils.recoverData(alr, selection));
            mainApplication.updateDataFrame(df);
        }else{
            DataFrame df = mainApplication.getActiveDataFrame();
            String[] sel_names = ds.getSelectedData();
            int m = sel_names.length+1;
            String[] new_names = new String[m];
            for(int i=0;i<m;i++) new_names[i] = "inv.alr." + (i+1);
            df.addData(new_names, CoDaStats.transformALRRaw(df.getNumericalData(sel_names)));
            mainApplication.updateDataFrame(df);
        }
        setVisible(false);
    }
}

