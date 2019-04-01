/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.gui.menu;

import coda.CoDaStats;
import coda.DataFrame;
import coda.Variable;
import coda.gui.CoDaPackMain;
import static coda.gui.CoDaPackMain.outputPanel;
import coda.gui.output.OutputElement;
import coda.gui.output.OutputForR;
import coda.gui.output.OutputText;
import coda.gui.utils.BinaryPartitionSelect;
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
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import org.rosuda.JRI.Rengine;

/**
 * EM_ZeroMissingMenu -> X numerica i positiva amb opció de retornar text, crear dataframe, afegir variables i  mostrar grafics
 * @author Guest2
 */
public class EM_ZeroMissingMenu extends AbstractMenuDialog{
    
    Rengine re;
    DataFrame df;
    JFrame frameEM_ZeroMissingMenu;
    JFrame[] framesEM_ZeroMissingMenu;
    JFileChooser chooser;
    String tempDirR;
    String[] tempsDirR;
    
    /* options var */
    
    JTextField DLProportion = new JTextField("0.65");
    double percentatgeDL = 0.65;
    String[] robOptions = {"FALSE","TRUE"};
    String[] iniCovOptions = {"multRepl","complete.obs"};
    JComboBox robList = new JComboBox(robOptions);
    JComboBox iniCovList = new JComboBox(iniCovOptions);
    JLabel robOption = new JLabel("Rob Option");
    JLabel iniCovOption = new JLabel("IniCov Option");
    JCheckBox performMax;
    JLabel lmax = new JLabel("Use minimum on detection limit");
    JLabel l_usedPercentatgeDL = new JLabel("DL proportion");
    JTextField dlProportion;
    
    /*JRadioButton B1 = new JRadioButton("B1");
    JRadioButton B2 = new JRadioButton("B2");
    JRadioButton B3 = new JRadioButton("B3");
    JRadioButton B4 = new JRadioButton("B4");
    JRadioButton B5 = new JRadioButton("B5");
    JRadioButton B6 = new JRadioButton("B6");
    JTextField P1 = new JTextField(20);
    JTextField P2 = new JTextField(20);
    JTextField P3 = new JTextField(20);*/
    
    public static final long serialVersionUID = 1L;
    
