package coda.ext.triangle;

import java.util.AbstractList;

/**
 * Simple implementation for triangles of a mesh.<p>
 *
 * An arbitrary type may be used to identify indices, and the corners
 * can be identified through their index. For coordinates of the
 * corner, the corresponding {@link LocatedMesh} should be queried.<p>
 *
 * Beside access to the corners as required by
 * <code>LocatedMesh</code>, this implementation also provides access
 * to the corners in form of a <code>List</code>.
 *
 * @param <V> the class used to represent triangle vertices
 *
 * @author <a href="mailto:Martin.vGagern@gmx.net">Martin von Gagern</a>
 * @since 1.0
 */
public class SimpleTriangle<V>
    extends AbstractList<V> implements CorneredTriangle<V> {

    private V c1, c2, c3;

    public SimpleTriangle(V c1, V c2, V c3) {
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
    }

    /**
     * Get specified corner.<p>
     *
     * @param index the index of the corner requested
     * @return an object identifying the requested corner
     * @throws IndexOutOfBoundsException unless 0 &lt;= index &lt; 3
     */
    public V getCorner(int index) {
        switch (index) {
        case 0: return c1;
        case 1: return c2;
        case 2: return c3;
        default: throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Get specified corner.
     * This is an alias to {@link #getCorner(int)} in order to
     * implement the <code>List</code> interface.
     *
     * @param index the index of the corner requested
     * @return an object identifying the requested corner
     * @throws IndexOutOfBoundsException unless 0 &lt;= index &lt; 3
     */
    public V get(int index) {
        return getCorner(index);
    }

    /**
     * Count corners.
     * This method will always return 3.
     *
     * @return three
     */
    public int size() {
        return 3;
    }

    /**
     * Calculate hash code of triangle.
     * This hash code is calculated in such a way that it is
     * compatible with the {@link #equals} method. It is based on the
     * hash codes of the three corner vertices.
     *
     * @return a hash code for the triangle
     */
    @Override public int hashCode() {
        return c1.hashCode() ^ c2.hashCode() ^ c3.hashCode();
    }

    /**
     * Compare two simple triangles for equality.
     * Triangles are deemed equal if all their vertices are equal, and
     * if they use the same orientation. They don't have to use the
     * same starting vertex as first vertex. In other words, only the
     * cyclic order of vertices is relevant. Only instances of
     * <code>SimpleTriangle</code> will be considered equal to a
     * <code>SimpleTriangle</code> in order to not break the symmetry
     * of equals.
     *
     * @param o another object
     * @return whether the other object is a SimpleTriangle with
     *         the same vertices and orientation
     */
    @Override public boolean equals(Object o) {
        return o instanceof SimpleTriangle && equal(this, (SimpleTriangle)o);
    }

    /**
     * Equality comparison for cornered triangles.
     * Triangles are deemed equal if all their vertices are equal,
     * and if they use the same orientation. They don't have to use
     * the same starting vertex as first vertex. In other words, only
     * the cyclic order of vertices is relevant. The classes of the
     * triangles are not compared by this implementation.
     * <code>null</code> is equal only to <code>null</code>.<p>
     *
     * This static method can be used by implementations of
     * <code>CorneredTriangle</code> that wish to implement the same
     * equality semantics. It can also be used by applications that
     * wish to use this semantics regardless of the actual triangle
     * implementation in use.
     *
     * @param t1 one triangle
     * @param t2 a second triangle
     * @return whether the two triangles have
     *         the same vertices and orientation
     * @see Object#equals
     */
    public static boolean
    equal(CorneredTriangle<?> t1, CorneredTriangle<?> t2) {
        if (t1 == null || t2 == null) return t1 == null && t2 == null;
        ROTATIONS: for (int rotation = 0; rotation < 3; ++rotation) {
            for (int corner = 0; corner < 3; ++corner) {
                if (!t1.getCorner((corner + rotation)%3)
                    .equals(t2.getCorner(corner))) {
                    continue ROTATIONS;
                }
            }
            return true; // rotation matches completely
        }
        return false; // no rotation matches
    }

}
