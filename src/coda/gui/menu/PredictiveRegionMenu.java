/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.gui.menu;

import coda.CoDaStats;
import coda.DataFrame;
import coda.ext.jama.Matrix;
import coda.gui.CoDaPackMain;
import coda.gui.output.OutputPlotHeader;


import coda.plot2.PlotUtils;
import coda.plot2.TernaryPlot2dDisplay;
import coda.plot2.TernaryPlot3dDisplay;
import coda.plot2.objects.Ternary2dCurveObject;
import coda.plot2.objects.Ternary2dDataObject;
import coda.plot2.objects.Ternary2dGridObject;
import coda.plot2.objects.Ternary2dObject;
import coda.plot2.objects.Ternary3dDataObject;
import coda.plot2.objects.Ternary3dObject;
import coda.plot2.objects.Ternary3dSurfaceObject;
import coda.plot2.window.TernaryPlot2dWindow;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author mcomas
 */
public class PredictiveRegionMenu extends AbstractMenuDialog{
    public static final long serialVersionUID = 1L;
    JTextField predLevel;
    JLabel text1 = new JLabel("Predictive level");
    public PredictiveRegionMenu(final CoDaPackMain mainApp){
        super(mainApp, "Predictive Region Menu", false);//, false, true, false);
        predLevel =  new JTextField("0.90 0.95 0.99", 14);

        optionsPanel.add(text1);
        optionsPanel.add(predLevel);
    }
    @Override
    public void acceptButtonActionPerformed() {

        String selectedNames[] = ds.getSelectedData();

        String[] v = predLevel.getText().split(" ");

        double [] confidence = new double[v.length];

        for(int i=0;i<v.length;i++) confidence[i] = Double.parseDouble(v[i]);

        if(selectedNames.length == 3 ){//|| selectedNames.length == 4){
            DataFrame df = mainApplication.getActiveDataFrame();
            boolean[] selection = getValidComposition(df, selectedNames);
            int [] mapping = df.getMapingToData(selectedNames, selection);
            double[][] data = df.getNumericalData(selectedNames, mapping);         

            String groupedBy = ds.getSelectedGroup();

            CoDaPackMain.outputPanel.addOutput(
                    new OutputPlotHeader("Predictive region", selectedNames));

            if(selectedNames.length == 3){
                TernaryPlot2dDisplay display = new TernaryPlot2dDisplay(selectedNames);

                double definedGrid[] = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};
                Ternary2dObject gridObject = new Ternary2dGridObject(display, definedGrid);
                display.addCoDaObject(gridObject);
                double transData[][] = new Matrix(data).transpose().getArray();
                Ternary2dObject dataObject = new Ternary2dDataObject(display, transData);
                dataObject.setColor(new Color(70, 70, 200));
                display.addCoDaObject(dataObject);

                for(int i=0;i<confidence.length;i++){
                    Ternary2dObject curveObject = new Ternary2dCurveObject(display,
                            PlotUtils.predictiveRegion2d(data, confidence[i]));
                    curveObject.setColor(Color.black);                    
                    display.addCoDaObject(curveObject);
                }

                TernaryPlot2dWindow plot = new TernaryPlot2dWindow(df, display, "Predictive Region");
                plot.setLocationRelativeTo(mainApplication);
                plot.setCenter(CoDaStats.center(data));
                plot.setVisible(true);
            }else{// selectedNames.length == 4
                TernaryPlot3dDisplay display = new TernaryPlot3dDisplay(selectedNames);


                double transData[][] = new Matrix(data).transpose().getArray();
                Ternary3dObject dataObject = new Ternary3dDataObject(display, transData);
                dataObject.setColor(new Color(70, 70, 200));
                display.addCoDaObject(dataObject);

                for(int i=0;i<confidence.length;i++){
                    Ternary3dObject ellipsoid3 = new Ternary3dSurfaceObject(display,
                            PlotUtils.predictiveRegion3d(transData, confidence[i]));
                    ellipsoid3.setColor(new Color(230, 200, 200));
                    display.addCoDaObject(ellipsoid3);
                    /*
                    Ternary3dObject curveObject = new Ternary2dCurveObject(display,
                            PlotUtils.predictiveRegion2d(data, confidence[i]));
                    curveObject.setColor(Color.black);
                    display.addCoDaObject(curveObject);
                     */
                }

                /*
                TernaryPlot3dBuilder builder = new TernaryPlot3dBuilder(selectedNames, data);
                builder.mapping(mapping);
                
                if(groupedBy != null){
                    builder.groups(coda.Utils.reduceData(
                            df.getDefinedGroup(groupedBy),
                            selection), coda.Utils.getCategories(
                            df.getCategoricalData(groupedBy),selection));
                }
                TernaryPlot3dDisplay display = builder.build();
                plot = new TernaryPlot3dWindow(display, "Ternary Plot 3d");
                plot.setLocationRelativeTo(mainApplication);
                 * */
                
            }
            
            setVisible(false);
        }else{
            JOptionPane.showMessageDialog(this, "<html>Select <b>three</b> variables</html>");
        }
        
    }
}
