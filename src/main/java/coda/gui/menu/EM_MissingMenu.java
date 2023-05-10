/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.gui.menu;

import coda.BasicStats;
import coda.DataFrame;
import coda.Variable;
import coda.gui.CoDaPackConf;
import coda.gui.CoDaPackMain;
import static coda.gui.CoDaPackMain.outputPanel;
import coda.gui.output.OutputElement;
import coda.gui.output.OutputForR;
import coda.gui.output.OutputText;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

/**
 * EM_MissingMenu -> X numerica i positiva amb opci� de retornar text, crear dataframe, afegir variables i  mostrar grafics
 * @author Guest2
 */
public class EM_MissingMenu extends AbstractMenuDialog{
    
    Rengine re;
    DataFrame df;
    JFrame frameEM_MissingMenu;
    JFrame[] framesEM_MissingMenu;
    JFileChooser chooser;
    String tempDirR;
    String[] tempsDirR;
    ArrayList<String> names;
    
    /* options var */
    
    JCheckBox B1 = new JCheckBox("Robust");
    //JTextField P1 = new JTextField(10);
    
    
    public static final long serialVersionUID = 1L;
    private static final String yamlUrl = CoDaPackConf.helpPath + "Irregular data.Logratio-EM missing Replacement.yaml";
    private static final String helpTitle = "Logratio-EM missing replacement Help Menu";
    
    public EM_MissingMenu(final CoDaPackMain mainApp, Rengine r){
        super(mainApp, "Logratio-EM missing replacement Menu", false);
        super.setHelpMenuConfiguration(yamlUrl, helpTitle);
        re = r;
        
        /* options configuration */
        
        this.optionsPanel.add(B1);
        this.names = new ArrayList<String>(mainApplication.getActiveDataFrame().getNames());
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        String rob = "FALSE";
        if(B1.isSelected()) rob = "TRUE";

        df = mainApplication.getActiveDataFrame();        
        String sel_names[] = super.ds.getSelectedData();
        
        if(sel_names.length > 0){
            
            String[] new_names = new String[sel_names.length];
            for(int i=0; i < sel_names.length; i++){
                new_names[i] = "m_" + sel_names[i];
            }

            double data[][] = df.getNumericalData(sel_names);
            for(int i=0; i < data.length; i++){
                
                System.out.println(Arrays.toString(data[i]));
            }
            int numNAs = BasicStats.nNaN(data);

            if(numNAs > 0){
                for(int i=0; i < data.length; i++){
                    re.assign(sel_names[i], data[i]);
                }
                re.eval("X = cbind(" + String.join(",", sel_names) + ")");               
                
                String E = "RES = zCompositions::lrEM(X,label=NaN,imp.missing=TRUE,ini.cov='complete.obs',rob=#ROB#)".replace("#ROB#", rob);
                System.out.println(E);
                re.eval(E);


                double resultat[][] = re.eval("t(as.matrix(RES))").asDoubleMatrix();
                

                df.addData(new_names, resultat);
                mainApplication.updateDataFrame(df);
                setVisible(false);

            }else{
                JOptionPane.showMessageDialog(this,"Please select a variable that contains missing values");
            }

            
            
        }
        else{
            JOptionPane.showMessageDialog(null,"Please select at least two columns");
        }
    }
    

    public DataFrame getDataFrame() {
        return this.df;
    }
    
    public ArrayList<String> getDataFrameNames(){
        return this.names;
    }
    
}