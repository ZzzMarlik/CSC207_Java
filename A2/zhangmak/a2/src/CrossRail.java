/*

The CrossRail class.  A CrossRail object has four ends.

*/

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * The CrossRail class that extend from Rail.
 * Describe a Rail that have four directions.
 *
 * @author Make Zhang
 * @version J.R.E 1.8.0
 */
class CrossRail extends Rail {

  /**
   * My line coordinates for drawing myself.
   */
  private double x1, y1, x2, y2, x3, y3, x4, y4;

  /**
   * (end1,end2) and (end3,end4) are the two pairs.
   * The are, in order, always 'north', 'south', 'east', and 'west'.
   */
  private Direction end1 = new Direction("north"),
      end2 = new Direction("south"),
      end3 = new Direction("east"), end4 = new Direction("west");

  /**
   * The Rail in different neighbours.
   */
  private Rail neighbour1, neighbour2, neighbour3, neighbour4;
  /**
   * Number of 0.5.
   */
  private static final double MAGIC05 = 0.5;

  /**
   * Return is it a direction that can exit.
   *
   * @param direction Given direction.
   * @return boolean
   */
  public boolean exitOK(final Direction direction) {
    return direction.equals(end1) || direction.equals(end2)
        || direction.equals(end3) || direction.equals(end4);
  }

  /**
   * A new CrossRail with given location.
   *
   * @param loc Given location
   */
  CrossRail(final GridLoc loc) {
    super(loc);
    super.setColor(Color.darkGray);
    setLoc(loc);
  }

  /**
   * Set the location of the crossRail with given location.
   *
   * @param loc Given location
   */
  public void setLoc(final GridLoc loc) {
    super.setLoc(loc);
    x1 = 0.0;
    y1 = MAGIC05;
    x2 = 1.0;
    y2 = MAGIC05;

    x3 = MAGIC05;
    y3 = 0.0;
    x4 = MAGIC05;
    y4 = 1.0;
  }

  /**
   * Register neighbour with a new rail according to given direction.
   *
   * @param rail Given rail
   * @param direction Given direction
   */
  public void register(final Rail rail, final Direction direction) {
    if (exitOK(direction)) {
      if (direction.equals(end1)) {
        neighbour1 = rail;
      } else if (direction.equals(end2)) {
        neighbour2 = rail;
      } else if (direction.equals(end3)) {
        neighbour3 = rail;
      } else if (direction.equals(end4)) {
        neighbour4 = rail;
      }
    }
  }

  /**
   * Given that d is the Direction from which a Car entered,
   * report where the Car will exit.
   *
   * @param d Given direction
   * @return Direction
   */
  public Direction exit(final Direction d) {
    if (exitOK(d)) {
      if (d.equals(end1)) {
        return end2;
      } else if (d.equals(end2)) {
        return end1;
      } else if (d.equals(end3)) {
        return end4;
      } else if (d.equals(end4)) {
        return end3;
      }
    }

    return null;
  }

  /**
   * d is the direction that I entered from, and must be one of end1 and end2.
   * Return the Rail at the other end.
   *
   * @param d Given direction
   * @return Rail at the other end.
   */
  public Rail nextRail(final Direction d) {
    if (exitOK(d)) {
      if (d.equals(end1)) {
        return neighbour2;
      } else if (d.equals(end2)) {
        return neighbour1;
      } else if (d.equals(end3)) {
        return neighbour4;
      } else if (d.equals(end4)) {
        return neighbour3;
      }
    }

    return null;
  }

  /**
   * Redraw myself.
   *
   * @param g Rail graph
   */
  public void draw(final Graphics g) {
    super.draw(g);
    g.setColor(super.getColor());
    Rectangle b = getBounds();
    g.drawLine((int) (x1 * b.width), (int) (y1 * b.height),
        (int) (x2 * b.width), (int) (y2 * b.height));
    g.drawLine((int) (x3 * b.width), (int) (y3 * b.height),
        (int) (x4 * b.width), (int) (y4 * b.height));
  }

  /**
   * toString method.
   *
   * @return @see java.lang.Object#toString()
   */
  public String toString() {
    return "CrossRail";
  }

}