    public EM_ZeroMissingMenu(final CoDaPackMain mainApp, Rengine r){
        super(mainApp, "EM_ZeroMissingMenu menu", false);
        re = r;
        
        /* options configuration */
        
        optionsPanel.add(robOption);
        optionsPanel.add(robList);
        optionsPanel.add(Box.createVerticalStrut(25));
        optionsPanel.add(Box.createHorizontalStrut(50));
        optionsPanel.add(iniCovOption);
        
        
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
        
        dlProportion = new JTextField(5);
        dlProportion.setText("0.65");
        dlProportion.setEnabled(true);
        optionsPanel.add(iniCovList);
        optionsPanel.add(Box.createVerticalStrut(75));
        optionsPanel.add(Box.createHorizontalStrut(50));
        optionsPanel.add(l_usedPercentatgeDL);
        optionsPanel.add(dlProportion);
        optionsPanel.add(Box.createVerticalStrut(25));
        optionsPanel.add(Box.createHorizontalStrut(50));
        performMax = new JCheckBox("Min result", false);
        performMax.setSelected(true);
        optionsPanel.add(lmax);
        optionsPanel.add(performMax);
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        String selectedNames[] = super.ds.getSelectedData();
        Vector<String> vSelectedNames = new Vector<String>(Arrays.asList(selectedNames));
        
        if(selectedNames.length > 0){
            
            df = mainApplication.getActiveDataFrame();
            DataFrame transformedDataFrame = new DataFrame(df);
            
            double[][] data = df.getNumericalData(selectedNames);
            Vector<Integer> rowsToDeleteVect = new Vector<Integer>();
            
            for(int i=0; i < data.length; i++){
                for(int j = 0; j < data[i].length;j++){
                    if(data[i][j] < 0.0) rowsToDeleteVect.add(j);
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
                        
                    // configurem la matriu DL
                    boolean takeMin = true;
                    if(!performMax.isSelected()) takeMin = false;
                    int m = selectedNames.length;
                    
                    double minimumsOfColumns[] = new double[m]; double minimumOfColumn;
            
                    // we search the maximum number for each column
            
                    for(int i =0; i < data.length;i++){
                        minimumOfColumn = 0.0;
                        for(int j=0;j < data[i].length;j++){
                            if((data[i][j] != 0 && data[i][j] < minimumOfColumn) || minimumOfColumn == 0) minimumOfColumn = data[i][j];
                        }
                        minimumsOfColumns[i] = minimumOfColumn;
                    }
                    
                    double dlevel[][] = df.getDetectionLevel(selectedNames);

                    if(takeMin){ // si s'ha seleccionat la opció d'agafar el màxim
                        for(int i =0; i < data.length;i++){
                            for(int j=0; j < data[i].length;j++){
                                if(data[i][j] == 0 && dlevel[i][j] == 0) dlevel[i][j] = minimumsOfColumns[i];
                            }
                        }
                    }

                    re.assign("DL", dlevel[0]);
                    re.eval("DL" + " <- matrix( " + "DL" + " ,nc=1");
                    for(int i=1; i < dlevel.length;i++){
                        re.assign("tmp", dlevel[i]);
                        re.eval("DL" + " <- cbind(" + "DL" + ",matrix(tmp,nc=1))");
                    }
                        
                        constructParametersToR();
                
                    this.dispose();

                    // executem script d'R

                        String  url = "Scripts_Amb_Base/scriptilr-EM_Missing_Zero (FET).R";
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
                
            }
            
        }
        else{
            JOptionPane.showMessageDialog(null,"Please select data");
        }
    }
    
    void constructParametersToR(){
        /* construim parametres string */
        
        if(this.DLProportion.getText().length() > 0) re.eval("P1 <- " + this.DLProportion.getText());
        else re.eval("P1 <- 0.65"); // value by default
        
        /*if(this.P1.getText().length() > 0) re.eval("P1 <- \"" + this.P1.getText() + "\"");
        else re.eval("P1 <- \"\"");
        if(this.P2.getText().length() > 0) re.eval("P2 <- \"" + this.P2.getText() + "\"");
        else re.eval("P2 <- \"\"");
        if(this.P3.getText().length() > 0) re.eval("P3 <- \"" + this.P3.getText() + "\"");
        else re.eval("P3 <- \"\"");*/
        
        /* construim parametres logics */
        
        Object selected = robList.getSelectedItem();
        if(selected.toString().equals("FALSE")) re.eval("B1 <- FALSE");
        else re.eval("B1 <- TRUE");
        
        selected = iniCovList.getSelectedItem();
        if(selected.toString().equals("complete.obs")) re.eval("B2 <- TRUE");
        else re.eval("B2 <- FALSE");
        
        /*if(this.B1.isSelected()) re.eval("B1 <- TRUE");
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
        else re.eval("B6 <- FALSE");*/
        
        /* construim la matriu BaseX */
        
        /*if(super.partitionILR == null || super.getPartition().length == 0){
            re.eval("BaseX <- NULL");
        }
        else{
            int[][] baseX = super.getPartition();
            re.assign("BaseX", baseX[0]);
            re.eval("BaseX" + " <- matrix( " + "BaseX" + " ,nc=1)");
            for(int i=1; i < baseX.length; i++){
                re.assign("tmp", baseX[i]);
                re.eval("BaseX" + " <- cbind(" + "BaseX" + ",matrix(tmp,nc=1))");
            }
        }*/
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
        this.framesEM_ZeroMissingMenu = new JFrame[numberOfGraphics];
        this.tempsDirR = new String[numberOfGraphics];
        for(int i=0; i < numberOfGraphics; i++){
            tempDirR = re.eval("cdp_res$graph[[" + String.valueOf(i+1) + "]]").asString();
            tempsDirR[i] = tempDirR;
            plotEM_ZeroMissingMenu(i);
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
        
        /*int numberOfNewVar = re.eval("length(names(cdp_res$new_data))").asInt();  numero de noves variables
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
        }*/
    }
    
    private void plotEM_ZeroMissingMenu(int position) {
            Font f = new Font("Arial", Font.PLAIN,12);
            UIManager.put("Menu.font", f);
            UIManager.put("MenuItem.font",f);
            JMenuBar menuBar = new JMenuBar();
            JMenu menu = new JMenu("File");
            JMenuItem menuItem = new JMenuItem("Open");
            menuBar.add(menu);
            framesEM_ZeroMissingMenu[position] = new JFrame();
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
            framesEM_ZeroMissingMenu[position].setJMenuBar(menuBar);
            panel.setSize(800,800);
            ImageIcon icon = new ImageIcon(tempDirR);
            JLabel label = new JLabel(icon,JLabel.CENTER);
            label.setSize(700, 700);
            panel.setLayout(new GridBagLayout());
            panel.add(label);
            framesEM_ZeroMissingMenu[position].getContentPane().add(panel);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            framesEM_ZeroMissingMenu[position].setSize(800,800);
            framesEM_ZeroMissingMenu[position].setLocation(dim.width/2-framesEM_ZeroMissingMenu[position].getSize().width/2, dim.height/2-framesEM_ZeroMissingMenu[position].getSize().height/2);
            
            WindowListener exitListener = new WindowAdapter(){
                
                @Override
                public void windowClosing(WindowEvent e){
                    int confirm = JOptionPane.showOptionDialog(null,"Are You Sure to Close Window?","Exit Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
                    if(confirm == 0){
                        framesEM_ZeroMissingMenu[position].dispose();
                        File file = new File(tempsDirR[position]);
                        file.delete();
                    }
                }
            };
            
            framesEM_ZeroMissingMenu[position].setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            framesEM_ZeroMissingMenu[position].addWindowListener(exitListener);
            framesEM_ZeroMissingMenu[position].setVisible(true);
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
                framesEM_ZeroMissingMenu[position].dispose();
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