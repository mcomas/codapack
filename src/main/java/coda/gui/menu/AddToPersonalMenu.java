/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package coda.gui.menu;

import coda.gui.CoDaPackMain;
import java.awt.Dimension;
import org.rosuda.JRI.Rengine;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import org.apache.commons.io.FilenameUtils;
/**
 *
 * @author dcano
 */
public class AddToPersonalMenu extends AbstractCrearMenu {
    CoDaPackMain mainApplication;
    String newTxtDir = " ", newRDir = " ";
    String dirTXTs = "", dirRs = "";
    
    JTextField jl1 = new JTextField(newTxtDir), jl2 = new JTextField(newRDir);
    
    public AddToPersonalMenu(final CoDaPackMain mainApp, Rengine r){
        super(mainApp, "Import Menu");
        //super.setHelpMenuConfiguration(yamlURL, helpTitle);
        
        
        JLabel jlTXT = new JLabel("    Select .txt:  ");
        jlTXT.setPreferredSize(new Dimension(150, 25));
        this.optionsPanel.add(jlTXT);
        
        //JTextField jl1 = new JTextField(newTxtDir);
        jl1.setPreferredSize(new Dimension(250, 25));
        this.optionsPanel.add(jl1);
        
        JButton P1 = new JButton("Select");
        this.optionsPanel.add(P1);
        this.optionsPanel.add(new JSeparator());
        P1.addActionListener(new java.awt.event.ActionListener(){
        
            public void actionPerformed(java.awt.event.ActionEvent evt){
               selectTxtFile();
            }
        });
        
        
        
        JLabel jlR = new JLabel("    Select .R:  ");
        jlR.setPreferredSize(new Dimension(150, 25));
        this.optionsPanel.add(jlR);
        
        //JTextField jl2 = new JTextField(newRDir);
        jl2.setPreferredSize(new Dimension(250, 25));
        this.optionsPanel.add(jl2);
        
        JButton P2 = new JButton("Select");
        this.optionsPanel.add(P2);
        this.optionsPanel.add(new JSeparator());
        P2.addActionListener(new java.awt.event.ActionListener(){
        
            public void actionPerformed(java.awt.event.ActionEvent evt){
               selectRFile();
            }
        });
           
    }
    
    void selectTxtFile(){
         JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        int returnValue = jfc.showOpenDialog(null);
        // int returnValue = jfc.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jfc.getSelectedFile();
                
                String fileName = selectedFile.getAbsolutePath();;
		String fe = FilenameUtils.getExtension(fileName);
		System.out.println("File extension is : "+fe);
                if ("txt".equals(fe)){
                    newTxtDir = selectedFile.getAbsolutePath();
                    jl1.setText(newTxtDir);
                }else{
                    JOptionPane.showMessageDialog(null, selectedFile.getAbsolutePath()+" no es un archiu txt");
                }
                //System.out.println(newTxtDir);
        }
        
    }
    
    void selectRFile(){
         JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        int returnValue = jfc.showOpenDialog(null);
        // int returnValue = jfc.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jfc.getSelectedFile();
                
                String fileName = selectedFile.getAbsolutePath();;
		String fe = FilenameUtils.getExtension(fileName);
		System.out.println("File extension is : "+fe);
                if ("R".equals(fe)){
                    newRDir = selectedFile.getAbsolutePath();
                    jl2.setText(newRDir);
                }else{
                    JOptionPane.showMessageDialog(null, selectedFile.getAbsolutePath()+" no es un archiu R");
                }
                //System.out.println(newRDir);
        }
    }


    @Override
    public void acceptButtonActionPerformed(){
        //move dir Txts i dirRs a 
        Path path = Paths.get("");
        String directoryName = path.toAbsolutePath().toString();
        String targetTXTs = directoryName+"\\menus_personalitzables";
        String targetRs = directoryName+"\\Scripts_Amb_Base";
        
        File fileTXT = new File(newTxtDir);
        File fileR = new File(newRDir);
       
        boolean boolTxt = false, boolR = false;
        if (fileTXT.renameTo(new File(targetTXTs+"\\"+ fileTXT.getName()))) {
            boolTxt = true;
            System.out.println("File is moved to " + targetTXTs+"\\" + fileTXT.getName());
        } else {
            System.out.println("Failed");
        }
        if (fileR.renameTo(new File(targetRs+"\\"+ fileR.getName()))) {
            boolR = true;
            System.out.println("File is moved to " + targetRs+"\\" + fileR.getName());
        } else {
            System.out.println("Failed");
        }
        
        if (boolTxt && boolR){
            JOptionPane.showMessageDialog(null, "Menu importat correctament");
        }else{
            JOptionPane.showMessageDialog(null, "No s'ha pogut importar el menu");
        }
    }
}
