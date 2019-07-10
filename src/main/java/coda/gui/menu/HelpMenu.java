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
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.ho.yaml.Yaml;

/**
 *
 * @author mcomas
 */
public class HelpMenu extends JDialog{
    
    private String name;
    private String description;
    private ArrayList<Map<String, Object>> options;
    private String references;
    
    public HelpMenu(JFrame mainApplication, String url) throws MalformedURLException, IOException{
        super(mainApplication, "Help");
        Point p = mainApplication.getLocation();
        p.x = p.x + (mainApplication.getWidth()-400)/2;
        p.y = p.y + (mainApplication.getHeight()-320)/2;
        setLocation(p);

        setSize(500,400);

        JPanel about = new JPanel();        
        about.setLayout(new BorderLayout());

        JLabel text = new JLabel();
        about.setBackground(new Color(200,200,200));
        
        Map<String, Object> map = Yaml.loadType(new File("Help/zPatterns.yaml"), HashMap.class); // guardem els atributs en un Map
        
        inicialitzateAtributes(map); // guardem els atributs
        
        String helpText;
        
        /* name */
        
        helpText = "<html><center><h2>" + this.name + "</h2></center>";
        
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
        
        about.add(new JLabel(helpText), BorderLayout.CENTER);
        this.setContentPane(about);
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
}
