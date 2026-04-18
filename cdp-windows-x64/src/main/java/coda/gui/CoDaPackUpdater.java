/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import coda.ext.json.JSONArray;
import coda.ext.json.JSONException;
import coda.ext.json.JSONObject;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 *
 * @author mcomas
 */
public class CoDaPackUpdater {

    static JSONObject serverData = null;
    
    public static final class UpdatingProcess{
        final static int interval = 1000;
        
        static JLabel label;
        JProgressBar pb;
        Timer timer;
        //JButton button;
        JEditorPane text;

        
        JSONArray updates = null;
        JSONArray updates_needed = new JSONArray();
        String last_version = null;
        static{
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name",
                    "CoDaPack");
            System.setProperty("com.apple.mrj.application.growbox.intrudes",
                    "false");
        }
        public UpdatingProcess() {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CoDaPackUpdater.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                Logger.getLogger(CoDaPackUpdater.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(CoDaPackUpdater.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedLookAndFeelException ex) {
                Logger.getLogger(CoDaPackUpdater.class.getName()).log(Level.SEVERE, null, ex);
            }
            JFrame frame = new JFrame("CoDaPack Updating");
            frame.setSize(600,600);

            text = new JEditorPane();
            text.setPreferredSize(new Dimension(400,400));
            text.setContentType("text/html");

            // Getting new updates information
            getUpdatesInfo();

            JScrollPane scroll = new JScrollPane(text);

            pb = new JProgressBar(0, 100);
            pb.setValue(0);
            pb.setStringPainted(true);

            label = new JLabel(" ");
            
            JPanel panel = new JPanel();
            panel.add(pb);

            JPanel panelC = new JPanel();
            panelC.setLayout(new BorderLayout());
            panelC.add(scroll, BorderLayout.CENTER);

            JPanel panelS = new JPanel();
            panelS.setLayout(new BorderLayout());
            panelS.add(panel, BorderLayout.NORTH);
            panelS.add(label, BorderLayout.CENTER);
            panelS.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JPanel panel1 = new JPanel();
            panel1.setLayout(new BorderLayout());
            panel1.add(panelC, BorderLayout.CENTER);
            panel1.add(panelS, BorderLayout.SOUTH);

            frame.setContentPane(panel1);
            frame.pack();
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                try {
                    //Process ps = Runtime.getRuntime().exec("java -jar CoDaPack.jar");
                    CoDaPackMain.main(null);
                } catch (Exception ex) {
                    Logger.getLogger(CoDaPackUpdater.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            });
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

            int w = frame.getSize().width;
            int h = frame.getSize().height;
            int x = (dim.width-w)/2;
            int y = (dim.height-h)/2;
            frame.setLocation(x, y);
            
            int response = JOptionPane.showConfirmDialog(frame, "A new version of CoDaPack is going to be installed.", "Update confirmation",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (response == JOptionPane.OK_OPTION){
                try {
                    updateCoDaPack();
                } catch (JSONException ex) {
                    Logger.getLogger(CoDaPackUpdater.class.getName()).log(Level.SEVERE, null, ex);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(CoDaPackUpdater.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(CoDaPackUpdater.class.getName()).log(Level.SEVERE, null, ex);
                }
            }            
        }
        public final void updateCoDaPack() throws JSONException, MalformedURLException, IOException{
            String str = "<html>" + "<font color=\"#008000\">" + "<b>" +
                    "Downloading is in process......." + "</b>" + "</font>" + "</html>";
            label.setText(str);
            for(int i = updates_needed.length()-1; i>=0; i--){
                JSONObject object = updates_needed.getJSONObject(i);
                if(object.has("remove")){
                    JSONArray removeFiles = object.getJSONArray("remove");
                    for(int j=0; j<removeFiles.length(); j++){
                        File f = new File(removeFiles.getString(j));
                        f.delete();
                    }
                }
                if(object.has("add")){
                    int size = object.getInt("size");
                    JSONArray addFiles = object.getJSONArray("add");
                    int oneChar, byteSize=0;
                    for(int j=0; j<addFiles.length(); j++){
                        System.out.println(addFiles.getString(j));
                        URL url = new URL( CoDaPackConf.HTTP_ROOT + "codapack/" + addFiles.getString(j));
                        //URL url = new URL("http://ima.udg.edu/~mcomas/codapack/" + addFiles.getString(j));
                        URLConnection urlC = url.openConnection();
                        //InputStream is = url.openStream();
                        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
                        
                        FileOutputStream fos = null;                        
                        File tempFile = new File(addFiles.getString(j).concat("_temp"));
                        fos = new FileOutputStream(tempFile);
                        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                        
//                        int val = 0, pval=0;
//                        while ((oneChar=is.read()) != -1){
//                            fos.write(oneChar);
//                            byteSize++;
//                            val = (int)((float)byteSize/(float)size * 100);
//                            if(pval < val){
//                                pval = val;
//                                pb.setValue(pval);
//                            }
//                        }
                        //System.out.println(((float)byteSize/(float)size) + "%");
                        //is.close();
                        //fos.close();

                        (new File(addFiles.getString(j))).delete();
                        tempFile.renameTo(new File(addFiles.getString(j)));
                    }
                }
            }
            str = "<html>" + "<font color=\"#FF0000\">" + "<b>" +
                            "Update completed." + "</b>" + "</font>" + "</html>";
            try {
                FileReader file = null;
                JSONObject configuration;
                file = new FileReader("codapack.conf");
                BufferedReader br = new BufferedReader(file);
                configuration = new JSONObject(br.readLine());
                file.close();

                configuration.put("codapack-version", last_version);

                PrintWriter printer = new PrintWriter("codapack.conf");
                configuration.write(printer);
                printer.close();
            } catch (FileNotFoundException ex) {

            } catch (IOException ex) {

            }catch (JSONException ex) {

            }
            label.setText(str);
        }
        public void getUpdatesInfo(){
            try {
                updates = serverData.getJSONArray("updates");
                last_version = serverData.getString("codapack-version");

                String message = "";
                for(int i=0, count = 0; i<updates.length(); i++){
                    JSONObject object = updates.getJSONObject(i);                    
                    if( CoDaPackConf.updateNeeded(object.getString("version")) ){
                        updates_needed.put(count++, object);
                        message += "<h3>Update version " + object.getString("version") + "</h3>";
                        message += object.getString("description");
                    }
                }
                text.setText("<html>" + message + "</html>");
            } catch (JSONException ex) {
                Logger.getLogger(CoDaPackUpdater.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException, IOException, JSONException {        
        System.out.println("Updating...");
        File f = new File("codapack.conf");
        if(f.exists()){
            // Loading current settings
            CoDaPackConf.loadConfiguration();
            
            try {
                System.out.println("Connecting to server...");
                URL url = new URL(CoDaPackConf.HTTP_ROOT + "codapack-updater.json");
                
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                serverData = new JSONObject(br.readLine());
                br.close();
                System.out.println("Server version is: " + serverData.getString("codapack-version"));
                if(CoDaPackConf.updateNeeded(serverData.getString("codapack-version")))
                    new UpdatingProcess();

            } catch (IOException ex) {
                System.out.println("Problems trying to connect to IMA server");
            }
        }        
    }

}