/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package coda.gui.menu;

import coda.gui.CoDaPackMain;
import javax.swing.*;
import org.rosuda.JRI.Rengine;
import java.awt.event.*;
import java.awt.Color;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 *
 * @author dcano
 */
public class CrearMenuPersonal extends AbstractCrearMenu  {
    //exec.R Name
    JTextField nomExec, nomArchiu;
    
    //groups:
    JRadioButton groupsButton = new JRadioButton("Groups");
    //Buttons to add
    JButton jbSelector = new JButton("AfegirSelector");
    JButton jbBool= new JButton("Afegir Botons I/0");
    JButton jbText= new JButton("Afegir Text");
    JButton jbPartitions= new JButton("Afegir Particions");
    JFrame winMain = new JFrame("winMain");
    JPanel jpMenu, jpan1 = new JPanel(), jpan2 = new JPanel(), jp3, jp4;
    JButton jp1 = new JButton("prova1");
    JButton jp2 = new JButton("prova2");
    ILRMenu ILRBools;
    
    //Arrays
    ArrayList<String> BotonsText = new ArrayList<String>();
    ArrayList<JTextField> TextBotonsText = new ArrayList<JTextField>();
    ArrayList<String> NomsBotonsBool = new ArrayList<String>();
    ArrayList<JTextField> EstatBotonsBool = new ArrayList<JTextField>();
    ArrayList<javax.swing.JComboBox> ArrayComboBox = new ArrayList<javax.swing.JComboBox>();
    
    boolean nPartitons = false;
    
    
    public CrearMenuPersonal(final CoDaPackMain mainApp, Rengine r){
        super(mainApp,"Crear Menu Personal");
        
        //Nom Archiu:
        nomArchiu = new JTextField("Nom Archiu",20);
        this.optionsPanel.add(new JLabel("Nom Archiu:"));
        this.optionsPanel.add(nomArchiu);
        this.optionsPanel.add(new JSeparator());
        //exec:
        nomExec = new JTextField("Example.R",20);
        //BotonsText.add("exec:");
        //TextBotonsText.add(nomExec);
        this.optionsPanel.add(new JLabel("Nom Executable:"));
        this.optionsPanel.add(nomExec);
        this.optionsPanel.add(new JSeparator());
        //this.optionsPanel.add(jbSelector);
        
        //groups:
        
        this.optionsPanel.add(groupsButton);
        this.optionsPanel.add(new JSeparator());
        
        // create a separator
        JSeparator s = new JSeparator();
         
        // set layout as vertical
        s.setOrientation(SwingConstants.HORIZONTAL);
        this.optionsPanel.add(s);
        
        
        this.optionsPanel.add(jbBool);
        jbBool.addActionListener(new java.awt.event.ActionListener(){
        
            public void actionPerformed(java.awt.event.ActionEvent evt){
               configurejbBool();
            }
        });
        this.optionsPanel.add(jbText);
        jbText.addActionListener(new java.awt.event.ActionListener(){
        
            public void actionPerformed(java.awt.event.ActionEvent evt){
               configurejbText();
            }
        });
        this.optionsPanel.add(jbPartitions);
        jbPartitions.addActionListener(new java.awt.event.ActionListener(){
        
            public void actionPerformed(java.awt.event.ActionEvent evt){
               configurejbPartitions();
            }
        });
        this.optionsPanel.add(jbSelector);
        jbSelector.addActionListener(new java.awt.event.ActionListener(){
        
            public void actionPerformed(java.awt.event.ActionEvent evt){
               configurejbGroups();
            }
        });
       
        /*JTextField P1 = new JTextField("Input Name Var"+BotonsText.size(),20);
        BotonsText.add("Input Name Var"+BotonsText.size());
        TextBotonsText.add(P1);
        this.optionsPanel.add(P1);*/
        /*for(int i= 0; i<10;i++){
            JTextField P1 = new JTextField("Input Name Var"+BotonsText.size(),20);
            BotonsText.add("Input Name Var"+BotonsText.size());
            TextBotonsText.add(P1);
            this.optionsPanel.add(P1);
            //this.optionsPanel.add(TextBotonsText.get(i));
        }*/
        
        for(int i= 0; i<TextBotonsText.size();i++){
            this.optionsPanel.add(TextBotonsText.get(i));
        }
        for(int i= 0; i<EstatBotonsBool.size();i++){
            this.optionsPanel.add(EstatBotonsBool.get(i));
        }
        jpan1.setVisible(true);
        jpan2.setVisible(true);
    }
    
