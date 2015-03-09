package coda.gui.menu;

import coda.CoDaStats;
import coda.DataFrame;
import coda.gui.CoDaPackMain;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author mcomas
 */
public class ZeroReplacementMenu extends AbstractMenuDialog{
    public static final long serialVersionUID = 1L;
    JTextField closure = new JTextField("1.0");
    double percentatgeDL = 0.65;
    JTextField usedPercentatgeDL;
    JLabel l_usedPercentatgeDL = new JLabel("DL proportion");
    JCheckBox performClosure;
    JLabel lclosure = new JLabel("Closure to");
    JTextField closureTo;
    public ZeroReplacementMenu(final CoDaPackMain mainApp){
        super(mainApp, "Zero Replacement Menu", false);
        
        usedPercentatgeDL =  new JTextField(5);
        usedPercentatgeDL.setText("0.65");
        optionsPanel.add(l_usedPercentatgeDL);
        optionsPanel.add(usedPercentatgeDL);
        
        
        performClosure = new JCheckBox("Closure result", false);
        performClosure.setSelected(true);
        performClosure.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(performClosure.isSelected()){
                    closureTo.setEnabled(true);
                }else{
                    closureTo.setEnabled(false);
                }
            }
        });
        closureTo =  new JTextField(5);
        closureTo.setText("1.0");
        
        optionsPanel.add(performClosure);
        optionsPanel.add(lclosure);
        optionsPanel.add(closureTo);
    }
    @Override
    public void acceptButtonActionPerformed() {
        
        percentatgeDL = Double.parseDouble(usedPercentatgeDL.getText());
        
        
        DataFrame df = mainApplication.getActiveDataFrame();
        String[] sel_names = ds.getSelectedData();
        int m = sel_names.length;
        String[] new_names = new String[m];
        for(int i=0;i<m;i++) new_names[i] = "z_" + sel_names[i];        
        double data[][] = df.getNumericalData(sel_names);
        double dlevel[][] = df.getDetectionLevel(sel_names);
        double[][] d = CoDaStats.roundedZeroReplacement(data, dlevel, percentatgeDL);
        if(performClosure.isSelected()){
            double vclosureTo = Double.parseDouble(closureTo.getText());
            df.addData(new_names, CoDaStats.closure(d, vclosureTo));
        }else{
            df.addData(new_names, d);
        }
        
        mainApplication.updateDataFrame(df);
       
        setVisible(false);
    }
}

