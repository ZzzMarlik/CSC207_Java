// The (x,y) location on the Track.

import java.awt.Point;

/**
 * The GridLoc class extend from Point.
 *
 * @author Make Zhang
 * @version J.R.E 1.8.0
 */
public final class GridLoc extends Point {

  /**
   * row coordinate.
   */
  private int row;
  /**
   * column coordinate.
   */
  private int col;

  /**
   * Set row.
   *
   * @param newRow Given row
   */
  void setRow(final int newRow) {
    this.row = newRow;
  }

  /**
   * Set column.
   *
   * @param newCol Given column
   */
  void setCol(final int newCol) {
    this.col = newCol;
  }

  /**
   * Get Row.
   *
   * @return row
   */
  int getRow() {
    return row;
  }

  /**
   * Get Column.
   *
   * @return col
   */
  int getCol() {
    return col;
  }

  /**
   * A new GridLoc with row and column coordinate.
   *
   * @param r row coordinate
   * @param c column coordinate
   */
  public GridLoc(final int r, final int c) {
    row = r;
    col = c;
  }

  /**
   * toString method.
   *
   * @return @see java.lang.Object#toString()
   */
  public String toString() {
    return (row + " " + col);
  }

}

