/**	
 *	Copyright 2011-2016 Marc Comas - Santiago Thi�
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

package coda.gui.menu;

import static coda.gui.CoDaPackConf.CoDaVersion;
import coda.gui.CoDaPackMain;
import coda.gui.output.OutputElement;
import coda.gui.output.OutputText;
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
import java.util.HashMap;
import java.util.Map;
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
import org.ho.yaml.Yaml;

public final class HelpMenu extends JFXPanel {
    
    public final long serialVersionUID = 1L;
    
    private Scene scene;
    
    private Writer fileWriter;
    
    private String urlYamlFile;
    
    private String name;
    private String description;
    private ArrayList<Map<String, Object>> options;
    private String references;
    
        public HelpMenu(String yamlPath) throws FileNotFoundException{
            
            this.urlYamlFile = yamlPath;
            
            try{
                fileWriter = new OutputStreamWriter(new FileOutputStream(System.getProperty("java.io.tmpdir") + "Help.html",false),StandardCharsets.ISO_8859_1);
            }catch(IOException e){
                    System.out.println("Problem occurs when deleting the directory : Help.html");
                    e.printStackTrace();
                }
                Platform.runLater(new Runnable(){
                    public void run(){
                        iniFx();
                    }
                });
                
            Map<String, Object> map = Yaml.loadType(new File(this.urlYamlFile), HashMap.class);
                
            inicialitzateAtributes(map);
            
            OutputElement oe = generateOutput(map);
            
            this.addOutput(oe);
        }
        
        private OutputElement generateOutput(Map<String, Object> map){
            
            String helpText;
        
            /* configuracio inicial MathJax */

            helpText = "<html><head><script type=\"text/javascript\" async src=\"file://C:\\Users\\Guest2\\Downloads\\MathJax\\MathJax.js?config=TeX-MML-AM_CHTML\"></script></head>";

            /* name */

            helpText += "<center><h2>" + this.name + "</h2></center>";

            /* description */

            helpText += "<h2>Description:</h2>" + this.description;

            /* options */

            helpText += "<h2>Options:</h2>";

            for(Map<String, Object> m: this.options){
                for(Map.Entry<String, Object> entry: m.entrySet()){
                    helpText += "<b>" + entry.getKey() + ":</b> " + (String) entry.getValue() + "<br>";
                }
            }

            /* references */

            helpText += "<h2>References</h2>" + this.references + "</html>";
            
            return (new OutputText(helpText));
        }
        
        private void inicialitzateAtributes(Map<String,Object> map){
        
            /* name */

            if(map.containsKey("name")) this.name = (String) map.get("name");
            else this.name = null;

            /* description */

            if(map.containsKey("description")) this.description = (String) map.get("description");
            else this.description = null;

            /* options */

            if(map.containsKey("options")) this.options = (ArrayList<Map<String, Object>>) map.get("options");
            else this.options = null;

            /* references */

            if(map.containsKey("references")) this.references = (String) map.get("references");
            else this.references = null;

        }
        
        public void iniFx(){
            scene = new Scene(new Browser(),500,350,Color.web("#666970"));
            this.setScene(scene);
        }
    
    	/*public void addWelcome(String CoDaVersion){
		String windowText = "<script type=\"text/javascript\" async src=\"file://C:\\Users\\Guest2\\Downloads\\MathJax\\MathJax.js?config=TeX-MML-AM_CHTML\"></script><b>CoDaPack</b> - Version " + CoDaVersion
                + "<br>This software is being developed by the "
                + "Research Group in Statistics and Compositional Data Analysis "
                + "at University of Girona<br><br>";
                
                Platform.runLater(new Runnable(){
                        public void run(){
                            ((Browser)scene.getRoot()).repaint(windowText);
                        }
                });
	}*/

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
                File htmlFile = new File(System.getProperty("java.io.tmpdir") + "Help.html");
                htmlFile.delete();
                
            } catch (IOException ex) {
                System.out.println("Problem occurs when deleting the directory : Help.html");
                ex.printStackTrace();
            }
        }
}

    class Browser extends Region{

	final WebView browser = new WebView();
	final WebEngine webEngine = browser.getEngine();

	public Browser(){

		getStyleClass().add("browser");
		webEngine.load("file:\\" + System.getProperty("java.io.tempdir") + "Help.html");
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

	@Override 
        protected double computePrefWidth(double height){
		return 500;
	}

	@Override 
        protected double computePrefHeight(double width){
		return 350;
	}

	public void repaint(String text){
            
            try(Writer fileWriter = new OutputStreamWriter(new FileOutputStream(System.getProperty("java.io.tmpdir") + "Help.html",true),StandardCharsets.ISO_8859_1)){
                fileWriter.write(text);
            }catch(IOException e){
                System.out.println("Problem occurs when deleting the directory : Help.html");
                e.printStackTrace();
            }

            webEngine.load("file:\\" + System.getProperty("java.io.tmpdir") + "Help.html");
            webEngine.getLoadWorker().stateProperty().addListener((obs,oldValue,newValue)->{
                webEngine.executeScript("window.scrollTo(0, document.body.scrollHeight || document.documentElement.scrollHeight);");
            });
        }
        
        public void clean(String text){
            
            try(Writer fileWriter = new OutputStreamWriter(new FileOutputStream(System.getProperty("java.io.tmpdir") + "Help.html",false),StandardCharsets.ISO_8859_1)){
                fileWriter.write(text);
            }catch(IOException e){
                System.out.println("Problem occurs when deleting the directory : Help.html");
                e.printStackTrace();
            }

            webEngine.load("file:\\" + System.getProperty("java.io.tmpdir") + "Help.html");
            webEngine.getLoadWorker().stateProperty().addListener((obs,oldValue,newValue)->{
                webEngine.executeScript("window.scrollTo(0, document.body.scrollHeight || document.documentElement.scrollHeight);");
            });
        }
}