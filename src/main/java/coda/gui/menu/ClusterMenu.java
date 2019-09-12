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
import java.util.Arrays;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
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
import org.apache.batik.swing.JSVGCanvas;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

/**
 * ClusterMenu -> X numerica i positiva amb opciï¿½ de retornar text, crear dataframe, afegir variables i  mostrar grafics
 * @author Guest2
 */
public class ClusterMenu extends AbstractMenuDialog{
    
    Rengine re;
    DataFrame df;
    JFrame frameClusterMenu;
    Vector<JFrame> framesClusterMenu;
    JFileChooser chooser;
    String tempDirR;
    Vector<String> tempsDirR;
    
    /* options var */
    
    JRadioButton numClusters = new JRadioButton("Number of Clusters");
    JTextField numClustersTF = new JTextField(7);
    JRadioButton searchOpt = new JRadioButton("Find optimal number between 2 and");
    JTextField searchOptTF = new JTextField(7);
    JTextField nameOfColumn = new JTextField(10);
    //String[] optMethodOptions = {"Calinski Index", "Average Silhouette"};
    JComboBox optMethod = new JComboBox(new String[]{"Calinski Index", "Average Silhouette"});
    //JRadioButton calinskiOption = new JRadioButton("Calinski option");
    
    public static final long serialVersionUID = 1L;
    private static final String yamlUrl = CoDaPackConf.helpPath + "Statistics.Multivariate Analysis.Cluster.K-Means Versio silhouette.yaml";
    private static final String helpTitle = "Cluster Help Menu";
    
