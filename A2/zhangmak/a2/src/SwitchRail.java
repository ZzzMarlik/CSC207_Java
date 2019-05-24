/*

The SwitchRail class.  A SwitchRail object has three ends, and a controller
which determines which ends are used.  If a Car moves on from the first end,
the switch determines which of the other two ends it leaves from.  If it moves
on from one of the other two ends, it automatically leaves by the first end.

*/

import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * The SwitchRail class that extend from Rail.
 * Describe a Rail that have three directions and three neighbours.
 *
 * @author Make Zhang
 * @version J.R.E 1.8.0
 */
public class SwitchRail extends Rail {

  /**
   * My line coordinates for drawing myself.
   */
  private double x1, y1, x2, y2, x3, y3;

  /**
   * Info for my corner portion.
   */
  private int startAngle;

  /**
   * (end1,end2) and (end1,end3) are the two pairs.
   * end1 and end2 are the straight directions (i.e., they are
   * opposite each other), and end1 and end3 form the corner.
   */
  private Direction end1, end2, end3;
  /**
   * The Rail in different neighbours.
   */
  private Rail neighbour1, neighbour2, neighbour3;

  /**
   * Whether the train am aligned to go straight.
   */
  private boolean goingStraight;
  /**
   * Number of 270.
   */
  private static final int MAGIC270 = 270;
  /**
   * Number of MAGIC05.
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
   * get goingStraight.
   *
   * @return Whether train am aligned to go straight.
   */
  boolean isGoingStraight() {
    return goingStraight;
  }

  /**
   * A new SwitchRail with three direction and a location.
   *
   * @param e1 given direction 1
   * @param e2 given direction 2
   * @param e3 given direction 3
   * @param loc given location
   */
  public SwitchRail(final Direction e1, final Direction e2,
      final Direction e3, final GridLoc loc) {
    super(loc);
    super.setColor(Color.magenta);
    end1 = e1;
    end2 = e2;
    end3 = e3;
    super.setLoc(loc);
    if (e1.equals("east") && e2.equals("west") && e3.equals("north")) {
      startAngle = MAGIC180;
      x1 = 0.0;
      y1 = MAGIC05;
      x2 = 1.0;
      y2 = MAGIC05;
      x3 = MAGIC05;
      y3 = -MAGIC05;
      super.setRailname("EWNRail");
    }
    if (e1.equals("east") && e2.equals("west") && e3.equals("south")) {
      startAngle = MAGIC90;
      x1 = 0.0;
      y1 = MAGIC05;
      x2 = 1.0;
      y2 = MAGIC05;
      x3 = MAGIC05;
      y3 = MAGIC05;
      super.setRailname("EWSRail");
    }
    if (e1.equals("north") && e2.equals("south") && e3.equals("east")) {
      startAngle = MAGIC180;
      x1 = MAGIC05;
      y1 = 0.0;
      x2 = MAGIC05;
      y2 = 1.0;
      x3 = MAGIC05;
      y3 = -MAGIC05;
      super.setRailname("NSERail");
    }
    if (e1.equals("north") && e2.equals("south") && e3.equals("west")) {
      startAngle = MAGIC270;
      x1 = MAGIC05;
      y1 = 0.0;
      x2 = MAGIC05;
      y2 = 1.0;
      x3 = -MAGIC05;
      y3 = -MAGIC05;
      super.setRailname("NSWRail");
    }
    if (e1.equals("south") && e2.equals("north") && e3.equals("east")) {
      startAngle = MAGIC90;
      x1 = MAGIC05;
      y1 = 0.0;
      x2 = MAGIC05;
      y2 = 1.0;
      x3 = MAGIC05;
      y3 = MAGIC05;
      super.setRailname("SNERail");
    }
    if (e1.equals("south") && e2.equals("north") && e3.equals("west")) {
      startAngle = 0;
      x1 = MAGIC05;
      y1 = 0.0;
      x2 = MAGIC05;
      y2 = 1.0;
      x3 = -MAGIC05;
      y3 = MAGIC05;
      super.setRailname("SNWRail");
    }
    if (e1.equals("west") && e2.equals("east") && e3.equals("north")) {
      startAngle = MAGIC270;
      x1 = 0.0;
      y1 = MAGIC05;
      x2 = 1.0;
      y2 = MAGIC05;
      x3 = -MAGIC05;
      y3 = -MAGIC05;
      super.setRailname("WENRail");
    }
    if (e1.equals("west") && e2.equals("east") && e3.equals("south")) {
      startAngle = 0;
      x1 = 0.0;
      y1 = MAGIC05;
      x2 = 1.0;
      y2 = MAGIC05;
      x3 = -MAGIC05;
      y3 = MAGIC05;
      super.setRailname("WESRail");
    }
  }

