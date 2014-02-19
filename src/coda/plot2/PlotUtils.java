/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.plot2;

import coda.BasicStats;
import coda.CoDaStats;
import coda.ext.jama.Matrix;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 *
 * @author mcomas
 */
public class PlotUtils {
    public static Shape drawAxis(double[] o, double[] v){

        return new Line2D.Double(
                o[0] -  v[0], o[1] -  v[1],
                o[0] +  v[0], o[1] +  v[1]);
    }
    public static Shape drawAxis(Point2D o, double[] v){

        return new Line2D.Double(
                o.getX() -  100*v[0], o.getY() -  100*v[1],
                o.getX() +  100*v[0], o.getY() +  100*v[1]);
    }
    public static Shape drawAxis(Point2D o, double length, Point2D v){

        return new Line2D.Double(
                o.getX() - length * v.getX(), o.getY() - length * v.getY(),
                o.getX() + length * v.getX(), o.getY() + length * v.getY());
    }
    public static double[] v_rot(double[] v, double cosA, double sinA,
        double cosB, double sinB){
        double tx, ty, tz;

        tx = cosB* v[0]
                + sinA * sinB * v[1]
                + cosA* sinB * v[2];
        ty = cosA * v[1]
                - sinA * v[2];
        tz = - sinB* v[0]
                + sinA * cosB * v[1]
                + cosA * cosB * v[2];
        v[0] = tx;
        v[1] = ty;
        v[2] = tz;

        return v;
    }
    public static double[] p_rot(double[] p, double[] origin, double cosA, double sinA,
            double cosB, double sinB){
        double x = p[0] - origin[0];
        double y = p[1] - origin[1];
        double z = p[2] - origin[2];

        double tx = cosB* x
                + sinA * sinB * y
                + cosA* sinB * z;
        double ty = cosA * y  - sinA * z;

        double tz = - sinB* x
                + sinA * cosB * y
                + cosA * cosB * z;

        p[0] = tx + origin[0];
        p[1] = ty + origin[1];
        p[2] = tz + origin[2];

        return p;

    }
    public static double[][] segment(double x[], double y[], int size){
        int D = x.length;
        double r[][] = new double[size][D];
        double t = 0;
        for(int i=0;i<size;i++, t += 1.0/((double)size-1)){
            for(int j=0;j<D;j++){
                r[i][j] = Math.pow(x[j], 1-t) * Math.pow(y[j], t);
            }            
        }
        return r;
    }
    public static double[][] line(double p[], double v[], int size){
        int D = p.length;
        double x[] = new double[D];
        double y[] = new double[D];
        for(int k =0;k<D;k++){
            x[k] = p[k] * Math.pow(v[k], -10);
            y[k] = p[k] * Math.pow(v[k], 10);
        }
        
        double r[][] = new double[size][D];
        double t = 0;
        for(int i=0;i<size;i++, t += 1.0/((double)size)){
            for(int j=0;j<D;j++){
                r[i][j] = Math.pow(x[j], t) * Math.pow(y[j], 1-t);
            }
        }
        return r;


    }
    public static double[][] ellipse(double xc[], double a[], double phi, int size){
        double cosphi = Math.cos(phi);
        double sinphi = Math.sin(phi);
        double r[][] = new double[size][];
        double t = 0;
        double x[] = new double[2];
        for(int i=0;i<size-1;i++,t += (2*Math.PI)/((double)size-1)){
            x[0] = xc[0] + a[0] * Math.cos(t) * cosphi - a[1] * Math.sin(t) * sinphi;
            x[1] = xc[1] + a[0] * Math.cos(t) * sinphi + a[1] * Math.sin(t) * cosphi;
            r[i] = CoDaStats.transformILR2Raw3(x[0],x[1]);
        }
        x[0] = xc[0] + a[0] * Math.cos(0) * cosphi - a[1] * Math.sin(0) * sinphi;
        x[1] = xc[1] + a[0] * Math.cos(0) * sinphi + a[1] * Math.sin(0) * cosphi;
        r[size-1] = CoDaStats.transformILR2Raw3(x[0],x[1]);
        return r;
    }
    public static double[][][] plane(double x[], double y[], double z[], int size){
        int D = x.length;
        int mult = 100;
        double plane[][][] = new double[mult*size][mult*size][D];
        double r = 0;
        for(int i=0;i<mult*size;i++, r += 1.0/((double)size -1)){
            double s = 0;
            for(int j=0;j<mult*size;j++, s += 1.0/((double)size -1)){
                for(int k=0;k<D;k++){
                    plane[i][j][k] =
                            Math.pow(x[k], (1-r) * (1-s)) *
                            Math.pow(y[k], r) *
                            Math.pow(z[k], s);
                }
            }
        }
        return plane;
    }
    public static double[][] confidenceRegion2d(double data[][], double confidence){

        double center[] = CoDaStats.center(data);

        double cdata[][] = CoDaStats.centerData(data);
        double pcomp[][] = CoDaStats.principalComponents(cdata);

        Matrix CLR = new Matrix(CoDaStats.transformRawCLR(cdata)).transpose();
        Matrix ILR = CLR.times(new Matrix(pcomp).transpose());

        double [][]cov = BasicStats.covariance(ILR.transpose().getArray());

        int D = pcomp[0].length;
        int N = data[0].length;
        double q = CoDaStats.confidenceRegion(D, N, confidence);

        double axis[] = new double[2];
        axis[0] = Math.sqrt(q * cov[0][0]);
        axis[1] = Math.sqrt(q * cov[1][1]);

        double curve[][] = PlotUtils.ellipse(axis, pcomp, 100);

        for(int i=0;i<curve.length;i++)
            curve[i] = CoDaStats.perturbation(curve[i], center);

        return curve;
    }
    public static double[][] predictiveRegion2d(double data[][], double confidence){

        double center[] = CoDaStats.center(data);

        double cdata[][] = CoDaStats.centerData(data);
        double pcomp[][] = CoDaStats.principalComponents(cdata);

        Matrix CLR = new Matrix(CoDaStats.transformRawCLR(cdata)).transpose();
        Matrix ILR = CLR.times(new Matrix(pcomp).transpose());

        double [][]cov = BasicStats.covariance(ILR.transpose().getArray());

        int D = pcomp[0].length;
        int N = data[0].length;
        double q = CoDaStats.predictiveRegion(D, N, confidence);

        double axis[] = new double[2];
        axis[0] = Math.sqrt(q * cov[0][0]);
        axis[1] = Math.sqrt(q * cov[1][1]);

        double curve[][] = PlotUtils.ellipse(axis, pcomp, 100);

        for(int i=0;i<curve.length;i++)
            curve[i] = CoDaStats.perturbation(curve[i], center);

        return curve;
    }
    public static double[][][] predictiveRegion3d(double data[][], double confidence){

        double center[] = CoDaStats.center(data);

        double cdata[][] = CoDaStats.centerData(data);
        double pcomp[][] = CoDaStats.principalComponents(cdata);

        Matrix CLR = new Matrix(CoDaStats.transformRawCLR(cdata)).transpose();
        Matrix ILR = CLR.times(new Matrix(pcomp).transpose());

        double [][]cov = BasicStats.covariance(ILR.transpose().getArray());

        int D = pcomp[0].length;
        int N = data[0].length;
        double q = CoDaStats.predictiveRegion(D, N, confidence);

        double axis[] = new double[3];
        axis[0] = Math.sqrt(q * cov[0][0]);
        axis[1] = Math.sqrt(q * cov[1][1]);
        axis[2] = Math.sqrt(q * cov[2][2]);

        double surface[][][] = PlotUtils.ellipsoid(axis, pcomp, 60);

        for(int i=0;i<surface.length;i++)
            for(int j=0;j<surface[0].length;j++)
                surface[i][j] = CoDaStats.perturbation(surface[i][j], center);

        return surface;
    }
    public static double[][] ellipse(double a[], double ilrBase[][], int size){
        double ellipse[][] = new double[size][];
        double t = 0;
        double x[] = new double[2];
        for(int i=0;i<size;i++,t += (2*Math.PI)/((double)size-1)){
                x[0] = a[0] * Math.cos(t);
                x[1] = a[1] * Math.sin(t);
                // Product matrix needed
                ellipse[i] = CoDaStats.transformILRRaw(x, ilrBase);
        }
        return ellipse;
    }
    public static double[][][] ellipsoid(double a[], double ilrBase[][], int size){
        double ellipsoid[][][] = new double[size][size][];
        double t = -0.5 * Math.PI;
        double x[] = new double[3];
        for(int i=0;i<size;i++,t += (Math.PI)/((double)size-1)){
            double s = -Math.PI;
            for(int j=0;j<size;j++,s += (2*Math.PI)/((double)size-1)){
                x[0] = a[0] * Math.cos(t) * Math.cos(s);
                x[1] = a[1] * Math.cos(t) * Math.sin(s);
                x[2] = a[2] * Math.sin(t);
                // Product matrix needed
                ellipsoid[i][j] = CoDaStats.transformILRRaw(x, ilrBase);
            }
        }
        return ellipsoid;
    }
    public static double[][][] ellipsoid(double a[], int size){

        double ellipsoid[][][] = new double[size][size][];
        double t = -0.5 * Math.PI;
        double x[] = new double[3];
        for(int i=0;i<size;i++,t += (Math.PI)/((double)size-1)){
            double s = -Math.PI;
            for(int j=0;j<size;j++,s += (2*Math.PI)/((double)size-1)){
                x[0] = a[0] * Math.cos(t) * Math.cos(s);
                x[1] = a[1] * Math.cos(t) * Math.sin(s);
                x[2] = a[2] * Math.sin(t);
                ellipsoid[i][j] = CoDaStats.transformILR3Raw4(x[0],x[1], x[2]);
            }
        }
        return ellipsoid;
    }
    public static Shape drawLine(double[] v, double[] w){
        return new Line2D.Double(v[0], v[1], w[0], w[1]);
    }
    public static Shape drawLine(Point2D v, Point2D w){
        return new Line2D.Double(v.getX(), v.getY(), w.getX(), w.getY());
    }
    public static Shape drawPoint(double[] p, double r){
        return new Ellipse2D.Double(p[0] - r, p[1] - r, 2*r, 2*r);
    }
    public static Shape drawPoint(Point2D p, double r){
        return new Ellipse2D.Double(p.getX() - r, p.getY() - r, 2*r, 2*r);
    }
}
