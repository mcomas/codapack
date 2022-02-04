/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.gui.menu;

import coda.Variable;
import coda.gui.CoDaPackMain;
import coda.gui.output.OutputElement;
import coda.gui.output.OutputForR;
import coda.gui.output.OutputText;
import coda.gui.utils.BinaryPartitionSelect;
import coda.gui.utils.FileNameExtensionFilter;
import java.awt.Dimension;
//import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.util.Arrays;
import java.util.Vector;
//import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
//import javax.swing.JPanel;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import coda.DataFrame;
import static coda.gui.CoDaPackMain.outputPanel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.io.File;     
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import org.apache.batik.swing.JSVGCanvas;

/**
 * T1-> menu que administra els archius R i executa depenent del archiu de text creat
 * @author dcano98
 */
public class T1 extends AbstractMenuGeneral{
     
    Rengine re;
    DataFrame df;
    
    JFrame frameT1;
    Vector<JFrame> framesT1;
    JFileChooser chooser;
    String tempDirR;
    Vector<String> tempsDirR;
    
    JTextArea areaPart = new JTextArea(5,5);
    
    /* options var */
    ILRMenu ilrX = null ;
    //ILRMenu ilrY = null;
    
    // Declaracio Array de 'options'
    ArrayList<String> BotonsText = new ArrayList<String>();
    ArrayList<JTextField> TextBotonsText = new ArrayList<JTextField>();
    ArrayList<String> NomsBotonsBool = new ArrayList<String>();
    ArrayList<JRadioButton> EstatBotonsBool = new ArrayList<JRadioButton>();
    ArrayList<javax.swing.JComboBox> ArrayComboBox = new ArrayList<javax.swing.JComboBox>();
    

    
    public static final long serialVersionUID = 1L;
    
    //variables globals de T1 per saber que es necessari al executar
    boolean groups;
    boolean twoVars = false, valid = true, archiuEntratTXT = false;
    String nomArchiu;
    
