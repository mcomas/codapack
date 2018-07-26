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
import java.awt.Rectangle;
import javafx.scene.paint.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.layout.Region;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public final class OutputPanel extends JFXPanel {
    
    public final long serialVersionUID = 1L;
    
    private Scene scene;
    
    private Writer fileWriter;
    
        public OutputPanel(){
            try{
                fileWriter = new OutputStreamWriter(new FileOutputStream("prova.html",true),StandardCharsets.ISO_8859_1);
            }catch(IOException e){
                    System.out.println("Problem occurs when deleting the directory : prova.html");
                    e.printStackTrace();
                }
                Platform.runLater(new Runnable(){
                    public void run(){
                        iniFx();
                    }
                });
        }
        
        public void iniFx(){
            scene = new Scene(new Browser(),500,350,Color.web("#666970"));
            this.setScene(scene);
        }
    
    	public void addWelcome(String CoDaVersion){
		String windowText = "<b>CoDaPack</b> - Version " + CoDaVersion
                + "<br>This software is being developed by the "
                + "Research Group in Statistics and Compositional Data Analysis "
                + "at University of Girona<br><br>";
                
                Platform.runLater(new Runnable(){
                        public void run(){
                            ((Browser)scene.getRoot()).repaint(windowText);
                        }
                });
	}

	public void addOutput(OutputElement oe){
		String windowText = "";
		windowText = oe.printHTML(windowText) + "<br>";
                
                final String aux = windowText;
                
		Platform.runLater(new Runnable(){
                        public void run(){
                            ((Browser)scene.getRoot()).repaint(aux);
                        }
                });
	}

	public void addOutput(ArrayList<OutputElement> outputs){

		String windowText = "";

		for(OutputElement oe: outputs){
			windowText = oe.printHTML(windowText) + "<br>";
		}
                
                final String aux = windowText;

		Platform.runLater(new Runnable(){
                        public void run(){
                            ((Browser)scene.getRoot()).repaint(aux);
                        }
                });
                
	}
        
        public void deleteHtml(){
            
            try {
                fileWriter.close();
                File htmlFile = new File("prova.html");
                htmlFile.delete();
                
            } catch (IOException ex) {
                System.out.println("Problem occurs when deleting the directory : prova.html");
                ex.printStackTrace();
            }
        }
}

class Browser extends Region{

	final WebView browser = new WebView();
	final WebEngine webEngine = browser.getEngine();

	public Browser(){

		getStyleClass().add("browser");
		webEngine.load("file:\\" + System.getProperty("user.dir") + "\\prova.html");
		getChildren().add(browser);
		webEngine.setUserStyleSheetLocation("file:\\" + System.getProperty("user.dir") + "\\resources\\style.css".toString());
	}

	private Node createSpacer(){
            Region spacer = new Region();
		HBox.setHgrow(spacer,Priority.ALWAYS);
		return spacer;
	}

	protected void layoutChildren(){
            double w = getWidth();
            double h = getHeight();
            layoutInArea(browser,0,0,w,h,0,HPos.CENTER, VPos.CENTER);
	}

	@Override protected double computePrefWidth(double height){
		return 500;
	}

	@Override protected double computePrefHeight(double width){
		return 350;
	}

	public void repaint(String text){
            
            try(Writer fileWriter = new OutputStreamWriter(new FileOutputStream("prova.html",true),StandardCharsets.ISO_8859_1)){
                fileWriter.write(text);
            }catch(IOException e){
                System.out.println("Problem occurs when deleting the directory : prova.html");
                e.printStackTrace();
            }

            webEngine.load("file:\\" + System.getProperty("user.dir") + "\\prova.html");
            webEngine.getLoadWorker().stateProperty().addListener((obs,oldValue,newValue)->{
                webEngine.executeScript("window.scrollTo(0, document.body.scrollHeight || document.documentElement.scrollHeight);");
            });
        }
}