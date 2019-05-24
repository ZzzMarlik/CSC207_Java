/*

The Track class.  A Track object is made up of Rails, and
has zero or more trains in it.

*/

import java.awt.Button;
import java.awt.Event;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Panel;

/**
 * The Car class which is extend from Frame.
 * Describe the whole Track
 *
 * @author Make Zhang
 * @version J.R.E 1.8.0
 */
class Track extends Frame {

  /**
   * The Panel on which the Track appears.
   */
  private TrackPanel trackPanel;

  // ------------------------------------------------------------------
  // The following items are the Rails and Trains on the Track.

  // private Rail first;    // The first Rail in the graph.
  /**
   * Number of 10.
   */
  private static final int MAGIC10 = 10;
  /**
   * Number of 20.
   */
  private static final int MAGIC20 = 20;
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
   * Number of 6.
   */
  private static final int MAGIC6 = 6;
  /**
   * Number of 7.
   */
  private static final int MAGIC7 = 7;
  /**
   * Number of 8.
   */
  private static final int MAGIC8 = 8;
  /**
   * The maximum number of trains I can hold.
   */
  private int maxTrains = MAGIC10;
  /**
   * The Trains running on me.
   */
  private Train[] trainList = new Train[maxTrains];
  /**
   * Total number of trains
   */
  private int numTrains = 0;
  /**
   * The grid of rails.
   */
  private Rail[][] rails;

  /**
   * Add the buttons for creating Rails.
   */
  private void buildTrack() {
    trackPanel = new TrackPanel();
    add("Center", trackPanel);

    Button runStopButton = new Button("Run");
    Button quitButton = new Button("Quit");
    Button accelButton = new Button("Accelerate");
    Button decelButton = new Button("Decelerate");
    Button accelLotsButton = new Button("Accelerate A Lot");
    Button decelLotsButton = new Button("Decelerate A Lot");

    Panel p2 = new Panel();
    p2.setLayout(new GridLayout(0, 1));
    p2.add(runStopButton);
    p2.add(accelLotsButton);
    p2.add(decelLotsButton);
    p2.add(accelButton);
    p2.add(decelButton);
    p2.add(quitButton);
    add("East", p2);

    pack();
  }


  /**
   * Read Rail-placing commands from the user.
   *
   * @param evt Event
   * @return boolean
   */
  public boolean handleEvent(final Event evt) {
    Object target = evt.target;

    if (evt.id == Event.ACTION_EVENT) {
      if (target instanceof Button) {
        if ("Run".equals(evt.arg)) {
          for (int i = 0; i < numTrains; i++) {
            trainList[i].start();
          }
          ((Button) target).setLabel("Suspend");
        } else if ("Suspend".equals(evt.arg)) {
          for (int i = 0; i < numTrains; i++) {
            trainList[i].interrupt();
          }
          ((Button) target).setLabel("Resume");
        } else if ("Resume".equals(evt.arg)) {
          for (int i = 0; i < numTrains; i++) {
            trainList[i].notify();
          }
          ((Button) target).setLabel("Suspend");
        } else if ("Accelerate".equals(evt.arg)) {
          for (int i = 0; i < numTrains; i++) {
            trainList[i].accelerate();
          }
        } else if ("Decelerate".equals(evt.arg)) {
          for (int i = 0; i < numTrains; i++) {
            trainList[i].decelerate();
          }
        } else if ("Accelerate A Lot".equals(evt.arg)) {
          for (int i = 0; i < numTrains; i++) {
            trainList[i].accelerateALot();
          }
        } else if ("Decelerate A Lot".equals(evt.arg)) {
          for (int i = 0; i < numTrains; i++) {
            trainList[i].decelerateALot();
          }
        } else if ("Quit".equals(evt.arg)) {
          for (int i = 0; i < numTrains; i++) {
            trainList[i].interrupt();
          }
          System.exit(0);
        }
        return true;
      }
    }

    // If we get this far, I couldn't handle the event
    return false;
  }

  /**
   * Connect rails r1 and r2; r2 is in direction d from r1.
   *
   * @param r1 rail 1
   * @param r2 rail 2
   * @param d direction
   */
  private void connectRails(final Rail r1, final Rail r2, final Direction d) {
    r1.register(r2, d);
    r2.register(r1, d.opposite());
  }

  /**
   * Add e to the rail at location loc.
   *
   * @param loc location
   * @param e Car
   */
  void addCar(final GridLoc loc, final Car e) {
    rails[loc.getRow()][loc.getCol()].enter(e);
    e.setRail(rails[loc.getRow()][loc.getCol()]);
  }