    public T1(final CoDaPackMain mainApp, Rengine r){
        super(mainApp, "T1 menu");
        re = r;
        
        String archiuTXT = mainApp.getNameTXT();
        boolean datavailable ;
        //----- hi ha data carregada? -----
        try {
            
            datavailable = true;
        }
        catch(NullPointerException e) {
            System.out.println("NullPointerException thrown!");
            datavailable = false;
            
        }
        /*(!mainApp.getActiveDataFrame().getNames().isEmpty()){
            ArrayList<String> names = new ArrayList<String>(mainApp.getActiveDataFrame().getNames());

            for(int s = 0; s<names.size(); s++){
                System.out.println(names.get(s));
            }
        }
        else{
            System.out.println("no hi ha data a seleccionar");
        }*/
        //System.out.println(mainApplication.getActiveDataFrame().getNames());
        
        //---------------------------------
        if(datavailable){
            /*JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = fileChooser.showOpenDialog(fileChooser);
            if (result == JFileChooser.APPROVE_OPTION) {
                //selecciona archiu per llegir
            */
            File archivoTXT;
            try{
                
                String canonicalPath = new File("./menus_personalitzables/"+archiuTXT).getCanonicalPath();
                

                String usingSystemProperty = System.getProperty("user.dir");
                
                archivoTXT = new File(canonicalPath);
            
            
                                
                //File selectedFile = fileChooser.getSelectedFile();
                String fileName = archivoTXT.getAbsolutePath();
                Scanner scan;
                try {
                    //llegeix archiu
                    scan = new Scanner(new File(fileName));
                    //variables auxiliars per saber quin tipus de variables crear en cada moment
                    String lineaAnterior = "";
                    String Tipusvar = "";
                    int NumVar = 0, cont = 0;
                    groups =false;
                    while(scan.hasNextLine()){//mentre no acabi el document

                        String line = scan.nextLine();
                        //llegeix el tipus de botons, entrades de text, particions a crear
                        if(line.equals("Exec:") || line.equals("exec:")){lineaAnterior = "Exec:";
                        }else if(line.equals("Groups:") || line.equals("groups:") ){ lineaAnterior = "Groups:";
                        }else if(line.equals("Variables:") || line.equals("variables:")){lineaAnterior = "Variables:";
                        }else if(line.equals("Selector:") || line.equals("selector:")){
                            lineaAnterior = "Selector:";
                            //groupsComboBox.setEnabled(true);
                            javax.swing.JComboBox groupsComboBox = new javax.swing.JComboBox();
                            ArrayComboBox.add(groupsComboBox);
                        }else if(line.equals("Name of Variables:") || line.equals("name of variables:") || line.equals("Name of variables:")){
                            lineaAnterior = "Name of Variables:";
                        }
                        else if(line.equals("Partitions:") || line.equals("partitions:") ){

                            lineaAnterior = "Partitions:";
                            //cont = 0;
                            /*
                            this.optionsPanel.add(new JLabel("Defined partition:"));
                            JScrollPane jScrollPane1 = new JScrollPane();
                            jScrollPane1.setPreferredSize(new Dimension(185,150));

                            jScrollPane1.setViewportView(areaPart);
                            this.optionsPanel.add(jScrollPane1);
                            JRadioButton part, base;
                            part = new JRadioButton("Partition");
                            part.setSelected(true);
                            base = new JRadioButton("Base");
                            base.setEnabled(false);
                            this.optionsPanel.add(new JSeparator());*/

                        }
                        //----------------------------------------------------------
                        //crea els botons, entrades de text, particions... que ha llegit previament amb el nom de cada linea
                        if(lineaAnterior.equals("Exec:") && !line.equals("Exec:") && !line.equals("exec:")){
                            if(lineaAnterior.equals("Exec:") && !line.isEmpty()){
                                //comprovar que el archiu existeix
                                File archivo = new File("./Scripts_Amb_Base/"+line);
                                if (!archivo.exists()) {
                                    JOptionPane.showMessageDialog(null,"OJO: No existe el archivo de ejecucion!!");
                                    valid = false;
                                }else{
                                    if (archivo.isFile()){ 
                                        nomArchiu = archivo.toString(); 
                                        valid = true;
                                        archiuEntratTXT =true;
                                    }
                                    else {
                                        JOptionPane.showMessageDialog(null,"El archiu "+ line +" no es un archiu executable");
                                        valid = false;
                                    }
                                }
                                lineaAnterior = "";
                            }   
                        }
                        else if(lineaAnterior.equals("Groups:") && !line.equals("Groups:") && !line.equals("groups:")){
                            if(lineaAnterior.equals("Groups:") && !line.isEmpty()){
                                if(line.equals("TRUE") || line.equals("True") || line.equals("true")) groups =true;
                                else if(line.equals("FALSE") || line.equals("False") || line.equals("false")) groups =false;
                                else{}//re = r;
                                lineaAnterior = "";
                            }
                        }
                        else if(lineaAnterior.equals("Variables:")&& !line.equals("Variables:") && !line.equals("variables:")){
                            if(lineaAnterior.equals("Variables:") && !line.isEmpty()){
                                NumVar = Integer.parseInt(line);
                                if(NumVar == 2){
                                    twoVars = true;
                                }
                            }
                        }
                        if( lineaAnterior.equals("Selector:")&& !line.equals("Selector:") && !line.equals("selector:")){
                            //guardar noms selector
                            if(!line.isEmpty()){
                                ArrayComboBox.get(ArrayComboBox.size()-1).addItem(line);
                                optionsPanel.add(ArrayComboBox.get(ArrayComboBox.size()-1));
                                this.optionsPanel.add(new JSeparator());
                            }
                        }
                        else if(lineaAnterior.equals("Name of Variables:") && !line.equals("Name of Variables:") && !line.equals("name of variables:") && !line.equals("Name of variables:")){

                            if(line.equals("Bool:")|| line.equals("Text:")){
                                Tipusvar = line;
                                this.optionsPanel.add(new JSeparator());
                            }
                            else{
                                if(Tipusvar.equals("Bool:")){
                                    if(!line.isEmpty()){
                                        JRadioButton newbutton = new JRadioButton(line);
                                        NomsBotonsBool.add(line);
                                        EstatBotonsBool.add(newbutton);
                                        this.optionsPanel.add(newbutton);
                                        this.optionsPanel.add(new JSeparator());
                                    }
                                }
                                else if (Tipusvar.equals("Text:")){
                                    if(!line.isEmpty()){
                                        char[] aCaracters = line.toCharArray();
                                        boolean textDefb = false;
                                        String textDef = "", newLine = "";
                                        for(int a=2; a<aCaracters.length; a++){
                                            if(textDefb)textDef += aCaracters[a]; 

                                            if(aCaracters[a]==':' && aCaracters[a-1] == 'd' && aCaracters[a-2] == 't'){textDefb = true;}

                                            if(!textDefb) newLine += aCaracters[a-2];
                                        }
                                        if(!textDefb){
                                            newLine+= aCaracters[aCaracters.length-2]+""+aCaracters[aCaracters.length-1];
                                        }

                                        JTextField P1 = new JTextField(textDef,20);
                                        BotonsText.add(newLine);
                                        TextBotonsText.add(P1);
                                        this.optionsPanel.add(new JLabel("      "+newLine+":"));
                                        this.optionsPanel.add(P1);
                                        this.optionsPanel.add(new JSeparator());
                                    }
                                }
                            }
                        }

                        if( lineaAnterior.equals("Partitions:") && !line.equals("Partitions:") && !line.equals("partitions:") ){
                            if(!line.isEmpty()){
                                /*JButton xILR = new JButton(line);
                                this.optionsPanel.add(xILR);
                                xILR.addActionListener(new java.awt.event.ActionListener(){

                                    public void actionPerformed(java.awt.event.ActionEvent evt){
                                       configureILRX();
                                    }
                                });*/
                                JButton manuallyPart = new JButton(line);
                                optionsPanel.add(manuallyPart);
                                manuallyPart.addActionListener(new java.awt.event.ActionListener(){

                                    public void actionPerformed(java.awt.event.ActionEvent evt){
                                        //initiatePartitionMenu();
                                        configureILRX();
                                    }
                                });
                                this.optionsPanel.add(new JSeparator());
                            }
                        }
                    }
                    //amb totes les variable ja llegides adapta el abstractMenu a les opcions entrades
                    //ex: amb groups, amb m�s d'una variable...
                    tempsDirR = new Vector<String>();
                    framesT1 = new Vector<JFrame>();
                    if(valid)super.activeGroups(mainApp, groups, twoVars, df);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(T1.class.getName()).log(Level.SEVERE, null, ex);
                }
            }catch (IOException e) {
                System.out.println("IOException Occured" + e.getMessage());
            }
        }else{
            JOptionPane.showMessageDialog(null,"Please select data");
        }
        
    }
    
