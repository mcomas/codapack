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

import coda.DataFrame;
import coda.Element;
import coda.Variable;
import coda.Zero;
import coda.gui.CoDaPackMain;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author david
 */
public class SetDetectionLimitMenu extends AbstractMenuDialog{
    public static final long serialVersionUID = 1L;
    //Fixem valor per defecte Detection Limit
    double detectionLimit = 0.01;
    //Creem el quadre de text
    JTextField usedDetectionLimit;
    //Creem el nom que tindrà el quade de text
    JLabel l_usedDetectionLimit = new JLabel("Detection Limit");
    
    public SetDetectionLimitMenu(CoDaPackMain mainApp) {
        super(mainApp, "Set Detection Limit", false);
        
        //assignem mida al quadre de text
        usedDetectionLimit =  new JTextField(5);
        //assignem valor per defecte al quadre de text
        usedDetectionLimit.setText("0.01");
        //definim el nom del quadre de text
        optionsPanel.add(l_usedDetectionLimit);
        //posem el quadre de text
        optionsPanel.add(usedDetectionLimit);
        
    }

    

    @Override
    public void acceptButtonActionPerformed() {
        //Copiem el valor del Detection Limit
        detectionLimit = Double.parseDouble(usedDetectionLimit.getText());
        
        //Obtenim el DataFrame actiu
        DataFrame df = mainApplication.getActiveDataFrame();
        //Guardem el nom de les columnes seleccionades en una taula d'strings
        String[] sel_names = ds.getSelectedData();
        //Guardem el nùmero de columnes seleccionades en una variable
        int m = sel_names.length; //m correspon al numero de columnes de data[][]
        //Obtenim el numero de files de cada columna
        int f=df.getMaxVariableLength();
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
        
        //Actualitzem el DataFrame
        mainApplication.updateDataFrame(df);
       
        setVisible(false);
    }
    
}
