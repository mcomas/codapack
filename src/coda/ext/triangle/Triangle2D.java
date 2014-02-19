package coda.ext.triangle;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.NoSuchElementException;

/**
 * A 2D triangle which is also a shape.
 *
 * @author <a href="mailto:Martin.vGagern@gmx.net">Martin von Gagern</a>
 * @since 1.0
 */
public class Triangle2D extends SimpleTriangle<Vertex2D> implements Shape {

    /**
     * Construct triangle from three vertices.
     * @param p1 first vertex
     * @param p2 second vertex
     * @param p3 third vertex
     */
    public Triangle2D(Vertex2D p1, Vertex2D p2, Vertex2D p3) {
        super(p1, p2, p3);
    }

    /**
     * Construct triangle from three corner points. The points will be
     * used to construct <code>Vertex2D</code> objects. After that,
     * changes to the points won't affect the triangle object.
     * @param p1 first corner point
     * @param p2 second corner point
     * @param p3 third corner point
     */
    public Triangle2D(Point2D p1, Point2D p2, Point2D p3) {
        super(new Vertex2D(p1), new Vertex2D(p2), new Vertex2D(p3));
    }

    /**
     * Construct triangle from corner coordinates.
     * @param x1 first corner x coordinate
     * @param y1 first corner y coordinate
     * @param x2 second corner x coordinate
     * @param y2 second corner y coordinate
     * @param x3 third corner x coordinate
     * @param y3 third corner y coordinate
     */
    public Triangle2D(double x1, double y1, double x2, double y2,
                      double x3, double y3) {
        super(new Vertex2D(x1, y2), new Vertex2D(x2, y2), new Vertex2D(x3, y3));
    }

    /**
     * Calculate integer bounding box.
     * @return integer bounding box containing the triangle
     * @see #getBounds2D()
     */
    public Rectangle getBounds() {
        Rectangle2D r = getBounds2D();
        int left = (int)Math.floor(r.getMinX());
        int right = (int)Math.ceil(r.getMaxX());
        int top = (int)Math.floor(r.getMinY());
        int bottom = (int)Math.ceil(r.getMaxY());
        return new Rectangle(left, top, right - left, top - bottom);
    }

    /**
     * Calculate bounding box.
     * The bounding box will not contain all corner points, as a
     * rectangle does not contain points on its lower and right
     * boundary.
     * @return bounding box containing the triangle
     */
    public Rectangle2D getBounds2D() {
        Point2D p = getCorner(0);
        Rectangle2D r = new Rectangle2D.Double(p.getX(), p.getY(), 0, 0);
        r.add(getCorner(1));
        r.add(getCorner(2));
        return r;
    }

    /**
     * Determine whether a point given by its coordinates lies within
     * the triangle.
     * @param x the x coordinate of a point
     * @param y the y coordinate of a point
     * @return whether the triangle contains the point
     */
    public boolean contains(double x, double y) {
        for (int i = 0; i < 3; ++i) {
            Point2D p1 = getCorner(i);
            Point2D p2 = getCorner((i + 1)%3);
            Point2D p3 = getCorner((i + 2)%3);
            double x1 = p1.getX(), y1 = p1.getY();
            double x2 = p2.getX(), y2 = p2.getY();
            double x3 = p3.getX(), y3 = p3.getY();
            // not contained if p3 and (x, y) lie on different sides of
            // triangle edge p1--p2, which can be seen from determinants:
            if (det(x1, y1, x2, y2, x3, y3)*det(x1, y1, x2, y2, x, y) < 0)
                return false;
        }
        return true;
    }

    /**
     * Determine whether a given point lies within the triangle.
     * @param p a point to check for insideness
     * @return whether the triangle contains the point
     */
    public boolean contains(Point2D p) {
        return contains(p.getX(), p.getY());
    }

    /**
     * Determine whether the triangle completely contains a rectangle
     * given by its coordinates.
     * @param x the x coordinate of a rectangle
     * @param y the y coordinate of a rectangle
     * @param w width of a rectangle
     * @param h height of a rectangle
     * @return whether the triangle contains the rectangle
     */
    public boolean contains(double x, double y, double w, double h) {
        // a triangle contains a rectangle if it contains all its corners
        double[] xr = { x, x+w, x, x+w }, yr = { y, y+h, y+h, y };
        for (int i = 0; i < 3; ++i) {
            Point2D p1 = getCorner(i);
            Point2D p2 = getCorner((i + 1)%3);
            Point2D p3 = getCorner((i + 2)%3);
            double x1 = p1.getX(), y1 = p1.getY();
            double x2 = p2.getX(), y2 = p2.getY();
            double x3 = p3.getX(), y3 = p3.getY();
            // not contained if p3 and a rectangle corner point
            // lie on different sides of triangle edge p1--p2
            double tdet = det(x1, y1, x2, y2, x3, y3);
            for (int j = 0; j < 4; ++j)
                if (tdet*det(x1, y1, x2, y2, xr[j], yr[j]) < 0)
                    return false;
        }
        return true;
    }

