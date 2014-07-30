/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.plot;

import coda.ext.json.JSONArray;
import coda.ext.json.JSONException;
import coda.ext.json.JSONObject;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author marc
 */
public final class CoDaDisplayConfiguration {

    public HashMap<String, Color> color = new HashMap<String, Color>();
    public HashMap<String, Float> size = new HashMap<String, Float>();
    static private Random random = new Random(100);
    public CoDaDisplayConfiguration(){
        loadConfiguration();
    }
    public void setDefaultSize(){
        
        size.put("label", 16f);
        size.put("vector_label", 12f);
        size.put("vector_axis", 1f);
        size.put("data", 3f);
        size.put("axis", 1f);
        size.put("grid", 0.5f);
        
    }
    public void setDefaultColor(){
        
        color = new HashMap<String, Color>();
        
        color.put("background", Color.WHITE);
        color.put("area", Color.WHITE);
        color.put("label", Color.black);
        color.put("vector_label", Color.black);
        color.put("vector_axis", Color.red);
        color.put("data0", new Color(70, 70, 200));//Color.BLUE);
        color.put("data1", Color.RED);
        color.put("data2", Color.GREEN);
        color.put("data3", Color.LIGHT_GRAY);
        color.put("data4", Color.GRAY);
        color.put("data5", Color.MAGENTA);
        color.put("data6", Color.YELLOW);
        color.put("data7", Color.CYAN);
        color.put("data8", Color.ORANGE);
        color.put("data9", Color.PINK);
        color.put("axisX", Color.BLACK);
        color.put("axisY", Color.BLACK);
        color.put("axisZ", Color.BLACK);
        color.put("axis", Color.BLACK);
        color.put("grid", Color.gray);
        color.put("Prin.Comp.1", Color.RED);
        color.put("Prin.Comp.2", Color.ORANGE);
        color.put("Prin.Comp.3", Color.YELLOW);

    }
    public void setColor(String e, int ngroup, Color c){
        String code = e.concat(Integer.toString(ngroup));
        if(color.containsKey(code)){
            color.remove(code);
        }
        color.put(code, c);
    }
    public void setColor(String e, Color c){
        if(color.containsKey(e)){
            color.remove(e);
        }
        color.put(e, c);
    }
    public Color getColor(String e, int ngroup){
        String code = e.concat(Integer.toString(ngroup));
        if(color.containsKey(code)){
            return color.get(code);
        }else{
            setColor(code, new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256)));
            return color.get(code);
        }                
    }
    public Color getColor(String e){
        return color.get(e);
    }
    public void setSize(String e, int ngroup, float s){
        String code = e.concat(Integer.toString(ngroup));
        if(size.containsKey(code)){
            size.remove(code);
        }
        size.put(code, s);
    }
    public void setSize(String e, float s){
        if(size.containsKey(e)){
            size.remove(e);
        }
        size.put(e, s);
    }
    public Float getSize(String e, int ngroup){
        String code = e.concat(Integer.toString(ngroup));
        return size.get(code);
    }
    public Float getSize(String e){
        return size.get(e);
    }
    public void saveConfiguration(){
        try {
            JSONObject configuration = new JSONObject();

            JSONObject colors = new JSONObject();
            for(String key: color.keySet()){
                Color c = color.get(key);
                JSONArray rgb = new JSONArray();
                System.out.println(key);
                
                rgb.put(0,c.getRed());
                rgb.put(1,c.getGreen());
                rgb.put(2,c.getBlue());
                rgb.put(3,c.getAlpha());
                colors.put(key, rgb);
            }
            configuration.put("color", colors);
            
            JSONObject sizes = new JSONObject();
            for(String key: size.keySet()){
                sizes.put(key, size.get(key));
            }
            configuration.put("size", sizes);
            
            PrintWriter printer = new PrintWriter("codaplot.conf");
            configuration.write(printer);
            printer.flush();
            printer.close();
        } catch (FileNotFoundException ex) {
            System.out.println("file not found");
            
        } catch (JSONException ex) {
            System.out.println("Some problem with JSON");
            
        }        
    }
    public void loadConfiguration(){
        setDefaultColor();
        setDefaultSize();
        try {
            FileReader file = new FileReader("codaplot.conf");
            JSONObject configuration = new JSONObject(new BufferedReader(file).readLine());
            file.close();

            JSONObject c = configuration.getJSONObject("color");
            for(String name : JSONObject.getNames(c)){
                JSONArray array = c.getJSONArray(name);
                int r = array.getInt(0);
                int g = array.getInt(1);
                int b = array.getInt(2);
                int a = array.getInt(3);
                if(color.containsKey(name)){
                    color.remove(name);
                }
                color.put(name, new Color(r,g,b,a));
            }
            JSONObject s = configuration.getJSONObject("size");
            for(String name : JSONObject.getNames(c)){
                if(size.containsKey(name)){
                    size.remove(name);
                }
                size.put(name, (float) s.getDouble(name));
              }
            
        } catch (FileNotFoundException ex) {
            
        } catch (IOException ex) {

        }catch (JSONException ex) {

        }
    }
}
