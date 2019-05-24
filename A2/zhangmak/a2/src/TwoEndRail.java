/*

The TwoEndRail class.  A TwoEndRail object has two ends,
which may or may be not be opposite each other.

*/

import java.awt.Color;

/**
 * The TwoEndRail class that extend from Rail.
 * Describe a Rail that have two ends.
 *
 * @author Make Zhang
 * @version J.R.E 1.8.0
 */
abstract class TwoEndRail extends Rail {

  /**
   * Different end directions.
   */
  private Direction end1, end2;
  /**
   * The Rail in different directions.
   */
  private Rail neighbour1, neighbour2;

  /**
   * Return is it a direction that can exit.
   *
   * @param d Given direction.
   * @return boolean
   */
  public boolean exitOK(final Direction d) {
    return d.equals(end1) || d.equals(end2);
  }

  /**
   * A new TwoEndRail with two directions and location.
   *
   * @param e1 direction1
   * @param e2 direction2
   * @param loc location
   */
  TwoEndRail(final Direction e1, final Direction e2, final GridLoc loc) {
    super(loc);
    super.setColor(Color.black);
    end1 = e1;
    end2 = e2;
  }


  /**
   * Register neighbour with a new rail according to given direction.
   *
   * @param r Given rail
   * @param d Given direction
   */
  public void register(final Rail r, final Direction d) {
    if (exitOK(d)) {
      if (d.equals(end1)) {
        neighbour1 = r;
      } else {
        neighbour2 = r;
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
      } else {
        return end1;
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
      } else {
        return neighbour1;
      }
    }
    return null;
  }

  /**
   * toString method.
   *
   * @return @see java.lang.Object#toString()
   */
  public String toString() {
    return "TwoEndRail";
  }
}

