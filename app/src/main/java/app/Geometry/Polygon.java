package app.src.main.geometry;

import java.util.List;
import app.src.main.geometry.Point;

/**
 * Polygon is a representation of any kind of polygon
 * I doesn't use java.awt.Polygon because it only allows points coordinates as int
 */
public class Polygon
{
    private List<Point> points;

    /**
     * @param List<Point> points
     */
    public Polygon(List<Point> points)
    {
        this.points = points;
    }

    /**
     * @param Point position - current position to know if it is inside of the polygon
     *
     * @return boolean - true if this polygon contains the position, false otherwise
     *
     * @see https://wrf.ecse.rpi.edu//Research/Short_Notes/pnpoly.html
     * (suggested by https://stackoverflow.com/questions/8721406/how-to-determine-if-a-point-is-inside-a-2d-convex-polygon/8721483#8721483)
     *
     * it's not my code, I just used because is free to use (https://wrf.ecse.rpi.edu//Research/Short_Notes/pnpoly.html#License to Use)
     * it's explained here (https://stackoverflow.com/questions/8721406/how-to-determine-if-a-point-is-inside-a-2d-convex-polygon/23223947#23223947)
     */
    public boolean contains(Point position)
    {
        boolean result = false;
        for (int length = this.points.size(), i = 0, j = length - 1; i < length; j = i++) {
            if ((this.points.get(i).y > position.y) != (this.points.get(j).y > position.y) &&
            (position.x < (this.points.get(j).x - this.points.get(i).x) * (position.y - this.points.get(i).y) / (this.points.get(j).y - this.points.get(i).y) + this.points.get(i).x)) {
                result = !result;
            }
        }

        return result;
    }
}