    public T1(final CoDaPackMain mainApp, Rengine r, String nom){
        super(mainApp, "T1 menu");
        re = r;
        
        String archiuTXT = nom;
        
            File archivoTXT;
            try{
                
                String canonicalPath = new File("./menus_personalitzables/"+archiuTXT).getCanonicalPath();

                String usingSystemProperty = System.getProperty("user.dir");
                archivoTXT = new File(canonicalPath);
            
            
                
                //File selectedFile = fileChooser.getSelectedFile();
                String fileName = archivoTXT.getAbsolutePath();
                Scanner scan;
                try {
                    //llegeix archiu
                    scan = new Scanner(new File(fileName));
                    //variables auxiliars per saber quin tipus de variables crear en cada moment
                    String lineaAnterior = "";
                    String Tipusvar = "";
                    int NumVar = 0, cont = 0;
                    groups =false;
                    while(scan.hasNextLine()){//mentre no acabi el document

                        String line = scan.nextLine();
                        //llegeix el tipus de botons, entrades de text, particions a crear
                        if(line.equals("Exec:") || line.equals("exec:")){lineaAnterior = "Exec:";
                        }else if(line.equals("Groups:") || line.equals("groups:") ){ lineaAnterior = "Groups:";
                        }else if(line.equals("Variables:") || line.equals("variables:")){lineaAnterior = "Variables:";
                        }else if(line.equals("Selector:") || line.equals("selector:")){
                            lineaAnterior = "Selector:";
                            javax.swing.JComboBox groupsComboBox = new javax.swing.JComboBox();
                            ArrayComboBox.add(groupsComboBox);
                        }else if(line.equals("Name of Variables:") || line.equals("name of variables:") || line.equals("Name of variables:")){
                            lineaAnterior = "Name of Variables:";
                        }
                        else if(line.equals("Partitions:") || line.equals("partitions:") ){

                            lineaAnterior = "Partitions:";
                            

                        }
                        
                        //crea els botons, entrades de text, particions... que ha llegit previament amb el nom de cada linea
                        if(lineaAnterior.equals("Exec:") && !line.equals("Exec:") && !line.equals("exec:")){
                            if(lineaAnterior.equals("Exec:") && !line.isEmpty()){
                                //comprovar que el archiu existeix
                                File archivo = new File("./Scripts_Amb_Base/"+line);
                                if (!archivo.exists()) {
                                    JOptionPane.showMessageDialog(null,"OJO: No existe el archivo de configuracion!!");
                                    valid = false;
                                }else{
                                    if (archivo.isFile()){ 
                                        nomArchiu = archivo.toString(); 
                                        valid = true;
                                        archiuEntratTXT =true;
                                    }
                                    else {
                                        JOptionPane.showMessageDialog(null,"El archiu "+ line +" no es un archiu executable");
                                        valid = false;
                                    }
                                }
                                lineaAnterior = "";
                            }   
                        }
                        else if(lineaAnterior.equals("Groups:") && !line.equals("Groups:") && !line.equals("groups:")){
                            if(lineaAnterior.equals("Groups:") && !line.isEmpty()){
                                if(line.equals("TRUE") || line.equals("True") || line.equals("true")) groups =true;
                                else if(line.equals("FALSE") || line.equals("False") || line.equals("false")) groups =false;
                                else{}//re = r;
                                lineaAnterior = "";
                            }
                        }
                        else if(lineaAnterior.equals("Variables:")&& !line.equals("Variables:") && !line.equals("variables:")){
                            if(lineaAnterior.equals("Variables:") && !line.isEmpty()){
                                NumVar = Integer.parseInt(line);
                                if(NumVar == 2){
                                    twoVars = true;
                                }
                            }
                        }
                        if( lineaAnterior.equals("Selector:")&& !line.equals("Selector:") && !line.equals("selector:")){
                            //guardar noms selector
                            if(!line.isEmpty()){
                                ArrayComboBox.get(ArrayComboBox.size()-1).addItem(line);
                                optionsPanel.add(ArrayComboBox.get(ArrayComboBox.size()-1));
                                this.optionsPanel.add(new JSeparator());
                            }
                        }
                        else if(lineaAnterior.equals("Name of Variables:") && !line.equals("Name of Variables:") && !line.equals("name of variables:") && !line.equals("Name of variables:")){

                            if(line.equals("Bool:")|| line.equals("Text:")){
                                Tipusvar = line;
                                this.optionsPanel.add(new JSeparator());
                            }
                            else{
                                if(Tipusvar.equals("Bool:")){
                                    if(!line.isEmpty()){
                                        JRadioButton newbutton = new JRadioButton(line);
                                        NomsBotonsBool.add(line);
                                        EstatBotonsBool.add(newbutton);
                                        this.optionsPanel.add(newbutton);
                                        this.optionsPanel.add(new JSeparator());
                                    }
                                }
                                else if (Tipusvar.equals("Text:")){
                                    if(!line.isEmpty()){
                                        char[] aCaracters = line.toCharArray();
                                        boolean textDefb = false;
                                        String textDef = "", newLine = "";
                                        for(int a=2; a<aCaracters.length; a++){
                                            if(textDefb)textDef += aCaracters[a]; 

                                            if(aCaracters[a]==':' && aCaracters[a-1] == 'd' && aCaracters[a-2] == 't'){textDefb = true;}

                                            if(!textDefb) newLine += aCaracters[a-2];
                                        }
                                        if(!textDefb){
                                            newLine+= aCaracters[aCaracters.length-2]+""+aCaracters[aCaracters.length-1];
                                        }

                                        JTextField P1 = new JTextField(textDef,20);
                                        BotonsText.add(newLine);
                                        TextBotonsText.add(P1);
                                        this.optionsPanel.add(new JLabel("      "+newLine+":"));
                                        this.optionsPanel.add(P1);
                                        this.optionsPanel.add(new JSeparator());
                                    }
                                }
                            }
                        }

                        if( lineaAnterior.equals("Partitions:") && !line.equals("Partitions:") && !line.equals("partitions:") ){
                            if(!line.isEmpty()){
                                
                                JButton manuallyPart = new JButton(line);
                                optionsPanel.add(manuallyPart);
                                manuallyPart.addActionListener(new java.awt.event.ActionListener(){

                                    public void actionPerformed(java.awt.event.ActionEvent evt){
                                        //initiatePartitionMenu();
                                        configureILRX();
                                    }
                                });
                                this.optionsPanel.add(new JSeparator());
                            }
                        }
                    }
                    //amb totes les variable ja llegides adapta el abstractMenu a les opcions entrades
                    //ex: amb groups, amb m�s d'una variable...
                    tempsDirR = new Vector<String>();
                    framesT1 = new Vector<JFrame>();
                    if(valid)super.activeGroups2(mainApp, groups, twoVars, df);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(T1.class.getName()).log(Level.SEVERE, null, ex);
                }
            }catch (IOException e) {
                System.out.println("IOException Occured" + e.getMessage());
            }
        
        
    }
    
    
    public void initiatePartitionMenu(){
        BinaryPartitionSelect binaryMenu = new BinaryPartitionSelect(this, ds.getSelectedData());
        binaryMenu.setVisible(true);
    }
    

