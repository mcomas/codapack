/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.gui.menu;

import coda.DataFrame;
import coda.gui.CoDaPackMain;
import static coda.gui.CoDaPackMain.outputPanel;
import coda.gui.output.OutputElement;
import coda.gui.output.OutputForR;
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
 *
 * @author Guest2
 */
public class S4 extends AbstractMenuDialog2NumOCatNumOCat{
    
    Rengine re;
    DataFrame df;
    JFrame frameS4;
    String tempDirR;
    
    public static final long serialVersionUID = 1L;
    
    public S4(final CoDaPackMain mainApp, Rengine r){
        super(mainApp,"S4 menu",false,false,true);
        re = r;
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        String selectedNames1[] = super.ds.getSelectedData1();
        String selectedNames2[] = super.ds.getSelectedData2();
        Vector<String> vSelectedNames1 = new Vector<String>(Arrays.asList(selectedNames1));
        Vector<String> vSelectedNames2 = new Vector<String>(Arrays.asList(selectedNames2));
        
        if(selectedNames1.length > 0 && selectedNames2.length > 0){
            
            df = mainApplication.getActiveDataFrame();
            double[][] numericData = df.getNumericalData(selectedNames1);
            
            // Create X matrix
            
            int aux = 0;
            for(int i=0; i < df.size(); i++){
                if(vSelectedNames1.contains(df.get(i).getName())){
                    if(aux == 0){
                        if(df.get(i).isNumeric()){
                            re.assign("X", df.get(i).getNumericalData());
                            re.eval("X" + " <- matrix( " + "X" + " ,nc=1)");
                        }
                        else{
                            re.assign("X", df.get(i).getTextData());
                            re.eval("X" + " <- matrix( " + "X" + " ,nc=1)");
                        }
                    }
                    else{
                        if(df.get(i).isNumeric()){
                            re.assign("tmp", df.get(i).getNumericalData());
                            re.eval("X" + " <- cbind(" + "X" + ",matrix(as.numeric(tmp),nc=1))");
                        }
                        else{
                            re.assign("tmp", df.get(i).getTextData());
                            re.eval("X" + " <- cbind(" + "X" + ",matrix(tmp,nc=1))");
                        }
                    }
                    aux++;
                }
            }
            
            // Create Y matrix
            
            aux = 0;
            for(int i=0; i < df.size(); i++){
                if(vSelectedNames2.contains(df.get(i).getName())){
                    if(aux == 0){
                        if(df.get(i).isNumeric()){
                            re.assign("Y", df.get(i).getNumericalData());
                            re.eval("Y" + " <- matrix( " + "Y" + " ,nc=1)");
                        }
                        else{
                            re.assign("Y", df.get(i).getTextData());
                            re.eval("Y" + " <- matrix( " + "Y" + " ,nc=1)");
                        }
                    }
                    else{
                        if(df.get(i).isNumeric()){
                            re.assign("tmp", df.get(i).getNumericalData());
                            re.eval("Y" + " <- cbind(" + "Y" + ",matrix(as.numeric(tmp),nc=1))");
                        }
                        else{
                            re.assign("tmp", df.get(i).getTextData());
                            re.eval("Y" + " <- cbind(" + "Y" + ",matrix(tmp,nc=1))");
                        }
                    }
                    aux++;
                }
            }
            
                String url = System.getProperty("user.dir") + "\\resources\\SumScript.R".toString();
                url = url.replaceAll("\\\\", "/");
                re.eval("source(\"" + url + "\")");
        }
        else{
            if(selectedNames1.length == 0) JOptionPane.showMessageDialog(null, "No data selected in data 1");
            else JOptionPane.showMessageDialog(null, "No data selected in data 2");
        }
    }
    
    void showText(){
        
        re.eval("out <- capture.output(text)");
        OutputElement e = new OutputForR(re.eval("out").asStringArray());
        outputPanel.addOutput(e);
    }
    
    void createDataFrame(){
        
        re.eval("mymatrix <- data.matrix(df)");
        double[][] resultsData = re.eval("mymatrix").asMatrix();
        DataFrame resultDataFrame = new DataFrame();
        String[]names = new String[df.getNames().size()];
        for(int i=0; i < names.length;i++) names[i] = df.getNames().get(i);
        resultDataFrame.addData(names, resultsData);
        mainApplication.addDataFrame(resultDataFrame);
    }
    
    void showGraphics(){
        
        int numberOfGraphics = re.eval("length(grafic)").asInt();
        for(int i=0; i < numberOfGraphics; i++){
            tempDirR = re.eval("grafic[" + String.valueOf(numberOfGraphics+1) + "]").asString();
            plotZPatterns();
        }   
    }
    
    private void plotZPatterns() {
            Font f = new Font("Arial", Font.PLAIN,12);
            UIManager.put("Menu.font", f);
            UIManager.put("MenuItem.font",f);
            JMenuBar menuBar = new JMenuBar();
            JMenu menu = new JMenu("File");
            JMenuItem menuItem = new JMenuItem("Open");
            menuBar.add(menu);
            frameS4 = new JFrame();
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
            menuItem.addActionListener(new quitListener());
            menu.add(submenuExport);
            menu.add(menuItem);
            frameS4.setJMenuBar(menuBar);
            panel.setSize(800,800);
            ImageIcon icon = new ImageIcon(tempDirR);
            JLabel label = new JLabel(icon,JLabel.CENTER);
            label.setSize(700, 700);
            panel.setLayout(new GridBagLayout());
            panel.add(label);
            frameS4.getContentPane().add(panel);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            frameS4.setSize(800,800);
            frameS4.setLocation(dim.width/2-frameS4.getSize().width/2, dim.height/2-frameS4.getSize().height/2);
            
            WindowListener exitListener = new WindowAdapter(){
                
                @Override
                public void windowClosing(WindowEvent e){
                    int confirm = JOptionPane.showOptionDialog(null,"Are You Sure to Close Window?","Exit Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
                    if(confirm == 0){
                        frameS4.dispose();
                        File file = new File(tempDirR);
                        file.delete();
                    }
                }
            };
            
            frameS4.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frameS4.addWindowListener(exitListener);
            frameS4.setVisible(true);
    }
    
    private class quitListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            int confirm = JOptionPane.showOptionDialog(null,"Are You Sure to Close Window?","Exit Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
            if(confirm == 0){
                frameS4.dispose();
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
