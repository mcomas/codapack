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


import coda.CoDaStats;
import coda.ext.jama.Matrix;
import coda.plotfx.TernaryPlot2dDisplayFX;
import coda.plot2.datafig.FilledCircle;
import coda.plot2.PlotUtils;

import coda.plotfx.objects.Ternary2dCurveObjectFX;
import coda.plotfx.objects.Ternary2dDataObjectFX;
import coda.plotfx.objects.Ternary2dGridObjectFX;
import coda.plotfx.objects.Ternary2dObjectFX;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author mcomas
 */
public class testPlotNew01 extends Application {
    
    
    public static void main(String args[]) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println(CoDaStats.ternaryTransform(1,0,0,0)[0]+","+CoDaStats.ternaryTransform(1,0,0,0)[1]+","+CoDaStats.ternaryTransform(1,0,0,0)[2]);
        System.out.println(CoDaStats.ternaryTransform(0,1,0,0)[0]+","+CoDaStats.ternaryTransform(0,1,0,0)[1]+","+CoDaStats.ternaryTransform(0,1,0,0)[2]);
        System.out.println(CoDaStats.ternaryTransform(0,0,1,0)[0]+","+CoDaStats.ternaryTransform(0,0,1,0)[1]+","+CoDaStats.ternaryTransform(0,0,1,0)[2]);
        System.out.println(CoDaStats.ternaryTransform(0,0,0,1)[0]+","+CoDaStats.ternaryTransform(0,0,0,1)[1]+","+CoDaStats.ternaryTransform(0,0,0,1)[2]);

        String names[] = {"X", "Y", "Z"};
        
        double dataset1[][] = new Matrix(data).transpose().getArray();
        //double dataset2[][] = new Matrix(data1).transpose().getArray();
        TernaryPlot2dDisplayFX display = new TernaryPlot2dDisplayFX(names);

        Ternary2dObjectFX gridObject = new Ternary2dGridObjectFX(display, definedGrid);
        display.addCoDaObject(gridObject);

        Ternary2dObjectFX dataObject = new Ternary2dDataObjectFX(display, data, new FilledCircle());
        dataObject.setColor(new java.awt.Color(70, 70, 200));
        display.addCoDaObject(dataObject);

        double a[] = {1,1,1};
        double v[] = {4,1.2,1};
        Ternary2dObjectFX lineObject1 = new Ternary2dCurveObjectFX(display,
                PlotUtils.line(a, v, 100));
        lineObject1.setColor(java.awt.Color.CYAN);
        display.addCoDaObject(lineObject1);

        double o2[] = {2,1};
        double a2[] = new double[2];
        for(int i=0;i<20;i++){
            a2[0] = 0.15 * i;
            a2[1] = 0.1 * i;
            Ternary2dObjectFX curveObject3 = new Ternary2dCurveObjectFX(display,
                    PlotUtils.ellipse(o2, a2, Math.PI, 200));
            curveObject3.setColor(new java.awt.Color(100, 70, 70));
            display.addCoDaObject(curveObject3);
        }
     
        GraphicsContext gc = display.getGraphicsContext2D();
        display.setWidth(600);
        display.setHeight(500);

        Pane root = new Pane();
        root.getChildren().add(display);
        
        Scene scene = new Scene(root);
        
        primaryStage.setTitle("Ternary plot");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    static double definedGrid[] =
        {0.01, 0.05, 0.10, 0.25, 0.5, 0.75, 0.9, 0.95, 0.99};
        //{0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};

    static double data[][] = {
            {31.7,3.8,6.4},
            {23.8,9,9.2},
            {9.100001,34.2,9.5},
            {23.8,7.2,10.1},
            {38.3,2.9,7.7},
            {26.2,4.2,12.5},
            {33,4.6,12.2},
            {5.2,42.9,9.600001},
            {11.7,26.7,9.600001},
            {46.6,0.7,5.6},
            {19.5,11.4,9.5},
            {37.3,2.7,5.5},
            {8.5,38.9,8},
            {12.9,23.4,15.8},
            {17.5,15.8,8.3},
            {7.3,40.9,12.9},
            {44.3,1,7.8},
            {32.3,3.1,8.7},
            {15.8,20.4,8.3},
            {11.5,23.8,11.6},
            {16.6,16.8,12},
            {25,6.8,10.9},
            {34,2.5,9.399999},
            {16.6,17.6,9.600001},
            {24.9,9.7,9.8}};   
}