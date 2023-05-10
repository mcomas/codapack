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
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

/**
 * BayesianMultRepMenu -> X numerica i positiva amb opcio de retornar text, crear dataframe, afegir variables i  mostrar grafics
 * @author Guest2
 */
public class BayesianMultRepMenu extends AbstractMenuDialog{
    
    Rengine re;
    DataFrame df;
    JFrame frameBayesianMultRepMenu;
    JFrame[] framesBayesianMultRepMenu;
    JFileChooser chooser;
    String tempDirR;
    String[] tempsDirR;
    ArrayList<String> names;
    
    /* options var */
    
    String[] P1Options = {"GBM","SQ","BL","CZM"};
    String[] P2Options = {"prop","p-counts"};
    
    JComboBox<String> P1ComboOptions = new JComboBox<String>(P1Options);
    JComboBox<String> P2ComboOptions = new JComboBox<String>(P2Options);
    
    
    public static final long serialVersionUID = 1L;
    private static final String yamlUrl = CoDaPackConf.helpPath + "Irregular data.Bayesian-multiplicative zero replacement.yaml";
    private static final String helpTitle = "Bayesian Multiplicative zero Replacement Help Menu";
    
    public BayesianMultRepMenu(final CoDaPackMain mainApp, Rengine r){
        super(mainApp, "Bayesian Multiplicative zero Replacement Menu", false);
        super.setHelpMenuConfiguration(yamlUrl, helpTitle);
        re = r;
        
        /* options configuration */
        JPanel methodPanel = new JPanel();
        methodPanel.add(new JLabel("Method: "));
        methodPanel.add(P1ComboOptions);
        JPanel resultPanel = new JPanel();
        resultPanel.add(new JLabel("Output: "));
        resultPanel.add(P2ComboOptions);
        
        this.optionsPanel.add(methodPanel);
        this.optionsPanel.add(resultPanel);
        this.names = new ArrayList<String>(mainApplication.getActiveDataFrame().getNames());
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        // Geting options
        String method = P1ComboOptions.getSelectedItem().toString();
        String resultFormat = P2ComboOptions.getSelectedItem().toString();

        df = mainApplication.getActiveDataFrame();
        String[] sel_names = ds.getSelectedData();
        
        
        if(sel_names.length >= 2){
            
            String[] new_names = new String[sel_names.length];
            for(int i=0; i < sel_names.length; i++){
                new_names[i] = "z_" + sel_names[i];
            }

            double data[][] = df.getNumericalData(sel_names);
            int numZeros = BasicStats.nZeros(data);

            if(numZeros > 0){
                for(int i=0; i < data.length; i++){
                    re.assign(sel_names[i], data[i]);
                }
                re.eval("X = cbind(" + String.join(",", sel_names) + ")");

                //re.eval("save.image('image.RData')");
                String E = "RES = zCompositions::cmultRepl(X,method='#METHOD#', output='#OUTPUT#')"
                            .replace("#METHOD#", method)
                            .replace("#OUTPUT#", resultFormat);
                System.out.println(E);
                re.eval(E);


                double resultat[][] = re.eval("t(as.matrix(RES))").asDoubleMatrix();
                

                df.addData(new_names, resultat);
                mainApplication.updateDataFrame(df);
                setVisible(false);

            }else{
                JOptionPane.showMessageDialog(this,"Please select a variable that contains some 0");
            }
            
            
        }else{
            JOptionPane.showMessageDialog(null,"Please select minimum two variables");
        }
    }
 
    public DataFrame getDataFrame() {
        return this.df;
    }
    
    public ArrayList<String> getDataFrameNames(){
        return this.names;
    }
    
    
}