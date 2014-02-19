/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.plot;

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
