package coda.ext.triangle;

import java.awt.geom.Point2D;

/**
 * A 2D point with equality comparison.<p>
 *
 * To easily construct meshes, it is useful to identify common
 * vertices. This implementation considers points equal if they
 * exactly agree in their coordinates. This contrasts with the
 * implementation in the super class, which inherits the equality
 * comparison from <code>Object</code>.
 *
 * @author <a href="mailto:Martin.vGagern@gmx.net">Martin von Gagern</a>
 * @since 1.0
 */
public class Vertex2D extends Point2D.Double {

    /**
     * Construct vertex from given point.
     *
     * @param p the point used to initialize the coordinates
     */
    public Vertex2D(Point2D p) {
        super(p.getX(), p.getY());
    }

    /**
     * Construct vertex from given coordinates.
     *
     * @param x the initial x coordinate
     * @param y the initial y coordinate
     */
    public Vertex2D(double x, double y) {
        super(x, y);
    }

    /**
     * Calculate hash code from coordinates.
     *
     * @return a hash code derived from the coordinates
     */
    @Override public int hashCode() {
        long xl = java.lang.Double.doubleToLongBits(x);
        long yl = java.lang.Double.doubleToLongBits(y);
        int xi = (int)xl ^ (int)(xl >>> 32);
        int yi = (int)(yl << 16) ^ (int)(yl >>> 16) ^ (int)(yl >>> 48);
        return xi ^ yi;
    }

    /**
     * Compare vertex to other point.
     * The comparison is based on coordinates, not object
     * identity. The classes aren't compared either. Any
     * <code>Point2D</code> with the same coordinates will compare
     * equal. <b>This is a violation of symmetry!</b> While it may be
     * handy to compare vertices with other points where you know what
     * you are doing, mixing them in any container that compares its
     * elements is a bad idea, as it can lead to inconsistent
     * behaviour.
     *
     * @param o another object
     * @return whether o is a <code>Point2D</code> with the same
     *         coordinates as this vertex
     * @see Point2D
     */
    @Override public boolean equals(Object o) {
        if (!(o instanceof Point2D)) return false;
        if (o == this) return true;
        Point2D p = (Point2D)o;
        return x == p.getX() && y == p.getY();
    }

}
