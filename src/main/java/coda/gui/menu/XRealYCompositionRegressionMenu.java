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
import coda.util.RScriptEngine;

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
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import org.rosuda.JRI.Rengine;

/**
 * LM2 -> X numerica i positiva i Y numerica amb opci� de retornar text, crear dataframe, afegir variables i  mostrar grafics
 * @author Guest2
 */
public class XRealYCompositionRegressionMenu extends AbstractMenuRBasedDialog{
    
    ILRMenu ilrY;
    
    /* options var */
    
    JRadioButton B1 = new JRadioButton("Residuals");
    JRadioButton B2 = new JRadioButton("Fitted");
    
    public static final long serialVersionUID = 1L;
    private static final String yamlUrl = CoDaPackConf.helpPath + "Statistics.Multivariate Analysis.Regression.X Composition Y Real.yaml";
    private static final String helpTitle = "X composition Y real regression Help Menu";
    
    public XRealYCompositionRegressionMenu(final CoDaPackMain mainApp, RScriptEngine r){
        super(mainApp,"X real Y composition regression Menu", new DataSelector1to2(mainApp.getActiveDataFrame(), false), r);
        script_file = "Regression_X_Real_Y_Composition.R";

        super.setHelpMenuConfiguration(yamlUrl, helpTitle);
    
        ((DataSelector1to2)ds).setTextA("Explanatory variables");
        ((DataSelector1to2)ds).setTextB("Compositional response");
        
        //super.setSelectedDataNames("Selected X:", "Selected Y:");
        
        
        /* options configuration */
        
        JButton yILR = new JButton("Set Y partition");
        this.optionsPanel.add(yILR);
        yILR.addActionListener(new java.awt.event.ActionListener(){
        
            public void actionPerformed(java.awt.event.ActionEvent evt){
               configureILRY();
            }
        });
        
        this.optionsPanel.add(B1);
        this.optionsPanel.add(B2);
    }
    
    public void configureILRY(){
        DataSelector1to2 ds_ = (DataSelector1to2)ds;
        if(this.ilrY == null || this.ilrY.getDsLength() != ds_.getSelectedDataB().length) this.ilrY = new ILRMenu(ds_.getSelectedDataB());
        this.ilrY.setVisible(true);
    }

    @Override
    public void acceptButtonActionPerformed(){
        df = mainApplication.getActiveDataFrame();

        DataSelector1to2 ds_ = (DataSelector1to2)ds;
        String vX[] = ds_.getSelectedDataA();
        String vY[] = ds_.getSelectedDataB();

        
        /* comprovar que les dades de X son positives */
        double[][] X = df.getNumericalData(vX);
        
        
        /* comprovar que les dades de X siguin numeriques */
        double[][] Y = df.getNumericalData(vY);
        boolean allYpositive = BasicStats.nNonPositive(Y) == 0;

        if(Y.length > 1 && X.length > 0 && allYpositive){            
            re.eval("PLOT_WIDTH = %d/72".formatted(PLOT_WIDTH));
            re.eval("PLOT_HEIGTH = %d/72".formatted(PLOT_HEIGHT));
            // Create matrices
            addMatrixToR(X, vX, "X");
            addMatrixToR(Y, vY, "Y");
            constructParametersToR();

            captureROutput();
            setVisible(false);
        }
        else{
            if(Y.length <= 1) JOptionPane.showMessageDialog(null, "Select at least two parts");
            else if(!allYpositive) JOptionPane.showMessageDialog(null, "Some value in Y is not positive");
            else JOptionPane.showMessageDialog(null, "Select at least one explanatory variable");
        }
        
    }
    
    void constructParametersToR(){
        
        /* construim parametres logics */
        
        if(this.B1.isSelected()) re.eval("B1 <- TRUE");
        else re.eval("B1 <- FALSE");
        if(this.B2.isSelected()) re.eval("B2 <- TRUE");
        else re.eval("B2 <- FALSE");
        
        /* construim la matriu BaseX */
        
        if(this.ilrY == null || this.ilrY.getPartition().length == 0){
            re.eval("BaseY <- NULL");
        }
        else{
            int[][] baseY = this.ilrY.getPartition();
            re.assign("BaseY", baseY[0]);
            re.eval("BaseY" + " <- matrix( " + "BaseY" + " ,nc=1)");
            for(int i=1; i < baseY.length; i++){
                re.assign("tmp", baseY[i]);
                re.eval("BaseY" + " <- cbind(" + "BaseY" + ",matrix(tmp,nc=1))");
            }
        }
    }
    
}
