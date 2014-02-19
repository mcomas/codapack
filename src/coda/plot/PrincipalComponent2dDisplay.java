/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.plot;

import coda.CoDaStats;
import java.awt.BasicStroke;
import java.awt.Graphics2D;

//import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

/**
 *
 * @author marc
 */
public final class PrincipalComponent2dDisplay extends TernaryPlot2dDisplay{

    private double[] pc1, pc2;
    
    private double[][] positive_pc1;
    private double[][] pospc1axis;
    private double[][] positive_pc2;
    private double[][] pospc2axis;
    private double[][] negative_pc1;
    private double[][] negpc1axis;
    private double[][] negative_pc2;
    private double[][] negpc2axis;

    public static class PrincipalComponent2dBuilder
            extends TernaryPlot2dBuilder{
        private double pc1[];
        private double pc2[];
        public PrincipalComponent2dBuilder(String[] names, double[][] data, double[] pc1, double[] pc2){
            super(names, data);
            this.pc1 = pc1;
            this.pc2 = pc2;
        }
        @Override
        public PrincipalComponent2dBuilder groups(int groups[], String gnames[]){
            return (PrincipalComponent2dBuilder) super.groups(groups, gnames);
        }
        @Override
        public PrincipalComponent2dBuilder mapping(int mapping[]){
            return (PrincipalComponent2dBuilder) super.mapping(mapping);
        }
        @Override
        public PrincipalComponent2dDisplay build(){
            return new PrincipalComponent2dDisplay(this);
        }
    }
    protected PrincipalComponent2dDisplay(PrincipalComponent2dBuilder builder){
        super(builder);
        
        this.pc1 = builder.pc1;
        this.pc2 = builder.pc2;

        legendNames.add(new LegendItem("PC1","Prin.Comp.1",(short)1));
        legendNames.add(new LegendItem("PC2","Prin.Comp.2",(short)1));
        setPrincipalComponents();
    }
    public void setPrincipalComponents(){
        int N = 100;
        double dx = 0.1;
        
        double[] step1 = CoDaStats.powering(pc1, dx);
        double[] step2 = CoDaStats.powering(pc2, dx);

        positive_pc1 = new double[N][3];
        positive_pc2 = new double[N][3];
        pospc1axis = new double[N][2];
        pospc2axis = new double[N][2];
        negative_pc1 = new double[N][3];
        negative_pc2 = new double[N][3];
        negpc1axis = new double[N][2];
        negpc2axis = new double[N][2];

        if(isCentered){
            positive_pc1[0][0] = positive_pc2[0][0] =
            positive_pc2[0][1] = positive_pc1[0][1] =
            positive_pc2[0][2] = positive_pc1[0][2] =
            negative_pc2[0][0] = negative_pc1[0][0] =
            negative_pc2[0][1] = negative_pc1[0][1] =
            negative_pc2[0][2] = negative_pc1[0][2] = 0.333333333333;
        }else{
            positive_pc1[0][0] = positive_pc2[0][0] = center[0];
            positive_pc2[0][1] = positive_pc1[0][1] = center[1];
            positive_pc2[0][2] = positive_pc1[0][2] = center[2];
            negative_pc2[0][0] = negative_pc1[0][0] = center[0];
            negative_pc2[0][1] = negative_pc1[0][1] = center[1];
            negative_pc2[0][2] = negative_pc1[0][2] = center[2];
        }
        pospc1axis[0] = CoDaStats.ternaryTransform3(positive_pc1[0]);
        
        pospc2axis[0] = CoDaStats.ternaryTransform3(positive_pc2[0]);
        for(int i=1;i<N;i++){
            positive_pc1[i] = CoDaStats.closure(
                    CoDaStats.perturbation(positive_pc1[i-1], step1, positive_pc1[i]));
            pospc1axis[i] = CoDaStats.ternaryTransform3(positive_pc1[i]);
            positive_pc2[i] = CoDaStats.closure(
                    CoDaStats.perturbation(positive_pc2[i-1], step2, positive_pc2[i]));
            pospc2axis[i] = CoDaStats.ternaryTransform3(positive_pc2[i]);
        }

        step1 = CoDaStats.powering(pc1, -dx);
        step2 = CoDaStats.powering(pc2, -dx);
        
        negpc1axis[0] = CoDaStats.ternaryTransform3(negative_pc1[0]);
        negpc2axis[0] = CoDaStats.ternaryTransform3(negative_pc2[0]);
        for(int i=1;i<N;i++){
            negative_pc1[i] = CoDaStats.closure(
                    CoDaStats.perturbation(negative_pc1[i-1], step1, negative_pc1[i]));
            negpc1axis[i] = CoDaStats.ternaryTransform3(negative_pc1[i]);
            negative_pc2[i] = CoDaStats.closure(
                    CoDaStats.perturbation(negative_pc2[i-1], step2, negative_pc2[i]));
            negpc2axis[i] = CoDaStats.ternaryTransform3(negative_pc2[i]);
        }
    }
    @Override
    public void setCentered(boolean centered){
        super.setCentered(centered);
        //isCentered = centered;
        setPrincipalComponents();
        /*
        if(centered){
            double [][]cdata = CoDaPack.centerData(data);

            setGrid(center);
            for(int i=0;i<origZ.length;i++){
            origZ[i] = CoDaPack.ternaryTransform(
                    cdata[0][i], cdata[1][i], cdata[2][i]);
            }
        }else{
            setGrid(null);
            for(int i=0;i<origZ.length;i++){
            origZ[i] = CoDaPack.ternaryTransform(
                    data[0][i], data[1][i], data[2][i]);
            }
        }
         * 
         */
    }
    @Override
    public void transformData(){
        super.transformData();
        for(int i=0;i<positive_pc1.length;i++){
            pospc1axis[i] = CoDaStats.ternaryTransform3(positive_pc1[i]);
            pospc1axis[i] = transform(pospc1axis[i][0], pospc1axis[i][1], pospc1axis[i]);
        }
        for(int i=0;i<negative_pc1.length;i++){
            negpc1axis[i] = CoDaStats.ternaryTransform3(negative_pc1[i]);
            negpc1axis[i] = transform(negpc1axis[i][0], negpc1axis[i][1], negpc1axis[i]);
        }
        for(int i=0;i<positive_pc2.length;i++){
            pospc2axis[i] = CoDaStats.ternaryTransform3(positive_pc2[i]);
            pospc2axis[i] = transform(pospc2axis[i][0], pospc2axis[i][1], pospc2axis[i]);
        }
        for(int i=0;i<negative_pc2.length;i++){
            negpc2axis[i] = CoDaStats.ternaryTransform3(negative_pc2[i]);
            negpc2axis[i] = transform(negpc2axis[i][0], negpc2axis[i][1], negpc2axis[i]);
        }
    } 
    @Override
    public void paintComponent(Graphics2D g2, double width, double height){
        super.paintComponent(g2, width, height);
        
        drawPrincipalComponents(g2);
    }
    public void drawPrincipalComponents(Graphics2D g2){
        g2.setColor( CoDaDisplayConfiguration.getColor("Prin.Comp.",1) );
        g2.setStroke(new BasicStroke(CoDaDisplayConfiguration.getSize("Prin.Comp.",1) ,
                BasicStroke.JOIN_MITER,
                BasicStroke.CAP_ROUND));

        Point2D from = null, to = null;
        //Path2D.Double principalComponent = new Path2D.Double();
        //Point2D o = null;
        //o = defaultTransform.transform(new Point2D.Double(pospc1axis[0][0],pospc1axis[0][1]), o);
        //principalComponent.moveTo(o.getX(), o.getY());
        for(int i=1;i<pospc1axis.length;i++){
            from = defaultTransform.transform(new Point2D.Double(pospc1axis[i-1][0],pospc1axis[i-1][1]), from);
            to = defaultTransform.transform(new Point2D.Double(pospc1axis[i][0],pospc1axis[i][1]), to);
            g2.draw(PlotUtils.drawLine(from, to));
            //o = defaultTransform.transform(new Point2D.Double(pospc1axis[i][0],pospc1axis[i][1]), o);
            //principalComponent.lineTo(o.getX(), o.getY());
        }
        //o = defaultTransform.transform(new Point2D.Double(pospc1axis[0][0],pospc1axis[0][1]), o);
        //principalComponent.moveTo(o.getX(), o.getY());
        for(int i=1;i<negpc1axis.length;i++){
            from = defaultTransform.transform(new Point2D.Double(negpc1axis[i-1][0],negpc1axis[i-1][1]), from);
            to = defaultTransform.transform(new Point2D.Double(negpc1axis[i][0],negpc1axis[i][1]), to);
            g2.draw(PlotUtils.drawLine(from, to));
            //o = defaultTransform.transform(new Point2D.Double(negpc1axis[i][0],negpc1axis[i][1]), o);
            //principalComponent.lineTo(o.getX(), o.getY());
        }
        //g2.draw(principalComponent);
        
        //principalComponent = new Path2D.Double();
        g2.setColor( CoDaDisplayConfiguration.getColor("Prin.Comp.",2) );
        g2.setStroke(new BasicStroke(CoDaDisplayConfiguration.getSize("Prin.Comp.",2) ,
                BasicStroke.JOIN_MITER,
                BasicStroke.CAP_ROUND));
        //o = defaultTransform.transform(new Point2D.Double(pospc1axis[0][0],pospc1axis[0][1]), o);
        //principalComponent.moveTo(o.getX(), o.getY());
        for(int i=1;i<pospc2axis.length;i++){
            from = defaultTransform.transform(new Point2D.Double(pospc2axis[i-1][0],pospc2axis[i-1][1]), from);
            to = defaultTransform.transform(new Point2D.Double(pospc2axis[i][0],pospc2axis[i][1]), to);
            g2.draw(PlotUtils.drawLine(from, to));
            //o = defaultTransform.transform(new Point2D.Double(pospc2axis[i][0],pospc2axis[i][1]), o);
            //principalComponent.lineTo(o.getX(), o.getY());
        }
        //o = defaultTransform.transform(new Point2D.Double(pospc1axis[0][0],pospc1axis[0][1]), o);
        //principalComponent.moveTo(o.getX(), o.getY());
        for(int i=1;i<negpc2axis.length;i++){
            from = defaultTransform.transform(new Point2D.Double(negpc2axis[i-1][0],negpc2axis[i-1][1]), from);
            to = defaultTransform.transform(new Point2D.Double(negpc2axis[i][0],negpc2axis[i][1]), to);
            g2.draw(PlotUtils.drawLine(from, to));
            //o = defaultTransform.transform(new Point2D.Double(negpc2axis[i][0],negpc2axis[i][1]), o);
            //principalComponent.lineTo(o.getX(), o.getY());
        }
        //g2.draw(principalComponent);
    }
}
