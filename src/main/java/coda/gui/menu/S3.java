/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.gui.menu;

import coda.DataFrame;
import coda.Variable;
import coda.gui.CoDaPackMain;
import static coda.gui.CoDaPackMain.outputPanel;
import coda.gui.output.OutputElement;
import coda.gui.output.OutputForR;
import coda.gui.output.OutputText;
import coda.gui.utils.FileNameExtensionFilter;
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
import java.util.Arrays;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
import org.rosuda.JRI.Rengine;

/**
 * S3 -> X numerica i Y numerica o categorica amb opció de retornar text, crear dataframe, afegir variables i  mostrar grafics
 * @author Guest2
 */
public class S3 extends AbstractMenuDialog2NumCatONum{
    
    Rengine re;
    DataFrame df;
    JFrame frameS3;
    JFrame[] framesS3;
    JFileChooser chooser;
    String tempDirR;
    String[] tempsDirR;
    ILRMenu ilrX;
    ILRMenu ilrY;
    
    /* options var */
    
    JRadioButton B1 = new JRadioButton("B1");
    JRadioButton B2 = new JRadioButton("B2");
    JRadioButton B3 = new JRadioButton("B3");
    JRadioButton B4 = new JRadioButton("B4");
    JRadioButton B5 = new JRadioButton("B5");
    JRadioButton B6 = new JRadioButton("B6");
    JTextField P1 = new JTextField(20);
    JTextField P2 = new JTextField(20);
    JTextField P3 = new JTextField(20);
    
    public static final long serialVersionUID = 1L;
    
    public S3(final CoDaPackMain mainApp, Rengine r){
        super(mainApp,"S3 menu",false,false,true);
        re = r;
        
        /* options configuration */
        
        JButton xILR = new JButton("Set X partition");
        this.optionsPanel.add(xILR);
        xILR.addActionListener(new java.awt.event.ActionListener(){
        
            public void actionPerformed(java.awt.event.ActionEvent evt){
               configureILRX();
            }
        });
        
        JButton yILR = new JButton("Set Y parition");
        this.optionsPanel.add(yILR);
        yILR.addActionListener(new java.awt.event.ActionListener(){
        
            public void actionPerformed(java.awt.event.ActionEvent evt){
               configureILRY();
            }
        });
        
        this.optionsPanel.add(new JLabel("      P1:"));
        this.optionsPanel.add(P1);
        this.optionsPanel.add(new JLabel("      P2:"));
        this.optionsPanel.add(P2);
        this.optionsPanel.add(new JLabel("      P3:"));
        this.optionsPanel.add(P3);
        this.optionsPanel.add(B1);
        this.optionsPanel.add(B2);
        this.optionsPanel.add(B3);
        this.optionsPanel.add(B4);
        this.optionsPanel.add(B5);
        this.optionsPanel.add(B6);
    }
    
    public void configureILRX(){
        if(this.ilrX == null || this.ilrX.getDsLength() != ds.getSelectedData1().length) this.ilrX = new ILRMenu(this.getSelectedData1());
        this.ilrX.setVisible(true);
    }
    
