/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.gui.menu;

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
 * LM1 -> X numerica i Y numerica positiva amb opci� de retornar text, crear dataframe, afegir variables i  mostrar grafics
 * @author Guest2
 */
public class XRealYCompositionRegressionMenu extends AbstractMenuDialog2NumCatONum{
    
    Rengine re;
    DataFrame df;
    JFrame frameLM1;
    Vector<JFrame> framesLM1;
    JFileChooser chooser;
    String tempDirR;
    Vector<String> tempsDirR;
    //ILRMenu ilrX;
    ILRMenu ilrY;
    ArrayList<String> names;
    
    /* options var */
    
    JRadioButton B1 = new JRadioButton("Residuals");
    JRadioButton B2 = new JRadioButton("Fitted");
    
    public static final long serialVersionUID = 1L;
    private static final String yamlUrl = CoDaPackConf.helpPath + "Statistics.Multivariate Analysis.Regression.X Real Y Composition.yaml";
    private static final String helpTitle = "X real Y composition regression Help Menu";
    
    public XRealYCompositionRegressionMenu(final CoDaPackMain mainApp, Rengine r){
        super(mainApp,"X real Y composition regression Menu",false,false,true);
        super.setSelectedDataNames("Selected X:", "Selected Y:");
        super.setHelpMenuConfiguration(yamlUrl, helpTitle);
        re = r;
        
        framesLM1 = new Vector<JFrame>();
        tempsDirR = new Vector<String>();
        
        JButton yILR = new JButton("Set Y partition");
        this.optionsPanel.add(yILR);
        yILR.addActionListener(new java.awt.event.ActionListener(){
        
            public void actionPerformed(java.awt.event.ActionEvent evt){
               configureILRY();
            }
        });
        
        this.optionsPanel.add(B1);
        this.optionsPanel.add(B2);
        this.names = new ArrayList<String>(mainApplication.getActiveDataFrame().getNames());
    }
    
