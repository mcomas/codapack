/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.gui.menu;


import coda.BasicStats;
import coda.CoDaStats;
import coda.DataFrame;
import coda.gui.CoDaPackMain;
import coda.gui.utils.DataSelector;
import coda.sim.CoDaRandom;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author mcomas
 */
public class NormalSampleMenu{
    ArrayList<JDialog> dialogs = new ArrayList<JDialog>();
    public NormalSampleMenu(final CoDaPackMain mainApp){
        dialogs.add(new MethodSelection(mainApp));
        dialogs.add(new SampleSelection(mainApp));
        dialogs.add(new DefineCenter(mainApp));
        dialogs.get(0).setVisible(true);
    }
    private abstract class AbstractDialog extends JDialog{
        JPanel mainArea = new JPanel();
        CoDaPackMain mainApp;
        JButton previous;
        JButton next;
        public AbstractDialog(CoDaPackMain mainApp){
            super(mainApp);
            this.mainApp = mainApp;
            this.setTitle("ALN sample generation");
            Point p = mainApp.getLocation();
            p.x = p.x + (mainApp.getWidth()-520)/2;
            p.y = p.y + (mainApp.getHeight()-430)/2;
            setLocation(p);
            setSize(560,430);
            getContentPane().setLayout(new BorderLayout());
            getContentPane().add(mainArea, BorderLayout.CENTER);
            JPanel bottom = new JPanel();
            previous = new JButton("Previous");
            previous.addActionListener(new java.awt.event.ActionListener() {
                
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    previousButton();
                }
            });
            next = new JButton("Next");
            next.addActionListener(new java.awt.event.ActionListener() {
                
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    nextButton();
                }
            });
            bottom.add(previous);
            bottom.add(next);
            getContentPane().add(bottom, BorderLayout.SOUTH);
        }
        public abstract void previousButton();
        public abstract void nextButton();
    }
    
    private class MethodSelection extends AbstractDialog{
        JRadioButton sample;
        JRadioButton scratch;
        public MethodSelection(CoDaPackMain mainApp){
            super(mainApp);
            JPanel panel0 = new JPanel();            
            JLabel label1 = new JLabel("Generate normals");
            sample = new JRadioButton("with sample");
            sample.setSelected(true);
            scratch = new JRadioButton("without sample");
            ButtonGroup group = new ButtonGroup();
            group.add(sample);
            group.add(scratch);
            panel0.add(label1);
            panel0.add(sample);
            panel0.add(scratch);
            mainArea.add(panel0);

            previous.setEnabled(false);
        }
        @Override
        public void previousButton() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void nextButton() {
            if(sample.isSelected()){
                this.setVisible(false);
                dialogs.get(1).setVisible(true);
            }else{
                this.setVisible(false);
                dialogs.get(2).setVisible(true);
            }
        }
    }

    private class SampleSelection extends AbstractDialog{
        DataSelector ds;
        JTextField sampleSize;
        public SampleSelection(CoDaPackMain mainApp){
            super(mainApp);
            ds = new DataSelector(mainApp.getActiveDataFrame(), CoDaPackMain.dataList.getSelectedData(), false);
            mainArea.add(ds);
            JPanel options = new JPanel();
            sampleSize = new JTextField(10);
            options.add(new JLabel("Sample size: "));
            options.add(sampleSize);

            getContentPane().add(options, BorderLayout.EAST);
            next.setText("Finish");
        }
        @Override
        public void previousButton() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        @Override
        public void nextButton() {
            int size = Integer.parseInt(sampleSize.getText());
            DataFrame df = mainApp.getActiveDataFrame();
            String[] sel_names = ds.getSelectedData();
            int m = ds.getSelectedData().length-1;
            int partition[][] = CoDaStats.defaultPartition(m+1);

            boolean selection[] = df.getValidCompositions(sel_names);
            double data[][] = df.getNumericalData(sel_names);
            double vdata[][] = coda.Utils.reduceData(data, selection);

            vdata = CoDaStats.transformRawILR(vdata, partition);

            double mean[] = BasicStats.mean(vdata);
            double cov[][] = BasicStats.covariance(vdata);

            double sample[][] = new double[m][size];
            for(int i=0;i<size;i++){
                double vec[] = CoDaRandom.normalRandomVariable(mean, cov);
                for(int j=0;j<m;j++){
                    sample[j][i] = vec[j];
                }
            }
            String new_names[] = new String[m+1];
            for(int i=0;i<m+1;i++)
                new_names[i] = "C" + (i+1);

            sample = CoDaStats.transformILRRaw(sample, partition);
            df.addData(new_names, sample);
            mainApp.updateDataFrame(df);
            this.setVisible(false);
        }
    }
    
    private class DefineCenter extends AbstractDialog{

        JTextField centerField;
        public DefineCenter(CoDaPackMain mainApp){
            super(mainApp);
            this.setTitle("Define a center");
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            JLabel label2 = new JLabel("Define a center for your composition");
            centerField = new JTextField("1 1 1 1 1", 20);
            panel.add(label2);
            panel.add(centerField);
            mainArea.add(panel);

        }

        @Override
        public void previousButton() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void nextButton() {
            setVisible(false);
            //String[] v = centerField.getText().split(" ");

            StringTokenizer tok = new StringTokenizer(centerField.getText());
            int e = tok.countTokens();

            double [] center = new double[e];

            for(int i=0;i<e;i++)
                center[i] = Double.parseDouble(tok.nextToken());

            new DefineVariance(mainApp, center).setVisible(true);
        }
    }
    private class DefineVariance extends AbstractDialog{

        JRadioButton basisMatrix;
        JRadioButton crudeData;
        JRadioButton logratiosVariance;
        JRadioButton logcontrast;
        double center[];
        public DefineVariance(CoDaPackMain mainApp, double center[]){
            super(mainApp);

            this.center = center;
            JLabel label = new JLabel("Define covariance structure using");

            basisMatrix = new JRadioButton("lognormal covariance");
            basisMatrix.setSelected(true);
            crudeData = new JRadioButton("crude covariance");
            logratiosVariance = new JRadioButton("logratios variances");
            logcontrast = new JRadioButton("logcontrasts");

            JPanel panel0 = new JPanel();
            panel0.setLayout(new BoxLayout(panel0, BoxLayout.Y_AXIS));
            ButtonGroup group = new ButtonGroup();
            group.add(basisMatrix);
            group.add(crudeData);
            group.add(logratiosVariance);
            group.add(logcontrast);

            panel0.add(label);
            panel0.add(basisMatrix);
            panel0.add(crudeData);
            panel0.add(logratiosVariance);
            panel0.add(logcontrast);
            mainArea.add(panel0);
        }

        @Override
        public void previousButton() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void nextButton() {
            setVisible(false);
            if(basisMatrix.isSelected()){
                new DefineBasisMatrix(mainApp, center).setVisible(true);
            }else if(crudeData.isSelected()){
                new DefineCrudeMatrix(mainApp, center).setVisible(true);
            }else if(logratiosVariance.isSelected()){
                new DefineLogratiosVariance(mainApp, center).setVisible(true);
            }else{
                new DefineLogcontrasts(mainApp, center).setVisible(true);
            }
        }
    }
    private class DefineBasisMatrix extends AbstractDialog{
        JTextArea covArea;
        double center[];
        JTextField sampleSize;
        public DefineBasisMatrix(CoDaPackMain mainApp, double center[]){
            super(mainApp);

            this.center = center;

            JPanel panel0 = new JPanel();
            panel0.setLayout(new BoxLayout(panel0, BoxLayout.Y_AXIS));
            JScrollPane jScrollPane1 = new JScrollPane();
            jScrollPane1.setPreferredSize(new java.awt.Dimension(300,300));
            covArea = new JTextArea(10, 10);
            jScrollPane1.setViewportView(covArea);
            panel0.add(new JLabel("Covariance matrix:"));
            panel0.add(jScrollPane1);

            mainArea.add(panel0);

            JPanel options = new JPanel();
            sampleSize = new JTextField(10);
            options.add(new JLabel("Sample size: "));
            options.add(sampleSize);

            getContentPane().add(options, BorderLayout.EAST);
            next.setText("Finish");
        }
        @Override
        public void previousButton() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        @Override
        public void nextButton() {
            int size = Integer.parseInt(sampleSize.getText());
            StringTokenizer tok = new StringTokenizer(covArea.getText());
            int e = tok.countTokens();
            int n = center.length;
            if(n*n == e){
                double covariance[][] = new double[n][n];
                for(int i=0;i<n;i++){
                    for(int j=0;j<n;j++){
                        covariance[i][j] = Double.valueOf(tok.nextToken());
                    }
                }
                while(tok.hasMoreTokens()){
                    String item = tok.nextToken();
                }

                double data[][] = new double[n][size];
                double mean[] = new double[center.length];
                for(int j=0;j<n;j++)
                    mean[j] = Math.log(center[j]);

                for(int i=0; i< size;i++){
                    double vec[] = CoDaRandom.normalRandomVariable(mean, covariance);
                    for(int j=0;j<n;j++){
                        data[j][i] = Math.exp(vec[j]);
                    }
                }
                String[] names = new String[n];
                for(int i=0; i<n;i++)
                    names[i] = "C" + (i+1);

                DataFrame df = mainApp.getActiveDataFrame();
                if(df == null){
                    df = new DataFrame();
                    mainApp.addDataFrame(df);
                }
                df.addData(names, data);
                mainApp.updateDataFrame(df);
                this.setVisible(false);
            }
        }
    }
    private class DefineCrudeMatrix extends AbstractDialog{
        double center[];

        JTextArea covArea;
        JTextField sampleSize;
        public DefineCrudeMatrix(CoDaPackMain mainApp, double center[]){
            super(mainApp);

            this.center = center;

            JPanel panel0 = new JPanel();
            panel0.setLayout(new BoxLayout(panel0, BoxLayout.Y_AXIS));
            JScrollPane jScrollPane1 = new JScrollPane();
            jScrollPane1.setPreferredSize(new java.awt.Dimension(300,300));
            covArea = new JTextArea(10, 10);
            jScrollPane1.setViewportView(covArea);
            panel0.add(new JLabel("Covariance matrix:"));
            panel0.add(jScrollPane1);

            mainArea.add(panel0);

            JPanel options = new JPanel();
            sampleSize = new JTextField(10);
            options.add(new JLabel("Sample size: "));
            options.add(sampleSize);

            getContentPane().add(options, BorderLayout.EAST);
            next.setText("Finish");
        }
        @Override
        public void previousButton() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        @Override
        public void nextButton() {
            int size = Integer.parseInt(sampleSize.getText());
            StringTokenizer tok = new StringTokenizer(covArea.getText());
            int nt = tok.countTokens();
            int D = center.length;
            if(D*D == nt){
                double covariance[][] = new double[D][D];
                for(int i=0;i<D;i++){
                    for(int j=0;j<D;j++){
                        covariance[i][j] = Double.valueOf(tok.nextToken());
                    }
                }
                while(tok.hasMoreTokens()){
                    String item = tok.nextToken();
                }
                int d = D-1;

                double mean[] = new double[d];
                for(int i=0;i<d;i++)
                        mean[i] = Math.log(center[i]/center[d]) - 0.5 *
                                (covariance[i][i]/(center[i]*center[i])
                                - covariance[d][d] /(center[d]*center[d]));
                
                double t[][] = new double[D][D];
                for(int i=0;i<D;i++)
                    for(int j=0;j<D;j++)
                        t[i][j] = covariance[i][i] / (center[i]*center[i]) -
                                2 * covariance[i][j] / (center[i]*center[j]) +
                                covariance[j][j] / (center[j]*center[j]) +
                                0.25 * Math.pow((covariance[i][i] / 
                                (center[i]*center[i]) - covariance[j][j]
                                / (center[j]*center[j])), 2);


                
                double cov[][] = new double[d][d];
                for(int i=0;i<d;i++)
                    for(int j=0;j<d;j++)
                        cov[i][j] = 0.5 * (t[i][d] + t[j][d] - t[i][j]);


                double data[][] = new double[d][size];
                //double mean[] = new double[d];
                //for(int j=0;j<d;j++)
                //    mean[j] = Math.log(center[j]/center[d]);

                for(int i=0; i< size;i++){
                    double vec[] = CoDaRandom.normalRandomVariable(mean, cov);
                    for(int j=0;j<d;j++){
                        data[j][i] = vec[j];
                    }
                }

                data = CoDaStats.transformALRRaw(data);
                
                String[] names = new String[D];
                for(int i=0; i<D;i++)
                    names[i] = "C" + (i+1);

                DataFrame df = mainApp.getActiveDataFrame();
                if(df == null){
                    df = new DataFrame();
                    mainApp.addDataFrame(df);
                }
                df.addData(names, data);
                mainApp.updateDataFrame(df);
                this.setVisible(false);
            }
        }
    }
    private class DefineLogratiosVariance extends AbstractDialog{
        double center[];

        JTextArea varArea;
        JTextField sampleSize;
        public DefineLogratiosVariance(CoDaPackMain mainApp, double center[]){
            super(mainApp);

            this.center = center;

            JPanel panel0 = new JPanel();
            panel0.setLayout(new BoxLayout(panel0, BoxLayout.Y_AXIS));
            JScrollPane jScrollPane1 = new JScrollPane();
            jScrollPane1.setPreferredSize(new java.awt.Dimension(300,300));
            varArea = new JTextArea(10, 10);
            jScrollPane1.setViewportView(varArea);
            panel0.add(new JLabel("Variation matrix:"));
            panel0.add(jScrollPane1);

            mainArea.add(panel0);

            JPanel options = new JPanel();
            sampleSize = new JTextField(10);
            options.add(new JLabel("Sample size: "));
            options.add(sampleSize);

            getContentPane().add(options, BorderLayout.EAST);
            next.setText("Finish");
        }

        @Override
        public void previousButton() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void nextButton() {
            int size = Integer.parseInt(sampleSize.getText());
            StringTokenizer tok = new StringTokenizer(varArea.getText());
            int nt = tok.countTokens();
            int D = center.length;
            if(D*D == nt){
                double t[][] = new double[D][D];
                for(int i=0;i<D;i++){
                    for(int j=0;j<D;j++){
                        t[i][j] = Double.valueOf(tok.nextToken());
                    }
                }
                while(tok.hasMoreTokens()){
                    String item = tok.nextToken();
                }
                int d = D-1;
                double mean[] = new double[d];
                for(int i=0;i<d;i++)
                        mean[i] = Math.log(center[i]/center[d]);

                double cov[][] = new double[d][d];
                for(int i=0;i<d;i++)
                    for(int j=0;j<d;j++)
                        cov[i][j] = 0.5 * (t[i][d] + t[j][d] - t[i][j]);


                double data[][] = new double[d][size];
                //double mean[] = new double[d];
                //for(int j=0;j<d;j++)
                //    mean[j] = Math.log(center[j]/center[d]);

                for(int i=0; i< size;i++){
                    double vec[] = CoDaRandom.normalRandomVariable(mean, cov);
                    for(int j=0;j<d;j++){
                        data[j][i] = vec[j];
                    }
                }

                data = CoDaStats.transformALRRaw(data);

                String[] names = new String[D];
                for(int i=0; i<D;i++)
                    names[i] = "C" + (i+1);

                DataFrame df = mainApp.getActiveDataFrame();
                if(df == null){
                    df = new DataFrame();
                    mainApp.addDataFrame(df);
                }
                df.addData(names, data);
                mainApp.updateDataFrame(df);
                this.setVisible(false);
            }
        }
    }
    private class DefineLogcontrasts extends AbstractDialog{
        double center[];
        JTextField a[];
        JTextField leftCoeff;
        JTextField rightCoeff;
        public DefineLogcontrasts(CoDaPackMain mainApp, double center[]){
            super(mainApp);

            this.center = center;
            int D = center.length;

            JPanel constantLogcontrast = new JPanel();
            constantLogcontrast.setBorder(
                    BorderFactory.createTitledBorder("Constant logcontrast"));

            a = new JTextField[D];
            for(int i=0;i<D;i++){
                a[i] = new JTextField(2);
                constantLogcontrast.add(a[i]);
                String text = i + 1 == D ? "log(C" +  (i+1) + ") = constant" : "log(C"+  (i+1) + ") + ";
                constantLogcontrast.add(new JLabel(text));
            }
            JButton defineLogcontrast = new JButton("Define constant logcontrast");
            defineLogcontrast.addActionListener(new java.awt.event.ActionListener() {
                
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    defineLogcontrastButton();
                }
            });

            JPanel logratio = new JPanel();
            logratio.setBorder(
                    BorderFactory.createTitledBorder("Component bound relation"));
            String[] components = new String[D];
            for(int i=0;i<D;i++)
                components[i] = "C" + (i+1);

            JComboBox left = new JComboBox(components);
            leftCoeff = new JTextField(3);
            JComboBox right = new JComboBox(components);
            rightCoeff = new JTextField(3);
            logratio.add(leftCoeff);
            logratio.add(left);
            logratio.add(new JLabel(" < "));
            logratio.add(rightCoeff);
            logratio.add(right);

            JButton defineLogratio = new JButton("Define component bound relation");
            defineLogratio.addActionListener(new java.awt.event.ActionListener() {
                
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    defineLogratioButton();
                }
            });

            mainArea.setLayout(new GridLayout(2,1));

            mainArea.add(constantLogcontrast);
            mainArea.add(logratio);

        }

        public void defineLogcontrastButton(){

        }
        public void defineLogratioButton(){
            
        }
        @Override
        public void previousButton() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void nextButton() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