    @Override
    public void acceptButtonActionPerformed(){
        
        df = mainApplication.getActiveDataFrame();

        String selectedNames1[] = super.ds.getSelectedData();
        String selectedNames2[] = super.ds.getSelectedData();

        String selectedNamesG1[] = {super.ds.getSelectedGroup()};
        String selectedNamesG2[] = {super.ds.getSelectedGroup2()};

        Vector<String> vSelectedNames1 = new Vector<String>(Arrays.asList(selectedNames1));
        Vector<String> vSelectedNames2 = new Vector<String>(Arrays.asList(selectedNames2));

        
//-------------------------------------
        //4 opcions possibles
        
        //if(groups && !twoVars){}//1 var i grups
        //if(groups && woVars){}//2 var i grups
        //if(!twoVars && !groups){}//1 var i no grups
        //if(twoVars && !groups){}//2 vars i no grups
        
        //if(selectedNames1.length > 0 && selectedNamesG1[0] != null){
        
        
        
        if(selectedNames1.length > 0){
        //1 var i grups
            //double[][] numericData = df.getNumericalData(selectedNames1);

            // Create X matrix
//-------------------------------- Es mante a tots iguals ------------
            // create dataframe on r
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

                re.eval(dataFrameString); // we create the dataframe in R
//-------------------------------------------------------------------
                // create dataframe on r
                //Al menu es poden seleccionar grups de dades
                if(groups){
                    for(int k=0; k < selectedNamesG1.length;k++){
                        re.eval(selectedNamesG1[k] + " <- NULL");
                        if(df.get(selectedNamesG1[k]).isNumeric()){
                            for(double j : df.get(selectedNamesG1[k]).getNumericalData()){
                                re.eval(selectedNamesG1[k] + " <- c(" + selectedNamesG1[k] + "," + String.valueOf(j) + ")");
                            }
                        }
                        else{ // categorical data
                            for(String j : df.get(selectedNamesG1[k]).getTextData()){
                                re.eval(selectedNamesG1[k] + " <- c(" + selectedNamesG1[k] + ",'" + j + "')");
                            }
                        }
                    }
                    dataFrameString = "Y <- data.frame(";
                    for(int i=0; i < selectedNamesG1.length;i++){
                        dataFrameString += selectedNamesG1[i];
                        if(i != selectedNamesG1.length-1) dataFrameString += ",";
                    }
                    dataFrameString +=")";
                    re.eval(dataFrameString);
                }
                //Al menu es poden seleccionar mes d'un conjunt de dades
                if(twoVars){
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
                    
                    if(groups){
                        for(int i=0; i < selectedNamesG2.length;i++){
                            re.eval(selectedNamesG2[i] + " <- NULL");
                            if(df.get(selectedNamesG2[i]).isNumeric()){
                                for(double j : df.get(selectedNamesG2[i]).getNumericalData()){
                                    re.eval(selectedNamesG2[i] + " <- c(" + selectedNamesG2[i] + "," + String.valueOf(j) + ")");
                                }
                            }
                            else{ // categorical data
                                for(String j : df.get(selectedNamesG2[i]).getTextData()){
                                    re.eval(selectedNamesG2[i] + " <- c(" + selectedNamesG2[i] + ",'" + j + "')");
                                }
                            }
                        }
                    }
                    dataFrameString +=")";

                    re.eval(dataFrameString); // we create the dataframe in R
                
                }
                
                constructParametersToR();        
                this.dispose();
                
//--------------Llegir d'archiu d'R per executar en cas de no haver sigut entrat al txt----- --

                if(!archiuEntratTXT){
                    //seleccionem i executem l'script R
                    frameT1 = new JFrame();
                    chooser = new JFileChooser();
                    frameT1.setSize(600,400);
                    chooser.setDialogTitle("Select R script to execute");
                    chooser.setFileFilter(new FileNameExtensionFilter("R data file", "R", "rda"));
                    chooser.setSize(400,400);
                    frameT1.add(chooser);
                    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                    frameT1.setLocation(dim.width/2-frameT1.getSize().width/2, dim.height/2-frameT1.getSize().height/2);
                    if(chooser.showOpenDialog(frameT1) == JFileChooser.APPROVE_OPTION){
                        String url = chooser.getSelectedFile().getAbsolutePath();
                        
                        url = url.replaceAll("\\\\", "/");
                        re.eval("tryCatch({error <- \"NULL\";source(\"" + url + "\")}, error = function(e){ error <<- e$message})");

                        String[] errorMessage = re.eval("error").asStringArray();
                        
                        
                        if(errorMessage[0].equals("NULL")){
                            // executem totes les accions possibles 
                            showText();
                            createVariables();
                            createDataFrame();
                            showGraphics();
                        }
                        else{
                            //JOptionPane.showMessageDialog(null, "Revisa les dades seleccionades o els valors entrats");
                        
                            OutputElement type = new OutputText("Error in R:");
                            outputPanel.addOutput(type);
                            OutputElement outElement = new OutputForR(errorMessage);
                            outputPanel.addOutput(outElement);
                        }
                    }
                    else{
                        frameT1.dispose();
                    }
                }else{
                    //archiu ja llegit al txt
                    
                    String url = nomArchiu;
                    url = url.replaceAll("\\\\", "/");
                    re.eval("tryCatch({error <- \"NULL\";source(\"" + url + "\")}, error = function(e){ error <<- e$message})");

                    String[] errorMessage = re.eval("error").asStringArray();
                    
                    
                    if(errorMessage[0].equals("NULL")){
                        // executem totes les accions possibles 
                        showText();
                        createVariables();
                        createDataFrame();
                        showGraphics();
                    }
                    else{
                        //JOptionPane.showMessageDialog(null, "Revisa les dades seleccionades o els valors entrats");
                        
                        OutputElement type = new OutputText("Error in R:");
                        outputPanel.addOutput(type);
                        OutputElement outElement = new OutputForR(errorMessage);
                        outputPanel.addOutput(outElement);
                        //JOptionPane.showMessageDialog(null, errorMessage);
            
                    }
                }
                
        }
        else{
            if(selectedNames1.length == 0) JOptionPane.showMessageDialog(null, "No data selected in data 1");
            else JOptionPane.showMessageDialog(null, "No group selected");
        }
        
        
    }
    public void configureILRX(){
        if(this.ilrX == null || this.ilrX.getDsLength() != ds.getSelectedData().length) this.ilrX = new ILRMenu(this.getSelectedData());
        this.ilrX.setVisible(true);
    }