  /**
   * A new SwitchRail with three direction.
   *
   * @param e1 given direction 1
   * @param e2 given direction 2
   * @param e3 given direction 3
   */
  public SwitchRail(final Direction e1,
      final Direction e2, final Direction e3) {
    super();
    super.setColor(Color.magenta);
    end1 = e1;
    end2 = e2;
    end3 = e3;
  }

  /**
   * Return is it a direction that can exit.
   *
   * @param d Given direction.
   * @return boolean
   */
  public boolean exitOK(final Direction d) {
    if (goingStraight) {
      return d.equals(end1) || d.equals(end2);
    } else {
      return d.equals(end1) || d.equals(end3);
    }
  }

  /**
   * Register neighbour with a new rail according to given direction.
   *
   * @param r Given rail
   * @param d Given direction
   */
  public void register(final Rail r, final Direction d) {
    if (d.equals(end1)) {
      neighbour1 = r;
    }
    if (d.equals(end2)) {
      neighbour2 = r;
    }
    if (d.equals(end3)) {
      neighbour3 = r;
    }
  }


  /**
   * Given that d is the Direction from which a Car entered,
   * report where the Car will exit.
   * Note that if d is not end1's Direction, then it will have to
   * exit toward end1.
   *
   * @param d Given direction
   * @return Direction
   */
  public Direction exit(final Direction d) {
    if (goingStraight && (d.equals(end1))) {
      return end2;
    }
    if (!goingStraight && (d.equals(end1))) {
      return end3;
    }
    if (goingStraight && d.equals(end2)) {
      return end1;
    }
    if (!goingStraight && d.equals(end3)) {
      return end1;
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
    if (goingStraight && (d.equals(end1))) {
      return neighbour2;
    }
    if (!goingStraight && (d.equals(end1))) {
      return neighbour3;
    }
    if (goingStraight && d.equals(end2)) {
      return neighbour1;
    }
    if (!goingStraight && d.equals(end3)) {
      return neighbour1;
    }
    return null;
  }

  /**
   * Handle a mouse click.  This will toggle the direction of the switch.
   *
   * @param evt event
   * @return boolean
   */
  public boolean handleEvent(final Event evt) {

    if (evt.id == Event.MOUSE_DOWN && !occupied()) {
      goingStraight = !goingStraight;
      repaint();
      return true;
    }

    // If we get this far, I couldn't handle the event
    return false;
  }

  /**
   * Redraw myself.
   *
   * @param g Rail graph
   */
  public void draw(final Graphics g) {

    super.draw(g);

    Rectangle b = getBounds();

    // Draw current direction of the switch darker.
    int arcAngle = MAGIC90;
    if (goingStraight) {
      g.setColor(Color.lightGray);
      g.drawArc((int) (x3 * b.width), (int) (y3 * b.height),
          b.width, b.height, startAngle, arcAngle);
      g.setColor(super.getColor());
      g.drawLine((int) (x1 * b.width), (int) (y1 * b.height),
          (int) (x2 * b.width), (int) (y2 * b.height));
    } else {
      g.setColor(Color.lightGray);
      g.drawLine((int) (x1 * b.width), (int) (y1 * b.height),
          (int) (x2 * b.width), (int) (y2 * b.height));
      g.setColor(super.getColor());
      g.drawArc((int) (x3 * b.width), (int) (y3 * b.height),
          b.width, b.height, startAngle, arcAngle);
    }
  }

  /**
   * toString method.
   *
   * @return @see java.lang.Object#toString()
   */
  public String toString() {
    return "SwitchRail";
  }
}

