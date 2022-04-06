/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package coda.gui.menu;

import coda.gui.CoDaPackConf;
import coda.gui.CoDaPackMain;
import java.awt.Dimension;
import org.rosuda.JRI.Rengine;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
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
    
    private static final String yamlURL = CoDaPackConf.helpPath + "Personal.ImportarMenuPersonal.yaml";
    private static final String helpTitle = "Crear Menu Personal Help Menu";
    
    public AddToPersonalMenu(final CoDaPackMain mainApp, Rengine r){
        super(mainApp, "Import Menu");
        super.setHelpMenuConfiguration(yamlURL, helpTitle);
        
        JLabel jlZIP = new JLabel("    Select .zip:  ");
        jlZIP.setPreferredSize(new Dimension(150, 25));
        this.optionsPanel.add(jlZIP);
        
        //JTextField jl1 = new JTextField(newTxtDir);
        jl1.setPreferredSize(new Dimension(250, 25));
        this.optionsPanel.add(jl1);
        
        JButton P1 = new JButton("Select");
        this.optionsPanel.add(P1);
        this.optionsPanel.add(new JSeparator());
        P1.addActionListener(new java.awt.event.ActionListener(){
        
            public void actionPerformed(java.awt.event.ActionEvent evt){
                try {
                    selectZipFile();
                } catch (IOException ex) {
                    Logger.getLogger(AddToPersonalMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        /*
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
           */
    }
    
     void selectZipFile() throws IOException{
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        int returnValue = jfc.showOpenDialog(null);
        // int returnValue = jfc.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jfc.getSelectedFile();
                 
                String fileName = selectedFile.getAbsolutePath();
		String fe = FilenameUtils.getExtension(fileName);
                if ("zip".equals(fe)){

                    newTxtDir = selectedFile.getAbsolutePath();
                    jl1.setText(newTxtDir);
                    


                }else{
                    JOptionPane.showMessageDialog(null, selectedFile.getAbsolutePath()+" no es un archiu .zip");
                }
        }
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

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());
        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();
        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }
        return destFile;
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
        
        try{
            /*
            //---move txt ----
            InputStream in = new FileInputStream(fileTXT);
            OutputStream out = new FileOutputStream(targetTXTs+"\\"+fileTXT.getName());
            byte[] buf = new byte[1024];
            int len;
            
            while((len = in.read(buf)) > 0 ){
                out.write(buf, 0, len);
            }
            
            in.close();
            out.close();
            
            //---move R ----
            InputStream in2 = new FileInputStream(fileR);
            OutputStream out2 = new FileOutputStream(targetRs+"\\"+fileR.getName());
            byte[] buf2 = new byte[1024];
            int len2;
            
            while((len2 = in2.read(buf2)) > 0 ){
                out2.write(buf2, 0, len2);
            }
            
            in2.close();
            out2.close();
            */
            
            //
                    String fileZip = jl1.getText(); 
                    File destDir = new File(directoryName);
                    File destDirTXT = new File(targetTXTs);
                    File destDirR = new File(targetRs);
                    //
                    byte[] buffer = new byte[1024];
                    ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
                    ZipEntry zipEntry = zis.getNextEntry();
                    while (zipEntry != null) {
                        File newFile = newFile(destDir, zipEntry);
                        if (zipEntry.isDirectory()) {
                            System.out.println(zipEntry + "is directory");
                        }
                        else{
                            // fix for Windows-created archives
                            File parent = newFile.getParentFile();
                            if (!parent.isDirectory() && !parent.mkdirs()) {
                                throw new IOException("Failed to create directory " + parent);
                            }
                            
                            // write file content
                            FileOutputStream fos = new FileOutputStream(newFile);
                            int len;
                            while ((len = zis.read(buffer)) > 0) {
                                fos.write(buffer, 0, len);
                            }
                            fos.close();

                            System.out.println(zipEntry);
                            String fileNamePath = newFile.getAbsolutePath();
                            String fe = FilenameUtils.getExtension(fileNamePath);
                            if ("txt".equals(fe)){
                                //myFile.renameTo(new File("/the/new/place/newName.file"));
                                System.out.println("destDir: "+newFile.toString());
                                System.out.println("destDirTXT: "+destDirTXT+"\\"+newFile.getName().toString());
                                Files.move(Paths.get(newFile.toString()), Paths.get(destDirTXT+"\\"+newFile.getName().toString()), StandardCopyOption.REPLACE_EXISTING);
                            }
                            else if("R".equals(fe)){
                                System.out.println("destDir: "+newFile.toString());
                                System.out.println("destDirTXT: "+destDirR+"\\"+newFile.getName().toString());
                                Files.move(Paths.get(newFile.toString()), Paths.get(destDirR+"\\"+newFile.getName().toString()), StandardCopyOption.REPLACE_EXISTING);
                            }
                            else{
                                System.out.println("Arxiu inncessari ");
                            }
                        }
                        //zis.closeEntry();
                        zipEntry = zis.getNextEntry();
                        
                    }
                    zis.closeEntry();
                    zis.close();
            JOptionPane.showMessageDialog(null, "Menu important correctament");
        }catch(IOException ex){
            JOptionPane.showMessageDialog(null, "no s'ha pogut importar el menu");
        }
        /*
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
        }*/
    }
}
