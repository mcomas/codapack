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

package coda.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author mcomas
 */
public class CoDaPackAbout extends JDialog{
    public CoDaPackAbout(JFrame mainApplication){
        super(mainApplication, "About");
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

        text.setText("<html><center>" + aboutText + "</center></html>");
        about.add(text, BorderLayout.CENTER);


        this.setContentPane(about);
    }
}
