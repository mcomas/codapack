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
import coda.gui.output.OutputPlotHeader;
import coda.gui.output.OutputText;
import coda.gui.utils.DataSelector1to1;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
 * ScatterplotMenu -> X numerica i Y numerica o categorica amb opci� de retornar text, crear dataframe, afegir variables i  mostrar grafics
 * @author Guest2
 */
public class ScatterplotMenu extends AbstractMenuDialog{
    
    Rengine re;
    DataFrame df;
    JFrame frameScatterplotMenu;
    Vector<JFrame> framesScatterplotMenu;
    JFileChooser chooser;
    String tempDirR;
    Vector<String> tempsDirR;
    ArrayList<String> names;
    
    /* options var */
    
    JRadioButton B1 = new JRadioButton("Set same scale");
    
    public static final long serialVersionUID = 1L;
    private static final String yamlUrl = "Graphs.Scatterplot 2D-3D.yaml";
    private static final String helpTitle = "Scatterplot Help Menu";
    
    public ScatterplotMenu(final CoDaPackMain mainApp, Rengine r){
        super(mainApp,"Scatterplot Menu",new DataSelector1to1(mainApp.getActiveDataFrame(), true));
        super.setHelpMenuConfiguration(yamlUrl, helpTitle);
        re = r;
        
        framesScatterplotMenu = new Vector<JFrame>();
        tempsDirR = new Vector<String>();
        this.optionsPanel.add(B1);
        this.names = new ArrayList<String>(mainApplication.getActiveDataFrame().getNames());
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        df = mainApplication.getActiveDataFrame();
        
        String selectedNames1[] = super.ds.getSelectedData();
        String selectedNames2[] = {super.ds.getSelectedGroup()};
        
        if(selectedNames1.length > 0){
            
            double[][] numericData = df.getNumericalData(selectedNames1);
            
            // Create X matrix
            
            // create dataframe X on r
            
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
                        
                        re.eval(dataFrameString); // we create the dataframe X in R

                        
            // create dataframe Y on r
            
                if (selectedNames2[0] != null){ // si tenim algun grup
            
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
                }
                else{ // si no tenim cap grup llavors posem la Y a null
                    dataFrameString = "Y <- NULL";
                }
                        
                        re.eval(dataFrameString); // we create the dataframe Y in R
            
                
                constructParametersToR();        
                this.dispose();
                
                // executem script d'R

                    String url = CoDaPackConf.rScriptPath + "Scatterplot.R";
                    
                    re.eval("tryCatch({error <- \"NULL\";source(\"" + url + "\")}, error = function(e){ error <<- e$message})");
                    
                    String[] errorMessage = re.eval("error").asStringArray();

                    if(errorMessage[0].equals("NULL")){
                        /* executem totes les accions possibles */
                        showText(selectedNames1);
                        createVariables();
                        createDataFrame();
                        try {
                            showGraphics();
                        } catch (IOException ex) {
                            Logger.getLogger(ScatterplotMenu.class.getName()).log(Level.SEVERE, null, ex);
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
            if(selectedNames1.length == 0) JOptionPane.showMessageDialog(null, "No data selected in data 1");
            //else JOptionPane.showMessageDialog(null, "No group selected");
        }
    }
    
    void constructParametersToR(){
        
        /* construim parametres logics */
        
        if(this.B1.isSelected()) re.eval("B1 <- TRUE");
        else re.eval("B1 <- FALSE");
    }
    
    void showText(String[] selectedNames){
        
        REXP result;
        String[] sortida;
        
        /* header output */
        
        CoDaPackMain.outputPanel.addOutput(new OutputPlotHeader("Scatterplot", selectedNames));
        
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
            plotScatterplotMenu(this.framesScatterplotMenu.size());
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
    
    private void plotScatterplotMenu(int position) throws IOException, IOException {
            Font f = new Font("Arial", Font.PLAIN,12);
            UIManager.put("Menu.font", f);
            UIManager.put("MenuItem.font",f);
            JMenuBar menuBar = new JMenuBar();
            JMenu menu = new JMenu("File");
            JMenuItem menuItem = new JMenuItem("Open");
            menuBar.add(menu);
            framesScatterplotMenu.add(new JFrame());
            JPanel panel = new JPanel();
            menu.add(menuItem);
            menuItem = new JMenuItem("Export");
            JMenu submenuExport = new JMenu("Export");
            menuItem = new JMenuItem("Export As SVG");
            menuItem.addActionListener(new ScatterplotMenu.FileChooserAction(position));
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
            menuItem.addActionListener(new ScatterplotMenu.quitListener(position));
            menu.add(submenuExport);
            menu.add(menuItem);
            framesScatterplotMenu.elementAt(position).setJMenuBar(menuBar);
            JSVGCanvas c = new JSVGCanvas();
            String uri = new File(tempsDirR.elementAt(position)).toURI().toString();
            c.setURI(uri);
  
            framesScatterplotMenu.elementAt(position).getContentPane().add(c);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            framesScatterplotMenu.elementAt(position).setSize(800,800);
            framesScatterplotMenu.elementAt(position).setLocation(dim.width/2-framesScatterplotMenu.elementAt(position).getSize().width/2, dim.height/2-framesScatterplotMenu.elementAt(position).getSize().height/2);
            
            WindowListener exitListener = new WindowAdapter(){
                
                @Override
                public void windowClosing(WindowEvent e){
                    int confirm = JOptionPane.showOptionDialog(null,"Are You Sure to Close Window?","Exit Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
                    if(confirm == 0){
                        framesScatterplotMenu.elementAt(position).dispose();
                        File file = new File(tempsDirR.elementAt(position));
                        file.delete();
                    }
                }
            };
            
            framesScatterplotMenu.elementAt(position).setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            framesScatterplotMenu.elementAt(position).addWindowListener(exitListener);
            framesScatterplotMenu.elementAt(position).setVisible(true);
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
                framesScatterplotMenu.elementAt(position).dispose();
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