    private void constructParametersToR() {
        
        
        //parametres logics "bool"
        /*
        if(this.B1.isSelected()) re.eval("B1 <- TRUE");
        else re.eval("B1 <- FALSE");
        */
        
        for (int i=1;i<=NomsBotonsBool.size();i++) {

            //if(this.EstatBotonsBool.get(i).isSelected()) re.eval(NomsBotonsBool.get(i)+" <- TRUE");
            //else re.eval(NomsBotonsBool.get(i)+" <- FALSE");
            if(this.EstatBotonsBool.get(i-1).isSelected()) re.eval("B"+ i +" <- TRUE");
            else re.eval("B"+ i +" <- FALSE");
        }
        
        //entrades de text "strings"
        /*
        if(this.P1.getText().length() > 0) re.eval("P1 <- \"" + this.P1.getText() + "\"");
        else re.eval("P1 <- \"\"");
        */
        if(BotonsText.size()==1){
            if(this.TextBotonsText.get(0).getText().length() > 0) re.eval("P1 <- \"" + this.TextBotonsText.get(0).getText() + "\"");
            else re.eval("P1 <- \"\"");
        }else if(BotonsText.size()>1){
            int aux = 1;
            for (int j=0;j<BotonsText.size(); j++) 
            {
                /*
                if(this.TextBotonsText.get(j).getText().length() > 0) re.eval(BotonsText.get(j)+" <- \"" + TextBotonsText.get(j).getText() + "\"");
                else re.eval(BotonsText.get(j)+" <- \"\"");
                */
               //if(j<=NomsBotonsBool.size() ){
                if(EstatBotonsBool.size() > j){
                    if(TextBotonsText.get(j).getText().length() > 0 && EstatBotonsBool.get(j).isSelected()) {//ERROR
                        re.eval("P"+aux+" <- " + TextBotonsText.get(j).getText());
                        aux++;
                    }
                //else re.eval("P"+j+" <- "+ this.TextBotonsText.get(j-1).getText());
                }
            }
        } 
        
        //baseX "partition ilrX"
        
        if(this.ilrX == null || this.ilrX.getPartition().length == 0){
            re.eval("BaseX <- NULL");
        }
        else{
            int[][] baseX = this.ilrX.getPartition();
            re.assign("BaseX", baseX[0]);
            re.eval("BaseX" + " <- matrix( " + "BaseX" + " ,nc=1)");
            for(int i=1; i < baseX.length; i++){
                re.assign("tmp", baseX[i]);
                re.eval("BaseX" + " <- cbind(" + "BaseX" + ",matrix(tmp,nc=1))");
            }
        }
        
        
        //comboBox 
        
            //k-means funciona sense res
            /*if(((Object)this.groupsComboBox.getSelectedItem()).toString().equals("Calinski Index")) re.eval("B2 <- TRUE");
            else re.eval("B2 <- FALSE");*/
        
        for(int i=0;i<ArrayComboBox.size();i++ ){
            String P1 = this.ArrayComboBox.get(i).getSelectedItem().toString();
            int aux = i+1;
            re.eval("P"+aux+" <- \"" + P1 + "\"");
        }
        


    }

