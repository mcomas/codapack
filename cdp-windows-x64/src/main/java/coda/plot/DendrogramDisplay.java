/**	
 *	Copyright 2011-2016 Marc Comas - Santiago Thió
 *
 *	This file is part of CoDaPack.
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

package coda.plot;

import coda.BasicStats;
import coda.DataFrame;
import coda.util.Node;
import coda.util.Tree;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 *
 * @author mcomas
 */
public class DendrogramDisplay extends CoDa2dDisplay{
    private double LEFT = 0, RIGHT = 0;
    private int auxToSetBalanceNames = 1;
    private String names[];
    private double x[][];
    private int partition[][];
    private int partition_orig[][];
    private int order[];
    private int groups[];
    private String gnames[];
    private int ngroups = 0;
    private int mapping[];
    //private TeXIcon iconLabel[];
    String textLabel[];
    // INDEX1 and INDEX2 are variables used in tree recursion. Don't touch them!!
    private static int INDEX1 = 0, INDEX2 = 0;
    //private double OY = -0.5, OX = 0.5;
    //private double treeHeight = 1, treeWidth = 1;
    private double moveUp = 0.2;
    private double totalHeight;
    private Tree<DendroBracket> T = new Tree<DendroBracket>();

    BasicStroke normal = new BasicStroke(config.getSize("axis"),
                BasicStroke.JOIN_MITER,
                BasicStroke.CAP_ROUND);
    float[] dash1 = { 2f };
    BasicStroke dashed = new BasicStroke(config.getSize("axis"),
        BasicStroke.JOIN_MITER, BasicStroke.CAP_ROUND,
        1.0f, dash1, 2f);
    
