/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package coda.gui.menu;

import coda.DataFrame;
import coda.gui.CoDaPackConf;
import coda.gui.CoDaPackMain;
import static coda.gui.CoDaPackMain.outputPanel;
import coda.gui.menu.AbstractMenuGeneral;
import coda.gui.output.OutputElement;
import coda.gui.output.OutputForR;
import coda.gui.output.OutputText;
import javax.script.ScriptException;
import javax.swing.JButton;
import org.rosuda.JRI.Rengine;
import java.util.Random;
import java.io.*;
import java.util.Arrays;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dcano
 */
public class AddToHTMLJavaScript extends AbstractMenuGeneral {
    
    Rengine re;
    DataFrame df;
    CoDaPackMain _mainApp;
    String HTMLScriptName;
    
    
    String tempDirR;
    Vector<String> tempsDirR;
        
    public AddToHTMLJavaScript(final CoDaPackMain mainApp, Rengine r) throws ScriptException, FileNotFoundException  {
        super(mainApp,"AddToHTMLJavaScript",false,false,true);
        //super.setSelectedDataNames("Selected data X:", "Selected data Y:");
        re = r;
        int[] points = {};
        _mainApp = mainApp;
        
        //outputPanel = new OutputPanel();
        //------------ progress bar -------
        String windowText = "<script type=\"text/javascript\" async src=\"file://" + CoDaPackConf.mathJaxPath + "?config=TeX-MML-AM_CHTML\"></script>\n"
                    //    + "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
                    //"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"></head>"
                    //+ "</script><b>CoDaPack</b> - Version " + CoDaVersion
                + "<link rel=\"stylesheet\" href=\"style.css\">\n"
                //----Vega include into HTML (not using now)
                /*+ "<script src=\"https://vega.github.io/vega/vega.min.js\"></script>\n"
                    + "<script src=\"https://cdn.jsdelivr.net/npm/vega@5\"></script>\n"
                + "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/vega/5.7.0/vega.js\"></script>\n" +
                "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/vega-lite/3.4.0/vega-lite.js\"></script>\n" +
                "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/vega-embed/5.1.3/vega-embed.js\"></script>\n"*/
                + "<script src='https://www.gstatic.com/charts/loader.js'></script>" 
                + "<script src=\"chart.js\"></script>\n"
                + "<script src=\"JavaScriptFile.js\"></script>\n"

                    + "";
        windowText += "<div id=\"myProgress\">\n" +
                "    <div id=\"myBar\"></div>\n" +
                "       <div id=\"label\">0%</div>\n" +
                "</div>\n"
                    + "<button class=\"button-30\" role=\"button\" onclick=\"move2(0)\">Cargar</button>\n\n"
                    
                    + "<button class=\"button-30\" role=\"button\"onclick=\"myFunction()\">Restart</button></br>\n\n";
        windowText +="<div style=\"overflow-x:auto;\">\n" +
                "  <table>\n" +
                "    <tr>\n" +
                "      <th>First Name</th>\n" +
                "      <th>Last Name</th>\n" ;
                for(int i = 0; i<13; i++){
                    int random_int = (int)Math.floor(Math.random()*(100-0+1)+0);
                    //points[i] = random_int;
                    windowText += "<td>Points</td>\n";
                }
                 windowText +="    </tr>\n" +
                "    <tr>\n" +
                "      <td>Jill</td>\n" +
                "      <td>Smith</td>\n"  ;
                for(int i = 0; i<12; i++){
                    int random_int = (int)Math.floor(Math.random()*(100-0+1)+0);
                    //points[i] = random_int;
                    windowText += "<td>"+random_int+"</td>\n";
                }
                 windowText +="    </tr>\n" +
                "    <tr>\n" +
                "      <td>Eve</td>\n" +
                "      <td>Jackson</td>\n"  ;
                for(int i = 0; i<8; i++){
                    int random_int = (int)Math.floor(Math.random()*(100-0+1)+0);
                    //points[i] = random_int;
                    windowText += "<td>"+random_int+"</td>\n";
                }
                 windowText +="    </tr>\n" +
                "    <tr>\n" +
                "      <td>Adam</td>\n" +
                "      <td>Johnson</td>\n"  ;
                for(int i = 0; i<11; i++){
                    int random_int = (int)Math.floor(Math.random()*(100-0+1)+0);
                    //points[i] = random_int;
                    windowText += "<td>"+random_int+"</td>\n";
                }
                 windowText +="    </tr>\n" +
                
                "    <tr>\n" +
                "      <td>David</td>\n" +
                "      <td>Cano</td>\n" ;
                for(int i = 0; i<11; i++){
                    int random_int = (int)Math.floor(Math.random()*(100-0+1)+0);
                    //points[i] = random_int;
                    windowText += "<td>"+random_int+"</td>\n";
                }
                windowText +="    </tr>\n" +
                
                "    <tr>\n" +
                "      <td>exemple</td>\n" +
                "      <td>num2</td>\n" ;
                for(int i = 0; i<11; i++){
                    int random_int = (int)Math.floor(Math.random()*(100-0+1)+0);
                    //points[i] = random_int;
                    windowText += "<td>"+random_int+"</td>\n";
                }
                 windowText += "    </tr>\n" +
                "    <tr>\n" +
                "      <td>Jhony</td>\n" +
                "      <td>Deep</td>\n";
                for(int i = 0; i<10; i++){
                    int random_int = (int)Math.floor(Math.random()*(100-0+1)+0);
                    //points[i] = random_int;
                    windowText += "<td>"+random_int+"</td>\n";
                }
                windowText += "    </tr>\n" +
                "  </table>\n" +
                "</div>\n"
                + "var chrome graf3 1:<input type=\"text\" id=\"v1\" placeholder=\"20\"></br>\n";
                
               
               //windowText += readHTML();
                
        //-----create name of html an html in temp folder
        /*NashornExample graal = new NashornExample();
        graal.example06();*/
        byte[] array = new byte[4]; // length is bounded by 7
        new Random().nextBytes(array);
        String nameHTML = "CoDaPack"+array[0]+array[1]+array[2]+array[3];
        //String generatedString = new String(nameHTML, Charset.forName("UTF-8"));
        mainApp.addTabbedPannel(nameHTML, windowText);
        HTMLScriptName = nameHTML;
        
        OutputElement inicialText = new OutputText(windowText);
        //buscar el outputpanel corresponent
        if (mainApp.searchPanel(nameHTML+".html")) outputPanel.addOutput(inicialText);
        
        //---------------------------------------
        
        
        JButton acceptButton = new JButton("Grafs");
        outputPanel.add(acceptButton);
        acceptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                crearGrafProva();
            }
        });
        
        mainApp.returnToMainPanel();
    }
    
    String readHTML(String name) throws FileNotFoundException{
        //passar el nom de l'arxiu html de sortida al arxiu R
        System.out.println("name: "+name);
        
        re.eval("arxSortida<- "+name); 
        re.assign("arxSortida", name);
        
        
        /* Constructing String Builder to
        append the string into the html */
        StringBuilder html = new StringBuilder();
 
        // Reading html file on local directory
        //FileReader fr = new FileReader("L:\\David\\mcomas\\codapack\\grficambplotlygeneratdesdr\\ex02.html");//name
        FileReader fr = new FileReader(name);//name
 
        // Try block to check exceptions
        try {
 
            // Initialization of the buffered Reader to get
            // the String append to the String Builder
            BufferedReader br = new BufferedReader(fr);
 
            String val;
 
            // Reading the String till we get the null
            // string and appending to the string
            while ((val = br.readLine()) != null) {
                html.append(val);
            }
 
            // AtLast converting into the string
            String HTMLContent = html.toString();
 
            // Closing the file after all the completion of
            // Extracting
            br.close();
            
            return HTMLContent;
        }
 
        // Catch block to handle exceptions
        catch (Exception ex) {
 
            /* Exception of not finding the location and
            string reading termination the function
            br.close(); */
            System.out.println(ex.getMessage());
            return "1";
        }
    }

   
    public void crearGrafProva(){
        
        //------------vega--
        
        Random r = new Random();
        int low = 10;
        int high = 100;
      
        String objJSONwithVars = "";
    
        for (int i = 0; i< 8; i++){
            //estruc = ['A', 20],
            int result = r.nextInt(high-low) + low;
            objJSONwithVars += "['"+i+"', "+result+"],";
        }
        
        //out.println(objJSONwithVars);
        
        String windowText=/*"<button  onclick=\"createGraf()\">createGraf</button>\n"
                + "<button onclick=\"createGraf2(0)\">createGraf2</button>"
                + "<button onclick=\"createGraf3(10,15,20,20,55)\">createGraf3</button>\n" +*/
                "<script src=\"vega_graph_test.js\"></script>\n"+
                "<button onclick=\"createGraf4()\">createGraf4</button></br>\n"
                + "<button onclick=\"createGraf5()\">createGraf5</button></br>\n"
                + "<button onclick=\"drawChart()\">createGraf_test</button></br>\n"
                //+ "<button onclick=\"drawChart("+objJSONwithVars+")\">createGraf_test</button></br>\n"
                /*+ " <div id=\"view\"></div>\n"
                + "<div id=\"myChart\"></canvas>\n"
                + "<div id=\"myChart2\"></canvas>\nisua"
                + "<div id=\"myChart3\"></canvas>\n"*/
                + "<div id=\"view\"></canvas>\n"
                + "<div id=\"chart\"></div>\n"
                + "<div id=\"chart-div\" style=\"width: 200px; height: 50px;\"></div>";
        
        OutputElement inicialText = new OutputText(windowText);
        if (_mainApp.searchPanel(HTMLScriptName+".html")) outputPanel.addOutput(inicialText);
        
        //---------end accept button vega
    }
    
    
    
    void insertHTMLtoCODA(String _nameHTML) throws IOException{
        String windowText;
        try {
            
            //---moure carpeta al temp
            String tmpdir = System.getProperty("java.io.tmpdir");
            System.out.println(tmpdir);
            File archivo = new File(tmpdir+"\\libHTML");
            if (!archivo.exists()) {
                System.out.println("directori no existent");
                String currentDir = System.getProperty("user.dir");
                //Files.move(new File(currentDir+"\\grficambplotlygeneratdesdr\\libHTML").toPath(), new File(tmpdir+"\\libHTML").toPath(), StandardCopyOption.REPLACE_EXISTING);
                copiarDirectorio(currentDir+"\\grficambplotlygeneratdesdr\\libHTML", tmpdir+"\\libHTML");
            }
            
            //windowText = readHTML(System.getProperty("user.dir")+"\\grficambplotlygeneratdesdr\\ex02.html");//_nameHTML
            windowText = readHTML(System.getProperty("user.dir")+"\\grficambplotlygeneratdesdr\\"+_nameHTML);//_nameHTML
            
            
            //---diferent tab
            byte[] array = new byte[4]; // length is bounded by 7
            new Random().nextBytes(array);
            String nameHTML = "CoDaPack"+array[0]+array[1]+array[2]+array[3];
            //String generatedString = new String(nameHTML, Charset.forName("UTF-8"));
            _mainApp.addTabbedPannel(nameHTML, windowText);
            HTMLScriptName = nameHTML;

            OutputElement inicialText = new OutputText(windowText);
            //buscar el outputpanel corresponent
            if (_mainApp.searchPanel(nameHTML+".html")) outputPanel.addOutput(inicialText);
            
            //---mateixa tab
            //if (_mainApp.searchPanel(HTMLScriptName+".html")) outputPanel.addOutput(inicialText);
            
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AddToHTMLJavaScript.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void copiarDirectorio(String origen, String destino) {
        comprobarCrearDirectorio(destino);
        File directorio = new File(origen);
        File f;
        if (directorio.isDirectory()) {
            comprobarCrearDirectorio(destino);
            String [] files = directorio.list();
            if (files.length > 0) {
                for (String archivo : files) {
                    f = new File (origen + File.separator + archivo);
                    if(f.isDirectory()) {
                        comprobarCrearDirectorio(destino+File.separator+archivo+File.separator);
                        copiarDirectorio(origen+File.separator+archivo+File.separator, destino+File.separator+archivo+File.separator);
                    } else { //Es un archivo
                        copiarArchivo(origen+File.separator+archivo, destino+File.separator+archivo);
                    }
                }
            }
        }
    }
    
    private void copiarArchivo(String sOrigen, String sDestino) {
        try {
            File origen = new File(sOrigen);
            File destino = new File(sDestino);
            InputStream in = new FileInputStream(origen);
            OutputStream out = new FileOutputStream(destino);

            byte[] buf = new byte[1024];
            int len;

            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            in.close();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void comprobarCrearDirectorio(String ruta) {
        File directorio = new File(ruta);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
    }
    
    @Override
    public void acceptButtonActionPerformed() {
       
        //String url = CoDaPackConf.rScriptPath + "..\\ex02.R";
        //String url = "L:\\\\David\\\\mcomas\\\\codapack\\\\ex02.html";
        
        String currentDir = System.getProperty("user.dir");
        String currentDir2 = currentDir.replaceAll("\\\\", "/");
        String url2 = currentDir2+"/grficambplotlygeneratdesdr/ex03.R";
        //String url2 ="L:\\David\\mcomas\\codapack\\grficambplotlygeneratdesdr\\ex02.R";

        re.eval("tryCatch({error <- \"NULL\";source(\"" + url2 + "\")}, error = function(e){ error <<- e$message})");
        
        
        String[] errorMessage = re.eval("error").asStringArray();
        System.out.println("AAerrorMessage: "+Arrays.toString(errorMessage));
        
        String nomArchiuSortida = "ex03.html";//---------------------------------------------------------------------------------------------------
        File file = new File(currentDir2+"/grficambplotlygeneratdesdr/"+nomArchiuSortida);
                
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("file created");
            } catch (IOException ex) {
                Logger.getLogger(AddToHTMLJavaScript.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        else{
            System.out.println("file not created");
        }
        
        
        if(errorMessage[0].equals("NULL")){
            try {
                /* executem totes les accions possibles */
                /*showText();
                createVariables();
                createDataFrame();
                showGraphics();*/
                
                insertHTMLtoCODA(nomArchiuSortida);
            } catch (IOException ex) {
                System.out.println("errorMessage: "+Arrays.toString(errorMessage));
                Logger.getLogger(AddToHTMLJavaScript.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            OutputElement type = new OutputText("Error in R:");
            outputPanel.addOutput(type);
            OutputElement outElement = new OutputForR(errorMessage);
            outputPanel.addOutput(outElement);
        }

        _mainApp.returnToMainPanel();
    }
}
