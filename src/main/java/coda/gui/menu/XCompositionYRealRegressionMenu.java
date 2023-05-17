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
import coda.gui.utils.DataSelector1to2;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import org.apache.batik.swing.JSVGCanvas;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

/**
 * LM2 -> X numerica i positiva i Y numerica amb opci� de retornar text, crear dataframe, afegir variables i  mostrar grafics
 * @author Guest2
 */
public class XCompositionYRealRegressionMenu extends AbstractMenuRBasedDialog{
    
    DataFrame df;
    JFrame frameLM2;
    Vector<JFrame> framesLM2;
    JFileChooser chooser;
    String tempDirR;
    Vector<String> tempsDirR;
    ILRMenu ilrX;
    ArrayList<String> names;
    
    /* options var */
    
    JRadioButton B1 = new JRadioButton("Residuals");
    JRadioButton B2 = new JRadioButton("Fitted");
    
    public static final long serialVersionUID = 1L;
    private static final String yamlUrl = CoDaPackConf.helpPath + "Statistics.Multivariate Analysis.Regression.X Composition Y Real.yaml";
    private static final String helpTitle = "X composition Y real regression Help Menu";
    
    public XCompositionYRealRegressionMenu(final CoDaPackMain mainApp, Rengine r){
        super(mainApp,"X composition Y real regression Menu", new DataSelector1to2(mainApp.getActiveDataFrame(), false), r);
        script_file = "Regression_X_Composition_Y_Real.R";

        super.setHelpMenuConfiguration(yamlUrl, helpTitle);
    
        ((DataSelector1to2)ds).setTextA("Compositional explanatory");
        ((DataSelector1to2)ds).setTextB("Real response");
        
        //super.setSelectedDataNames("Selected X:", "Selected Y:");
        
        framesLM2 = new Vector<JFrame>();
        tempsDirR = new Vector<String>();
        
        /* options configuration */
        
        JButton xILR = new JButton("Set X partition");
        this.optionsPanel.add(xILR);
        xILR.addActionListener(new java.awt.event.ActionListener(){
        
            public void actionPerformed(java.awt.event.ActionEvent evt){
               configureILRX();
            }
        });
        
        this.optionsPanel.add(B1);
        this.optionsPanel.add(B2);
        this.names = new ArrayList<String>(mainApplication.getActiveDataFrame().getNames());
    }
    
    public void configureILRX(){
        DataSelector1to2 ds_ = (DataSelector1to2)ds;
        if(this.ilrX == null || this.ilrX.getDsLength() != ds_.getSelectedDataA().length) this.ilrX = new ILRMenu(ds_.getSelectedDataA());
        this.ilrX.setVisible(true);
    }

    @Override
    public void acceptButtonActionPerformed(){
        df = mainApplication.getActiveDataFrame();

        DataSelector1to2 ds_ = (DataSelector1to2)ds;
        String vX[] = ds_.getSelectedDataA();
        String vY[] = ds_.getSelectedDataB();

        boolean allXpositive = true;
        
        /* comprovar que les dades de X son positives */
        double[][] X = df.getNumericalData(vX);
        allXpositive = BasicStats.nNonPositive(X) == 0;
        
       /* comprovar que les dades de X siguin numeriques */
       double[][] Y = df.getNumericalData(vY);
        
        if(X.length > 1 && Y.length == 1 && allXpositive){            
            re.eval("PLOT_WIDTH = %d/72".formatted(PLOT_WIDTH));
            re.eval("PLOT_HEIGTH = %d/72".formatted(PLOT_HEIGHT));
            // Create matrices
            addMatrixToR(X, vX, "X");
            addMatrixToR(Y, vY, "Y");
            constructParametersToR();

            captureROutput();
        }
        else{
            if(X.length <= 1) JOptionPane.showMessageDialog(null, "Select at least two parts");
            else if(!allXpositive) JOptionPane.showMessageDialog(null, "Some value in X is not positive");
            else JOptionPane.showMessageDialog(null, "Only one response variable");
        }
    }
    
    void constructParametersToR(){
        
        /* construim parametres logics */
        
        if(this.B1.isSelected()) re.eval("B1 <- TRUE");
        else re.eval("B1 <- FALSE");
        if(this.B2.isSelected()) re.eval("B2 <- TRUE");
        else re.eval("B2 <- FALSE");
        
        /* construim la matriu BaseX */
        
        if(this.ilrX == null || this.ilrX.getPartition().length == 0){
            re.eval("BaseX <- NULL");
        }
        else{
            int[][] baseX = this.ilrX.getPartition();
            re.assign("BaseX", baseX[0]);
            re.eval("BaseX" + " <- matrix( " + "BaseX" + " ,nc=1)");
            for(int i=1; i < baseX.length; i++){
                re.assign("tmp", baseX[i]);
                re.eval("BaseX" + " <- cbind(" + "BaseX" + ",matrix(tmp,nc=1))");
            }
        }
    }
    
}
