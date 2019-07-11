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
import javax.swing.UIManager;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

/**
 * GeoMeanPlotMenu -> X numerica i positiva amb opci� de retornar text, crear dataframe, afegir variables i  mostrar grafics
 * @author Guest2
 */
public class GeoMeanPlotMenu extends AbstractMenuDialog{
    
    Rengine re;
    DataFrame df;
    JFrame frameGeoMeanPlotMenu;
    JFrame[] framesGeoMeanPlotMenu;
    JFileChooser chooser;
    String tempDirR;
    String[] tempsDirR;
    
    public static final long serialVersionUID = 1L;
    
    public GeoMeanPlotMenu(final CoDaPackMain mainApp, Rengine r){
        super(mainApp, "GeoMeanPlotMenu menu", true);
        re = r;
        
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
        String selectedNames2[] = {super.ds.getSelectedGroup()};
        Vector<String> vSelectedNames2 = sortSelectedNames(selectedNames2);
        
        if(selectedNames.length > 0 && selectedNames2[0] != null){
            
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
                    
                        String url = CoDaPackConf.rScriptPath + "scripGeometricMeanBarplot (FET).R";
                        
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
                                Logger.getLogger(GeoMeanPlotMenu.class.getName()).log(Level.SEVERE, null, ex);
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
            if(selectedNames.length == 0) JOptionPane.showMessageDialog(null,"Please select data");
            else JOptionPane.showMessageDialog(null,"Please select categorical data");
        }
    }
    
    void showText(){
        
        REXP result;
        String[] sortida;
        
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
        this.framesGeoMeanPlotMenu = new JFrame[numberOfGraphics];
        this.tempsDirR = new String[numberOfGraphics];
        for(int i=0; i < numberOfGraphics; i++){
            tempDirR = re.eval("cdp_res$graph[[" + String.valueOf(i+1) + "]]").asString();
            tempsDirR[i] = tempDirR;
            plotGeoMeanPlotMenu();
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
    
    private void plotGeoMeanPlotMenu() throws IOException {
            Font f = new Font("Arial", Font.PLAIN,12);
            UIManager.put("Menu.font", f);
            UIManager.put("MenuItem.font",f);
            JMenuBar menuBar = new JMenuBar();
            JMenu menu = new JMenu("File");
            JMenuItem menuItem = new JMenuItem("Open");
            menuBar.add(menu);
            frameGeoMeanPlotMenu = new JFrame();
            JPanel panel = new JPanel();
            menu.add(menuItem);
            menuItem = new JMenuItem("Export");
            JMenu submenuExport = new JMenu("Export");
            menuItem = new JMenuItem("Export As PNG");
            menuItem.addActionListener(new FileChooserAction());
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
            menuItem.addActionListener(new quitListener());
            menu.add(submenuExport);
            menu.add(menuItem);
            frameGeoMeanPlotMenu.setJMenuBar(menuBar);
            panel.setSize(800,800);
            BufferedImage img = ImageIO.read(new File(tempDirR));
            ImageIcon icon = new ImageIcon(img);
            Image image = icon.getImage();
            Image newImg = image.getScaledInstance(panel.getWidth()-100, panel.getHeight()-100, Image.SCALE_SMOOTH);
            ImageIcon imageFinal = new ImageIcon(newImg);
            JLabel label = new JLabel(imageFinal);
            label.addComponentListener(new ComponentAdapter(){
                public void componentResized(ComponentEvent e){
                    JLabel label = (JLabel) e.getComponent();
                    Dimension size = label.getSize();
                    re.eval("mypath = tempdir()");
                    tempDirR = re.eval("print(mypath)").asString();
                    tempDirR += "\\out.png";
                
                    re.eval("png(base::paste(tempdir(),\"out.png\",sep=\"\\\\\"),width="+String.valueOf(size.width-100)+",height=" + String.valueOf(size.height-100) +")");
                    re.eval("png(mypath,width="+String.valueOf(size.width-100)+",height="+String.valueOf(size.height-100)+")");
                    re.eval("barplot(log(Num/Den), beside=TRUE,col=c(1:nCat))");
                    re.eval("dev.off()");
                    BufferedImage img = null;
                    try {
                        img = ImageIO.read(new File(tempDirR));
                    } catch (IOException ex) {
                        Logger.getLogger(ZpatternsMenu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ImageIcon icon = new ImageIcon(img);
                    Image image = icon.getImage();
                    Image newImg = image.getScaledInstance(size.width-100, size.height-100, Image.SCALE_SMOOTH);
                    ImageIcon imageFinal = new ImageIcon(newImg);
                    label.setIcon(imageFinal);
                }
            });
            panel.setLayout(new GridBagLayout());
            panel.add(label);
            frameGeoMeanPlotMenu.getContentPane().add(label);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            frameGeoMeanPlotMenu.setSize(800,800);
            frameGeoMeanPlotMenu.setLocation(dim.width/2-frameGeoMeanPlotMenu.getSize().width/2, dim.height/2-frameGeoMeanPlotMenu.getSize().height/2);
            
            WindowListener exitListener = new WindowAdapter(){
                
                @Override
                public void windowClosing(WindowEvent e){
                    int confirm = JOptionPane.showOptionDialog(null,"Are You Sure to Close Window?","Exit Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
                    if(confirm == 0){
                        frameGeoMeanPlotMenu.dispose();
                        File file = new File(tempDirR);
                        file.delete();
                    }
                }
            };
            
            frameGeoMeanPlotMenu.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frameGeoMeanPlotMenu.addWindowListener(exitListener);
            frameGeoMeanPlotMenu.setVisible(true);
    }

    public DataFrame getDataFrame() {
        return this.df;
    }
    
    private class quitListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            int confirm = JOptionPane.showOptionDialog(null,"Are You Sure to Close Window?","Exit Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
            if(confirm == 0){
                frameGeoMeanPlotMenu.dispose();
                File file = new File(tempDirR);
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