    public void configureILRY(){
        if(this.ilrY == null || this.ilrY.getDsLength() != ds.getSelectedData2().length) this.ilrY = new ILRMenu(this.getSelectedData2());
        this.ilrY.setVisible(true);
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        String selectedNames1[] = super.ds.getSelectedData1();
        Vector<String> vSelectedNames1 = new Vector<String>(Arrays.asList(selectedNames1));
        String selectedNames2[] = super.ds.getSelectedData2();
        Vector<String> vSelectedNames2 = new Vector<String>(Arrays.asList(selectedNames2));
        
        if(selectedNames1.length > 0 && selectedNames2.length > 0){
            
            df = mainApplication.getActiveDataFrame();
            double[][] numericData = df.getNumericalData(selectedNames1);
            
            // Create X matrix
            
            // create dataframe on r
            
                        int auxPos = 0;
                        for(int i=0; i < df.size();i++){ // totes les columnes
                            if(vSelectedNames1.contains(df.get(i).getName())){
                                re.eval(vSelectedNames1.elementAt(auxPos) + " <- NULL");
                                if(df.get(i).isNumeric()){
                                    for(double j : df.get(i).getNumericalData()){
                                        re.eval(vSelectedNames1.elementAt(auxPos) + " <- c(" + vSelectedNames1.elementAt(auxPos) +"," + String.valueOf(j) + ")");
                                    }
                                }
                                else{
                                    for(String j : df.get(i).getTextData()){
                                        re.eval(vSelectedNames1.elementAt(auxPos) + " <- c(" + vSelectedNames1.elementAt(auxPos) +",'" + j + "')");
                                    }
                                }
                                auxPos++;
                            }
                        }
                        
                        String dataFrameString = "X <- data.frame(";
                        for(int i=0; i < selectedNames1.length;i++){
                            dataFrameString += vSelectedNames1.elementAt(i);
                            if(i != selectedNames1.length-1) dataFrameString += ",";
                        }
                        
                        dataFrameString +=")";
                        
                        re.eval(dataFrameString); // we create the dataframe in R

                        
            // create dataframe on r
            
                        auxPos = 0;
                        for(int i=0; i < df.size();i++){ // totes les columnes
                            if(vSelectedNames2.contains(df.get(i).getName())){
                                re.eval(vSelectedNames2.elementAt(auxPos) + " <- NULL");
                                if(df.get(i).isNumeric()){
                                    for(double j : df.get(i).getNumericalData()){
                                        re.eval(vSelectedNames2.elementAt(auxPos) + " <- c(" + vSelectedNames2.elementAt(auxPos) +"," + String.valueOf(j) + ")");
                                    }
                                }
                                else{
                                    for(String j : df.get(i).getTextData()){
                                        re.eval(vSelectedNames2.elementAt(auxPos) + " <- c(" + vSelectedNames2.elementAt(auxPos) +",'" + j + "')");
                                    }
                                }
                                auxPos++;
                            }
                        }
                        
                        dataFrameString = "Y <- data.frame(";
                        for(int i=0; i < selectedNames2.length;i++){
                            dataFrameString += vSelectedNames2.elementAt(i);
                            if(i != selectedNames2.length-1) dataFrameString += ",";
                        }
                        
                        dataFrameString +=")";
                        
                        re.eval(dataFrameString); // we create the dataframe in R
            
                
                constructParametersToR();        
                this.dispose();
                
                // executem script d'R
                
                frameS3 = new JFrame();
                chooser = new JFileChooser();
                frameS3.setSize(600,400);
                chooser.setDialogTitle("Select R script to execute");
                chooser.setFileFilter(new FileNameExtensionFilter("R data file", "R", "rda"));
                chooser.setSize(400,400);
                frameS3.add(chooser);
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                frameS3.setLocation(dim.width/2-frameS3.getSize().width/2, dim.height/2-frameS3.getSize().height/2);
                
                if(chooser.showOpenDialog(frameS3) == JFileChooser.APPROVE_OPTION){
                    String url = chooser.getSelectedFile().getAbsolutePath();
                    url = url.replaceAll("\\\\", "/");
                    re.eval("tryCatch({error <- \"NULL\";source(\"" + url + "\")}, error = function(e){ error <<- e$message})");
                    
                    String[] errorMessage = re.eval("error").asStringArray();

                    if(errorMessage[0].equals("NULL")){
                        /* executem totes les accions possibles */
                        showText();
                        createVariables();
                        createDataFrame();
                        showGraphics();
                    }
                    else{
                        OutputElement type = new OutputText("Error in R:");
                        outputPanel.addOutput(type);
                        OutputElement outElement = new OutputForR(errorMessage);
                        outputPanel.addOutput(outElement);
                    }
                }
                else{
                    frameS3.dispose();
                }
        }
        else{
            if(selectedNames1.length == 0) JOptionPane.showMessageDialog(null, "No data selected in data 1");
            else JOptionPane.showMessageDialog(null, "No data selected in data 2");
        }
    }
    
    void constructParametersToR(){
        /* construim parametres string */
        
        if(this.P1.getText().length() > 0) re.eval("P1 <- \"" + this.P1.getText() + "\"");
        else re.eval("P1 <- \"\"");
        if(this.P2.getText().length() > 0) re.eval("P2 <- \"" + this.P2.getText() + "\"");
        else re.eval("P2 <- \"\"");
        if(this.P3.getText().length() > 0) re.eval("P3 <- \"" + this.P3.getText() + "\"");
        else re.eval("P3 <- \"\"");
        
        /* construim parametres logics */
        
        if(this.B1.isSelected()) re.eval("B1 <- TRUE");
        else re.eval("B1 <- FALSE");
        if(this.B2.isSelected()) re.eval("B2 <- TRUE");
        else re.eval("B2 <- FALSE");
        if(this.B3.isSelected()) re.eval("B3 <- TRUE");
        else re.eval("B3 <- FALSE");
        if(this.B4.isSelected()) re.eval("B4 <- TRUE");
        else re.eval("B4 <- FALSE");
        if(this.B5.isSelected()) re.eval("B5 <- TRUE");
        else re.eval("B5 <- FALSE");
        if(this.B6.isSelected()) re.eval("B6 <- TRUE");
        else re.eval("B6 <- FALSE");
        
        /* construim la matriu BaseX */
        
        if(this.ilrX == null || this.ilrX.getPartition().length == 0){
            re.eval("BaseX <- NULL");
        }
        else{
            int[][] baseX = this.ilrX.getPartition();
            re.assign("BaseX", baseX[0]);
            re.eval("BaseX" + " <- matrix( " + "BaseX" + " ,nr=1)");
            for(int i=1; i < baseX.length; i++){
                re.assign("tmp", baseX[i]);
                re.eval("BaseX" + " <- rbind(" + "BaseX" + ",matrix(tmp,nr=1))");
            }
        }
        
        /* construim la matriu BaseY */
        
        if(this.ilrY == null || this.ilrY.getPartition().length == 0){
            re.eval("BaseY <- NULL");
        }
        else{
            int[][] baseY = this.ilrY.getPartition();
            re.assign("BaseY", baseY[0]);
            re.eval("BaseY" + " <- matrix( " + "BaseY" + " ,nr=1)");
            for(int i=1; i < baseY.length; i++){
                re.assign("tmp", baseY[i]);
                re.eval("BaseY" + " <- rbind(" + "BaseY" + ",matrix(tmp,nr=1))");
            }
        }
    }
    