    void showText(){
        
        REXP result;
        String[] sortida;
        
        /* header output */
        
        outputPanel.addOutput(new OutputText("personalized Menu:"));
        
        /* R output */
        
        int midaText = re.eval("length(cdp_res$text)").asInt();
        for(int i=0; i < midaText; i++){
            result = re.eval("cdp_res$text[[" + String.valueOf(i+1) + "]]");
            sortida = result.asStringArray();
            outputPanel.addOutput(new OutputForR(sortida));
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

        for(int i=0; i < numberOfGraphics; i++){
            tempDirR = re.eval("cdp_res$graph[[" + String.valueOf(i+1) + "]]").asString();
            tempsDirR.add(tempDirR);
            plotT1(this.framesT1.size());
            
        }   
    }
    
    private void plotT1(int position) {
            
            Font f = new Font("Arial", Font.PLAIN,12);
            UIManager.put("Menu.font", f);
            UIManager.put("MenuItem.font",f);
            JMenuBar menuBar = new JMenuBar();
            JMenu menu = new JMenu("File");
            JMenuItem menuItem = new JMenuItem("Open");
            menuBar.add(menu);
            framesT1.add(new JFrame());
            menu.add(menuItem);
            menuItem = new JMenuItem("Export");
            JMenu submenuExport = new JMenu("Export");
            menuItem = new JMenuItem("Export As SVG");
            menuItem.addActionListener(new T1.FileChooserAction(position));
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
            menuItem.addActionListener(new T1.quitListener(position));
            menu.add(submenuExport);
            menu.add(menuItem);
            framesT1.elementAt(position).setJMenuBar(menuBar);
            JSVGCanvas c = new JSVGCanvas();
            String uri = new File(tempsDirR.elementAt(position)).toURI().toString();
            c.setURI(uri);
            
            framesT1.elementAt(position).getContentPane().add(c);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            framesT1.elementAt(position).setSize(800,800);
            framesT1.elementAt(position).setLocation(dim.width/2-framesT1.elementAt(position).getSize().width/2, dim.height/2-framesT1.elementAt(position).getSize().height/2);
            
            WindowListener exitListener = new WindowAdapter(){
                
                @Override
                public void windowClosing(WindowEvent e){
                    int confirm = JOptionPane.showOptionDialog(null,"Are You Sure to Close Window?","Exit Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
                    if(confirm == 0){
                        framesT1.elementAt(position).dispose();
                        File file = new File(tempsDirR.elementAt(position));
                        file.delete();
                    }
                }
            };
            
            framesT1.elementAt(position).setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            framesT1.elementAt(position).addWindowListener(exitListener);
            framesT1.elementAt(position).setVisible(true);
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
    
    public void setPartition(int[][] partition){
        String spart = "";
        for(short i=0;i<partition.length;i++){
            for(short j=0;j<partition[0].length;j++){
                if(partition[i][j] == -1) spart += " " + partition[i][j];
                else spart += "  " + partition[i][j];
            }
            spart += "\n";
        }
        areaPart.setText(spart);
        //partitionILR = partition;
    }
    
    private class quitListener implements ActionListener{
        
        int position;
        
        public quitListener(int position){
            this.position = position;
        }
        
        public void actionPerformed(ActionEvent e){
            int confirm = JOptionPane.showOptionDialog(null,"Are You Sure to Close Window?","Exit Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
            if(confirm == 0){
                framesT1.elementAt(position).dispose();
                File file = new File(tempsDirR.elementAt(position));
                file.delete();
            }
        }
    }
}