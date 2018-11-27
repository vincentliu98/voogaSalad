package grids;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Abstract representation of a position in the grid.
 * Can be used as keys in a hashmap
 * @author jz192
 */

public class PointImpl implements Point {
    private final int x;
    private final int y;
    public static final PointImpl ZERO = new PointImpl(0, 0);





    /**
     * Instantiate a point given x and y coordinates.
     * @param x is the x-coord
     * @param y is the y-coord
     */
    public PointImpl(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }
    @Override
    public Point add(int x, int y) {
        return new PointImpl(this.x + x, this.y + y);
    }

    @Override
    public Point add(Point p) {
        return new PointImpl(this.x + p.getX(), this.y + p.getY());
    }

    @Override
    public boolean equals(Object p) {
        if (p == null || !(p instanceof Point)) {
            return false;
        }
        Point position = (Point) p;
        return this.x == position.getX() && this.y == position.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Point " + "(" + x + ", " + y + ")";
    }
}
