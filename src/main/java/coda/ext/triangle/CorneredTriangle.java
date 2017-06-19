package coda.ext.triangle;

/**
 * Interface for triangles of a mesh.<p>
 *
 * An arbitrary type may be used to identify indices, and the corners
 * can be identified through their index. For coordinates of the
 * corner, the corresponding {@link LocatedMesh} should be queried.<p>
 *
 * @param <V> the class used to represent triangle vertices
 *
 * @author <a href="mailto:Martin.vGagern@gmx.net">Martin von Gagern</a>
 * @since 1.0
 */
public interface CorneredTriangle<V> {

    /**
     * Get specified corner.<p>
     *
     * @param index the index of the corner requested
     * @return an object identifying the requested corner
     * @throws IndexOutOfBoundsException unless 0 &lt;= index &lt; 3
     */
    public V getCorner(int index);

}
