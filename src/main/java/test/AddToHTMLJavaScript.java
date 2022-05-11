/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import coda.DataFrame;
import coda.Variable;
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
import org.rosuda.JRI.REXP;

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
    String[] tempsDirR;
        
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
                + "<script src=\"https://vega.github.io/vega/vega.min.js\"></script>\n"
                    + "<script src=\"https://cdn.jsdelivr.net/npm/vega@5\"></script>\n"
                + "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/vega/5.7.0/vega.js\"></script>\n" +
                "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/vega-lite/3.4.0/vega-lite.js\"></script>\n" +
                "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/vega-embed/5.1.3/vega-embed.js\"></script>\n"
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
                
               
               windowText += readHTML();
                
        
        /*NashornExample graal = new NashornExample();
        graal.example06();*/
        byte[] array = new byte[4]; // length is bounded by 7
        new Random().nextBytes(array);
        String nameHTML = "CoDaPack"+array[0]+array[1]+array[2]+array[3];
        //String generatedString = new String(nameHTML, Charset.forName("UTF-8"));
        //System.out.println("nameHTML: "+nameHTML);
        mainApp.addTabbedPannel(nameHTML, windowText);
        HTMLScriptName = nameHTML;
        
        OutputElement inicialText = new OutputText(windowText);
        //buscar el outputpanel corresponent
        if (mainApp.searchPanel(nameHTML+".html")) outputPanel.addOutput(inicialText);
        
        JButton acceptButton = new JButton("Grafs");
        outputPanel.add(acceptButton);
        acceptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                crearGrafProva();
            }
        });
        
        mainApp.returnToMainPanel();
    }
    
    String readHTML() throws FileNotFoundException{
         /* Constructing String Builder to
        append the string into the html */
        StringBuilder html = new StringBuilder();
 
        // Reading html file on local directory
        FileReader fr = new FileReader("L:\\David\\mcomas\\codapack\\grficambplotlygeneratdesdr\\ex2.html");
 
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
            //System.out.println(HTMLContent);
 
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
        /*String windowText="<button class=\"button-85\" role=\"button\" onclick=\"createGraf()\">createGraf</button>\n"
                + "<button onclick=\"createGraf2(0)\">createGraf2</button>"
                + "<button onclick=\"createGraf3(10,15,20,20,55)\">createGraf3</button></br>\n"
                + "<canvas id=\"myChart\"></canvas>\n"
                + "<canvas id=\"myChart2\"></canvas>\n"
                + "<canvas id=\"myChart3\"></canvas>\n";
        OutputElement inicialText = new OutputText(windowText);
        outputPanel.addOutput(inicialText);*/
        
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
    
    void showText(){
        
        REXP result;
        String[] sortida;
        
        /* header output */
        
        outputPanel.addOutput(new OutputText("personalized Menu:"));
        
        /* R output */
        
        int midaText = re.eval("length(cdp_res$text)").asInt();
        for(int i=0; i < midaText; i++){
            result = re.eval("cdp_res$text[[" + String.valueOf(i+1) + "]]");
            sortida = result.asStringArray();
            outputPanel.addOutput(new OutputForR(sortida));
        }
    }
    
    void createVariables(){
        
        int numberOfNewVar = re.eval("length(colnames(cdp_res$new_data))").asInt(); /* numero de columnes nomes*/
        
        for(int i=0; i < numberOfNewVar; i++){
            String varName = re.eval("colnames(cdp_res$new_data)[" + String.valueOf(i+1) + "]").asString();
            String isNumeric = re.eval("as.character(is.numeric(cdp_res$new_data[["+ String.valueOf(i+1) +"]]))").asString();
            if(isNumeric.equals("TRUE")){
                double[] data = re.eval("as.numeric(cdp_res$new_data[," + String.valueOf(i+1) + "])").asDoubleArray();
                df.addData(varName,data);
            }
            else{ // categoric
                String[] data = re.eval("as.character(cdp_res$new_data[," + String.valueOf(i+1) + "])").asStringArray();
                df.addData(varName, new Variable(varName,data));
            }
            mainApplication.updateDataFrame(df);
        }
        
    }
    
    void createDataFrame(){
        int nDataFrames = re.eval("length(cdp_res$dataframe)").asInt();
        for(int i=0; i < nDataFrames; i++){
            int nVariables = re.eval("length(cdp_res$dataframe[[" + String.valueOf(i+1) + "]])").asInt();
            DataFrame newDataFrame = new DataFrame();
            for(int j=0; j < nVariables; j++){
                String varName = re.eval("names(cdp_res$dataframe[[" + String.valueOf(i+1) + "]][" + String.valueOf(j+1) + "])").asString();
                String isNumeric = re.eval("class(unlist(cdp_res$dataframe[[" + String.valueOf(i+1) + "]][" + String.valueOf(j+1) + "]))").asString();
                if(isNumeric.equals("numeric")){ /* crear una variable numerica */
                    double[] data = re.eval("as.numeric(unlist(cdp_res$dataframe[[" + String.valueOf(i+1) + "]][" + String.valueOf(j+1) + "]))").asDoubleArray();
                    newDataFrame.addData(varName, data);
                }
                else{ /* crear una variable categorica */
                    String[] data = re.eval("as.character(unlist(cdp_res$dataframe[[" + String.valueOf(i+1) + "]][" + String.valueOf(j+1) + "]))").asStringArray();
                    newDataFrame.addData(varName, new Variable(varName,data));
                }
            }
            
            newDataFrame.setName(re.eval("names(cdp_res$dataframe)[" + String.valueOf(i+1) + "]").asString());
            mainApplication.addDataFrame(newDataFrame);
        }
    }
    
    void showGraphics(){
        
        int numberOfGraphics = re.eval("length(cdp_res$graph)").asInt(); /* num de grafics */

        for(int i=0; i < numberOfGraphics; i++){
            tempDirR = re.eval("cdp_res$graph[[" + String.valueOf(i+1) + "]]").asString();
            tempsDirR.add(tempDirR);
            //plotT1(this.framesT1.size());
            
        }   
    }
    
    public void acceptButtonActionPerformed(){
        
        
        /*try { 
            Runtime.getRuntime().exec("Rscript L:\\David\\mcomas\\codapack\\grficambplotlygeneratdesdr\\ex02.R");
        } catch (IOException ex) {
            Logger.getLogger(AddToHTMLJavaScript.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        String url = CoDaPackConf.rScriptPath + "ex02.R";
                    
                    re.eval("tryCatch({error <- \"NULL\";source(\"" + url + "\")}, error = function(e){ error <<- e$message})");
                    
                    String[] errorMessage = re.eval("error").asStringArray();

                    if(errorMessage[0].equals("NULL")){
                        /* executem totes les accions possibles */
                        showText();
                        createVariables();
                        createDataFrame();
                        try {
                            showGraphics();
                        } catch (IOException ex) {
                            OutputElement type = new OutputText("Error in R:");
                            outputPanel.addOutput(type);
                            OutputElement outElement = new OutputForR(errorMessage);
                            outputPanel.addOutput(outElement);
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