    public DendrogramDisplay(DendrogramBuilder builder){
        names = builder.names;
        x = builder.x;
        partition_orig = builder.partition;
        partition = new int[partition_orig.length][];
        order = new int[partition_orig.length];
        orderPartition(0, IntStream.range(0, partition_orig.length).toArray(), new int[0]);
        for(int i=0;i<order.length;i++){
            partition[i] = partition_orig[order[i]];
        }
        //Arrays.stream(indexos).map(i -> original[i]).toArray()
        // for(int i=0;i<partition.length;i++){
        //     if(Arrays.stream(partition[i]).allMatch(x -> x == 1 || x == -1)){
        //         System.out.println("Partition " + i + " is the root.");
        //     }
        // }
        System.out.println("Order: " + Arrays.toString(order));
        groups = builder.groups;
        gnames = builder.gnames;
        for(int i=0;i<gnames.length;i++)
            legendNames.add(new LegendItem(gnames[i], "data".concat(Integer.toString(i))));
        mapping = builder.mapping;

        // Calculate number of groups
        for(int group : groups) ngroups =  (group > ngroups ? group : ngroups);
        ngroups++;

        //iconLabel = new TeXIcon[names.length];
        textLabel = names;
        int s = config.getSize("label").intValue();
        /*for(int i =0;i<names.length;i++){
            try{
                iconLabel[i] = new TeXFormula(names[i])
                    .createTeXIcon(TeXConstants.STYLE_DISPLAY, s);
            }catch(ParseException e){
                // LaTeX instruction not compiled
                iconLabel[i] = new TeXFormula("Item_" + (i+1))
                    .createTeXIcon(TeXConstants.STYLE_DISPLAY, s);
            }
        }*/
        boolean active[] = new boolean[names.length];
        Arrays.fill(active, true);
        
        INDEX1 = 0; Node<DendroBracket> root = treeInitialization(0, active);
        totalHeight = getTreeHeight(root);
        INDEX1 = 0; root.getData().top = setCoordinates(root, origin[1] + (1+moveUp)*v_y[1]);

        T.setRootElement(root);
    }
    public int orderPartition(int ind, int suport[], int excluded[]){
        if(suport.length == 1) return ind;
        for(int i=0;i<partition_orig.length;i++){
            int []part = partition_orig[i];
            int []s = Arrays.stream(suport).map(j -> part[j]).toArray();
            int []o = Arrays.stream(excluded).map(j -> part[j]).toArray();
            if(Arrays.stream(s).allMatch(x -> x == 1 || x == -1) && 
               Arrays.stream(o).allMatch(x -> x == 0)){
                order[ind++] = i;
                System.out.println("Partition " + i + " is the root.");
                int[] right = IntStream.range(0, part.length).filter(j -> part[j] == 1).toArray();
                int[] right_out = IntStream.range(0, part.length).filter(j -> part[j] == 0 || part[j] == -1).toArray();
                ind = orderPartition(ind, right, right_out);
                int[] left = IntStream.range(0, part.length).filter(j -> part[j] == -1).toArray();
                int[] left_out = IntStream.range(0, part.length).filter(j -> part[j] == 0 || part[j] == 1).toArray();
                ind = orderPartition(ind, left, left_out);
            }
        }
        return ind;
    }
    private Node<DendroBracket> treeInitialization(int index, boolean active[]){
        //Checking if previous node was a leaf
        int count = 0;
        for(boolean value : active) if(value) count++;
        if( count < 2 ) return null;

        DendroBracket stats = new DendroBracket(ngroups);
        Node<DendroBracket> node = new Node<DendroBracket>(stats);
        stats.balance = INDEX1;
        stats.t_mean = BasicStats.mean(x[order[INDEX1]]);
        stats.t_variance = BasicStats.variance(x[order[INDEX1]]);
        /*
         CoDaPack.percentile(x[INDEX1], p);
        stats.t_minimum = quartiles[0];
        stats.t_fifth_percentile = quartiles[1];
        stats.t_first_quartile = quartiles[2];
        stats.t_median = quartiles[3];
        stats.t_third_quartile = quartiles[4];
        stats.t_ninetyfifth_percentile = quartiles[5];
        stats.t_maximum = quartiles[6];
        */
        int p[] = {0, 5, 25, 50, 75, 95, 100};
        for(int gr=0; gr<ngroups;gr++){
            double [] xdata = coda.Utils.reduceData(x[order[INDEX1]], groups, gr);
            stats.mean[gr] = BasicStats.mean(xdata);
            stats.variance[gr] = BasicStats.variance(xdata);
            double quartiles[] = BasicStats.percentile(xdata, p);
            stats.minimum[gr] = quartiles[0];
            stats.fifth_percentile[gr] = quartiles[1];
            stats.first_quartile[gr] = quartiles[2];
            stats.median[gr] = quartiles[3];
            stats.third_quartile[gr] = quartiles[4];
            stats.ninetyfifth_percentile[gr] = quartiles[5];
            stats.maximum[gr] = quartiles[6];
            if(Math.abs(stats.maximum[gr]) > RIGHT || Math.abs(stats.minimum[gr]) > RIGHT){
                RIGHT = (int)((Math.abs(stats.maximum[gr]) > Math.abs(stats.minimum[gr]) ?
                    Math.abs(stats.maximum[gr]) :
                    Math.abs(stats.minimum[gr]))+1) ;
                LEFT = -RIGHT;
            }
        }
        INDEX1++;

        boolean next_active[] = new boolean[active.length];
        
        //Right node
        for(int i=0;i<active.length;i++)
            next_active[i] = ( active[i] && (partition[index][i] == 1) ? true : false);
        Node<DendroBracket> right = treeInitialization(INDEX1, next_active);
        if(right != null) node.addChild(right);

        
        //Left node
        for(int i=0;i<active.length;i++)
            next_active[i] = ( active[i] && (partition[index][i] == -1) ? true : false);
        Node<DendroBracket> left = treeInitialization(INDEX1, next_active);
        if(left != null) node.addChild(left);
        
        
        
        return node;
    }
    
