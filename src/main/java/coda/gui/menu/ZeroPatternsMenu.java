package coda.gui.menu;

import coda.BasicStats;
import coda.DataFrame;
import coda.Variable;
import coda.gui.CoDaPackConf;
import coda.gui.CoDaPackMain;
import static coda.gui.CoDaPackMain.outputPanel;
import coda.gui.output.OutputForR;
import coda.gui.output.OutputText;
import coda.gui.utils.DataSelector;

import java.awt.Dimension;
import java.io.File;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import org.apache.batik.swing.JSVGCanvas;


public class ZeroPatternsMenu extends AbstractMenuRBasedDialog{
    private static final String yamlUrl = CoDaPackConf.helpPath + "Irregular data.zPatterns.yaml";
    private static final String helpTitle = "zPatterns Help Menu";
    Rengine re;
    
    JCheckBox B1 = new JCheckBox("Include means");
    JCheckBox B2 = new JCheckBox("Include %");
    JCheckBox B3 = new JCheckBox("Add pattern", true);
    
    // It should be common to all R calling AbstractRBasedMenuDialog
    String script_file = "zpatterns.R";
    int PLOT_WIDTH = 850;
    int PLOT_HEIGHT = 500;
    
    public ZeroPatternsMenu(final CoDaPackMain mainApp, Rengine r){
        super(mainApp,"Zpatterns Plot Menu", new DataSelector(mainApp.getActiveDataFrame(), false), r);
        super.setHelpMenuConfiguration(yamlUrl, helpTitle);
        re = r;
        
        this.optionsPanel.setLayout(new BoxLayout(this.optionsPanel, BoxLayout.PAGE_AXIS));
 
        B1.setAlignmentX(0);
        B2.setAlignmentX(0);
        B3.setAlignmentX(0);
        //pP1.setAlignmentX(0);

        this.optionsPanel.add(Box.createRigidArea(new Dimension(15,15)));
        this.optionsPanel.add(B1);
        this.optionsPanel.add(B2);
        this.optionsPanel.add(B3);

        // this.optionsPanel.add(pP1);

        // this.names = new ArrayList<String>(mainApplication.getActiveDataFrame().getNames());
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        df = mainApplication.getActiveDataFrame();
        
        String sel_names[] = super.ds.getSelectedData();
        
        if(sel_names.length > 1){
            
            double[][] data = df.getNumericalData(sel_names);
            int nZeros = BasicStats.nZeros(data);
            if(nZeros > 0){
                for(int i=0; i < data.length; i++){
                    re.assign(sel_names[i], data[i]);
                }
                re.eval("X = cbind(" + String.join(",", sel_names) + ")");
                re.eval("X[is.nan(X)] = NA_real_");
                re.eval("B1 = %s".formatted(B1.isSelected() ? "TRUE": "FALSE"));
                re.eval("B2 = %s".formatted(B2.isSelected() ? "TRUE": "FALSE"));
                re.eval("B3 = %s".formatted(B3.isSelected() ? "TRUE": "FALSE"));
                re.eval("PLOT_WIDTH = %d/72".formatted(PLOT_WIDTH));
                re.eval("PLOT_HEIGTH = %d/72".formatted(PLOT_HEIGHT));


                //re.eval("save.image('image.RData')");
                String url = CoDaPackConf.rScriptPath + script_file;
                System.out.println(url);

                re.eval("error = tryCatch(source('%s'), error = function(e) e$message)".formatted(url));
                String errorMessage = re.eval("error").asString();
                if(errorMessage == null){
                    showText();
                    createVariables();
                    showGraphics();
                }
                /*
                if(errorMessage[0].equals("NULL")){
                    // executem totes les accions possibles
                    
                    
                    createDataFrame();
                    try {
                        showGraphics();
                    } catch (IOException ex) {
                        Logger.getLogger(ZpatternsMenu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else{
                    OutputElement type = new OutputText("Error in R:");
                    outputPanel.addOutput(type);
                    OutputElement outElement = new OutputForR(errorMessage);
                    outputPanel.addOutput(outElement);
                }
                */
                setVisible(false);
            }else{
                JOptionPane.showMessageDialog(this, "No zero detected");
            }
        }else{
            JOptionPane.showMessageDialog(this, "Select at least two parts");
            
        }
    }

    
    void showText(){
        
        REXP result;
        String[] sortida;
        
        /* header output */
        
        outputPanel.addOutput(new OutputText("Zpatterns Plot:"));
        
        /* R output */        
        String outputString[] = re.eval("unlist(cdp_res[['text']])").asStringArray();
        System.out.println(Arrays.toString(outputString));
        outputPanel.addOutput(new OutputForR(outputString));

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
            
            String dataFrameName = re.eval("names(cdp_res$dataframe)[" + String.valueOf(i+1) + "]").asString();
            
            while(!mainApplication.isDataFrameNameAvailable(dataFrameName)){
                dataFrameName += "c";
            }
            
            newDataFrame.setName(dataFrameName);
            mainApplication.addDataFrame(newDataFrame);
        }
    }
    
    void showGraphics(){
        
        String fnames[] = re.eval("cdp_res$graph").asStringArray();
        System.out.println(Arrays.toString(fnames));
        for(String fname: fnames){
            JSVGCanvas c = new JSVGCanvas();
            String uri = new File(fname).toURI().toString();
            c.setURI(uri);
            JFrame jf = new JFrame();
            jf.setSize(PLOT_WIDTH,PLOT_HEIGHT);
            jf.getContentPane().add(c);
            jf.setVisible(true);
        }
        /*
        for(int i=0; i < numberOfGraphics; i++){
            tempDirR = re.eval("cdp_res$graph[[" + String.valueOf(i+1) + "]]").asString();
            tempsDirR.add(tempDirR);
            plotZpatternsMenu(this.framesZpatternsMenu.size());
        }  
        */
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
    /*
    private void plotZpatternsMenu(int position) throws IOException {

            Font f = new Font("Arial", Font.PLAIN,12);
            UIManager.put("Menu.font", f);
            UIManager.put("MenuItem.font",f);
            JMenuBar menuBar = new JMenuBar();
            JMenu menu = new JMenu("File");
            JMenuItem menuItem = new JMenuItem("Open");
            menuBar.add(menu);
            framesZpatternsMenu.add(new JFrame());
            menu.add(menuItem);
            menuItem = new JMenuItem("Export");
            JMenu submenuExport = new JMenu("Export");
            menuItem = new JMenuItem("Export As SVG");
            menuItem.addActionListener(new FileChooserAction(position));
            submenuExport.add(menuItem);
            menuItem = new JMenuItem("Export As JPEG");
            //submenuExport.add(menuItem);
            menuItem = new JMenuItem("Export As PDF");
            //submenuExport.add(menuItem);
            menuItem = new JMenuItem("Export As WMF");
            //submenuExport.add(menuItem);
            menuItem = new JMenuItem("Export As Postscripts");
            //submenuExport.add(menuItem);
            menuItem = new JMenuItem("Quit");
            menuItem.addActionListener(new quitListener(position));
            menu.add(submenuExport);
            menu.add(menuItem);
            framesZpatternsMenu.elementAt(position).setJMenuBar(menuBar);
            JSVGCanvas c = new JSVGCanvas();
            String uri = new File(tempsDirR.elementAt(position)).toURI().toString();
            c.setURI(uri);
            
            framesZpatternsMenu.elementAt(position).getContentPane().add(c);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            framesZpatternsMenu.elementAt(position).setSize(800,800);
            framesZpatternsMenu.elementAt(position).setLocation(dim.width/2-framesZpatternsMenu.elementAt(position).getSize().width/2, dim.height/2-framesZpatternsMenu.elementAt(position).getSize().height/2);
            
            WindowListener exitListener = new WindowAdapter(){
                
                @Override
                public void windowClosing(WindowEvent e){
                    int confirm = JOptionPane.showOptionDialog(null,"Are You Sure to Close Window?","Exit Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
                    if(confirm == 0){
                        framesZpatternsMenu.elementAt(position).dispose();
                        File file = new File(tempsDirR.elementAt(position));
                        file.delete();
                    }
                }
            };
            
            framesZpatternsMenu.elementAt(position).setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            framesZpatternsMenu.elementAt(position).addWindowListener(exitListener);
            framesZpatternsMenu.elementAt(position).setVisible(true);

    }
*/

    
    /*public ArrayList<String> getDataFrameNames(){
        return this.names;
    }*/
     /*
    private class quitListener implements ActionListener{
        
        int position;
        
        public quitListener(int position){
            this.position = position;
        }
        
        public void actionPerformed(ActionEvent e){
            int confirm = JOptionPane.showOptionDialog(null,"Are You Sure to Close Window?","Exit Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
            if(confirm == 0){
                framesZpatternsMenu.elementAt(position).dispose();
                File file = new File(tempsDirR.elementAt(position));
                file.delete();
            }
        }
    }
    
    private class FileChooserAction implements ActionListener{
        
        int position;
        
        public FileChooserAction(int position){
            this.position = position;
        }
        
        public void actionPerformed(ActionEvent e){
            JFrame frame = new JFrame();
            JFileChooser jf = new JFileChooser();
            frame.setSize(400,400);
            jf.setDialogTitle("Select the folder to save the file");
            jf.setApproveButtonText("Save");
            jf.setSelectedFile(new File(".svg"));
            jf.setSize(400,400);
            frame.add(jf);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
            boolean canExit = false;
            while(! canExit){
                int result = jf.showSaveDialog(frame);
                if(JFileChooser.CANCEL_OPTION == result){
                    frame.dispose();
                    canExit = true;
                }
                if(JFileChooser.APPROVE_OPTION == result){ // guardem arxiu en el path
                    File f = new File(tempsDirR.elementAt(position));
                    f.deleteOnExit();
                    String path = jf.getSelectedFile().getAbsolutePath();
                    File f2 = new File(path);
                    if(f2.exists()){
                        int response = JOptionPane.showConfirmDialog(null, "Do you want to replace the existing file?", 
                                "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(response != JOptionPane.YES_OPTION) canExit = false;
                        else{
                            f.renameTo(f2);
                            frame.dispose();
                            canExit = true;
                        }
                    }
                    else{
                        f.renameTo(f2);
                        frame.dispose();
                        canExit = true;
                    }
                }
            }
        }
    }
    */
}
