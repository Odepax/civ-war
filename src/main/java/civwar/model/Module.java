package civwar.model;

import civwar.view.interfacing.Depictable;

/**
 * A module represents an improvement given to a {@link Building building}. A module
 * improves the building itself, as well as the troops that come from and to it.
 */
public abstract class Module implements Depictable {
	private String name;
	public synchronized String getName() { return name; }
	private synchronized void setName(String value) { name = value; }

	private String description;
	public synchronized String getDescription() { return description; }
	private synchronized void setDescription(String value) { description = value; }

	public Module(String name, String description) {
		setName(name);
		setDescription(description);
	}

	/**
	 * This method is called each time this module is applied to an <b>enemy</b> attack troop.
	 *
	 * @param troop The troop this module is applied on.
	 */
	public void modifyIncomingTroop(AttackTroop troop) {}

	/**
	 * This method is called each time this module is applied to an <b>ally</b> attack troop.
	 *
	 * @param troop The troop this module is applied on.
	 */
	public void modifyOutgoingTroop(AttackTroop troop) {}

	/**
	 * This method is called each time this module is applied on a building.
	 *
	 * @param building The building this module is applied on.
	 */
	public void modifyBuilding(Building building) {}

	/** {@inheritDoc} */
	@Override
	public String getDepictingIconPath() {
		return "module/" + name.toLowerCase().replace(' ', '-') + ".png";
	}
}
