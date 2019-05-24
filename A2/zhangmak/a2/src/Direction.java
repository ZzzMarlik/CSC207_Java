// A direction; one of 'north', 'south', 'east' and 'west'.

/**
 * The Direction class.
 * Describe a direction.
 *
 * @author Make Zhang
 * @version J.R.E 1.8.0
 */
class Direction {

  /**
   * Default direction.
   */
  private String direction;

  /**
   * A new direction.
   *
   * @param s Given string of direction
   */
  Direction(final String s) {

    if (!s.equals("north") && !s.equals("south")
        && !s.equals("east") && !s.equals("west")) {
      System.err.println(s + " is an invalid direction. Must be "
          + "'north', 'south', 'east' or 'west'");
      System.exit(0);
    }

    direction = s;

  }

  /**
   * Compare two direction in Direction form.
   *
   * @param d Given direction
   * @return Equal or not
   */
  boolean equals(final Direction d) {
    return d.equals(direction);
  }

  /**
   * Compare two direction in String form.
   *
   * @param s Given String
   * @return Equal or not
   */
  boolean equals(final String s) {
    return s.equals(direction);
  }

  /**
   * toString method.
   *
   * @return @see java.lang.Object#toString()
   */
  public String toString() {
    return direction;
  }

  /**
   * Return opposite direction.
   *
   * @return opposite direction
   */
  Direction opposite() {
    Direction d;
    switch (direction) {
      case "north":
        d = new Direction("south");
        break;
      case "south":
        d = new Direction("north");
        break;
      case "east":
        d = new Direction("west");
        break;
      case "west":
        d = new Direction("east");
        break;
      default:
        d = new Direction("Invalid name");
        break;
    }

    return d;
  }

}

