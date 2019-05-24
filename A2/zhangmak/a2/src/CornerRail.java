/*

The CornerRail class.  A CornerRail object has two ends,
which must be not be opposite each other.

*/

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * The CornerRail class that extend from TwoEndRail.
 * Describe a Rail that is located at the corner of the track.
 *
 * @author Make Zhang
 * @version J.R.E 1.8.0
 */
public class CornerRail extends TwoEndRail {

  /**
   * The multipliers for the width and height.
   */
  private double x1, y1;
  /**
   * The startAngle of the cornerRail.
   */
  private int startAngle;
  /**
   * Number of 270.
   */
  private static final int MAGIC270 = 270;
  /**
   * Number of 0.5.
   */
  private static final double MAGIC05 = 0.5;
  /**
   * Number of 180.
   */
  private static final int MAGIC180 = 180;
  /**
   * Number of 90.
   */
  private static final int MAGIC90 = 90;

  /**
   * Set the newStartAngle to startAngle.
   *
   * @param newStartAngle number of startAngle.
   */
  private void setStartAngle(final int newStartAngle) {
    this.startAngle = newStartAngle;
  }

  /**
   * A new CornerRail with two directions and location.
   *
   * @param direction1 first direction
   * @param direction2 second direction
   * @param loc initial location
   */
  public CornerRail(final Direction direction1,
      final Direction direction2, final GridLoc loc) {
    super(direction1, direction2, loc);
    if (direction1.equals("north") && direction2.equals("west")) {
      setLoc(loc);
      setStartAngle(MAGIC270);
      x1 = -MAGIC05;
      y1 = -MAGIC05;
      super.setRailname("NWRail");
    }
    if (direction1.equals("north") && direction2.equals("east")) {
      setLoc(loc);
      startAngle = MAGIC180;
      x1 = MAGIC05;
      y1 = -MAGIC05;
      super.setRailname("NERail");
    }
    if (direction1.equals("south") && direction2.equals("west")) {
      setLoc(loc);
      startAngle = 0;
      x1 = -MAGIC05;
      y1 = MAGIC05;
      super.setRailname("SWRail");
    }
    if (direction1.equals("south") && direction2.equals("east")) {
      setLoc(loc);
      startAngle = MAGIC90;
      x1 = MAGIC05;
      y1 = MAGIC05;
      super.setRailname("SERail");
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
    g.drawArc((int) (x1 * b.width), (int) (y1 * b.height),
        b.width, b.height, startAngle, MAGIC90);
  }

  /**
   * toString method.
   *
   * @return @see java.lang.Object#toString()
   */
  public String toString() {
    return "CornerRail";
  }


}