    public void configureILRY(){
        if(this.ilrY == null || this.ilrY.getDsLength() != ds.getSelectedData2().length) this.ilrY = new ILRMenu(this.getSelectedData2());
        this.ilrY.setVisible(true);
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        String selectedNames1[] = super.ds.getSelectedData1();
        boolean allYNumeric = true;
        boolean allYPositive = true;
        df = mainApplication.getActiveDataFrame();
                
        String selectedNames2[] = super.ds.getSelectedData2();

        for(int i=0; i < selectedNames2.length && allYNumeric;i++){
            if(df.get(selectedNames2[i]).isText()) allYNumeric = false;
        }
        
        if(allYNumeric){
            double[][] data = df.getNumericalData(selectedNames2);
            
            for(int i=0; i < data.length && allYPositive; i++){
                for(int j = 0; j < data[i].length && allYPositive;j++){
                    if(data[i][j] <= 0.0) allYPositive = false;
                }
            }
        }
            
        
        if(selectedNames1.length > 0 && selectedNames2.length > 0 && allYNumeric && allYPositive){
            
            double[][] numericData = df.getNumericalData(selectedNames1);
            
            // Create X matrix
            
            // create dataframe on r
            
                    for(int i=0; i < selectedNames1.length;i++){
                        re.eval(selectedNames1[i] + " <- NULL");
                        if(df.get(selectedNames1[i]).isNumeric()){
                            for(double j : df.get(selectedNames1[i]).getNumericalData()){
                                re.eval(selectedNames1[i] + " <- c(" + selectedNames1[i] + "," + String.valueOf(j) + ")");
                            }
                        }
                        else{ // categorical data
                            for(String j : df.get(selectedNames1[i]).getTextData()){
                                re.eval(selectedNames1[i] + " <- c(" + selectedNames1[i] + ",'" + j + "')");
                            }
                        }
                    }
                        
                        String dataFrameString = "X <- data.frame(";
                        for(int i=0; i < selectedNames1.length;i++){
                            dataFrameString += selectedNames1[i];
                            if(i != selectedNames1.length-1) dataFrameString += ",";
                        }
                        
                        dataFrameString +=")";
                        
                        re.eval(dataFrameString); // we create the dataframe in R

                        
            // create dataframe on r
            
                    for(int i=0; i < selectedNames2.length;i++){
                        re.eval(selectedNames2[i] + " <- NULL");
                        if(df.get(selectedNames2[i]).isNumeric()){
                            for(double j : df.get(selectedNames2[i]).getNumericalData()){
                                re.eval(selectedNames2[i] + " <- c(" + selectedNames2[i] + "," + String.valueOf(j) + ")");
                            }
                        }
                        else{ // categorical data
                            for(String j : df.get(selectedNames2[i]).getTextData()){
                                re.eval(selectedNames2[i] + " <- c(" + selectedNames2[i] + ",'" + j + "')");
                            }
                        }
                    }
                        
                        dataFrameString = "Y <- data.frame(";
                        for(int i=0; i < selectedNames2.length;i++){
                            dataFrameString += selectedNames2[i];
                            if(i != selectedNames2.length-1) dataFrameString += ",";
                        }
                        
                        dataFrameString +=")";
                        
                        re.eval(dataFrameString); // we create the dataframe in R
            
                
                constructParametersToR();        
                this.dispose();
                
                // executem script d'R
                
                    String url = CoDaPackConf.rScriptPath + "Regression_X_Real_Y_Composition.R";

                    re.eval("tryCatch({error <- \"NULL\";source(\"" + url + "\")}, error = function(e){ error <<- e$message})");
                    
                    String[] errorMessage = re.eval("error").asStringArray();

                    if(errorMessage[0].equals("NULL")){
                        /* executem totes les accions possibles */
                        showText();
                        createVariables();
                        createDataFrame();
                        try {
                            showGraphics();
                        } catch (IOException ex) {
                            Logger.getLogger(XRealYCompositionRegressionMenu.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    else{
                        OutputElement type = new OutputText("Error in R:");
                        outputPanel.addOutput(type);
                        OutputElement outElement = new OutputForR(errorMessage);
                        outputPanel.addOutput(outElement);
                    }
        }
        else{
            if(selectedNames1.length == 0){
                JOptionPane.showMessageDialog(null, "No data selected in data 1");
            }
            else if(!allYNumeric){
                JOptionPane.showMessageDialog(null, "Some data in Y is not numeric");
            }
            else if(!allYPositive){
                JOptionPane.showMessageDialog(null, "Some data in Y is not positive");
            }
            else{
                JOptionPane.showMessageDialog(null, "No data selected in data 2");
            }
        }
    }
    
    void constructParametersToR(){
        /* construim parametres string */
        
        /* construim parametres logics */
        
        if(this.B1.isSelected()) re.eval("B1 <- TRUE");
        else re.eval("B1 <- FALSE");
        if(this.B2.isSelected()) re.eval("B2 <- TRUE");
        else re.eval("B2 <- FALSE");
        
        /* construim la matriu BaseY */
        
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
    
    void showText(){
        
        REXP result;
        String[] sortida;
        
        /* header output */
        
        outputPanel.addOutput(new OutputText("X real Y composition regression:"));
        
        /* R output */
        
        int midaText = re.eval("length(cdp_res$text)").asInt();
        for(int i=0; i < midaText; i++){
            result = re.eval("cdp_res$text[[" + String.valueOf(i+1) + "]]");
            sortida = result.asStringArray();
            outputPanel.addOutput(new OutputForR(sortida));
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
            
            String dataFrameName = re.eval("names(cdp_res$dataframe)[" + String.valueOf(i+1) + "]").asString();
            
            while(!mainApplication.isDataFrameNameAvailable(dataFrameName)){
                dataFrameName += "c";
            }
            
            newDataFrame.setName(dataFrameName);
            mainApplication.addDataFrame(newDataFrame);
        }
    }
    
    void showGraphics() throws IOException{
        
        int numberOfGraphics = re.eval("length(cdp_res$graph)").asInt(); /* num de grafics */

        for(int i=0; i < numberOfGraphics; i++){
            tempDirR = re.eval("cdp_res$graph[[" + String.valueOf(i+1) + "]]").asString();
            tempsDirR.add(tempDirR);
            plotLM1(this.framesLM1.size());
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
    }
    
    private void plotLM1(int position) throws IOException {

            Font f = new Font("Arial", Font.PLAIN,12);
            UIManager.put("Menu.font", f);
            UIManager.put("MenuItem.font",f);
            JMenuBar menuBar = new JMenuBar();
            JMenu menu = new JMenu("File");
            JMenuItem menuItem = new JMenuItem("Open");
            menuBar.add(menu);
            framesLM1.add(new JFrame());
            JPanel panel = new JPanel();
            menu.add(menuItem);
            menuItem = new JMenuItem("Export");
            JMenu submenuExport = new JMenu("Export");
            menuItem = new JMenuItem("Export As SVG");
            menuItem.addActionListener(new FileChooserAction(position));
            submenuExport.add(menuItem);
            menuItem = new JMenuItem("Export As JPEG");
            //submenuExport.add(menuItem);
            menuItem = new JMenuItem("Export As PDF");
            //submenuExport.add(menuItem);
            menuItem = new JMenuItem("Export As WMF");
            //submenuExport.add(menuItem);
            menuItem = new JMenuItem("Export As Postscripts");
            //submenuExport.add(menuItem);
            menuItem = new JMenuItem("Quit");
            menuItem.addActionListener(new quitListener(position));
            menu.add(submenuExport);
            menu.add(menuItem);
            framesLM1.elementAt(position).setJMenuBar(menuBar);
            JSVGCanvas c = new JSVGCanvas();
            String uri = new File(tempsDirR.elementAt(position)).toURI().toString();
            c.setURI(uri);
  
            framesLM1.elementAt(position).getContentPane().add(c);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            framesLM1.elementAt(position).setSize(800,800);
            framesLM1.elementAt(position).setLocation(dim.width/2-framesLM1.elementAt(position).getSize().width/2, dim.height/2-framesLM1.elementAt(position).getSize().height/2);
            
            WindowListener exitListener = new WindowAdapter(){
                
                @Override
                public void windowClosing(WindowEvent e){
                    int confirm = JOptionPane.showOptionDialog(null,"Are You Sure to Close Window?","Exit Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
                    if(confirm == 0){
                        framesLM1.elementAt(position).dispose();
                        File file = new File(tempsDirR.elementAt(position));
                        file.delete();
                    }
                }
            };
            
            framesLM1.elementAt(position).setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            framesLM1.elementAt(position).addWindowListener(exitListener);
            framesLM1.elementAt(position).setVisible(true);

    }

    public DataFrame getDataFrame() {
        return this.df;
    }
    
    public ArrayList<String> getDataFrameNames(){
        return this.names;
    }
    
    private class quitListener implements ActionListener{
        
        int position;
        
        public quitListener(int position){
            this.position = position;
        }
        
        public void actionPerformed(ActionEvent e){
            int confirm = JOptionPane.showOptionDialog(null,"Are You Sure to Close Window?","Exit Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
            if(confirm == 0){
                framesLM1.elementAt(position).dispose();
                File file = new File(tempsDirR.elementAt(position));
                file.delete();
            }
        }
    }
    
    private class FileChooserAction implements ActionListener{
        
        int position;
        
        public FileChooserAction(int position){
            this.position = position;
        }
        
        public void actionPerformed(ActionEvent e){
            JFrame frame = new JFrame();
            JFileChooser jf = new JFileChooser();
            frame.setSize(400,400);
            jf.setDialogTitle("Select the folder to save the file");
            jf.setApproveButtonText("Save");
            jf.setSelectedFile(new File(".svg"));
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
                    File f = new File(tempsDirR.elementAt(position));
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
