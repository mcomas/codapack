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
import coda.gui.utils.DataSelector;

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
import java.util.Vector;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

/**
 * EM_ZeroMissingMenu -> X numerica i positiva amb opci� de retornar text, crear dataframe, afegir variables i  mostrar grafics
 * @author Guest2
 */
public class ZeroMissingEMMenu extends AbstractMenuDialog{
    
    Rengine re;
    DataFrame df;
    JFrame frameEM_ZeroMissingMenu;
    JFrame[] framesEM_ZeroMissingMenu;
    JFileChooser chooser;
    String tempDirR;
    String[] tempsDirR;
    ArrayList<String> names;
    
    /* options var */
    
    JTextField DLProportion = new JTextField("0.65");
    double percentatgeDL = 0.65;
    
    JCheckBox robOption = new JCheckBox("Robust");
    String[] iniCovOptions = {"multRepl","complete.obs"};
    JComboBox<String> iniCovList = new JComboBox<String>(iniCovOptions);

    //JCheckBox performMax;
    //JLabel lmax = new JLabel("Use minimum on detection limit");    
    JTextField dlProportion = new JTextField(5);
    
    public static final long serialVersionUID = 1L;
    private static final String yamlUrl = CoDaPackConf.helpPath + "Irregular data.Logratio-EM Zero-Missing Replacement.yaml";
    private static final String helpTitle = "Logratio-EM zero & missing replacement Help Menu";
    
    public ZeroMissingEMMenu(final CoDaPackMain mainApp, Rengine r){
        super(mainApp, "Logratio-EM zero & missing replacement Menu", new DataSelector(mainApp.getActiveDataFrame(), false));
        super.setHelpMenuConfiguration(yamlUrl, helpTitle);
        re = r;
        
        /* options configuration */
        
        optionsPanel.add(robOption);
        JPanel covPanel = new JPanel();
        covPanel.add(new JLabel("IniCov"));
        covPanel.add(iniCovList);
        optionsPanel.add(covPanel);
        
        
        iniCovList.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                JComboBox comboBox = (JComboBox) event.getSource();
                Object selected = comboBox.getSelectedItem();
                if(selected.toString().equals("multRepl")){
                    dlProportion.setEnabled(true);
                }
                else{
                    dlProportion.setEnabled(false);
                }
            }
        });
        JPanel dlPanel = new JPanel();
        dlPanel.add(new JLabel("DL proportion"));  
        dlProportion.setText("0.65");
        dlProportion.setEnabled(true);
        dlPanel.add(dlProportion);
        optionsPanel.add(iniCovList);

        this.names = new ArrayList<String>(mainApplication.getActiveDataFrame().getNames());
        //performMax = new JCheckBox("Min result", false);
        //performMax.setSelected(true);
        //optionsPanel.add(lmax);
        //optionsPanel.add(performMax);
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        // Geting options
        String rob = "FALSE";
        if(robOption.isSelected()) rob = "TRUE";
        String iniCov = iniCovList.getSelectedItem().toString();
        String fracDL = dlProportion.getText();

        df = mainApplication.getActiveDataFrame();
        String[] sel_names = ds.getSelectedData();
        
        if(sel_names.length > 0){
            
            String[] new_names = new String[sel_names.length];
            for(int i=0; i < sel_names.length; i++){
                new_names[i] = "r_" + sel_names[i];
            }

            double data[][] = df.getNumericalData(sel_names);
            int numZeros = BasicStats.nZeros(data);
            int numNAs = BasicStats.nNaN(data);

            if(numZeros > 0 & numNAs > 0){
                for(int i=0; i < data.length; i++){
                    re.assign(sel_names[i], data[i]);
                }
                re.eval("X = cbind(" + String.join(",", sel_names) + ")");
                re.eval("X[is.nan(X)] = NA_real_");

                double dlevel[][] = df.getDetectionLevel(sel_names);                
                int numDlevel = BasicStats.nPositive(dlevel);

                if(numDlevel == numZeros){
                    for(int i=0; i < dlevel.length; i++){
                        re.assign(sel_names[i], dlevel[i]);
                    }
                    re.eval("DL = cbind(" + String.join(",", sel_names) + ")");

                    //re.eval("save.image('image.RData')");
                    String E = "RES = zCompositions::lrEMplus(X,dl=DL,rob=#ROB#,ini.cov='#INI.COV#',frac=#FRAC#)"
                               .replace("#FRAC#", fracDL)
                               .replace("#ROB#", rob)
                               .replace("#INI.COV#", iniCov);
                    System.out.println(E);
                    re.eval(E);
       
                    double resultat[][] = re.eval("t(as.matrix(RES))").asDoubleMatrix();
                    

                    df.addData(new_names, resultat);
                    mainApplication.updateDataFrame(df);
                    setVisible(false);


                }else{
                    JOptionPane.showMessageDialog(this, "Please set detection limit for all zeros");
                }
            
            } else{
                JOptionPane.showMessageDialog(this,"Please select a dataset with zeros and missing values");
            }
            
        }
        else{
            JOptionPane.showMessageDialog(null,"Please select data");
        }
    }

    public DataFrame getDataFrame(){
        return this.df;
    }
    
    public ArrayList<String> getDataFrameNames(){
        return this.names;
    }
    
}