/**	
 *	Copyright 2011-2016 Marc Comas - Santiago Thió
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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.gui.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author mcomas
 */
public class HelpMenu extends JDialog{
    
    public HelpMenu(JFrame mainApplication, String url) throws MalformedURLException, IOException{
        super(mainApplication, "Help");
        Point p = mainApplication.getLocation();
        p.x = p.x + (mainApplication.getWidth()-400)/2;
        p.y = p.y + (mainApplication.getHeight()-320)/2;
        setLocation(p);

        setSize(400,320);

        JPanel about = new JPanel();        
        about.setLayout(new BorderLayout());

        JLabel text = new JLabel();
        about.setBackground(new Color(200,200,200));
        
        //text.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        //text.setAlignmentY(TOP_ALIGNMENT);
        String aboutText = "<h2>CoDaPack Team</h2>";
        aboutText += "<p>CoDaPack is being developed at <a href='http://imae.udg.edu'>Deptartment of Computer Science " +
                ", Applied Mathematics and Statistics</a> in the University of Girona</p><br>";

        aboutText += "<p>Please, if you detect some bug or if you think in new features send us an e-mail at:</p><br>" +
                "<b>Marc Comas-Cuf&iacute;</b>: mcomas@imae.udg.edu,<br>" +
                "<b>Santiago Thi&oacute;-Henestrosa</b>: thio@imae.udg.edu";

        try {
            
            InputStream input = new FileInputStream(url);
            String aux = IOUtils.toString(input);
            text.setText(aux);
            about.add(text, BorderLayout.CENTER);
            this.setContentPane(about);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HelpMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
