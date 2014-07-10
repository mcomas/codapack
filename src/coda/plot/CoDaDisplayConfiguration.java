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
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author marc
 */
public class CoDaDisplayConfiguration {

    static protected HashMap<String, Color> color = new HashMap<String, Color>();
    static protected HashMap<String, Float> size = new HashMap<String, Float>();
    static private Random random = new Random(100);
    private CoDaDisplayConfiguration(){}
    static{        
        setDefaultColor();
        setDefaultSize();
        //default_color.put("area", new Color(0, 0, 255, 20));
    }
    public static void setDefaultSize(){
        size.put("label", 16f);
        size.put("vector_label", 12f);
        size.put("vector_axis", 1f);
        size.put("data0", 3f);//Color.BLUE);
        size.put("data1", 3f);
        size.put("data2", 3f);
        size.put("data3", 3f);
        size.put("data4", 3f);
        size.put("data5", 3f);
        size.put("data6", 3f);
        size.put("data7", 3f);
        size.put("data8", 3f);
        size.put("data9", 3f);
        size.put("data", 3f);
        size.put("axisX", 1f);
        size.put("axisY", 1f);
        size.put("axisZ", 1f);
        size.put("axis", 1f);
        size.put("grid", 0.5f);
        size.put("Prin.Comp.1", 0.75f);
        size.put("Prin.Comp.2", 0.75f);
        size.put("Prin.Comp.3", 1f);
    }
    public static void setDefaultColor(){
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
    public static Set<String> getColorKeySet(){
        return color.keySet();
    }
    public static Set<String> getSizeKeySet(){
        return size.keySet();
    }
    public static void setColor(String e, int ngroup, Color c){
        String code = e.concat(Integer.toString(ngroup));
        if(color.containsKey(code)){
            color.remove(code);
        }
        color.put(code, c);
    }
    public static void setColor(String e, Color c){
        if(color.containsKey(e)){
            color.remove(e);
        }
        color.put(e, c);
    }
    public static Color getColor(String e, int ngroup){
        String code = e.concat(Integer.toString(ngroup));
        if(color.containsKey(code)){
            return color.get(code);
        }else{
            setColor(code, new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256)));
            return color.get(code);
        }                
    }
    public static Color getColor(String e){
        return color.get(e);
    }
    public static void setSize(String e, int ngroup, float s){
        String code = e.concat(Integer.toString(ngroup));
        if(size.containsKey(code)){
            size.remove(code);
        }
        size.put(code, s);
    }
    public static void setSize(String e, float s){
        if(size.containsKey(e)){
            size.remove(e);
        }
        size.put(e, s);
    }
    public static Float getSize(String e, int ngroup){
        String code = e.concat(Integer.toString(ngroup));
        return size.get(code);
    }
    public static Float getSize(String e){
        return size.get(e);
    }
    public static void saveConfiguration(){
        try {
            JSONObject configuration = new JSONObject();

            Set<String> items = CoDaDisplayConfiguration.getColorKeySet();
            int N = items.size();
            String keys[] = items.toArray(new String[N]);

            JSONObject colors = new JSONObject();
            for(int i=0;i<N;i++){
                Color c = color.get(keys[i]);
                JSONArray rgb = new JSONArray();

                rgb.put(0,c.getRed());
                rgb.put(1,c.getGreen());
                rgb.put(2,c.getBlue());
                rgb.put(3,c.getAlpha());

                colors.put(keys[i], rgb);
            }
            configuration.put("color", colors);
            
            PrintWriter printer = new PrintWriter("codaplot.conf");
            configuration.write(printer);
            printer.flush();
            printer.close();
        } catch (FileNotFoundException ex) {
            
        } catch (JSONException ex) {
            
        }        
    }
    public static void loadConfiguration(){
        try {
            FileReader file = new FileReader("codaplot.conf");
            JSONObject configuration = new JSONObject(new BufferedReader(file).readLine());
            file.close();

            JSONObject c = configuration.getJSONObject("color");

            String[] names = JSONObject.getNames(c);
            
            for(String name : names){
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
        } catch (FileNotFoundException ex) {
            setDefaultColor();
            setDefaultSize();
        } catch (IOException ex) {

        }catch (JSONException ex) {

        }
    }
}
