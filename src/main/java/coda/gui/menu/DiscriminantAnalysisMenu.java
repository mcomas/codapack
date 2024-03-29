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
import coda.gui.utils.BoxDataSelector;
import coda.gui.utils.DataSelector1to1;

import java.awt.BorderLayout;
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
import java.util.Arrays;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
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
 * DiscriminantMenu -> X numerica i Y numerica o categorica amb opci� de retornar text, crear dataframe, afegir variables i  mostrar grafics
 * @author Guest2
 */
public class DiscriminantAnalysisMenu extends AbstractMenuDialog{
    
    Rengine re;
    DataFrame df;
    ArrayList<String> names;
    JFrame frameDiscriminantMenu;
    Vector<JFrame> framesDiscriminantMenu;
    JFileChooser chooser;
    String tempDirR;
    Vector<String> tempsDirR;
    DataFrame dfZ = null;
    ILRMenu ilrX;
    
    /* options var */
    
    JRadioButton B1 = new JRadioButton("Use of uniform prior");
    JRadioButton B2 = new JRadioButton("Predicting for a new sample Z");
    JRadioButton B3 = new JRadioButton("Discriminant scores");
    JRadioButton B4 = new JRadioButton("Max. posteriori prob. classification");
    JRadioButton B5 = new JRadioButton("Posterior prob. for the classes");
    
    public static final long serialVersionUID = 1L;
    private static final String yamlUrl = "Statistics.Multivariate Analysis.Discriminant Analysis.yaml";
    private static final String helpTitle = "Discriminant Help Menu";
    
    public DiscriminantAnalysisMenu(final CoDaPackMain mainApp, Rengine r){
        super(mainApp,"Discriminant Menu", new DataSelector1to1(mainApp.getActiveDataFrame(), true));
        super.setHelpMenuConfiguration(yamlUrl, helpTitle);
        re = r;
        //super.setSelectedDataName("Selected X:");
        
        framesDiscriminantMenu = new Vector<JFrame>();
        tempsDirR = new Vector<String>();
        
        /* options configuration */
        
        JButton xILR = new JButton("Set X partition");
        this.optionsPanel.add(xILR);
        xILR.addActionListener(new java.awt.event.ActionListener(){
        
            public void actionPerformed(java.awt.event.ActionEvent evt){
               configureILRX();
            }
        });
        
        B2.addActionListener(new java.awt.event.ActionListener(){
            
            public void actionPerformed(java.awt.event.ActionEvent evt){
                if(B2.isSelected()){
                    if(ds.getSelectedData().length == 0){
                        JOptionPane.showMessageDialog(null, "Please select first the variables");
                        B2.setSelected(false);
                    }
                    else{
                        configureSampleZ(ds.getSelectedData());
                    }
                }
            }
        });
        
        this.optionsPanel.add(B1);
        this.optionsPanel.add(B2);
        this.optionsPanel.add(B3);
        this.optionsPanel.add(B4);
        this.optionsPanel.add(B5);
        this.names= new ArrayList<String>(mainApplication.getActiveDataFrame().getNames());
    }
    
    public void configureILRX(){
        if(this.ilrX == null || this.ilrX.getDsLength() != ds.getSelectedData().length) this.ilrX = new ILRMenu(this.getSelectedData());
        this.ilrX.setVisible(true);
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        boolean sampleZ = true;
        df = mainApplication.getActiveDataFrame();
        
        if(B2.isSelected()){
            if(re.eval("is.null(Z)").asBool().isTRUE()) sampleZ = false;
        }
        
        String selectedNames1[] = super.ds.getSelectedData();
        String selectedNames2[] = {super.ds.getSelectedGroup()};
        
        if(selectedNames1.length > 0 && selectedNames2[0] != null && sampleZ){
            
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
                
                    String url = CoDaPackConf.rScriptPath + "Discriminant_analysis.R";
                
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
                            Logger.getLogger(DiscriminantAnalysisMenu.class.getName()).log(Level.SEVERE, null, ex);
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
            if(selectedNames1.length == 0) JOptionPane.showMessageDialog(null, "No data selected in data X");
            else if(selectedNames2[0] == null) JOptionPane.showMessageDialog(null, "No group selected");
            else{
                JOptionPane.showMessageDialog(null,"Please configure the Z sample with correct variables");
                B2.setSelected(false);
            }
        }
    }
    
