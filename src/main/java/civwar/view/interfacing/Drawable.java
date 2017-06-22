package civwar.view.interfacing;

import java.awt.Graphics2D;

/** Represents an entity that can be drawn on a canvas. */
public interface Drawable {
	/**
	 * Draws this object on a canvas.
	 *
	 * @param g The graphic context of the canvas this object has to be drawn on.
	 */
	public void draw(Graphics2D g);
}
