import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Panel;

/**
 * The TrackPanel class which is extend from Panel.
 *
 * @author Make Zhang
 * @version J.R.E 1.8.0
 */
class TrackPanel extends Panel {
  // ------------------------------------------------------------------
  // The following items are used for double buffering.

  /**
   * The buffer in which to draw the image; used for double buffering.
   */
  private Image backBuffer;

  /**
   * The graphics context to use when double buffering.
   */
  private Graphics backG;
  /**
   * Number of 10.
   */
  private static final int MAGIC10 = 10;

  /**
   * Add r to Panel.
   *
   * @param r given rail
   */
  void addToPanel(final Rail[][] r) {

    setLayout(new GridLayout(r.length, r[0].length, 0, 0));

    for (Rail[] aR : r) {
      for (int col = 0; col < r[0].length; col++) {
        add("", aR[col]);
      }
    }
  }

  // paint
  // ------------------------------------------------------------------
  // Paint the display.

  /**
   * Paint the display.
   *
   * @param g graph
   */
  public void paint(final Graphics g) {
    update(g);
  }

  /**
   * Return new Insets.
   *
   * @return Insets
   */
  public Insets getInsets() {
    return new Insets(MAGIC10, MAGIC10, MAGIC10, MAGIC10);
  }

  // update
  // ------------------------------------------------------------------
  // Update the display; tell all my Tracks to update themselves.

  /**
   * Update the display; tell all my Tracks to update themselves.
   *
   * @param g graph
   */
  public void update(final Graphics g) {

    // Get my width and height.
    int w = getBounds().width;
    int h = getBounds().height;

    // If we don't yet have an Image, create one.
    if (backBuffer == null
        || backBuffer.getWidth(null) != w
        || backBuffer.getHeight(null) != h) {
      backBuffer = createImage(w, h);
      if (backBuffer != null) {

        // If we have a backG, it belonged to an old Image.
        // Get rid of it.
        if (backG != null) {
          backG.dispose();
        }
        backG = backBuffer.getGraphics();
      }
    }

    if (backBuffer != null) {

      // Fill in the Graphics context backG.
      g.setColor(Color.white);
      g.fillRect(0, 0, w, h);

      // Now copy the new image to g.
      // g.drawImage(backBuffer, 0, 0, null);
    }

  }
}

