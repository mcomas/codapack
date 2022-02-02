/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package coda.gui.menu;

import coda.gui.CoDaPackMain;
import static coda.gui.CoDaPackMain.re;
import javax.swing.*;
import org.rosuda.JRI.Rengine;
import java.awt.*;
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

    //T1 previsualitzacio;
    int WIDTH = 560;
    int HEIGHT = 430;
    //exec.R Name
    JTextField nomExec, nomArchiu;
    
    //groups:
    JRadioButton groupsButton = new JRadioButton("Groups");
    //Buttons to add
    JButton jbSelector = new JButton("AfegirSelector");
    JButton jbBool= new JButton("Afegir Botons I/0");
    JButton jbText= new JButton("Afegir Text");
    JButton jbRefresh= new JButton("Refrescar");
    JFrame winMain = new JFrame("winMain");
    JPanel jpMenu, jpan1 = new JPanel(), jpan2 = new JPanel(), jpan3 = new JPanel(), jpan4 = new JPanel();
    JButton jbPartitions= new JButton("Afegir Particions");
    JButton jp1 = new JButton("prova1");
    JButton jp2 = new JButton("prova2");
    ILRMenu ILRBools;
    
    //Arrays
    ArrayList<String> BotonsText = new ArrayList<String>();
    ArrayList<JTextField> TextBotonsText = new ArrayList<JTextField>();
    ArrayList<String> NomsBotonsBool = new ArrayList<String>();
    ArrayList<JTextField> EstatBotonsBool = new ArrayList<JTextField>();
    ArrayList<javax.swing.JComboBox> ArrayComboBox = new ArrayList<javax.swing.JComboBox>();
    
    JRadioButton partitionsButton = new JRadioButton("partitions");
    

    
    
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
        
        /*
        this.optionsPanel.add(jbPartitions);
        jbPartitions.addActionListener(new java.awt.event.ActionListener(){
        
            public void actionPerformed(java.awt.event.ActionEvent evt){
               configurejbPartitions();
            }
        });*/
        this.optionsPanel.add(jbSelector);
        jbSelector.addActionListener(new java.awt.event.ActionListener(){
        
            public void actionPerformed(java.awt.event.ActionEvent evt){
               configurejbGroups();
            }
        });
        this.optionsPanel.add(partitionsButton);
        
        
       
        
        /*jpan1.setSize(100, HEIGHT);
        jpan2.setSize(100, HEIGHT);
        jpan3.setSize(100, HEIGHT);
        jpan4.setSize(100, HEIGHT);*/
        
        jpan1.setPreferredSize(new Dimension(500, 300));
        this.optionsPanel.add(jpan1);
        //this.optionsPanel.add(jpan1);
        jpan2.setPreferredSize(new Dimension(500, 300));
        this.optionsPanel.add(jpan2);
        jpan3.setPreferredSize(new Dimension(500, 300));
        this.optionsPanel.add(jpan3);
        jpan4.setPreferredSize(new Dimension(500, 300));
        this.optionsPanel.add(jpan4);
        
        //jpan1.setLayout(null);
        jpan1.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        jpan1.setBounds(new Rectangle(500,50,100,75));
        JLabel jb1 = new JLabel("                                                      Grups                                                           ");
        jpan1.add(jb1, BorderLayout.CENTER);
        jpan1.setVisible(true);
        
        //jpan2.setLayout(null);
        jpan2.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        jpan2.setBounds(new Rectangle(500,50,100,75));
        JLabel jb2 = new JLabel("                                                      Booleans                                                           ");
        jpan2.add(jb2, BorderLayout.CENTER);
        jpan2.setVisible(false);
        
        //jpan3.setLayout(null);
        jpan3.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        jpan3.setBounds(new Rectangle(500,50,100,75));
        JLabel jb3 = new JLabel("                                                      Entrada text/num                                                           ");
        jpan3.add(jb3, BorderLayout.CENTER);
        jpan3.setVisible(false);
        
        //jpan4.setLayout(null);
        jpan4.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        JLabel jb4 = new JLabel("                                                      SECTION 4                                                          ");
        jpan4.setBounds(new Rectangle(500,50,100,75));
        jpan4.add(jb4, BorderLayout.CENTER);
        jpan4.setVisible(false);
        
        
        jbRefresh.setBounds(new Rectangle(200,50,100,750));
        this.optionsPanel.add(jbRefresh, BorderLayout.SOUTH);
        jbRefresh.addActionListener(new java.awt.event.ActionListener(){
        
            public void actionPerformed(java.awt.event.ActionEvent evt){
               configurejbRefresh();
            }
        });
        
    
    
    }

    
    public void configurejbGroups(){
        System.out.println("configurejbGroups");
        //jpan1 = new JPanel();   
        
        JLabel jlG = new JLabel("                Grup:  ");
        jlG.setPreferredSize(new Dimension(200, 25));
        this.jpan1.add(jlG, BorderLayout.LINE_START);
        
        JTextField jl1 = new JTextField("Nom Grup");
        jl1.setPreferredSize(new Dimension(200, 25));
        this.jpan1.add(jl1, BorderLayout.CENTER);
        
        this.jpan1.setVisible(false);
        this.jpan1.setVisible(true);
        this.jpan2.setVisible(false);
        this.jpan3.setVisible(false);
        this.jpan4.setVisible(false);
        

    }
    
    
    
    public void configurejbBool(){
        System.out.println("configurejbBool");
        
        JLabel jlG = new JLabel("                       Bool:  ");
        jlG.setPreferredSize(new Dimension(200, 25));
        
        JTextField P1 = new JTextField("Input Name Var"+NomsBotonsBool.size(),20);
        NomsBotonsBool.add("Input Name Var"+NomsBotonsBool.size());
        EstatBotonsBool.add(P1);
       
        //this.optionsPanel.add(new JLabel("      name Var"+NomsBotonsBool.size()+":"));
        //this.optionsPanel.add(P1);
        P1.setPreferredSize(new Dimension(300, 25));
        this.jpan2.add(jlG, BorderLayout.LINE_START);
        this.jpan2.add(P1, BorderLayout.CENTER);
        
        
        this.jpan2.setVisible(false);
        this.jpan1.setVisible(false);
        this.jpan2.setVisible(true);
        this.jpan3.setVisible(false);
        this.jpan4.setVisible(false);
        /*
        JTextField P1 = new JTextField("Input Name Var"+NomsBotonsBool.size(),20);
        NomsBotonsBool.add("Input Name Var"+NomsBotonsBool.size());
        EstatBotonsBool.add(P1);
        this.optionsPanel.add(new JLabel("      name Var"+NomsBotonsBool.size()+":"));
        this.optionsPanel.add(P1);
        //this.optionsPanel.setVisible(true);
        
        //this.optionsPanel.add(jbBool);
         System.out.println("configurejbBool2");*/
         


         
    }
    
    public void configurejbText(){
        System.out.println("configurejbText");
        //for(int i = 0; i< 5; i++){
        JLabel jlG = new JLabel("                Entrada de text:  ");
        jlG.setPreferredSize(new Dimension(200, 25));
        JTextField P1 = new JTextField("Input Name Var"+BotonsText.size(),20);
        BotonsText.add("Input Name Var"+BotonsText.size());
        TextBotonsText.add(P1);
        //this.optionsPanel.add(new JLabel("      name Var"+BotonsText.size()+":"));
        //this.optionsPanel.add(P1);
        P1.setPreferredSize(new Dimension(300, 25));
        this.jpan3.add(jlG, BorderLayout.LINE_START);
        this.jpan3.add(P1, BorderLayout.CENTER);
        
        this.jpan3.setVisible(false);
        this.jpan1.setVisible(false);
        this.jpan2.setVisible(false);
        this.jpan3.setVisible(true);
        this.jpan4.setVisible(false);
        /*
        JTextField P1 = new JTextField("Input Name Var"+BotonsText.size(),20);
        BotonsText.add("Input Name Var"+BotonsText.size());
        TextBotonsText.add(P1);
        this.optionsPanel.add(new JLabel("      name Var"+BotonsText.size()+":"));
        this.optionsPanel.add(P1);
        */

        
        //}
        //this.optionsPanel.setVisible(true);
        
    }
    

    public void configurejbRefresh(){
        System.out.println("configurejbrefresh");
        
        /*String testNameArch = "prev.txt";
        previsualitzacio.TEST(testNameArch);*/
        
        

        //new T1(this,re).setVisible(true);
        //previsualitzacio.TEST();
        
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

        else contenido += "Groups:\n" +"False\n \n";
        //Partitions:
        //contenido += "Partitions: \n";
        if(partitionsButton.isSelected()){

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
