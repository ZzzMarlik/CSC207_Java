/*

The Car class.  A Car object is a car in a train.  It has
weight and color, and can draw() and move().

*/

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * The Car class. Describe a single car with color, weight, direction.
 *
 * @author Make Zhang
 * @version J.R.E 1.8.0
 */
public class Car {

  // My color.
  /**
   * This car's color.
   */
  private Color color;

  // My weight, in stone.
  /**
   * This car's weight.
   */
  private static final int WEIGHT = 100;

  /**
   * The Car that immediately follows me.
   */
  private Car nextCar;

  /**
   * This car's currentRail.
   */
  private Rail currentRail;
  /**
   * This car's dir.
   */
  private Direction dir;
  /**
   * Number of 4.
   */
  private static final int MAGIC4 = 4;
  /**
   * Number of 3.
   */
  private static final int MAGIC3 = 3;
  /**
   * Number of 5.
   */
  private static final int MAGIC5 = 5;
  /**
   * Number of 7.
   */
  private static final int MAGIC7 = 7;
  /**
   * Number of 8.
   */
  private static final int MAGIC8 = 8;

  /**
   * set the direction of the car to dir.
   *
   * @param direction the direction to car
   */
  public void setDirection(final Direction direction) {
    dir = direction;
  }

  /**
   * set the newRail to the car to currentRail.
   *
   * @param newRail the car's current rail
   */
  public void setRail(final Rail newRail) {
    currentRail = newRail;
  }

  /**
   * set the newColor to the car to color.
   *
   * @param newColor the car's color
   */
  void setColor(final Color newColor) {
    this.color = newColor;
  }

  /**
   * Get car's weight.
   *
   * @return WEIGHT
   */
  int getWeight() {
    return WEIGHT;
  }

  /**
   * set the nextCAR to the car to nextCar.
   *
   * @param nextCAR nextCar of the car
   */
  void setNextCar(final Car nextCAR) {
    this.nextCar = nextCAR;
  }

  /**
   * @return the nextCar
   */
  Car getNextCar() {
    return nextCar;
  }

  /**
   * @return currentRail
   */
  Rail getCurrentRail() {
    return currentRail;
  }

  /**
   * Move forward one TrackPiece; t is the current TrackPiece.  Tell
   * all of the cars I am pulling to move as well.
   */
  void move() {

    Direction nD = currentRail.exit(dir);
    Direction nextDir = nD.opposite();
    Rail nextRail = currentRail.nextRail(dir);
    if ((nextRail.exitOK(nextDir)) && (!nextRail.occupied())) {
      dir = nextDir;
      if (nextRail.enter(this)) {
        currentRail.leave();
        currentRail = nextRail;

        // We have to call this here rather than within currentRail.enter()
        // because otherwise the wrong Rail is used...
        currentRail.repaint();

        if (nextCar != null) {
          nextCar.move();
        }
      }
    }
  }

  /**
   * @return true if the current Rail is a SwitchRail and I am going straight through it.
   */
  private boolean switchStraight() {

    return (currentRail instanceof SwitchRail)
        && ((SwitchRail) currentRail).isGoingStraight();
  }

  /**
   * @return Return true if the current Rail is a SwitchRail and I am going around a corner.
   */
  private boolean switchCorner() {

    return (currentRail instanceof SwitchRail)
        && !(((SwitchRail) currentRail).isGoingStraight());
  }

  /**
   * Redraw myself.
   *
   * @param g graph of the car
   */
  void draw(final Graphics g) {

    Rectangle b = currentRail.getBounds();

    double width = b.width;
    double height = b.height;

    int sqrtOfHypotenuse = (int) Math.sqrt((width * width / MAGIC4)
        + (height * height / MAGIC4));

    int[] xPoints = new int[MAGIC5];
    int[] yPoints = new int[MAGIC5];

    if (currentRail instanceof StraightRail
        || currentRail instanceof CrossRail
        || switchStraight()) {
      if (dir.equals("north") || dir.equals("south")) {
        makeStraightPolygon(xPoints, yPoints);
      } else {
        makeStraightPolygon(yPoints, xPoints);
      }
    } else if (currentRail instanceof CornerRail || switchCorner()) {
      switch (currentRail.getRailname()) {
        case "NERail":
        case "NSERail":
        case "EWNRail":
          makeCornerPolygon(xPoints, yPoints, -sqrtOfHypotenuse,
              sqrtOfHypotenuse, (int) width, (int) (width / 2),
              (int) (height / 2), 0);

          break;
        case "NWRail":
        case "NSWRail":
        case "WENRail":
          makeCornerPolygon(xPoints, yPoints, sqrtOfHypotenuse,
              sqrtOfHypotenuse, (int) (width / 2), 0,
              0, (int) (height / 2));

          break;
        case "SERail":
        case "SNERail":
        case "EWSRail":
          makeCornerPolygon(xPoints, yPoints, -sqrtOfHypotenuse,
              -sqrtOfHypotenuse, (int) (width / 2), (int) width,
              (int) height, (int) (height / 2));

          break;
        case "SWRail":
        case "SNWRail":
        case "WESRail":
          makeCornerPolygon(xPoints, yPoints, sqrtOfHypotenuse,
              -sqrtOfHypotenuse, 0, (int) (width / 2),
              (int) (height / 2), (int) height);

          break;
        default:
          break;
      }
    }

    g.setColor(color);
    g.drawPolygon(xPoints, yPoints, MAGIC5);

  }

  /**
   * The points, in order, are the back right of the car,
   * the front right of the car, the front left of the car,
   * and the back left of the car.
   *
   * @param xPoints Points x
   * @param yPoints Points y
   * @param xSideOffset SideOffset x
   * @param ySideOffset SideOffset y
   * @param x0Mod Mod of x0
   * @param x1Mod Mod of x1
   * @param y0Mod Mod of y0
   * @param y1Mod Mod of y1
   */
  private void makeCornerPolygon(final int[] xPoints, final int[] yPoints,
      final int xSideOffset, final int ySideOffset, final int x0Mod,
      final int x1Mod, final int y0Mod, final int y1Mod) {

    xPoints[0] = x0Mod;
    xPoints[1] = x1Mod;
    xPoints[2] = xPoints[1] + xSideOffset / 2;
    xPoints[MAGIC3] = xPoints[0] + xSideOffset / 2;
    xPoints[MAGIC4] = xPoints[0];

    yPoints[0] = y0Mod;
    yPoints[1] = y1Mod;
    yPoints[2] = yPoints[1] + ySideOffset / 2;
    yPoints[MAGIC3] = yPoints[0] + ySideOffset / 2;
    yPoints[MAGIC4] = yPoints[0];
  }

  /**
   * Make a StraightPolygon.
   *
   * @param aPoints Points of a
   * @param bPoints Points of b
   */
  private void makeStraightPolygon(final int[] aPoints, final int[] bPoints) {
    Rectangle b = currentRail.getBounds();
    int width = b.width;
    int height = b.height;

    aPoints[0] = width / MAGIC4;
    aPoints[1] = MAGIC3 * width / MAGIC4;
    aPoints[2] = MAGIC3 * width / MAGIC4;
    aPoints[MAGIC3] = width / MAGIC4;
    aPoints[MAGIC4] = aPoints[0];

    bPoints[0] = height / MAGIC8;
    bPoints[1] = height / MAGIC8;
    bPoints[2] = MAGIC7 * height / MAGIC8;
    bPoints[MAGIC3] = MAGIC7 * height / MAGIC8;
    bPoints[MAGIC4] = bPoints[0];
  }

  /**
   * @return Car
   */
  public String toString() {
    return "Car";
  }

}