    void showText(){
        
        int midaText = re.eval("length(cdp_res$text)").asInt();
        for(int i=0; i < midaText; i++){
            re.eval("out <- capture.output(cdp_res$text[[" + String.valueOf(i+1) + "]])");
            OutputElement e = new OutputForR(re.eval("out").asStringArray());
            outputPanel.addOutput(e);
        }
    }
    
    void createDataFrame(){
        int nDataFrames = re.eval("length(cdp_res$dataframe)").asInt();
        for(int i=0; i < nDataFrames; i++){
            int nVariables = re.eval("length(cdp_res$dataframe[[" + String.valueOf(i+1) + "]])").asInt();
            DataFrame newDataFrame = new DataFrame();
            for(int j=0; j < nVariables; j++){
                String varName = re.eval("names(cdp_res$dataframe[[" + String.valueOf(i+1) + "]][" + String.valueOf(j+1) + "])").asString();
                String isNumeric = re.eval("class(unlist(cdp_res$dataframe[[" + String.valueOf(i+1) + "]][" + String.valueOf(j+1) + "]))").asString();
                if(isNumeric.equals("numeric")){ /* crear una variable numerica */
                    double[] data = re.eval("as.numeric(unlist(cdp_res$dataframe[[" + String.valueOf(i+1) + "]][" + String.valueOf(j+1) + "]))").asDoubleArray();
                    newDataFrame.addData(varName, data);
                }
                else{ /* crear una variable categorica */
                    String[] data = re.eval("as.character(unlist(cdp_res$dataframe[[" + String.valueOf(i+1) + "]][" + String.valueOf(j+1) + "]))").asStringArray();
                    newDataFrame.addData(varName, new Variable(varName,data));
                }
            }
            
            newDataFrame.setName(re.eval("names(cdp_res$dataframe)[" + String.valueOf(i+1) + "]").asString());
            mainApplication.addDataFrame(newDataFrame);
        }
    }
    
    void showGraphics(){
        
        int numberOfGraphics = re.eval("length(cdp_res$graph)").asInt(); /* num de grafics */
        this.framesS3 = new JFrame[numberOfGraphics];
        this.tempsDirR = new String[numberOfGraphics];
        for(int i=0; i < numberOfGraphics; i++){
            tempDirR = re.eval("cdp_res$graph[[" + String.valueOf(i+1) + "]]").asString();
            tempsDirR[i] = tempDirR;
            plotS3(i);
        }  
    }
    
    void createVariables(){
        
        int numberOfNewVar = re.eval("length(colnames(cdp_res$new_data))").asInt(); /* numero de columnes nomes*/
        
        for(int i=0; i < numberOfNewVar; i++){
            String varName = re.eval("colnames(cdp_res$new_data)[" + String.valueOf(i+1) + "]").asString();
            String isNumeric = re.eval("as.character(is.numeric(cdp_res$new_data[["+ String.valueOf(i+1) +"]]))").asString();
            if(isNumeric.equals("TRUE")){
                double[] data = re.eval("as.numeric(cdp_res$new_data[," + String.valueOf(i+1) + "])").asDoubleArray();
                df.addData(varName,data);
            }
            else{ // categoric
                String[] data = re.eval("as.character(cdp_res$new_data[," + String.valueOf(i+1) + "])").asStringArray();
                df.addData(varName, new Variable(varName,data));
            }
            mainApplication.updateDataFrame(df);
        }
        
        /*
        int numberOfNewVar = re.eval("length(names(cdp_res$new_data))").asInt();  numero de noves variables
        for(int i=0; i < numberOfNewVar; i++){
            String varName = re.eval("names(cdp_res$new_data)[" + String.valueOf(i+1) + "]").asString();  guardem el nom de la variable 
            String isNumeric = re.eval("class(unlist(cdp_res$new_data[[" + String.valueOf(i+1) + "]]))").asString();
            if(isNumeric.equals("numeric")){  creem variable numerica 
                double[] data = re.eval("as.numeric(unlist(cdp_res$new_data[[" + String.valueOf(i+1) + "]]))").asDoubleArray();
                df.addData(varName,data);
            }
            else{  crear variable categorica 
                String[] data = re.eval("as.character(unlist(cdp_res$new_data[[" + String.valueOf(i+1) + "]]))").asStringArray();
                df.addData(varName, new Variable(varName,data));
            }
            mainApplication.updateDataFrame(df);
        }
        */
    }
    