  // paint
  // ------------------------------------------------------------------

  /**
   * Paint the display.
   *
   * @param g graph
   */
  public void paint(final Graphics g) {
    update(g);
  }

  // update
  // ------------------------------------------------------------------

  /**
   * Update the display; tell all my Tracks to update themselves.
   *
   * @param g graph
   */
  public void update(final Graphics g) {

    trackPanel.repaint();
  }


  /**
   * Add T to myself.
   *
   * @param t Train
   */
  void addTrain(final Train t) {
    trainList[numTrains] = t;
    numTrains++;
  }

  /**
   * Set up a new, simple Track.
   */
  Track() {
    rails = new Rail[MAGIC20][MAGIC20];

    buildTrack();

    for (int row = 0; row < rails.length; row++) {
      for (int col = 0; col < rails[0].length; col++) {
        rails[row][col] = new EmptyRail();
      }
    }

//    ArrayList rowList0 = new ArrayList<Rail>();
//    ArrayList colList0 = new ArrayList<ArrayList>();
//    colList0.add(rowList0);
//    rowList0.add(new CornerRail(new Direction("south"), new Direction("east"), new GridLoc(0, 0)));

    rails[0][0] = new CornerRail(new Direction("south"),
        new Direction("east"), new GridLoc(0, 0)) {
    };

    rails[0][1] = new StraightRail(new Direction("east"), new Direction("west"),
        new GridLoc(0, 1)) {
    };
    connectRails(rails[0][0], rails[0][1], new Direction("east"));

//    rails[0][2] = new CornerRail(new Direction("south"),
//        new Direction("west"), new GridLoc(0, 2)) {
//    };
    rails[0][2] = new SwitchRail(new Direction("west"), new Direction("east"),
        new Direction("south"), new GridLoc(0, 2)) {
    };
    connectRails(rails[0][1], rails[0][2], new Direction("east"));

    rails[0][MAGIC3] = new StraightRail(new Direction("east"), new Direction("west"),
        new GridLoc(0, MAGIC3)) {
    };
    connectRails(rails[0][2], rails[0][MAGIC3], new Direction("east"));

    rails[0][MAGIC4] = new StraightRail(new Direction("east"), new Direction("west"),
        new GridLoc(0, MAGIC4)) {
    };
    connectRails(rails[0][MAGIC3], rails[0][MAGIC4], new Direction("east"));

    rails[0][MAGIC5] = new StraightRail(new Direction("east"), new Direction("west"),
        new GridLoc(0, MAGIC5)) {
    };
    connectRails(rails[0][MAGIC4], rails[0][MAGIC5], new Direction("east"));

    rails[0][MAGIC6] = new StraightRail(new Direction("east"), new Direction("west"),
        new GridLoc(0, MAGIC6)) {
    };
    connectRails(rails[0][MAGIC5], rails[0][MAGIC6], new Direction("east"));

    rails[0][MAGIC7] = new StraightRail(new Direction("east"), new Direction("west"),
        new GridLoc(0, MAGIC7)) {
    };
    connectRails(rails[0][MAGIC6], rails[0][MAGIC7], new Direction("east"));

    rails[0][MAGIC8] = new CornerRail(new Direction("south"),
        new Direction("west"), new GridLoc(0, MAGIC8)) {
    };
    connectRails(rails[0][MAGIC7], rails[0][MAGIC8], new Direction("east"));

    rails[1][MAGIC8] = new CornerRail(new Direction("north"),
        new Direction("west"), new GridLoc(1, MAGIC8)) {
    };
    connectRails(rails[0][MAGIC8], rails[1][MAGIC8], new Direction("south"));

    rails[1][2] = new StraightRail(new Direction("north"), new Direction("south"),
        new GridLoc(1, 2)) {
    };
    connectRails(rails[0][2], rails[1][2], new Direction("south"));

    rails[2][2] = new CrossRail(new GridLoc(2, 2));
    connectRails(rails[1][2], rails[2][2], new Direction("south"));

    rails[2][MAGIC3] = new StraightRail(new Direction("east"), new Direction("west"),
        new GridLoc(2, MAGIC3)) {
    };
    connectRails(rails[2][2], rails[2][MAGIC3], new Direction("east"));

    rails[2][MAGIC4] = new StraightRail(new Direction("east"), new Direction("west"),
        new GridLoc(2, MAGIC4)) {
    };
    connectRails(rails[2][MAGIC3], rails[2][MAGIC4], new Direction("east"));

    rails[2][MAGIC5] = new CrossRail(new GridLoc(2, MAGIC5));
    connectRails(rails[2][MAGIC4], rails[2][MAGIC5], new Direction("east"));

    rails[2][MAGIC6] = new CornerRail(new Direction("south"),
        new Direction("west"), new GridLoc(2, MAGIC6)) {
    };
    connectRails(rails[2][MAGIC5], rails[2][MAGIC6], new Direction("east"));

    rails[MAGIC3][MAGIC6] = new CornerRail(new Direction("north"),
        new Direction("west"), new GridLoc(MAGIC3, MAGIC6)) {
    };
    connectRails(rails[2][MAGIC6], rails[MAGIC3][MAGIC6], new Direction("south"));

    rails[MAGIC3][MAGIC5] = new CornerRail(new Direction("north"),
        new Direction("east"), new GridLoc(MAGIC3, MAGIC5)) {
    };
    connectRails(rails[MAGIC3][MAGIC6], rails[MAGIC3][MAGIC5], new Direction("west"));
    connectRails(rails[2][MAGIC5], rails[MAGIC3][MAGIC5], new Direction("south"));

    rails[1][MAGIC5] = new CornerRail(new Direction("south"),
        new Direction("east"), new GridLoc(1, MAGIC5)) {
    };
    connectRails(rails[2][MAGIC5], rails[1][MAGIC5], new Direction("north"));

    rails[1][MAGIC6] = new StraightRail(new Direction("east"), new Direction("west"),
        new GridLoc(1, MAGIC6)) {
    };
    connectRails(rails[1][MAGIC5], rails[1][MAGIC6], new Direction("east"));

    rails[1][MAGIC7] = new SwitchRail(new Direction("west"), new Direction("east"),
        new Direction("south"), new GridLoc(1, MAGIC7)) {
    };
    connectRails(rails[1][MAGIC6], rails[1][MAGIC7], new Direction("east"));
    connectRails(rails[1][MAGIC8], rails[1][MAGIC7], new Direction("west"));

    rails[2][MAGIC7] = new StraightRail(new Direction("north"), new Direction("south"),
        new GridLoc(2, MAGIC7)) {
    };
    connectRails(rails[1][MAGIC7], rails[2][MAGIC7], new Direction("south"));

    rails[MAGIC3][MAGIC7] = new StraightRail(new Direction("north"), new Direction("south"),
        new GridLoc(MAGIC3, MAGIC7)) {
    };
    connectRails(rails[2][MAGIC7], rails[MAGIC3][MAGIC7], new Direction("south"));

    rails[MAGIC4][MAGIC7] = new CornerRail(new Direction("north"),
        new Direction("west"), new GridLoc(MAGIC4, MAGIC7)) {
    };
    connectRails(rails[MAGIC3][MAGIC7], rails[MAGIC4][MAGIC7], new Direction("south"));

    rails[MAGIC4][MAGIC6] = new StraightRail(new Direction("east"), new Direction("west"),
        new GridLoc(MAGIC4, MAGIC6)) {
    };
    connectRails(rails[MAGIC4][MAGIC7], rails[MAGIC4][MAGIC6], new Direction("west"));

    rails[MAGIC4][MAGIC5] = new SwitchRail(new Direction("west"), new Direction("east"),
        new Direction("south"), new GridLoc(MAGIC4, MAGIC5)) {
    };
    connectRails(rails[MAGIC4][MAGIC6], rails[MAGIC4][MAGIC5], new Direction("west"));

    rails[MAGIC4][MAGIC4] = new CornerRail(new Direction("north"),
        new Direction("east"), new GridLoc(MAGIC4, MAGIC4)) {
    };
    connectRails(rails[MAGIC4][MAGIC5], rails[MAGIC4][MAGIC4], new Direction("west"));

    rails[MAGIC3][MAGIC4] = new CornerRail(new Direction("south"),
        new Direction("west"), new GridLoc(MAGIC3, MAGIC4)) {
    };
    connectRails(rails[MAGIC4][MAGIC4], rails[MAGIC3][MAGIC4], new Direction("north"));

    rails[MAGIC3][MAGIC3] = new SwitchRail(new Direction("east"), new Direction("west"),
        new Direction("south"), new GridLoc(MAGIC3, MAGIC3)) {
    };
    connectRails(rails[MAGIC3][MAGIC4], rails[MAGIC3][MAGIC3], new Direction("west"));

    rails[MAGIC4][MAGIC3] = new SwitchRail(new Direction("south"), new Direction("north"),
        new Direction("west"), new GridLoc(MAGIC4, MAGIC3)) {
    };
    connectRails(rails[MAGIC4][MAGIC3], rails[MAGIC3][MAGIC3], new Direction("north"));

    rails[MAGIC5][MAGIC3] = new CornerRail(new Direction("north"),
        new Direction("east"), new GridLoc(MAGIC5, MAGIC3)) {
    };
    connectRails(rails[MAGIC5][MAGIC3], rails[MAGIC4][MAGIC3], new Direction("north"));

    rails[MAGIC5][MAGIC4] = new StraightRail(new Direction("east"), new Direction("west"),
        new GridLoc(MAGIC5, MAGIC4)) {
    };
    connectRails(rails[MAGIC5][MAGIC4], rails[MAGIC5][MAGIC3], new Direction("west"));

    rails[MAGIC5][MAGIC5] = new CornerRail(new Direction("north"),
        new Direction("west"), new GridLoc(MAGIC5, MAGIC5)) {
    };
    connectRails(rails[MAGIC5][MAGIC5], rails[MAGIC5][MAGIC4], new Direction("west"));
    connectRails(rails[MAGIC5][MAGIC5], rails[MAGIC4][MAGIC5], new Direction("north"));

    // These are just put there to see what they look like.
//    rails[6][0] = new SwitchRail(new Direction("east"), new Direction("west"),
//        new Direction("north"), new GridLoc(6, 0)) {
//    };
//    rails[6][1] = new SwitchRail(new Direction("east"), new Direction("west"),
//        new Direction("south"), new GridLoc(6, 1)) {
//    };
//    rails[6][2] = new SwitchRail(new Direction("west"), new Direction("east"),
//        new Direction("north"), new GridLoc(6, 2)) {
//    };
//    rails[6][3] = new SwitchRail(new Direction("west"), new Direction("east"),
//        new Direction("south"), new GridLoc(6, 3)) {
//    };
//    rails[6][4] = new SwitchRail(new Direction("north"), new Direction("south"),
//        new Direction("east"), new GridLoc(6, 4)) {
//    };
//    rails[6][5] = new SwitchRail(new Direction("north"), new Direction("south"),
//        new Direction("west"), new GridLoc(6, 5)) {
//    };
//    rails[6][6] = new SwitchRail(new Direction("south"), new Direction("north"),
//        new Direction("east"), new GridLoc(6, 6)) {
//    };
//    rails[6][7] = new SwitchRail(new Direction("south"), new Direction("north"),
//        new Direction("west"), new GridLoc(6, 7)) {
//    };
// -----------------------------------------------------------------------------

    rails[MAGIC3][2] = new CrossRail(new GridLoc(MAGIC3, 2));
    connectRails(rails[MAGIC3][MAGIC3], rails[MAGIC3][2], new Direction("west"));
    connectRails(rails[2][2], rails[MAGIC3][2], new Direction("south"));

    rails[MAGIC4][2] = new SwitchRail(new Direction("west"), new Direction("east"),
        new Direction("north"), new GridLoc(MAGIC4, 2)) {
    };
    connectRails(rails[MAGIC3][2], rails[MAGIC4][2], new Direction("south"));
    connectRails(rails[MAGIC4][MAGIC3], rails[MAGIC4][2], new Direction("west"));

    rails[MAGIC4][1] = new CornerRail(new Direction("north"),
        new Direction("east"), new GridLoc(MAGIC4, 1)) {
    };
    connectRails(rails[MAGIC4][2], rails[MAGIC4][1], new Direction("west"));

    rails[MAGIC3][1] = new CornerRail(new Direction("south"),
        new Direction("east"), new GridLoc(MAGIC3, 1)) {
    };
    connectRails(rails[MAGIC3][2], rails[MAGIC3][1], new Direction("west"));
    connectRails(rails[MAGIC3][1], rails[MAGIC4][1], new Direction("south"));

    rails[2][1] = new StraightRail(new Direction("east"), new Direction("west"),
        new GridLoc(2, 1)) {
    };
    connectRails(rails[2][2], rails[2][1], new Direction("west"));

    rails[2][0] = new CornerRail(new Direction("north"),
        new Direction("east"), new GridLoc(2, 0)) {
    };
    connectRails(rails[2][1], rails[2][0], new Direction("west"));

    rails[1][0] = new StraightRail(new Direction("north"), new Direction("south"),
        new GridLoc(1, 0)) {
    };
    connectRails(rails[2][0], rails[1][0], new Direction("north"));
    connectRails(rails[1][0], rails[0][0], new Direction("north"));

    trackPanel.addToPanel(rails);
  }

}
