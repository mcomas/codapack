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
import java.awt.Desktop;
import java.net.URI;
import javafx.concurrent.Worker.State;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLAnchorElement;


/**
 *
 * @author mcomas
 */
public final class Output extends BorderPane{
    // De moment no cal
    //private ArrayList<OutputElement> output = new ArrayList<OutputElement>();
    public String windowText = "<html>{POINTER}</html>";
    public Output() {
        addWelcome(CoDaPackConf.CoDaVersion);
        
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
                
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            if (newState == State.SUCCEEDED) {
                Document doc = webEngine.getDocument();
                NodeList nodeList = doc.getElementsByTagName("a");
                for (int i = 0; i < nodeList.getLength(); i++){
                    Node node= nodeList.item(i);
                    EventTarget eventTarget = (EventTarget) node;
                    eventTarget.addEventListener("click", new EventListener(){
                        @Override
                        public void handleEvent(Event evt)
                        {
                            EventTarget target = evt.getCurrentTarget();
                            HTMLAnchorElement anchorElement = (HTMLAnchorElement) target;
                            String href = anchorElement.getHref();
                            //System.out.println(href);
                            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                                try {
                                    desktop.browse(new URI(href));
                                } catch (Exception e) {
                                }
                            }                            
                            evt.preventDefault();
                        }
                    }, false);
                }
            }
        });
        webEngine.loadContent(windowText.replace("{POINTER}", ""));
        this.setCenter(browser);

    }
    public void addWelcome(String CoDaVersion){
        String message = "<div><b>CoDaPack</b> - Version " + CoDaVersion
                + "<p>This software is being developed by the "
                + "<a href='http://imae.udg.edu/Recerca/EIO/inici_eng.html'>Research Group in Statistics and Compositional Data Analysis</a> "
                + "(University of Girona).</p></div> {POINTER}";
        windowText = windowText.replace("{POINTER}", message);
    }
    public void addOutput(OutputElement oe){
//        //De moment no cal
//        //output.add(oe); 
//        String text = jEditorPane1.getText();
//        windowText = text.substring(39, text.length()-15); 
//        windowText = oe.printHTML(windowText)+ "<br>";
//        jEditorPane1.setText(windowText);
//        repaint();
   }
//    public void addOutput(ArrayList<OutputElement> outputs){
//        String text = jEditorPane1.getText();
//        windowText = text.substring(39, text.length()-15);
//        for(OutputElement oe : outputs){
//            // De moment no cal
//            //output.add(oe); 
//            windowText = oe.printHTML(windowText)+ "<br>";
//        }
//        final int old = jScrollPane1.getVerticalScrollBarPolicy();
//        jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                jEditorPane1.setText(windowText);
//                jScrollPane1.setVerticalScrollBarPolicy(old);
//            }
//        });
//        repaint();
//    }
}
