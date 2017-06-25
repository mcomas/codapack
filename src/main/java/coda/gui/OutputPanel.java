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

package coda.gui;

import coda.gui.output.OutputElement;
import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import javax.swing.undo.UndoManager;

/**
 *
 * @author mcomas
 */
public final class OutputPanel extends JPanel{
    public final long serialVersionUID = 1L;
    
    private String windowText = "";

    private HTMLEditorKit hed = new HTMLEditorKit();
    protected UndoManager undoManager = new UndoManager();

    private Document doc;

    private static JEditorPane jEditorPane1;
    private static JScrollPane jScrollPane1;

    // De moment no cal
    //private ArrayList<OutputElement> output = new ArrayList<OutputElement>();

    public OutputPanel() {
        setLayout(new BorderLayout());

        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();

        //jEditorPane1.setEditable(false); //<- No sé què és millor?
        setPreferredSize(new java.awt.Dimension(500, 350));
        setVisible(true);

        jScrollPane1.setViewportView(jEditorPane1);
        add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jEditorPane1.setContentType("text/html");
        setHTMLStyle(hed.getStyleSheet());

        doc = hed.createDefaultDocument();

        jEditorPane1.setEditorKit(hed);
        jEditorPane1.setDocument(doc);
    }
    public void setHTMLStyle(StyleSheet styleSheet){
        styleSheet.addRule("td,th {"
            + "border:1px solid #98bf21;"
            + "padding:3px 7px 2px 7px;}");
        styleSheet.addRule("th{"
            + "text-align:left;"
            + "padding-top:5px;"
            + "padding-bottom:4px;"
            + "background-color:#277BCC;"
            + "color:#ffffff;}");
        styleSheet.addRule(".alt{"
            + "color:#000000;"
            + "background-color:#FFFADA;}"); //#A2C1D7;}");
        styleSheet.addRule("td.h2{"
            + "background-color:#CC0000;}");
        styleSheet.addRule("td.h1{"
            + "background-color:#FFCCCC;}");
        styleSheet.addRule("td.l2{"
            + "background-color:#2B60DE;}");
        styleSheet.addRule("td.l1{"
            + "background-color:#ADDFFF;}");
        styleSheet.addRule("td.normal{"
            + "background-color:#FFFFFF;}");
        styleSheet.addRule(
                "body{"
          + "font-family: Monospace; "
          + "font-size:" + "12px" + ";"
          + "color:#000000;"
          + "text-decoration: none;"
          + "margin:10px;"
          + "border-collapse:collapse;}");        
    }

    public void addWelcome(String CoDaVersion){
        windowText = "<b>CoDaPack</b> - Version " + CoDaVersion
                + "<br>This software is being developed by the "
                + "Research Group in Statistics and Compositional Data Analysis "
                + "at University of Girona";
        jEditorPane1.setText(windowText);
        repaint();
    }
    public void addOutput(OutputElement oe){
        //De moment no cal
        //output.add(oe); 
        String text = jEditorPane1.getText();
        windowText = text.substring(39, text.length()-15); 
        windowText = oe.printHTML(windowText)+ "<br>";
        jEditorPane1.setText(windowText);
        repaint();
    }
    public void addOutput(ArrayList<OutputElement> outputs){
        String text = jEditorPane1.getText();
        windowText = text.substring(39, text.length()-15);
        for(OutputElement oe : outputs){
            // De moment no cal
            //output.add(oe); 
            windowText = oe.printHTML(windowText)+ "<br>";
        }
        final int old = jScrollPane1.getVerticalScrollBarPolicy();
        jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                jEditorPane1.setText(windowText);
                jScrollPane1.setVerticalScrollBarPolicy(old);
            }
        });
        repaint();
    }
}
