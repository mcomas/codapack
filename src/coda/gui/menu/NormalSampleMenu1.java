/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.gui.menu;


import coda.gui.CoDaPackMain;
import coda.gui.utils.DataSelector;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author mcomas
 */
public class NormalSampleMenu1 extends JDialog{

    final DataSelector ds;

    public NormalSampleMenu1(final CoDaPackMain mainApp){
        super(mainApp, "ALN data generation");
        setSize(560,430);
        JTabbedPane tabbedPane = new JTabbedPane();
        ds = new DataSelector(mainApp.getActiveDataFrame(), CoDaPackMain.dataList.getSelectedData(), false);


        /*
         *  Generating normal variables from sample
         */
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        panel1.add(ds, BorderLayout.CENTER);
        JPanel optionsPanel1 = new JPanel();
        optionsPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Options"));
        optionsPanel1.setPreferredSize(new Dimension(200,200));
        //optionsPanel1.setLayout(new BoxLayout(optionsPanel1, BoxLayout.Y_AXIS));
        //eastPanel.add(optionsPanel1);
        panel1.add(optionsPanel1, BorderLayout.EAST);

        /*
         *  Generating normal variables from parameters
         */
        JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout());
        
        JTable variationArray = new JTable(10,10);
        variationArray.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane jScrollVariationArray = new JScrollPane();
        jScrollVariationArray.setViewportView(variationArray);        
        panel2.add(jScrollVariationArray, BorderLayout.CENTER);


        JPanel optionsPane2 = new JPanel();
        optionsPane2.setPreferredSize(new java.awt.Dimension(200, 430));
        JTextArea matrixArea = new JTextArea(1, 1);
        JScrollPane jScrollMatrixArea = new JScrollPane();
        jScrollMatrixArea.setPreferredSize(new java.awt.Dimension(150, 150));

        jScrollMatrixArea.setViewportView(matrixArea);
        JTextField ncomponents = new JTextField(5);
        optionsPane2.add(new JLabel("Nr. components: "));
        optionsPane2.add(ncomponents);
        optionsPane2.add(new JLabel("Mean: "));
        JTextField center = new JTextField(20);
        optionsPane2.add(center);
        optionsPane2.add(new JLabel("Covariance: "));
        optionsPane2.add(jScrollMatrixArea);

        panel2.add(optionsPane2, BorderLayout.EAST);
        
        JPanel panel3 = new JPanel();

        tabbedPane.addTab("From sample", panel1);
        tabbedPane.addTab("Normal paramaters", panel2);
        tabbedPane.addTab("By logcontrast", panel3);
        

        getContentPane().add(tabbedPane);
    }
}