    private void plotS3(int position) {
            Font f = new Font("Arial", Font.PLAIN,12);
            UIManager.put("Menu.font", f);
            UIManager.put("MenuItem.font",f);
            JMenuBar menuBar = new JMenuBar();
            JMenu menu = new JMenu("File");
            JMenuItem menuItem = new JMenuItem("Open");
            menuBar.add(menu);
            framesS3[position] = new JFrame();
            JPanel panel = new JPanel();
            menu.add(menuItem);
            menuItem = new JMenuItem("Export");
            JMenu submenuExport = new JMenu("Export");
            menuItem = new JMenuItem("Export As PNG");
            menuItem.addActionListener(new FileChooserAction());
            submenuExport.add(menuItem);
            menuItem = new JMenuItem("Export As JPEG");
            submenuExport.add(menuItem);
            menuItem = new JMenuItem("Export As PDF");
            submenuExport.add(menuItem);
            menuItem = new JMenuItem("Export As WMF");
            submenuExport.add(menuItem);
            menuItem = new JMenuItem("Export As Postscripts");
            submenuExport.add(menuItem);
            menuItem = new JMenuItem("Quit");
            menuItem.addActionListener(new quitListener(position));
            menu.add(submenuExport);
            menu.add(menuItem);
            framesS3[position].setJMenuBar(menuBar);
            panel.setSize(800,800);
            ImageIcon icon = new ImageIcon(tempDirR);
            JLabel label = new JLabel(icon,JLabel.CENTER);
            label.setSize(700, 700);
            panel.setLayout(new GridBagLayout());
            panel.add(label);
            framesS3[position].getContentPane().add(panel);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            framesS3[position].setSize(800,800);
            framesS3[position].setLocation(dim.width/2-framesS3[position].getSize().width/2, dim.height/2-framesS3[position].getSize().height/2);
            
            WindowListener exitListener = new WindowAdapter(){
                
                @Override
                public void windowClosing(WindowEvent e){
                    int confirm = JOptionPane.showOptionDialog(null,"Are You Sure to Close Window?","Exit Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
                    if(confirm == 0){
                        framesS3[position].dispose();
                        File file = new File(tempsDirR[position]);
                        file.delete();
                    }
                }
            };
            
            framesS3[position].setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            framesS3[position].addWindowListener(exitListener);
            framesS3[position].setVisible(true);
    }
    
    private class quitListener implements ActionListener{
        
        int position;
        
        public quitListener(int position){
            this.position = position;
        }
        
        public void actionPerformed(ActionEvent e){
            int confirm = JOptionPane.showOptionDialog(null,"Are You Sure to Close Window?","Exit Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
            if(confirm == 0){
                framesS3[position].dispose();
                File file = new File(tempsDirR[position]);
                file.delete();
            }
        }
    }
    
    private class FileChooserAction implements ActionListener{
        
        public void actionPerformed(ActionEvent e){
            JFrame frame = new JFrame();
            JFileChooser jf = new JFileChooser();
            frame.setSize(400,400);
            jf.setDialogTitle("Select the folder to save the file");
            jf.setApproveButtonText("Save");
            jf.setSelectedFile(new File(".png"));
            jf.setSize(400,400);
            frame.add(jf);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
            boolean canExit = false;
            while(! canExit){
                int result = jf.showSaveDialog(frame);
                if(JFileChooser.CANCEL_OPTION == result){
                    frame.dispose();
                    canExit = true;
                }
                if(JFileChooser.APPROVE_OPTION == result){ // guardem arxiu en el path
                    File f = new File(tempDirR);
                    f.deleteOnExit();
                    String path = jf.getSelectedFile().getAbsolutePath();
                    File f2 = new File(path);
                    if(f2.exists()){
                        int response = JOptionPane.showConfirmDialog(null, "Do you want to replace the existing file?", 
                                "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(response != JOptionPane.YES_OPTION) canExit = false;
                        else{
                            f.renameTo(f2);
                            frame.dispose();
                            canExit = true;
                        }
                    }
                    else{
                        f.renameTo(f2);
                        frame.dispose();
                        canExit = true;
                    }
                }
            }
        }
    }
    
}
