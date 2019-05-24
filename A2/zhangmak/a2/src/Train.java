/*

The Train class.  Trains have an Engine, followed by one or more Cars,
followed by a Caboose.  There is no limit to the length of a Train.  The
train has a speed, which is related to the size of the engine, the weight
of the whole train, and the amount of power flowing through the tracks.

A train has a delay, which is directly related to the speed -- the faster
the train is moving, the shorter the delay.  Each turn, a Train will move
one track piece in the current direction.

*/

import java.awt.Color;

/**
 * The Train class that extend from Thread.
 * Describe a Train that is formed cars.
 *
 * @author Make Zhang
 * @version J.R.E 1.8.0
 */
class Train extends Thread {

  /**
   * The sum of the weights of my cars.
   */
  private int totalWeight;
  /**
   * The amount of time between each of my turns.
   */
  private int delay;
  /**
   * The Car pulling the train.
   */
  private Car engine;
  /**
   * The Car at the end of the train.
   */
  private Car caboose;
  /**
   * The number of cars in me.
   */
  private int numCars = 0;
  /**
   * Number of 20.
   */
  private static final int MAGIC20 = 20;

  /**
   * A new Train.
   *
   * @param threadName a name of thread
   */
  Train(final String threadName) {
    super(threadName);
  }

  /**
   * Add Car T to the end of me.
   *
   * @param newCar Given Car
   */
  void addToTrain(final Car newCar) {
    if (numCars != 0) {
      newCar.setColor(Color.red);
      caboose.setNextCar(newCar);
    } else {
      newCar.setColor(Color.green);
      engine = newCar;
    }

    totalWeight += newCar.getWeight();
    caboose = newCar;
    numCars++;
  }

  /**
   * Set my delay between moves to d.
   *
   * @param d speed
   */
  void setSpeed(final int d) {
    delay = d + totalWeight;
  }

  /**
   * Add Car to Track T at location loc moving in direction dir.
   *
   * @param track Given Track
   * @param dir Given Direction
   * @param loc Given Location
   */
  void addToTrack(final Track track, final Direction dir, final GridLoc loc) {
    /*
    The Track on which I am running.
   */
    track.addTrain(this);
    Direction temp = dir;
    Car currCar = engine;
    while (currCar != null) {
      currCar.setDirection(temp);
      track.addCar(loc, currCar);

      // Now figure out the dir for the next Car,
      // and the next loc.

      if (dir.equals("north")) {
        loc.setRow(loc.getRow() - 1);
      } else if (dir.equals("south")) {
        loc.setRow(loc.getRow() + 1);
      } else if (dir.equals("east")) {
        loc.setCol(loc.getCol() + 1);
      } else if (dir.equals("west")) {
        loc.setCol(loc.getCol() - 1);
      }

      Direction nD = currCar.getCurrentRail().exit(dir);
      Rail nextRail = currCar.getCurrentRail().nextRail(nD);

      // Now I know the Rail on which the next currCar will
      // be.  Find out how it got on to it.
      temp = nextRail.exit(dir.opposite());

      currCar = currCar.getNextCar();
    }
  }


  /**
   * Halve my delay.
   */
  void accelerateALot() {
    delay /= 2;
  }


  /**
   * Double my delay.
   */
  void decelerateALot() {
    delay *= 2;
  }


  /**
   * Speed up by a factor of 20ms.
   */
  void accelerate() {
    delay -= MAGIC20;
  }

  /**
   * Slow down by a factor of 20ms.
   */
  void decelerate() {
    delay += MAGIC20;
  }

  /**
   * Make the train to run.
   */
  public void run() {
    while (true) {
      engine.move();

      // Sleep for 1 second.
      try {
        sleep(delay);
      } catch (InterruptedException e) {
        break;
      }
    }
  }
}