    /**
     * Determine whether the triangle completely contains a given
     * rectangle.
     * @param r a rectangle to check for containment
     * @return whether the triangle contains the rectangle
     */
    public boolean contains(Rectangle2D r) {
        return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    /**
     * Determine whether the triangle intersects a rectangle given by
     * its coordinates.
     * @param x the x coordinate of a rectangle
     * @param y the y coordinate of a rectangle
     * @param w width of a rectangle
     * @param h height of a rectangle
     * @return whether the triangle intersects the rectangle
     */
    public boolean intersects(double x, double y, double w, double h) {
        // a cheap bounding box check first off, before we start the real work
        if (!getBounds2D().intersects(x, y, w, h)) return false;
        return intersectsImpl(new Rectangle2D.Double(x, y, w, h));
    }

    /**
     * Determine whether the triangle intersects a given rectangle.
     * @param r a rectangle to check for intersection
     * @return whether the triangle intersects the rectangle
     */
    public boolean intersects(Rectangle2D r)  {
        // a cheap bounding box check first off, before we start the real work
        if (!getBounds2D().intersects(r)) return false;
        return intersectsImpl(r);
    }

    /**
     * Internal implementation of intersection test. This encapsulates
     * the common code of both overloaded forms, while leaving out the
     * quick bounding box check to be performed on the input of each
     * version.
     * @param r a rectangle to check for intersection
     * @return whether the triangle intersects the rectangle
     */
    private boolean intersectsImpl(Rectangle2D r)  {
        // a triangle intersects a rectangle if
        // 1) the rectangle contains the triangle, i.e. all its corners
        // 2) the triangle contains the rectangle, i.e. all its corners
        // 3) a triangle edge intersects a rectangle edge

        rectContainsTriangle: do {
            for (int i = 0; i < 3; ++i)
                if (!r.contains(getCorner(i)))
                    break rectContainsTriangle;
            return true;
        } while (false);

        if (this.contains(r)) return true;

        for (int i = 0; i < 3; ++i) {
            Point2D p1 = getCorner(i), p2 = getCorner((i + 1)%3);
            if (r.intersectsLine(p1.getX(), p1.getY(), p2.getX(), p2.getY()))
                return true;
        }

        return false;
    }

    /**
     * Calculate a determinant. This is used to determine orientation
     * of triangles, which in turn is used for insideness checks.<p>
     * <pre>
     *                               | x1 x2 x3 |
     * det(x1, y1, x2, y2, x3, y3) = | y1 y2 y3 |
     *                               | 1  1  1  |
     * </pre>
     * @see #contains
     */
    private static double det(double x1, double y1,
                              double x2, double y2,
                              double x3, double y3) {
        return x1*y2 + x2*y3 + x3*y1 - x1*y3 - x2*y1 - x3*y2;
    }

    /**
     * Get a path iterator for this shape.
     * @param at a transformation to be applied to all coordinates
     * @return an iterator over the triangle outline
     */
    public PathIterator getPathIterator(AffineTransform at) {
        return new Iter(at);
    }

    /**
     * Get a flattening path iterator for this shape.
     * As triangles are flat already, this is equal to the
     * non-flattening version.
     * @param at a transformation to be applied to all coordinates
     * @return an iterator over the triangle outline
     * @see #getPathIterator(AffineTransform)
     */
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return new Iter(at);
    }

    /**
     * Path iterator implementation.
     * It will generate this segment sequence:
     * <ol>
     * <li>move to first corner</li>
     * <li>line to second corner</li>
     * <li>line to third corner</li>
     * <li>close path</li>
     * </ol>
     *
     * @author <a href="mailto:Martin.vGagern@gmx.net">Martin von Gagern</a>
     */
    private class Iter implements PathIterator {

        /**
         * Affine transform used to transform coordinates.
         */
        private AffineTransform at;

        /**
         * Index of current segment.
         */
        private int i = 0;

        /**
         * Constructs iterator.
         * @param at a transformation to be applied to all coordinates
         */
        public Iter(AffineTransform at) {
            this.at = at;
        }

        /**
         * Get winding rule. Irrelevant for triangles.
         * @return <code>WIND_NON_ZERO</code>
         * @see PathIterator#WIND_NON_ZERO
         */
        public int getWindingRule() {
            return WIND_NON_ZERO;
        }

        /**
         * Check whether traversal is complete.
         * @return whether the traversal is complete
         */
        public boolean isDone() {
            return i == 4;
        }

        /**
         * Move iterator to next segment.
         */
        public void next() {
            if (isDone())
                throw new NoSuchElementException("End of path reached");
            ++i;
        }

        /**
         * Retrieve current segment with double coordinates.
         * @param coords an array to receive the coordinates
         * @return the type of the current segment
         */
        public int currentSegment(double[] coords) {
            if (i == 3) return SEG_CLOSE;
            Point2D p = getCorner(i);
            coords[0] = p.getX();
            coords[1] = p.getY();
            if (at != null) at.transform(coords, 0, coords, 0, 1);
            if (i == 0) return SEG_MOVETO;
            else return SEG_LINETO;
        }

        /**
         * Retrieve current segment with float coordinates.
         * @param coords an array to receive the coordinates
         * @return the type of the current segment
         */
        public int currentSegment(float[] coords) {
            if (i == 3) return SEG_CLOSE;
            Point2D p = getCorner(i);
            coords[0] = (float)p.getX();
            coords[1] = (float)p.getY();
            if (at != null) at.transform(coords, 0, coords, 0, 1);
            if (i == 0) return SEG_MOVETO;
            else return SEG_LINETO;
        }

    }

}