    void constructParametersToR(){
        
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
    
    void configureSampleZ(String[] nomVars){
        boolean allRight = true;
        Vector<String> dataFrameNames = new Vector<String>();
        // obtenim els noms de les taules que hi han carregades
        
        for(int i=0; i < mainApplication.getAllDataFrames().size(); i++){
            dataFrameNames.add(mainApplication.getAllDataFrames().get(i).name);
        }
        
        BoxDataSelector bds = new BoxDataSelector(dataFrameNames.toArray(new String[dataFrameNames.size()]));
        JDialog jd = new JDialog();
        jd.add(bds);
        jd.setSize(190,370);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        jd.setLocation(dim.width/2-jd.getSize().width/2, dim.height/2-jd.getSize().height/2);
        JButton accept = new JButton("Accept");
        jd.add(accept, BorderLayout.SOUTH);
        accept.addActionListener(new java.awt.event.ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String selected[] = bds.getSelectedData();
                int n = selected.length;
                if(n == 1){
                    int pos =0;
                    for(int i=0; i < dataFrameNames.size(); i++){
                        if(dataFrameNames.get(i).equals(selected[0])) pos = i;
                    }
                    DataFrame sampleZDf = mainApplication.getAllDataFrames().get(pos);
                    boolean found = false; boolean foundall = true;
                    for(int i=0; i < nomVars.length  && foundall; i++){
                        found = false;
                        for(int j=0; j < sampleZDf.size() && !found;j++){
                            if(sampleZDf.get(j).getName().equals(nomVars[i])) found = true;
                        }
                        if(found == false) foundall = false;
                    }
                    
                    if(foundall){ // creem Z SAMPLE
                        Vector<String> vSelectedNames1 = new Vector<String>(Arrays.asList(nomVars));
                        int auxPos = 0;
                        for(int i=0; i < sampleZDf.size();i++){ // totes les columnes
                            if(vSelectedNames1.contains(sampleZDf.get(i).getName())){
                                re.eval(vSelectedNames1.elementAt(auxPos) + " <- NULL");
                                if(sampleZDf.get(i).isNumeric()){
                                    for(double j : sampleZDf.get(i).getNumericalData()){
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
                        
                        String dataFrameString = "Z <- data.frame(";
                        for(int i=0; i < nomVars.length;i++){
                            dataFrameString += vSelectedNames1.elementAt(i);
                            if(i != nomVars.length-1) dataFrameString += ",";
                        }
                        
                        dataFrameString +=")";
                        
                        re.eval(dataFrameString); // we create the dataframe in R
                        dfZ = sampleZDf; // ens guardem la taula 
                    }
                    else{
                        re.eval("Z <- NULL");
                    }
                }
                jd.setVisible(false);
            }
            
        });
        jd.setVisible(true);
    }
    
    void showText(){
        
        REXP result;
        String[] sortida;
        
        /* header output */
        
        outputPanel.addOutput(new OutputText("Discriminant Analysis:"));
        
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
            plotDiscriminantMenu(this.framesDiscriminantMenu.size());
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
        
        numberOfNewVar = re.eval("length(colnames(cdp_res$new_data2))").asInt(); /* numero de columnes nomes*/
        
        for(int i=0; i < numberOfNewVar; i++){
            String varName = re.eval("colnames(cdp_res$new_data2)[" + String.valueOf(i+1) + "]").asString();
            String isNumeric = re.eval("as.character(is.numeric(cdp_res$new_data2[["+ String.valueOf(i+1) +"]]))").asString();
            if(isNumeric.equals("TRUE")){
                double[] data = re.eval("as.numeric(cdp_res$new_data2[," + String.valueOf(i+1) + "])").asDoubleArray();
                dfZ.addData(varName,data);
            }
            else{ // categoric
                String[] data = re.eval("as.character(cdp_res$new_data2[," + String.valueOf(i+1) + "])").asStringArray();
                dfZ.addData(varName, new Variable(varName,data));
            }
        }
    }
    
    private void plotDiscriminantMenu(int position) throws IOException {
            Font f = new Font("Arial", Font.PLAIN,12);
            UIManager.put("Menu.font", f);
            UIManager.put("MenuItem.font",f);
            JMenuBar menuBar = new JMenuBar();
            JMenu menu = new JMenu("File");
            JMenuItem menuItem = new JMenuItem("Open");
            menuBar.add(menu);
            framesDiscriminantMenu.add(new JFrame());
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
            framesDiscriminantMenu.elementAt(position).setJMenuBar(menuBar);
            JSVGCanvas c = new JSVGCanvas();
            String uri = new File(tempsDirR.elementAt(position)).toURI().toString();
            c.setURI(uri);

            framesDiscriminantMenu.elementAt(position).getContentPane().add(c);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            framesDiscriminantMenu.elementAt(position).setSize(800,800);
            framesDiscriminantMenu.elementAt(position).setLocation(dim.width/2-framesDiscriminantMenu.elementAt(position).getSize().width/2, dim.height/2-framesDiscriminantMenu.elementAt(position).getSize().height/2);
            
            WindowListener exitListener = new WindowAdapter(){
                
                @Override
                public void windowClosing(WindowEvent e){
                    int confirm = JOptionPane.showOptionDialog(null,"Are You Sure to Close Window?","Exit Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
                    if(confirm == 0){
                        framesDiscriminantMenu.elementAt(position).dispose();
                        File file = new File(tempsDirR.elementAt(position));
                        file.delete();
                    }
                }
            };
            
            framesDiscriminantMenu.elementAt(position).setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            framesDiscriminantMenu.elementAt(position).addWindowListener(exitListener);
            framesDiscriminantMenu.elementAt(position).setVisible(true);
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
                framesDiscriminantMenu.elementAt(position).dispose();
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
