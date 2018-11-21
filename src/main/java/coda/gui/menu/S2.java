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
import java.util.Arrays;
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
 * S2 -> X numerica i Y categorica amb opci� de retornar text, crear dataframe, afegir variables i  mostrar grafics
 * @author Guest2
 */
public class S2 extends AbstractMenuDialog2NumCat{
    
    Rengine re;
    DataFrame df;
    JFrame frameS2;
    JFrame[] framesS2;
    JFileChooser chooser;
    String[] tempsDirR;
    String tempDirR;
    
    public static final long serialVersionUID = 1L;
    
    public S2(final CoDaPackMain mainApp, Rengine r){
        super(mainApp,"Advanced Filter Menu", false,false,true);
        re = r;
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        String selectedNames1[] = ds.getSelectedData1();
        Vector<String> vSelectedNames1 = new Vector<String>(Arrays.asList(selectedNames1));
        String selectedNames2[] = ds.getSelectedData2();
        Vector<String> vSelectedNames2 = new Vector<String>(Arrays.asList(selectedNames2));
        
        if(selectedNames1.length > 0 && selectedNames2.length > 0){
            
            df = mainApplication.getActiveDataFrame();
            
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
            
            // create dataframe on R
            
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
                        
                this.dispose();
                
                // executem script d'R
                
                frameS2 = new JFrame();
                chooser = new JFileChooser();
                frameS2.setSize(600,400);
                chooser.setDialogTitle("Select R script to execute");
                chooser.setFileFilter(new FileNameExtensionFilter("R data file", "R", "rda"));
                chooser.setSize(400,400);
                frameS2.add(chooser);
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                frameS2.setLocation(dim.width/2-frameS2.getSize().width/2, dim.height/2-frameS2.getSize().height/2);
                
                if(chooser.showOpenDialog(frameS2) == JFileChooser.APPROVE_OPTION){
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
                    frameS2.dispose();
                }
        }
        else{
            if(selectedNames1.length == 0) JOptionPane.showMessageDialog(null,"No data selected in data 1");
            else JOptionPane.showMessageDialog(null,"No data selected in data 2");
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
        this.framesS2 = new JFrame[numberOfGraphics];
        this.tempsDirR = new String[numberOfGraphics];
        for(int i=0; i < numberOfGraphics; i++){
            tempDirR = re.eval("cdp_res$graph[[" + String.valueOf(i+1) + "]]").asString();
            tempsDirR[i] = tempDirR;
            plotS2(i);
        }   
    }
    
    void createVariables(){
        int numberOfNewVar = re.eval("length(names(cdp_res$new_data))").asInt(); /* numero de noves variables*/
        for(int i=0; i < numberOfNewVar; i++){
            String varName = re.eval("names(cdp_res$new_data)[" + String.valueOf(i+1) + "]").asString(); /* guardem el nom de la variable */
            String isNumeric = re.eval("class(unlist(cdp_res$new_data[[" + String.valueOf(i+1) + "]]))").asString();
            if(isNumeric.equals("numeric")){ /* creem variable numerica */
                double[] data = re.eval("as.numeric(unlist(cdp_res$new_data[[" + String.valueOf(i+1) + "]]))").asDoubleArray();
                df.addData(varName,data);
            }
            else{ /* crear variable categorica */
                String[] data = re.eval("as.character(unlist(cdp_res$new_data[[" + String.valueOf(i+1) + "]]))").asStringArray();
                df.addData(varName, new Variable(varName,data));
            }
            mainApplication.updateDataFrame(df);
        }
    }
    
    private void plotS2(int position) {
            Font f = new Font("Arial", Font.PLAIN,12);
            UIManager.put("Menu.font", f);
            UIManager.put("MenuItem.font",f);
            JMenuBar menuBar = new JMenuBar();
            JMenu menu = new JMenu("File");
            JMenuItem menuItem = new JMenuItem("Open");
            menuBar.add(menu);
            framesS2[position] = new JFrame();
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
            framesS2[position].setJMenuBar(menuBar);
            panel.setSize(800,800);
            ImageIcon icon = new ImageIcon(tempDirR);
            JLabel label = new JLabel(icon,JLabel.CENTER);
            label.setSize(700, 700);
            panel.setLayout(new GridBagLayout());
            panel.add(label);
            framesS2[position].getContentPane().add(panel);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            framesS2[position].setSize(800,800);
            framesS2[position].setLocation(dim.width/2-framesS2[position].getSize().width/2, dim.height/2-framesS2[position].getSize().height/2);
            
            WindowListener exitListener = new WindowAdapter(){
                
                @Override
                public void windowClosing(WindowEvent e){
                    int confirm = JOptionPane.showOptionDialog(null,"Are You Sure to Close Window?","Exit Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
                    if(confirm == 0){
                        framesS2[position].dispose();
                        File file = new File(tempsDirR[position]);
                        file.delete();
                    }
                }
            };
            
            framesS2[position].setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            framesS2[position].addWindowListener(exitListener);
            framesS2[position].setVisible(true);
    }
    
    private class quitListener implements ActionListener{
        
        int position;
        
        public quitListener(int position){
            this.position = position;
        }
        
        public void actionPerformed(ActionEvent e){
            int confirm = JOptionPane.showOptionDialog(null,"Are You Sure to Close Window?","Exit Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
            if(confirm == 0){
                framesS2[position].dispose();
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