    private double getTreeHeight(Node<DendroBracket> node){
        if(node.getNumberOfChildren() == 0) return node.getData().t_variance;
        List<Node<DendroBracket>> children = node.getChildren();
        if(node.getNumberOfChildren() == 1){
            Node<DendroBracket> left = children.get(0);
            return node.getData().t_variance + getTreeHeight(left);
        }else{
            Node<DendroBracket> left = children.get(0);
            Node<DendroBracket> right = children.get(1);
            return node.getData().t_variance + Math.max(
                    getTreeHeight(left),
                    getTreeHeight(right));
        }
    }
    
    private Point2D setCoordinates(Node<DendroBracket> node, double h0){
        DendroBracket data = node.getData();
        int nchildren = node.getNumberOfChildren();        

        double v_x0 = v_x[0] * (displayWidth/displayHeight);
        double h1 = h0 - 2 * v_y[1] * (data.t_variance/totalHeight);
        double xt = 2 * v_x0  / (names.length-1.0);
        
        List<Node<DendroBracket>> children = node.getChildren();

        Point2D pL, pR;
        if(nchildren == 0){
            double pos = names.length-(++INDEX1);
            double px = origin[0] - v_x0 + xt * pos;
            data.right = pR = new Point2D.Double(px, h1);
            pos = names.length-(++INDEX1);
            px = origin[0] - v_x0 + xt * pos;
            data.left = pL = new Point2D.Double(px, h1);
            
        }else if(nchildren == 1){
            int s = 0;
            for(int v : partition[node.getData().balance]) s += v;
            if(s > 0){
                Node<DendroBracket> right = children.get(0);
                pR = setCoordinates(right, h1);
                data.right = right.getData().top = pR;

                double pos = names.length-(++INDEX1);
                double px = origin[0] - v_x0 + xt * pos;
                data.left = pL = new Point2D.Double(px, h1);
            }else{
                double pos = names.length-(++INDEX1);
                double px = origin[0] - v_x0 + xt * pos;
                data.right = pR = new Point2D.Double(px, h1);

                Node<DendroBracket> left = children.get(0);
                pL = setCoordinates(left, h1);
                data.left = left.getData().top = pL;
            }            
        }else{ // nchildre == 2
            Node<DendroBracket> right = children.get(0);
            pR = setCoordinates(right, h1);
            data.right = right.getData().top = pR;

            Node<DendroBracket> left = children.get(1);
            pL = setCoordinates(left, h1);
            data.left = left.getData().top = pL;
        }
        double join = getRelative(data, data.t_mean);
        return new Point2D.Double(join, h0);
    }

    public void printBalancesStatistics(){
        List<Node<DendroBracket>> list = T.toList();
        for(Node<DendroBracket> node : list){
            DendroBracket stats = node.getData();
            String out = "";
            out += stats.t_mean + "\t";
            out += stats.t_variance + "\t";
            /*
            out += stats.t_minimum + "\t";
            out += stats.t_first_quartile + "\t";
            out += stats.t_median + "\t";
            out += stats.t_third_quartile + "\t";
            out += stats.t_maximum + "\t";
*/
            System.out.println(out);
        }
    }
    @Override
    public void transformData() {
        INDEX1 = 0;
        Node<DendroBracket> root = T.getRootElement();
        root.getData().top = setCoordinates(root, origin[1] + (1+moveUp) * v_y[1]);
    }
    @Override
    public void paintComponent(Graphics2D g2, double width, double height){
        super.paintComponent(g2, width, height);

        factor = 0.40 * (height < width ? height : width);
        defaultTransform = new AffineTransform(
                factor, 0 ,
                0, -factor,
                width/2, height/2);               
        this.auxToSetBalanceNames = 0;
        drawBracket(T.getRootElement(), g2);
        INDEX1 = 0; INDEX2 = 0; drawLabels(T.getRootElement(), g2);
    }

    private double getRelative(DendroBracket data, double value){        
        double left_coord = data.left.getX();
        double right_coord = data.right.getX();

        return left_coord + (right_coord - left_coord) / (RIGHT - LEFT) *
                ( value - LEFT  );
    }

