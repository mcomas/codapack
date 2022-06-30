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

import static coda.gui.CoDaPackConf.CoDaVersion;
import static coda.gui.CoDaPackMain.outputPanel;
import coda.gui.output.OutputElement;
import coda.gui.output.OutputText;
import javafx.scene.paint.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.layout.Region;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javax.script.*;

import java.lang.*;
import javax.swing.JProgressBar;



public final class OutputPanel extends JFXPanel {
    
    public final long serialVersionUID = 1L;
    
    private Scene scene;
    
    private Writer fileWriter;
    int width = 0;
    //--------- JavaScript functions
//    private static final String ENGINE_NAME = "graal.js";
//
//    private final ScriptEngine engine;
//
//    private Invocable invoker;
    JProgressBar barra;
    
    String HTMLName = "";
    //----------
    
        public OutputPanel() {
            
            try{
                HTMLName = "CoDaPack.html";
                //System.out.println(System.getProperty("java.io.tmpdir") + "/CoDaPack.html");
                fileWriter = new OutputStreamWriter(new FileOutputStream(System.getProperty("java.io.tmpdir") + "/CoDaPack.html",true),StandardCharsets.ISO_8859_1);
            }catch(IOException e){
                    System.out.println("Problem occurs when deleting the directory : CoDaPack.html");
                    e.printStackTrace();
                }
                Platform.runLater(new Runnable(){
                    public void run(){
                        iniFx();
                    }
                });
        }
        
        public OutputPanel(String nameScript) {
            HTMLName = nameScript+".html";
            try{
                //System.out.println(System.getProperty("java.io.tmpdir") + "/"+nameScript+".html");
                fileWriter = new OutputStreamWriter(new FileOutputStream(System.getProperty("java.io.tmpdir") + "/"+nameScript+".html",true),StandardCharsets.ISO_8859_1);
            }catch(IOException e){
                    System.out.println("Problem occurs when deleting the directory : CoDaPack.html");
                    e.printStackTrace();
                }
                Platform.runLater(new Runnable(){
                    public void run(){
                        iniFx();
                    }
                });
        }
        
        
        public void iniFx(){
            scene = new Scene(new Browser(HTMLName),500,350,Color.web("#666970"));
            this.setScene(scene);
        }
    
    	public void addWelcome(String CoDaVersion) /**/throws ScriptException, Exception{
            clearOutput();	
            String windowText = "<script type=\"text/javascript\" async src=\"file://" + CoDaPackConf.mathJaxPath + "?config=TeX-MML-AM_CHTML\"></script>\n"
                    //    + "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
                    //"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"></head>"
                    //+ "</script><b>CoDaPack</b> - Version " + CoDaVersion
                + "<link rel=\"stylesheet\" href=\"style.css\">\n"
                + "<script src=\"https://vega.github.io/vega/vega.min.js\"></script>\n"
                    + "<script src=\"https://cdn.jsdelivr.net/npm/vega@5\"></script>\n"
                + "<script src=\"chart.js\"></script>\n"
                + "<script src=\"JavaScriptFile.js\"></script>\n"

                    + "";
                //+ "<canvas id=\"myChart\"></canvas>\n" ;
                    
                
                /*Platform.runLater(new Runnable(){
                    public void run(){
                        ((Browser)scene.getRoot()).repaint(windowText);
                    }
                });*/
                
                //barra = graal.example05(barra, width);
                //graal.example05(barra, width);
                
                
	}

	public void addOutput(OutputElement oe){
		String windowText = "";
		windowText = oe.printHTML(windowText) + "";
                
                final String aux = windowText;
                
		Platform.runLater(new Runnable(){
                        public void run(){
                            ((Browser)scene.getRoot()).repaint(aux);
                        }
                });
	}
        
        public void clearOutput(){
            
            String aux = "<b>CoDaPack</b> - Version " + CoDaVersion
                + "<br>This software is being developed by the "
                + "Research Group in Statistics and Compositional Data Analysis "
                + "at University of Girona<br><br>";
            
            Platform.runLater(new Runnable(){
                public void run(){
                    ((Browser)scene.getRoot()).clean(aux);
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
                File htmlFile = new File(System.getProperty("java.io.tmpdir") + "/"+HTMLName);
                htmlFile.delete();
                
            } catch (IOException ex) {
                System.out.println("Problem occurs when deleting the directory : CoDaPack.html");
                ex.printStackTrace();
            }
        }
        
        public String returnHTMLName(){
            return HTMLName;
        }
}

class Browser extends Region{

	final WebView browser = new WebView();
	final WebEngine webEngine = browser.getEngine();
        String HTMLName = "";

	public Browser(){
                HTMLName = "CoDaPack.html";
		getStyleClass().add("browser");
		webEngine.load("file:\\" + System.getProperty("java.io.tempdir") + "/CoDaPack.html");
		getChildren().add(browser);
		webEngine.setUserStyleSheetLocation(getClass().getResource(CoDaPackMain.RESOURCE_PATH + "style.css").toString());
	}
        
        public Browser(String nameScript){
                HTMLName = nameScript;
		getStyleClass().add("browser");
		webEngine.load("file:\\" + System.getProperty("java.io.tempdir") + "/"+HTMLName);
		getChildren().add(browser);
		webEngine.setUserStyleSheetLocation(getClass().getResource(CoDaPackMain.RESOURCE_PATH + "style.css").toString());
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
            
            try(Writer fileWriter = new OutputStreamWriter(new FileOutputStream(System.getProperty("java.io.tmpdir") + "/"+HTMLName,true),StandardCharsets.ISO_8859_1)){
                fileWriter.write(text);
            }catch(IOException e){
                System.out.println("Problem occurs when deleting the directory : CoDaPack.html");
                e.printStackTrace();
            }

            webEngine.load("file:\\" + System.getProperty("java.io.tmpdir") + "/"+HTMLName);
            webEngine.getLoadWorker().stateProperty().addListener((obs,oldValue,newValue)->{
                webEngine.executeScript("window.scrollTo(0, document.body.scrollHeight || document.documentElement.scrollHeight);");
            });
        }
        
        public void clean(String text){
            
            try(Writer fileWriter = new OutputStreamWriter(new FileOutputStream(System.getProperty("java.io.tmpdir") + "/"+HTMLName,false),StandardCharsets.ISO_8859_1)){
                fileWriter.write(text);
            }catch(IOException e){
                System.out.println("Problem occurs when deleting the directory : CoDaPack.html");
                e.printStackTrace();
            }

            webEngine.load("file:\\" + System.getProperty("java.io.tmpdir") + "/"+HTMLName);
            webEngine.getLoadWorker().stateProperty().addListener((obs,oldValue,newValue)->{
                webEngine.executeScript("window.scrollTo(0, document.body.scrollHeight || document.documentElement.scrollHeight);");
            });
        }
}