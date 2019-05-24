/*

The Rail class.  A Rail object is a piece of track.  It knows
whether there is a Train on it or not, and Trains can enter()
and leave().  Given an entrance, a Rail can report where the exit()
is.

*/

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * The Rail class that extend from Canvas.
 * Describe a General Rail.
 *
 * @author Make Zhang
 * @version J.R.E 1.8.0
 */
abstract class Rail extends Canvas {

  /**
   * The color of the Rail.
   */
  private Color color;

  /**
   * Get color.
   *
   * @return color
   */
  Color getColor() {
    return color;
  }

  /**
   * Set color.
   *
   * @param newColor Given color
   */
  void setColor(final Color newColor) {
    this.color = newColor;
  }

  /**
   * Whether the Rail have a train.
   */
  private boolean haveATrain;
  /**
   * The current car on the Rail.
   */
  private Car currentCar;
  /**
   * The location of the rail.
   */
  private GridLoc location;
  /**
   * The name of the rail.
   */
  private String railname;

  /**
   * get rail name.
   *
   * @return rail name
   */
  String getRailname() {
    return railname;
  }

  /**
   * set rail name.
   *
   * @param railName given rail name
   */
  void setRailname(final String railName) {
    this.railname = railName;
  }

  /**
   * A new Rail with given location.
   *
   * @param loc given location
   */
  Rail(final GridLoc loc) {
    location = loc;
    haveATrain = false;
  }

  /**
   * A new Rail.
   */
  Rail() {
    super();
    haveATrain = false;
    final int size = 20;
    setSize(size, size);
  }

  /**
   * Return true iff I have a Car.
   *
   * @return boolean
   */
  boolean occupied() {
    return haveATrain;
  }

  /**
   * set location.
   *
   * @param loc given location
   */
  public void setLoc(final GridLoc loc) {
    location = loc;
  }


  /**
   * Redraw myself.
   *
   * @param g A graph of rail
   */
  public void draw(final Graphics g) {

    Rectangle b = getBounds();
    g.setColor(Color.white);
    g.fillRect(0, 0, b.width - 1, b.height - 1);
    g.setColor(Color.lightGray);
    g.drawRect(0, 0, b.width - 1, b.height - 1);

    if (haveATrain) {
      currentCar.draw(g);
    }
  }


  /**
   * Register that a Train is on me.  Return true if successful,
   * false otherwise.
   *
   * @param newCar given new car
   * @return if a train is on me
   */
  boolean enter(final Car newCar) {
    if (!haveATrain) {
      haveATrain = true;
      currentCar = newCar;
      return true;
    }
    return false;
  }

  /**
   * Register that a Train is no longer on me.
   */
  void leave() {
    haveATrain = false;
    repaint();
  }

  /**
   * Update my display.
   *
   * @param g graph of rail
   */
  public void paint(final Graphics g) {
    draw(g);
  }

  /**
   * Return true if d is a valid direction for me.
   *
   * @param d given direction
   * @return boolean
   */
  public abstract boolean exitOK(Direction d);

  /**
   * Register neighbour with a new rail according to given direction.
   *
   * @param r Given rail
   * @param d Given direction
   */
  public abstract void register(Rail r, Direction d);

  /**
   * Given that d is the Direction from which a Car entered,
   * report where the Car will exit.
   *
   * @param d Given direction
   * @return Direction
   */
  public abstract Direction exit(Direction d);

  /**
   * d is the direction that I entered from, and must be one of end1 and end2.
   * Return the Rail at the other end.
   *
   * @param d Given direction
   * @return Rail at the other end.
   */
  public abstract Rail nextRail(Direction d);

  /**
   * toString method.
   *
   * @return @see java.lang.Object#toString()
   */
  public String toString() {
    return "Rail";
  }

  @Override
  public GridLoc getLocation() {
    return location;
  }
}

