// Author: Paul Gries
// Written: 27 June 1997
// Last revised: 29 June 1997

// Simulate trains running around a track.

import java.awt.Color;
import java.awt.Frame;

/**
 * The class TrainSimulation contains all the methods and instance variables
 * necessary to keep track of and run the train simulation.
 *
 * @author Make Zhang
 * @version J.R.E 1.8.0
 */
public class TrainSimulation extends Frame {

  /**
   * Number of 4.
   */
  private static final int MAGIC4 = 4;
  /**
   * Number of 540.
   */
  private static final int MAGIC540 = 540;
  /**
   * Number of 400.
   */
  private static final int MAGIC400 = 400;
  /**
   * Number of 620.
   */
  private static final int MAGIC620 = 620;
  /**
   * Number of 350.
   */
  private static final int MAGIC350 = 350;
  /**
   * Number of 8.
   */
  private static final int MAGIC8 = 8;
  /**
   * Number of 5.
   */
  private static final int MAGIC5 = 5;
  /**
   * The Tracks on which the Trains run.
   */
  private static Track[] tracks = new Track[MAGIC4];

  /**
   * The Trains running on the Tracks.
   */
  private static Train[] trains = new Train[MAGIC8];

  // main
  // ------------------------------------------------------------------
  // This is where it all starts.

  /**
   * This is where it all starts.
   *
   * @param args String[]
   */
  public static void main(final String[] args) {

    // Track 2.
    tracks[0] = new Track();
    tracks[0].setSize(MAGIC540, MAGIC400);
    tracks[0].setLocation(0, 0);
    tracks[0].setBackground(Color.white);
    tracks[0].setVisible(true);

    trains[0] = new Train("Train 0");
    trains[0].addToTrain(new Car());
    trains[0].addToTrain(new Car());

    trains[1] = new Train("Train 1");
    trains[1].addToTrain(new Car());
    trains[1].addToTrain(new Car());

//        T.trains[2] = new Train();
//        T.trains[2].addToTrain(new Engine());
//        T.trains[2].addToTrain(new Caboose());

    trains[0].addToTrack(tracks[0], new Direction("east"), new GridLoc(2, 2));
    trains[0].setSpeed(MAGIC620);
    trains[1].addToTrack(tracks[0],
        new Direction("south"), new GridLoc(1, MAGIC5));
    trains[1].setSpeed(MAGIC350);

//        T.trains[2].addToTrack(T.tracks[0], new Direction("east")
// , new GridLoc(2, 2));
//        T.trains[2].setSpeed(754);

//        T.trains[0].start();
    //       T.trains[1].start();
//        T.trains[2].start();

/* Uncomment this to get more tracks!!!

        // Track 2.
        T.tracks[1] = new Track();
        T.tracks[1].resize (400, 400);
        T.tracks[1].move(410, 0);
        T.tracks[1].setBackground(Color.white);
        T.tracks[1].show();

        T.trains[3] = new Train("Train 3");
        T.trains[3].addToTrain(new Car());
        T.trains[4] = new Train("Train 4");
        T.trains[4].addToTrain(new Car());

        T.trains[3].addToTrack(T.tracks[1],
         new Direction("east"), new GridLoc(0, 0));
        T.trains[3].setSpeed(500);
        T.trains[4].addToTrack(T.tracks[1],
         new Direction("north"), new GridLoc(1, 2));
        T.trains[4].setSpeed(400);

        T.trains[3].start();
        T.trains[4].start();

        // Track 3.
        T.tracks[2] = new Track();
        T.tracks[2].resize (400, 400);
        T.tracks[2].move(400, 400);
        T.tracks[2].setBackground(Color.white);
        T.tracks[2].show();

        T.trains[5] = new Train("Train 5");
        T.trains[5].addToTrain(new Car());
        T.trains[6] = new Train("Train 6");
        T.trains[6].addToTrain(new Car());

        T.trains[5].addToTrack(T.tracks[2],
         new Direction("south"), new GridLoc(2, 4));
        T.trains[5].setSpeed(500);
        T.trains[6].addToTrack(T.tracks[2],
         new Direction("north"), new GridLoc(2, 0));
        T.trains[6].setSpeed(400);

        T.trains[5].start();
        T.trains[6].start();
*/
  }
}

