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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import org.rosuda.JRI.Rengine;

/**
 * S1 -> X numerica i positiva amb opci� de retornar text, crear dataframe i  mostrar grafics
 * @author Guest2
 */
public class S1 extends AbstractMenuDialog{
    
    Rengine re;
    DataFrame df;
    JFrame frameS1;
    JFrame[] framesS1;
    JFileChooser chooser;
    String tempDirR;
    String[] tempsDirR;
    
    public static final long serialVersionUID = 1L;
    
    public S1(final CoDaPackMain mainApp, Rengine r){
        super(mainApp, "S1 menu", false);
        re = r;
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
                    if(data[i][j] <= 0.0) rowsToDeleteVect.add(j);
                }
            }
            
            if(rowsToDeleteVect.size() == df.getMaxVariableLength()) JOptionPane.showMessageDialog(null,"No positive data to analize");
            else{
                if(rowsToDeleteVect.size() > 0){ // if some row to ignore we transform the dataFrame
                    JOptionPane.showMessageDialog(null, "Some data will be ignored in the calcs");
                    int[] rowsToDelete = new int[rowsToDeleteVect.size()];
                    for(int i=0; i < rowsToDeleteVect.size();i++) rowsToDelete[i] = rowsToDeleteVect.elementAt(i);
                    transformedDataFrame.subFrame(rowsToDelete);
                        
                    // Create dataframe on R
                    
                        int auxPos = 0;
                        for(int i=0; i < transformedDataFrame.size();i++){ // totes les columnes
                            if(vSelectedNames.contains(transformedDataFrame.get(i).getName())){
                                re.eval(vSelectedNames.elementAt(auxPos) + " <- NULL");
                                if(transformedDataFrame.get(i).isNumeric()){
                                    for(double j : transformedDataFrame.get(i).getNumericalData()){
                                        re.eval(vSelectedNames.elementAt(auxPos) + " <- c(" + vSelectedNames.elementAt(auxPos) +"," + String.valueOf(j) + ")");
                                    }
                                }
                                else{
                                    for(String j : transformedDataFrame.get(i).getTextData()){
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
    
                }
                
                this.dispose();
                
                // executem script d'R
                
                frameS1 = new JFrame();
                chooser = new JFileChooser();
                frameS1.setSize(600,400);
                chooser.setDialogTitle("Select R script to execute");
                chooser.setFileFilter(new FileNameExtensionFilter("R data file", "R", "rda"));
                chooser.setSize(400,400);
                frameS1.add(chooser);
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                frameS1.setLocation(dim.width/2-frameS1.getSize().width/2, dim.height/2-frameS1.getSize().height/2);
                
                if(chooser.showOpenDialog(frameS1) == JFileChooser.APPROVE_OPTION){
                    String url = chooser.getSelectedFile().getAbsolutePath();
                    url = url.replaceAll("\\\\", "/");
                    re.eval("source(\"" + url + "\")");
                    /* posar les comandes que es volen aqui */
                    showText();
                    createDataFrame();
                    showGraphics();
                    createVariables();
                    /* aqui s'acaba les comandes que es volen */
                }
                else{
                    frameS1.dispose();
                }
                
            }
            
        }
        else{
            JOptionPane.showMessageDialog(null,"Please select data");
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
            re.eval("mymatrix <- data.matrix(cdp_res$dataframe[[" + String.valueOf(i+1) + "]])");
            double [][] resultsData = re.eval("mymatrix").asMatrix();
            DataFrame resultDataFrame = new DataFrame();
            String[] names = new String[df.getNames().size()];
            for(int j=0; j < names.length; j++) names[j] = df.getNames().get(i);
            resultDataFrame.addData(names,resultsData);
            resultDataFrame.setName(re.eval("names(cdp_res$dataframe)[" + String.valueOf(i+1) + "]").asString());
            mainApplication.addDataFrame(resultDataFrame);
        }
    }
    
    void showGraphics(){
        
        int numberOfGraphics = re.eval("length(cdp_res$graph)").asInt(); /* num de grafics */
        this.framesS1 = new JFrame[numberOfGraphics];
        this.tempsDirR = new String[numberOfGraphics];
        for(int i=0; i < numberOfGraphics; i++){
            tempDirR = re.eval("cdp_res$graph[[" + String.valueOf(i+1) + "]]").asString();
            tempsDirR[i] = tempDirR;
            plotS1(i);
        }     
    }
    
    void createVariables(){
        int numberOfNewVar = re.eval("length(names(cdp_res$new_data))").asInt(); /* numero de noves variables*/
        for(int i=0; i < numberOfNewVar; i++){
            String varName = re.eval("names(cdp_res$new_data)[" + String.valueOf(i+1) + "]").asString(); /* guardem el nom de la variable */
            String isNumeric = re.eval("toString(is.numeric(cdp_res$new_data[[" + String.valueOf(i+1) + "]]))").asString();
            if(isNumeric.equals("TRUE")){ /* creem variable numerica */
                double[] data = re.eval("cdp_res$new_data[[" + String.valueOf(i+1) + "]]").asDoubleArray();
                df.addData(varName,data);
            }
            else{ /* crear variable categorica */
                String[] data = re.eval("cdp_res$new_data[[" + String.valueOf(i+1) + "]]").asStringArray();
                df.addData(varName, new Variable(varName,data));
            }
            mainApplication.updateDataFrame(df);
        }
    }
    
    private void plotS1(int position) {
            Font f = new Font("Arial", Font.PLAIN,12);
            UIManager.put("Menu.font", f);
            UIManager.put("MenuItem.font",f);
            JMenuBar menuBar = new JMenuBar();
            JMenu menu = new JMenu("File");
            JMenuItem menuItem = new JMenuItem("Open");
            menuBar.add(menu);
            framesS1[position] = new JFrame();
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
            framesS1[position].setJMenuBar(menuBar);
            panel.setSize(800,800);
            ImageIcon icon = new ImageIcon(tempDirR);
            JLabel label = new JLabel(icon,JLabel.CENTER);
            label.setSize(700, 700);
            panel.setLayout(new GridBagLayout());
            panel.add(label);
            framesS1[position].getContentPane().add(panel);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            framesS1[position].setSize(800,800);
            framesS1[position].setLocation(dim.width/2-framesS1[position].getSize().width/2, dim.height/2-framesS1[position].getSize().height/2);
            
            WindowListener exitListener = new WindowAdapter(){
                
                @Override
                public void windowClosing(WindowEvent e){
                    int confirm = JOptionPane.showOptionDialog(null,"Are You Sure to Close Window?","Exit Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
                    if(confirm == 0){
                        framesS1[position].dispose();
                        File file = new File(tempsDirR[position]);
                        file.delete();
                    }
                }
            };
            
            framesS1[position].setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            framesS1[position].addWindowListener(exitListener);
            framesS1[position].setVisible(true);
    }
    
    private class quitListener implements ActionListener{
        
        int position;
        
        public quitListener(int position){
            this.position = position;
        }
        
        public void actionPerformed(ActionEvent e){
            int confirm = JOptionPane.showOptionDialog(null,"Are You Sure to Close Window?","Exit Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
            if(confirm == 0){
                framesS1[position].dispose();
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