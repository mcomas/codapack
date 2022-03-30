/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package coda.gui.menu;

import coda.gui.CoDaPackMain;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import org.apache.commons.io.FilenameUtils;
import org.rosuda.JRI.Rengine;

/**
 *
 * @author dcano
 */
public class ExportPersonalMenu extends AbstractCrearMenu{
    CoDaPackMain mainApplication;
    String newTxtDir = " ", newRDir = " ";
    String dirTXTs = "", dirRs = "";
    
    JTextField jl1 = new JTextField(newTxtDir), jl2 = new JTextField(newRDir), jl3 = new JTextField("");
    
    public ExportPersonalMenu(final CoDaPackMain mainApp, Rengine r){
        super(mainApp, "Export Menu");
        //super.setHelpMenuConfiguration(yamlURL, helpTitle);
        
        //selector archiu txt
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
        
       
        /*
        //selector archiu R
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
        //selector archiu Desti
        JLabel jlDesti = new JLabel("    Select Destination:  ");
        jlDesti.setPreferredSize(new Dimension(150, 25));
        this.optionsPanel.add(jlDesti);
        
        //JTextField jl2 = new JTextField(newRDir);
        jl3.setPreferredSize(new Dimension(250, 25));
        this.optionsPanel.add(jl3);
        
        JButton P3 = new JButton("Select");
        this.optionsPanel.add(P3);
        this.optionsPanel.add(new JSeparator());
        P3.addActionListener(new java.awt.event.ActionListener(){
        
            public void actionPerformed(java.awt.event.ActionEvent evt){
               selectDestination();
            }
        });
           
    }
    
    void selectTxtFile(){
        JFileChooser jfc = new JFileChooser(System.getProperty("user.dir")+"\\menus_personalitzables");

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
                    File txtFil = new File(newTxtDir);
                    buscaArchiuR(txtFil);
                }else{
                    JOptionPane.showMessageDialog(null, selectedFile.getAbsolutePath()+" no es un archiu txt");
                }
                //System.out.println(newTxtDir);
        }
        
    }
    
    void buscaArchiuR(File archivoTXT){
        String rFileName;
        String fileName = archivoTXT.getAbsolutePath();
                Scanner scan;
                try {
                    boolean trobat = false;
                    String lineaAnterior = "";
                    //llegeix archiu
                    scan = new Scanner(new File(fileName));
                    while(scan.hasNextLine() && !trobat){
                        String line = scan.nextLine();
                        if(line.equals("Exec:") || line.equals("exec:"))lineaAnterior = "Exec:";
                        else if(lineaAnterior == "Exec:" && line != ""){
                            rFileName = line;
                            
                            //---Buscar archiu
                            //buscar(rFileName, Paths.get(".").toFile());
                            File aux = new File(System.getProperty("user.dir")+"\\Scripts_Amb_Base\\"+rFileName);
                            String fileNameR = aux.getAbsolutePath();
                            String fe = FilenameUtils.getExtension(fileNameR);
                            System.out.println("File extension is : "+fe);
                            if ("R".equals(fe)){
                                newRDir = aux.getAbsolutePath();
                                jl2.setText(newRDir);
                            }else{
                                JOptionPane.showMessageDialog(null, aux.getAbsolutePath()+" no es un archiu R");
                            }
                            System.out.println("newRDir: "+newRDir);
                            //---
                            trobat= true;
                        }
                    }
                }catch(IOException ex){
                    JOptionPane.showMessageDialog(null, "No s'ha trobat archiu .R");
                }
    }
    
    private static File buscar(String archivoABuscar, File directorio) {
    File[] archivos = directorio.listFiles();
    for (File archivo : archivos) {
        if (archivo.getName().equals(archivoABuscar)) {
            return archivo;
        }
        if (archivo.isDirectory()) {
            File resultadoRecursion = buscar(archivoABuscar, archivo);
            if (resultadoRecursion != null) {
                return resultadoRecursion;
            }
        }
    }
    return null;
}
    
    
    void selectRFile(){
         JFileChooser jfc = new JFileChooser(System.getProperty("user.dir")+"\\Scripts_Amb_Base");

        int returnValue = jfc.showOpenDialog(null);
        // int returnValue = jfc.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jfc.getSelectedFile();
                
                String fileName = selectedFile.getAbsolutePath();
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

    void selectDestination(){
        
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
         // Guardar el directorio seleccionado chooser.showSaveDialog (parent);
        Component parent = null;
        int returnVal = chooser.showSaveDialog(parent);

         // Obtener el objeto de archivo seleccionado JFileChooser.APPROVE_OPTION
         // Si el directorio guardado es consistente con el objeto de archivo seleccionado, devolverá 0 si tiene éxito
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            // obtener ruta
            String selectPath = chooser.getSelectedFile().getPath();
            jl3.setText(selectPath);
            //JOptionPane.showMessageDialog(null, "El directorio que elija es:" + selectPath);
        }else{
            JOptionPane.showMessageDialog(null, "Directori incorrecte");
        }
        
    }

    @Override
    public void acceptButtonActionPerformed(){
        
        //move dir Txts i dirRs a 
        Path path = Paths.get("");
        String directoryName = path.toAbsolutePath().toString();
        //String targetTXTs = directoryName+"\\menus_personalitzables";
        //String targetRs = directoryName+"\\Scripts_Amb_Base";
        
        
        File fileTXT = new File(newTxtDir);
        Path fileTXTPath = Paths.get(newTxtDir);
        File fileR = new File(newRDir);
        Path fileRPath = Paths.get(newRDir);
        
        String targetS = jl3.getText();
        File targetTXT = new File(targetS+"\\"+fileTXT.getName());
        File targetR = new File(targetS+"\\"+fileR.getName());
        Path targetPath = Paths.get(targetS);
       
        try {
            
//------Crea carpeta amb els archius necessaris dins
            //crear directorio
            File directorio = new File(targetS+"\\export_Coda\\");
            if (!directorio.exists()) {
                if (directorio.mkdirs()) {
                    System.out.println("Directorio creado");
                }
            }
            //----move txt---
            InputStream in = new FileInputStream(fileTXT);
            OutputStream out = new FileOutputStream(directorio+"\\"+fileTXT.getName());
           System.out.println(directorio+"\\"+fileTXT.getName());
            byte[] buf = new byte[1024];
            int len;

            while ((len = in.read(buf)) > 0) {
              out.write(buf, 0, len);
            }
            
            in.close();
            out.close();
            
             //----move R---
            InputStream in2 = new FileInputStream(fileR);
            OutputStream out2 = new FileOutputStream(directorio+"\\"+fileR.getName());
            System.out.println(directorio+"\\"+fileR.getName());
            byte[] buf2 = new byte[1024];
            int len2;

            while ((len2 = in2.read(buf2)) > 0) {
              out2.write(buf2, 0, len2);
            }
            
            in2.close();
            out2.close();
            


//---------- funciona: seleccion de los dos archivos i del destino
/*
            //----move txt---
            InputStream in = new FileInputStream(fileTXT);
            OutputStream out = new FileOutputStream(targetTXT);
            byte[] buf = new byte[1024];
            int len;

            while ((len = in.read(buf)) > 0) {
              out.write(buf, 0, len);
            }
            
            in.close();
            out.close();
            
             //----move R---
            InputStream in2 = new FileInputStream(fileR);
            OutputStream out2 = new FileOutputStream(targetR);
            byte[] buf2 = new byte[1024];
            int len2;

            while ((len2 = in.read(buf2)) > 0) {
              out.write(buf2, 0, len2);
            }
            
            in2.close();
            out2.close();
*/
//-------            
            JOptionPane.showMessageDialog(null, "Menu exportat correctament");
            
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "No s'ha pogut exportar el menu");
        }
    }
}