    private void drawLimit(Graphics2D g2, double y, double x, Color color, double size){
        double height_2 = size * v_y[1];

        g2.setPaint( color );
        Point2D o = null;
        Point2D u = new Point2D.Double(x, y+height_2);
        Point2D d = new Point2D.Double(x, y-height_2);

        g2.draw(PlotUtils.drawLine(
                defaultTransform.transform(u, o),
                defaultTransform.transform(d, o)));
    }

    private void drawBox(Graphics2D g2, double y, double x1, double x2, Color color, double size){
        double height_2 = size * v_y[1];
        Point2D o1 = null, o2 =null;
        Point2D ul = new Point2D.Double(x1, y+height_2);
        Point2D ur = new Point2D.Double(x2, y+height_2);
        Point2D dl = new Point2D.Double(x1, y-height_2);
        Point2D dr = new Point2D.Double(x2, y-height_2);
        Rectangle2D box = new Rectangle2D.Double(
                defaultTransform.transform(ul, o1).getX(),
                defaultTransform.transform(ul, o1).getY(),
                defaultTransform.transform(ur, o1).getX() - defaultTransform.transform(ul, o1).getX(),
                defaultTransform.transform(dr, o1).getY() - defaultTransform.transform(ur, o1).getY());
        g2.setPaint( Color.WHITE );
        g2.fill(box);
        g2.setPaint( color ); //CoDaDisplayConfiguration.getColor("area")
        g2.fill(box);
        g2.setColor( config.getColor("axis") );
        g2.draw(box);
    }