    void crearBoto(){
        jpan2 = new JPanel();   
        
        JLabel jl1 = new JLabel("Este es el panel 2");
        
        jp2.add(jl1);
        jp2.setSize(100, 100);
        jp2.setVisible(false);
        jp2.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    }
    
    public void configurejbGroups(){
        System.out.println("configurejbGroups");
        jpan1 = new JPanel();   
        
        JLabel jl1 = new JLabel("Este es el panel 1");
        
        jpan1.add(jl1);
        jpan1.setSize(100, 100);
        jpan1.setVisible(false);
        jpan1.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        
        crearBoto();
        //----
        //jpan1.setVisible(true);
        //jpan2.setVisible(false);
        //----
        this.optionsPanel.add(jpan1);
    }
    
    public void configurejbBool(){
        System.out.println("configurejbBool");
        
        JTextField P1 = new JTextField("Input Name Var"+NomsBotonsBool.size(),20);
        NomsBotonsBool.add("Input Name Var"+NomsBotonsBool.size());
        EstatBotonsBool.add(P1);
        this.optionsPanel.add(new JLabel("      name Var"+NomsBotonsBool.size()+":"));
        this.optionsPanel.add(P1);
        //this.optionsPanel.setVisible(true);
        
        //this.optionsPanel.add(jbBool);
         System.out.println("configurejbBool2");
         
         crearBoto();
         
    }
    
    public void configurejbText(){
        System.out.println("configurejbText");
        //for(int i = 0; i< 5; i++){
        JTextField P1 = new JTextField("Input Name Var"+BotonsText.size(),20);
        BotonsText.add("Input Name Var"+BotonsText.size());
        TextBotonsText.add(P1);
        this.optionsPanel.add(new JLabel("      name Var"+BotonsText.size()+":"));
        this.optionsPanel.add(P1);
        
        
        //}
        //this.optionsPanel.setVisible(true);
        
    }
    
    public void configurejbPartitions(){
        System.out.println("configurejbPartitions");
        
        nPartitons= !nPartitons;
        
        System.out.println("configurejbPartitions2");
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        String contenido = "";
        
        //exec:
        if(nomExec.getText() != "Example.R")
        contenido += "exec: \n"+nomExec.getText()+"\n \n";
        else{
            System.out.println("No s'ha entrat cap archiu executable");
        }
        
        //Groups:
        if(groupsButton.isSelected())contenido += "Groups:\n" +"True\n \n";
        else contenido += "Groups:\n" +"False +\n \n";
        //Partitions:
        //contenido += "Partitions: \n";
        if(nPartitons){
            contenido += "Partitions:\n"+ "Set partition\n\n";
        }
          
        //Selector:
        //contenido += "Selector: \n";
        
        //Name of Variables:
        contenido += "Name of Variables:: \n";
        
        //Bool:
        contenido += "Bool: \n";
        
        for (int i=0;i<NomsBotonsBool.size(); i++) 
        {
            contenido += (EstatBotonsBool.get(i).getText()+"\n");
            System.out.println("TextP1: P"+i+" <- " + EstatBotonsBool.get(i).getText());
        }
        
        //Text:
        contenido += "Text: \n";
        
        for (int j=0;j<BotonsText.size(); j++) 
        {
            contenido += (TextBotonsText.get(j).getText()+"\n");
            System.out.println("TextP1: P"+j+" <- " + TextBotonsText.get(j).getText());
        }
        
        try {
            
            String ruta = "./menus_personalitzables/"+nomArchiu.getText()+".txt";
            System.out.println("ruta: "+ruta);
            //String contenido = "Contenido de ejemplo";
            File file = new File(ruta);
            // Si el archivo no existe es creado
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(contenido);
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
