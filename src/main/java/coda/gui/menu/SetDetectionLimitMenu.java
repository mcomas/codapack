/**	
 *	Copyright 2011-2016 Marc Comas - Santiago Thió - David Gamez
 *
 *	This file is part of CoDaPack.
 *
 *  CoDaPack is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CoDaPack is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CoDaPack.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.gui.menu;

import coda.*;
import coda.gui.CoDaPackMain;

import javax.swing.*;

/**
 *
 * @author david
 */
public class SetDetectionLimitMenu extends AbstractMenuDialogSDL{
    public static final long serialVersionUID = 1L;
    private static final String yamlUrl = "Help/Irregular data.Set Detection Limit.yaml";
    private static final String helpTitle = "Set Detection Limit Help";
    //Fixem valor per defecte Detection Limit
    double detectionLimit = 0.01;
    //Creem el quadre de text
    JTextField usedDetectionLimit;
    //Creem el nom que tindrà el quade de text
    JRadioButton l_usedDetectionLimit = new JRadioButton("Detection Limit");
    //Creem la opció Take Minimun of each column
    JRadioButton l_takeMinimunColum = new JRadioButton("Take Minimum of Each Column");
    //Creem la opció Global of Selected Columns
    JRadioButton l_globalSelectedColumns = new JRadioButton("Global Minimum of Selected Columns");
    DataFrame df;
    
    public SetDetectionLimitMenu(CoDaPackMain mainApp) {
        super(mainApp, "Set Detection Limit", false);
        super.setHelpMenuConfiguration(yamlUrl, helpTitle);

        //Marquem el botó Detection Limit com a seleccionat per defecte
        l_usedDetectionLimit.setSelected(true);
        //assignem mida al quadre de text
        usedDetectionLimit =  new JTextField(5);
        //assignem valor per defecte al quadre de text
        usedDetectionLimit.setText("0.01");
        //definim el nom del quadre de text
        optionsPanel.add(l_usedDetectionLimit);
        //posem el quadre de text
        optionsPanel.add(usedDetectionLimit);
        //posem la opció take minimum
        optionsPanel.add(l_takeMinimunColum);
        //posem la opció global minimum
        optionsPanel.add(l_globalSelectedColumns);

        //Afegir els botons a un grup per a impedir que s'en pugui seleccionar més d'un
        ButtonGroup group = new ButtonGroup();
        group.add(l_usedDetectionLimit);
        group.add(l_takeMinimunColum);
        group.add(l_globalSelectedColumns);
        
    }

    

    @Override
    public void acceptButtonActionPerformed() {
        //Creem tres booleans per a saber la opció triada
        boolean isDetectionSelected = l_usedDetectionLimit.isSelected();
        boolean isTakeMinimumSelected = l_takeMinimunColum.isSelected();
        boolean isGlobalMinimumSelected = l_globalSelectedColumns.isSelected();

        //Obtenim el DataFrame actiu
        df = mainApplication.getActiveDataFrame();
        //Guardem el nom de les columnes seleccionades en una taula d'strings
        String[] sel_names = ds.getSelectedData();
        //Guardem el nùmero de columnes seleccionades en una variable
        int m = sel_names.length; //m correspon al numero de columnes de data[][]
        //Obtenim el numero de files de cada columna
        int f=df.getMaxVariableLength();

        //Valorem el cas que s'hagi trial la opció Detection Limit
        if (isDetectionSelected) {
            detectionLimit = Double.parseDouble(usedDetectionLimit.getText());
            System.out.println("S'ha seleccionat Detection Limit amb el valor: "+detectionLimit);
            //Fem un recorregut per a cada columna del dataframe que s'ha seleccionat
            for(int i=0;i<m;i++) {
                //Obtenim l'array de doubles corresponent a cada columna seleccionada
                Variable var = df.get(sel_names[i]);
                //Fem un recorregut per totes les files de la columna seleccionada
                for(int j=0;j<f;j++) {
                    //Carreguem cada element fila a fila de la columna seleccionada
                    Element el = var.get(j);
                    //Comprovem si es tracta d'una instància de Zero
                    if (el instanceof Zero) {
                        //Si és un Zero li afegim el Detection Limit
                        df.get(sel_names[i]).set(j, new Zero(detectionLimit));
                    }
                }
            }
        }
        else if (isTakeMinimumSelected) {
            double takeMinimum;
            System.out.println("S'ha selecionat la opció d'agafar com a Detection Limit el minim de cada columa");
            //Fem un recorregut per a cada columna del dataframe que s'ha seleccionat
            for(int i=0;i<m;i++) {
                //Obtenim l'array de doubles corresponent a cada columna seleccionada
                Variable var = df.get(sel_names[i]);
                //Fem un recorregut per totes les files de la columna seleccionada
                takeMinimum = 2000000000.05;
                for(int j=0;j<f;j++) {
                    //Carreguem cada element fila a fila de la columna seleccionada
                    Numeric el = (Numeric) var.get(j);
                    //Comprovem si es tracta d'una instància de Zero
                    if (el instanceof Zero) {

                    }
                    else if (el.getValue() < takeMinimum) takeMinimum = el.getValue();
                }
                System.out.println("El valor minim de la columna "+sel_names[i]+" es: "+takeMinimum);
                for(int k=0;k<f;k++) {
                    //Carreguem cada element fila a fila de la columna seleccionada
                    Element el = var.get(k);
                    //Comprovem si es tracta d'una instància de Zero
                    if (el instanceof Zero) {
                        //Si és un Zero li afegim el Detection Limit
                        df.get(sel_names[i]).set(k, new Zero(takeMinimum));
                    }
                }
            }
        }
        else if (isGlobalMinimumSelected) {
            double globalMinimum = 2000000000.05;
            System.out.println("S'ha selecionat la opció d'agafar com a Detection Limit el minim global");
            //Fem un recorregut per a cada columna del dataframe que s'ha seleccionat
            for(int i=0;i<m;i++) {
                //Obtenim l'array de doubles corresponent a cada columna seleccionada
                Variable var = df.get(sel_names[i]);
                //Fem un recorregut per totes les files de la columna seleccionada
                for(int j=0;j<f;j++) {
                    //Carreguem cada element fila a fila de la columna seleccionada
                    Numeric el = (Numeric) var.get(j);
                    //Comprovem si es tracta d'una instància de Zero
                    if (el instanceof Zero) {

                    }
                    else if (el.getValue() < globalMinimum) globalMinimum = el.getValue();
                }
            }
            System.out.println("El valor minim global es: "+globalMinimum);
            for(int k=0;k<m;k++) {
                //Obtenim l'array de doubles corresponent a cada columna seleccionada
                Variable var = df.get(sel_names[k]);
                //Fem un recorregut per totes les files de la columna seleccionada
                for(int l=0;l<f;l++) {
                    //Carreguem cada element fila a fila de la columna seleccionada
                    Element el = var.get(l);
                    //Comprovem si es tracta d'una instància de Zero
                    if (el instanceof Zero) {
                        //Si és un Zero li afegim el Detection Limit
                        df.get(sel_names[k]).set(l, new Zero(globalMinimum));
                    }
                }
            }
        }

        //Actualitzem el DataFrame
        mainApplication.updateDataFrame(df);
       
        setVisible(false);
    }
    
    public DataFrame getDataFrame(){
        return this.df;
    }
    
}