    public ClusterMenu(final CoDaPackMain mainApp, Rengine r){
        super(mainApp, "Cluster Menu", false);
        super.setSelectedDataName("Select Composition:");
        super.setHelpMenuConfiguration(yamlUrl, helpTitle);
        re = r;
        
        this.optionsPanel.add(numClusters);
        numClusters.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(numClusters.isSelected()){
                    numClustersTF.setEnabled(true);
                    searchOpt.setSelected(false);
                    searchOptTF.setEnabled(false);
                    optMethod.setEnabled(false);
                }else{
                    numClustersTF.setEnabled(false);
                    searchOpt.setSelected(true);
                    searchOptTF.setEnabled(true);
                    optMethod.setEnabled(true);
                }
            }
        });
        this.optionsPanel.add(numClustersTF);
        this.optionsPanel.add(new JLabel(">=2"));
        this.optionsPanel.add(searchOpt);
        searchOpt.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(searchOpt.isSelected()){
                    searchOptTF.setEnabled(true);
                    optMethod.setEnabled(true);
                    numClusters.setSelected(false);
                    numClustersTF.setEnabled(false);
                }else{
                    searchOptTF.setEnabled(false);
                    optMethod.setEnabled(false);
                    numClusters.setSelected(true);
                    numClustersTF.setEnabled(true);
                }
            }
        });
        this.optionsPanel.add(searchOptTF);
        this.optionsPanel.add(new JLabel(">2"));
        this.optionsPanel.add(new JLabel("         "));
        this.optionsPanel.add(new JLabel("Name of column of groups"));
        nameOfColumn.setText("Group");
        this.optionsPanel.add(nameOfColumn);
        this.optionsPanel.add(new JLabel("Optimal Method"));
        this.optionsPanel.add(optMethod);
        framesClusterMenu = new Vector<JFrame>();
        tempsDirR = new Vector<String>();
    }
    
    private Vector<String> sortSelectedNames(String[] selectedNames){
        
        Vector<String> aux = new Vector<String>(Arrays.asList(selectedNames));
        Vector<String> res = new Vector<String>();
        
        ArrayList<String> sortedNames = df.getNames();
        
        for(String s : sortedNames){
            if(aux.contains(s)) res.add(s);
        }
        
        return res;
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        df = mainApplication.getActiveDataFrame();
        
        String selectedNames[] = super.ds.getSelectedData();
        Vector<String> vSelectedNames = sortSelectedNames(selectedNames);
        
        if(selectedNames.length > 0){
            
            DataFrame transformedDataFrame = new DataFrame(df);
            
            double[][] data = df.getNumericalData(selectedNames);
            Vector<Integer> rowsToDeleteVect = new Vector<Integer>();
            
            for(int i=0; i < data.length; i++){
                for(int j = 0; j < data[i].length;j++){
                    if(data[i][j] <= 0.0) rowsToDeleteVect.add(j);
                }
            }
            
            if(rowsToDeleteVect.size() == df.getMaxVariableLength()) JOptionPane.showMessageDialog(null,"No positive data to analize");
            else{
                if(rowsToDeleteVect.size() > 0){ // if some row to ignore we transform the dataFrame
                    JOptionPane.showMessageDialog(null,"Some Data is negative into a var selected");
                }
                else{
                    
                    // create dataframe on r
                        
                        int auxPos = 0;
                        for(int i=0; i < df.size();i++){ // totes les columnes
                            if(vSelectedNames.contains(df.get(i).getName())){
                                re.eval(vSelectedNames.elementAt(auxPos) + " <- NULL");
                                if(df.get(i).isNumeric()){
                                    for(double j : df.get(i).getNumericalData()){
                                        re.eval(vSelectedNames.elementAt(auxPos) + " <- c(" + vSelectedNames.elementAt(auxPos) +"," + String.valueOf(j) + ")");
                                    }
                                }
                                else{
                                    for(String j : df.get(i).getTextData()){
                                        re.eval(vSelectedNames.elementAt(auxPos) + " <- c(" + vSelectedNames.elementAt(auxPos) +",'" + j + "')");
                                    }
                                }
                                auxPos++;
                            }
                        }
                        
                        String dataFrameString = "X <- data.frame(";
                        for(int i=0; i < selectedNames.length;i++){
                            dataFrameString += vSelectedNames.elementAt(i);
                            if(i != selectedNames.length-1) dataFrameString += ",";
                        }
                        
                        dataFrameString +=")";
                        
                        re.eval(dataFrameString); // we create the dataframe in R
                        
                        constructParametersToR();
                
                    this.dispose();

                    // executem script d'R
                    
                        String url = CoDaPackConf.rScriptPath + "scripCluster K-means versio 0 (FETversió silhouette).R";
                        
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
                                Logger.getLogger(ClusterMenu.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        else{
                            OutputElement type = new OutputText("Error in R:");
                            outputPanel.addOutput(type);
                            OutputElement outElement = new OutputForR(errorMessage);
                            outputPanel.addOutput(outElement);
                           }

                }
                
            }
            
        }
        else{
            JOptionPane.showMessageDialog(null,"Please select data");
        }
    }
    
    void constructParametersToR(){
        /* construim parametres string */
        
        if(this.numClusters.isSelected()){
            re.eval("B1 <- TRUE");
            re.eval("P1 <- " + this.numClustersTF.getText());
        }
        else{
            re.eval("B1 <- FALSE");
            re.eval("P1 <- " + this.searchOptTF.getText());
        }
        
        if(((Object)this.optMethod.getSelectedItem()).toString().equals("Calinski Index")) re.eval("B2 <- TRUE");
        else re.eval("B2 <- FALSE");
    }
    
    void showText(){
        
        REXP result;
        String[] sortida;
        
        /* header output */
        
        outputPanel.addOutput(new OutputText("Cluster K-means:"));
        
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
            
            newDataFrame.setName(re.eval("names(cdp_res$dataframe)[" + String.valueOf(i+1) + "]").asString());
            mainApplication.addDataFrame(newDataFrame);
        }
    }
    
    void showGraphics() throws IOException{
        
        int numberOfGraphics = re.eval("length(cdp_res$graph)").asInt(); /* num de grafics */

        for(int i=0; i < numberOfGraphics; i++){
            tempDirR = re.eval("cdp_res$graph[[" + String.valueOf(i+1) + "]]").asString();
            tempsDirR.add(tempDirR);
            plotClusterMenu(this.framesClusterMenu.size());
        }    
    }
    
    void createVariables(){
        
        int numberOfNewVar = re.eval("length(colnames(cdp_res$new_data))").asInt(); /* numero de columnes nomes*/
        
        if(nameOfColumn.getText().length() == 0) nameOfColumn.setText("Group"); /* si esta buit posem per defecte Group */
        
        for(int i=0; i < numberOfNewVar; i++){
            String varName = nameOfColumn.getText();
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
    
private void plotClusterMenu(int position) throws IOException, IOException {
            Font f = new Font("Arial", Font.PLAIN,12);
            UIManager.put("Menu.font", f);
            UIManager.put("MenuItem.font",f);
            JMenuBar menuBar = new JMenuBar();
            JMenu menu = new JMenu("File");
            JMenuItem menuItem = new JMenuItem("Open");
            menuBar.add(menu);
            framesClusterMenu.add(new JFrame());
            JPanel panel = new JPanel();
            menu.add(menuItem);
            menuItem = new JMenuItem("Export");
            JMenu submenuExport = new JMenu("Export");
            menuItem = new JMenuItem("Export As SVG");
            menuItem.addActionListener(new ClusterMenu.FileChooserAction());
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
            menuItem.addActionListener(new ClusterMenu.quitListener(position));
            menu.add(submenuExport);
            menu.add(menuItem);
            framesClusterMenu.elementAt(position).setJMenuBar(menuBar);
            JSVGCanvas c = new JSVGCanvas();
            String uri = new File(tempsDirR.elementAt(position)).toURI().toString();
            c.setURI(uri);
            framesClusterMenu.elementAt(position).getContentPane().add(c);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            framesClusterMenu.elementAt(position).setSize(800,800);
            framesClusterMenu.elementAt(position).setLocation(dim.width/2-framesClusterMenu.elementAt(position).getSize().width/2, dim.height/2-framesClusterMenu.elementAt(position).getSize().height/2);
            
            WindowListener exitListener = new WindowAdapter(){
                
                @Override
                public void windowClosing(WindowEvent e){
                    int confirm = JOptionPane.showOptionDialog(null,"Are You Sure to Close Window?","Exit Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
                    if(confirm == 0){
                        framesClusterMenu.elementAt(position).dispose();
                        File file = new File(tempsDirR.elementAt(position));
                        file.delete();
                    }
                }
            };
            
            framesClusterMenu.elementAt(position).setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            framesClusterMenu.elementAt(position).addWindowListener(exitListener);
            framesClusterMenu.elementAt(position).setVisible(true);
    }

    public DataFrame getDataFrame() {
        return this.df;
    }
    
    private class quitListener implements ActionListener{
        
        int position;
        
        public quitListener(int position){
            this.position = position;
        }
        
        public void actionPerformed(ActionEvent e){
            int confirm = JOptionPane.showOptionDialog(null,"Are You Sure to Close Window?","Exit Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
            if(confirm == 0){
                framesClusterMenu.elementAt(position).dispose();
                File file = new File(tempsDirR.elementAt(position));
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