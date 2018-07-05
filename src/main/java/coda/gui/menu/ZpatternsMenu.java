/** 
 *  Copyright 2011-2016 Marc Comas - Santiago Thió
 *
 *  This file is part of CoDaPack.
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

import coda.DataFrame;
import coda.gui.CoDaPackMain;
import static coda.gui.CoDaPackMain.outputPanel;
import coda.gui.output.OutputElement;
import coda.gui.output.OutputForR;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.rosuda.JRI.Rengine;
import java.awt.Toolkit;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Guest2
 */
public class ZpatternsMenu extends AbstractMenuDialog{
    
    Rengine re;
    JFrame plotPNG = new JFrame();
    JPanel panel = new JPanel();
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    
    public static final long serialVersionUID = 1L;
    
    public ZpatternsMenu(final CoDaPackMain mainApp, Rengine r){
        super(mainApp, "ZPatterns Plot Menu", true);
        re = r;
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        JFrame frame = new JFrame();
        frame.setTitle("Message");
        
        String selectedNames[] = ds.getSelectedData();
        
        if(selectedNames.length > 0){
            DataFrame df = mainApplication.getActiveDataFrame();
            double[][] data = df.getNumericalData(selectedNames); // matriu amb les dades corresponents
                
                re.assign("X", data[0]);
                re.eval("X" + " <- matrix( " + "X" + " ,nc=1)");
                for(int i=1; i < data.length; i++){
                    re.assign("tmp", data[i]);
                    re.eval("X" + " <- cbind(" + "X" + ",matrix(tmp,nc=1))");
                }
                
                re.eval("png('out.png',width=700,height=700)");
                re.eval("zCompositions::zPatterns(X,label=0)");
                re.eval("out <- capture.output(zCompositions::zPatterns(X,label=0))");
                re.eval("dev.off()");
                
                setVisible(false);
                
                        // jframe configuration
        
                panel.setSize(800,800);
                plotPNG.setTitle("zPatterns Plot Output");
                plotPNG.setIconImage(
                Toolkit.getDefaultToolkit().
                        getImage(
                getClass().getResource(CoDaPackMain.RESOURCE_PATH + "logoL.png")));
                ImageIcon icon = new ImageIcon("out.png");
                JLabel label = new JLabel(icon,JLabel.CENTER);
                label.setSize(700,700);
                panel.setLayout(new GridBagLayout());
                panel.add(label);
                plotPNG.getContentPane().add(panel);
                plotPNG.setSize(800,800);
                plotPNG.setLocation(dim.width/2-plotPNG.getSize().width/2, dim.height/2-plotPNG.getSize().height/2);
                plotPNG.setVisible(true);
                
                File file = new File("out.png");
                file.delete();
                
                // we show the output 
                
                OutputElement e = new OutputForR(re.eval("out").asStringArray());
                outputPanel.addOutput(e);
        }
        else{ // no data selected
            JOptionPane.showMessageDialog(frame, "Please select data");
        }
    }
}
