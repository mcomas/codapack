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
        aboutText += "<p>CoDaPack is being developed at <a href='http://ima.udg.edu'>Deptartment of Computer Science " +
                "and Applied Mathematics</a> in the University of Girona</p><br>";

        aboutText += "<p>Please, if you detect some bug or if you think in new features send us an e-mail at:</p><br>" +
                "<b>Marc Comas-Cufí</b>: mcomas@ima.udg.edu,<br>" +
                "<b>Santiago Thió-Henestrosa</b>: thio@ima.udg.edu";

        text.setText("<html><center>" + aboutText + "</center></html>");
        about.add(text, BorderLayout.CENTER);


        this.setContentPane(about);
    }
}
