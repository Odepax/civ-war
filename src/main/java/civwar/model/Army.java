package civwar.model;

import java.awt.Color;

/**
 * An army is meant to be an entity that possesses {@link Building buildings} and
 * {@link AttackTroop troops} on the battlefield.
 *
 * @version 0.1
 */
public class Army {
	/**
	 * Hold a reference to the default neutral army.
	 *
	 * @see #neutral
	 */
	public static final Army NEUTRAL = new Army("Neutral", Color.LIGHT_GRAY, true);

	private Color color;
	public synchronized Color getColor() { return color; }
	public synchronized void setColor(Color value) { color = value; }

	private String name;
	public synchronized String getName() { return name; }
	public synchronized void setName(String value) { name = value; }

	/**
	 * Indicates if this army is neutral. It influences the behavior
	 * of other parts of the business logic.
	 *
	 * @see Building#regenerateTroops() Building's garrison does not regenerate
	 * if this building belongs to the neutral army.
	 */
	private boolean neutral;
	public synchronized boolean isNeutral() { return neutral; }
	public synchronized void setNeutral(boolean value) { neutral = value; }

	/**
	 * Shortcut for {@code new Army(name, color, false)}.
	 *
	 * @see #Army(String, Color, boolean)
	 */
	public Army(String name, Color color) {
		this(name, color, false);
	}

	public Army(String name, Color color, boolean neutral) {
		setName(name);
		setColor(color);
		setNeutral(neutral);
	}
}