    private void drawBracket(Node<DendroBracket> node, Graphics2D g2){

        double param_size = 0.018;
        DendroBracket data = node.getData();
        float s = 1.5f;
        g2.setColor( config.getColor("axis") );
        g2.setStroke(normal);
        Point2D top = null;
        top = defaultTransform.transform(data.top, top);
        //g2.fill(PlotUtils.drawPoint(top, s));

        Point2D bottom = null;
        bottom = defaultTransform.transform(new Point2D.Double(data.top.getX(), data.left.getY()), bottom);
        g2.drawString("bal " + (order[this.auxToSetBalanceNames]+1),(int)bottom.getX()+5,(int)bottom.getY()-5);
        g2.draw(PlotUtils.drawLine(top,  bottom));

        this.auxToSetBalanceNames++;

        Point2D l = null;
        l = defaultTransform.transform(data.left, l);
        //g2.fill(PlotUtils.drawPoint(l, s));
        Point2D r = null;
        r = defaultTransform.transform(data.right, r);
        //g2.fill(PlotUtils.drawPoint(r, s));

        g2.draw(PlotUtils.drawLine(l,  r));

        if(node.getNumberOfChildren() == 2){
            drawBracket(node.getChildren().get(0),g2);
            drawBracket(node.getChildren().get(1),g2);
        }else if(node.getNumberOfChildren() == 1){
            drawBracket(node.getChildren().get(0),g2);
            g2.setStroke(dashed);
            g2.setColor( config.getColor("axis") );
            if(node.getChildren().get(0).data.top == data.left){
                top = defaultTransform.transform(data.right, top);
                bottom = defaultTransform.transform(new Point2D.Double(data.right.getX(), origin[1] - v_y[1] + moveUp * v_y[1]), bottom);
                g2.draw(PlotUtils.drawLine(top,  bottom));
            }else{
                top = defaultTransform.transform(data.left, top);
                bottom = defaultTransform.transform(new Point2D.Double(data.left.getX(), origin[1] - v_y[1] + moveUp * v_y[1]), bottom);
                g2.draw(PlotUtils.drawLine(top,  bottom));
            }
            g2.setStroke(normal);
        }else{
            g2.setStroke(dashed);
            g2.setColor( config.getColor("axis") );
            top = defaultTransform.transform(data.left, top);
            bottom = defaultTransform.transform(new Point2D.Double(data.left.getX(), origin[1] - v_y[1] + moveUp * v_y[1]), bottom);
            g2.draw(PlotUtils.drawLine(top,  bottom));

            top = defaultTransform.transform(data.right, top);
            bottom = defaultTransform.transform(new Point2D.Double(data.right.getX(), origin[1] - v_y[1] + moveUp * v_y[1]), bottom);
            g2.draw(PlotUtils.drawLine(top,  bottom));
            g2.setStroke(normal);
        }

        for(int gr=0;gr < ngroups;gr++){
            double height = 2*v_y[1]*(data.variance[gr]/totalHeight);
            double mean = getRelative(data, data.mean[gr]);


            top = defaultTransform.transform( new Point2D.Double(mean, data.left.getY()+height), top );
            bottom = defaultTransform.transform(new Point2D.Double(mean, data.left.getY()), bottom);
            g2.setColor(config.getColor("data", gr));
            g2.draw(PlotUtils.drawLine(top,  bottom));

            double y = data.left.getY() - (2 * ngroups - 1)* param_size * v_y[1];//- param_size * v_y[1] * (ngroups-1);
            double position = y + (ngroups-gr-1) * 2 * param_size * v_y[1];
            g2.setColor(config.getColor("axis"));
            l = defaultTransform.transform(
                    new Point2D.Double(
                    getRelative(data, data.fifth_percentile[gr]),
                    position), l);
            r = defaultTransform.transform(
                    new Point2D.Double(
                    getRelative(data, data.ninetyfifth_percentile[gr]),
                    position), r);
            g2.draw(PlotUtils.drawLine(l,  r));
            drawLimit(g2,
                    position,
                    //data.left.getY(),
                    getRelative(data, data.fifth_percentile[gr]),
                    config.getColor("axis"), param_size); //CoDaDisplayConfiguration.getColor("data" + gr)
            drawLimit(g2,
                    position,
                    //data.left.getY(),
                    getRelative(data, data.ninetyfifth_percentile[gr]),
                    config.getColor("axis"), param_size); //CoDaDisplayConfiguration.getColor("data" + gr)
            drawBox(g2,
                    position,
                    //data.left.getY(),
                    getRelative(data, data.first_quartile[gr]),
                    getRelative(data, data.third_quartile[gr]),
                    config.getColor("data", gr), param_size);
            drawLimit(g2,
                    position,
                    //data.left.getY(),
                    getRelative(data, data.median[gr]),
                    config.getColor("axis"), param_size);
        }
        
    }
    private void drawLabels(Node<DendroBracket> node, Graphics2D g2){
        int act = INDEX1++;
        int nchildren = node.getNumberOfChildren();
        int[] aPart = partition[act];
        int separation = 40;
        double coordX;
        Point2D o = null;
        Font font = new Font("Monospace", Font.PLAIN, 
                config.getSize("label").intValue());
        g2.setFont(font);
        FontMetrics metric = g2.getFontMetrics();
        double xt = 2 * v_x[0] * (displayWidth/displayHeight) / (names.length-1.0);
        g2.setColor(config.getColor("label"));
        List<Node<DendroBracket>> children = node.getChildren();
        if(nchildren == 0){
            int l = 0, r = 0;
            for(int i=0;i<aPart.length; i++)
                if(aPart[i] != 0)
                    if(aPart[i] == -1) l = i;
                    else r = i;

            double pos = names.length - (++INDEX2);
            coordX = origin[0] - v_x[0] * (displayWidth/displayHeight) + xt * pos;
            o = defaultTransform.transform(new Point2D.Double(coordX, origin[1] - v_y[1] + moveUp * v_y[1]), o);
            g2.drawString(textLabel[r],
                    (int)o.getX() - metric.stringWidth(textLabel[r])/2,
                    (int)o.getY() + separation );
            /*iconLabel[l].paintIcon(null, g2,
                    (int)o.getX() - iconLabel[l].getIconWidth()/2,
                    (int)o.getY() - iconLabel[l].getIconHeight() + separation);*/

            pos = names.length - (++INDEX2);
            coordX = origin[0] - v_x[0] * (displayWidth/displayHeight) + xt * pos;
            o = defaultTransform.transform(new Point2D.Double(coordX, origin[1] - v_y[1] + moveUp * v_y[1]), o);
            g2.drawString(textLabel[l],
                    (int)o.getX() - metric.stringWidth(textLabel[l])/2,
                    (int)o.getY() + separation );

            //System.out.println(names[l]);
            //System.out.println(names[r]);
        }else if(nchildren == 1){
            int s = 0;
            for(int v : partition[node.getData().balance]) s += v;
            if(s > 0){
                Node<DendroBracket> right = children.get(0);
                drawLabels(right, g2);

                int l = 0;
                for(int i=0;i<aPart.length; i++)
                    if(aPart[i] == -1) l = i;
                double pos = names.length - (++INDEX2);
                coordX = origin[0] - v_x[0] * (displayWidth/displayHeight) + xt * pos;
                o = defaultTransform.transform(new Point2D.Double(coordX, origin[1] - v_y[1] + moveUp * v_y[1]), o);
                g2.drawString(textLabel[l],
                    (int)o.getX() - metric.stringWidth(textLabel[l])/2,
                    (int)o.getY() + separation );
                //System.out.println(names[r]);
            }else{
                int r = 0;
                for(int i=0;i<aPart.length; i++)
                    if(aPart[i] == 1) r = i;
                double pos = names.length - (++INDEX2);
                coordX = origin[0] - v_x[0] * (displayWidth/displayHeight) + xt * pos;
                o = defaultTransform.transform(new Point2D.Double(coordX, origin[1] - v_y[1] + moveUp * v_y[1]), o);
                g2.drawString(textLabel[r],
                    (int)o.getX() - metric.stringWidth(textLabel[r])/2,
                    (int)o.getY() + separation );
                //System.out.println(names[l]);

                Node<DendroBracket> left = children.get(0);
                drawLabels(left, g2);
            }
        }else{ // nchildre == 2
            Node<DendroBracket> right = children.get(0);
            drawLabels(right, g2);

            Node<DendroBracket> left = children.get(1);
            drawLabels(left, g2);
        }
    }
    public static class DendrogramBuilder{
        private String names[];
        private double x[][];
        private int partition[][];
        private int groups[] = null;
        private String gnames[] = new String[0];
        private int mapping[] = null;

