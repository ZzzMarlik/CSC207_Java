/*

The StraightRail class.  A StraightRail object has two ends,
which must be opposite each other.

*/

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * The StraightRail class that extend from TwoEndRail.
 * Describe a Rail that only have two direction.
 *
 * @author Make Zhang
 * @version J.R.E 1.8.0
 */
public class StraightRail extends TwoEndRail {

  /**
   * The multipliers for the endpoints of the StraightRail.
   */
  private double x1, y1, x2, y2;
  /**
   * Number of 0.5.
   */
  private static final double MAGIC05 = 0.5;

  /**
   * A new StraightRail with two direction and a location.
   *
   * @param e1 given direction1
   * @param e2 given direction2
   * @param loc given location
   */
  public StraightRail(final Direction e1,
      final Direction e2, final GridLoc loc) {
    super(e1, e2, loc);
    super.setColor(Color.blue);
    if (e1.equals("east") && e2.equals("west")) {
      super.setLoc(loc);
      x1 = 0.0;
      y1 = MAGIC05;
      x2 = 1.0;
      y2 = MAGIC05;
      super.setRailname("EWRail");
    }
    if (e1.equals("north") && e2.equals("south")) {
      super.setLoc(loc);
      x1 = MAGIC05;
      y1 = 0.0;
      x2 = MAGIC05;
      y2 = 1.0;
      super.setRailname("NSRail");
    }
  }

  /**
   * Redraw myself.
   *
   * @param g A graph of rail
   */
  public void draw(final Graphics g) {
    super.draw(g);
    g.setColor(super.getColor());
    Rectangle b = getBounds();
    g.drawLine((int) (x1 * b.width), (int) (y1 * b.height),
        (int) (x2 * b.width), (int) (y2 * b.height));
  }

  /**
   * toString method.
   *
   * @return @see java.lang.Object#toString()
   */
  public String toString() {
    return "StraightRail";
  }
}

