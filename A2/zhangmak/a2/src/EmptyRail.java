/*

The EmptyRail class.  This is a place on the Track which does not have an actual
piece of track.

*/

/**
 * The EmptyRail.
 *
 * @author Make Zhang
 * @version J.R.E 1.8.0
 */
class EmptyRail extends Rail {

  /**
   * Return true if d is a valid direction for me.
   *
   * @param d Given direction
   * @return boolean
   */
  public boolean exitOK(final Direction d) {
    return false;
  }


  /**
   * Register that Rail r is in Direction d.
   *
   * @param r Given rail
   * @param d Given direction
   */
  public void register(final Rail r, final Direction d) {
  }

  /**
   * Given that d is the Direction from which a Car entered,
   * report where the Car will exit.
   *
   * @param d Given direction
   * @return Direction
   */
  public Direction exit(final Direction d) {
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
    return null;
  }

}