        public DendrogramBuilder(String names[], double x[][], int partition[][]){
            this.names = names;
            this.x = x;
            this.partition = partition;
            mapping = new int[x[0].length];
            groups = new int[x[0].length];
            for(int i=0; i<x[0].length;i++){
                mapping[i] = i;
                groups[i] = 0;
            }
        }
        public DendrogramBuilder groups(int groups[], String gnames[]){
            this.groups = groups;
            this.gnames = gnames;
            return this;
        }
        public DendrogramBuilder mapping(int mapping[]){
            this.mapping = mapping;
            return this;
        }
        public DendrogramDisplay build(){
            return new DendrogramDisplay(this);
        }
    }

    private class DendroBracket{
        public int balance;
        public double t_mean;
        public double t_variance;
        /*
        public double t_minimum;
        public double t_fifth_percentile;
        public double t_first_quartile;
        public double t_median;
        public double t_third_quartile;
        public double t_ninetyfifth_percentile;
        public double t_maximum;*/
        
        public double mean[];
        public double variance[];
        public double minimum[];
        public double fifth_percentile[];
        public double first_quartile[];
        public double median[];
        public double third_quartile[];
        public double ninetyfifth_percentile[];
        public double maximum[];

        public Point2D top = null;
        public Point2D left = null;
        public Point2D right = null;

        public DendroBracket(int ngroups){
            mean = new double[ngroups];
            variance = new double[ngroups];
            minimum = new double[ngroups];
            fifth_percentile = new double[ngroups];
            first_quartile = new double[ngroups];
            median = new double[ngroups];
            third_quartile = new double[ngroups];
            ninetyfifth_percentile = new double[ngroups];
            maximum = new double[ngroups];
        }
    }
}