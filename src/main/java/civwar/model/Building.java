package civwar.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import java.util.List;
import java.util.Vector;

import civwar.view.interfacing.Drawable;

/**
 * Buildings are a central element of the game. They can be taken by the
 * {@link AttackTroop troops} of an {@link Army army}, and sent troops
 * to conquer other buildings.
 *
 * @version 0.1
 */
public class Building implements Drawable {
	private Point2D position;
	public synchronized Point2D getPosition() { return position; }
	public synchronized void setPosition(Point2D value) { position = value; }

	private int garrison;
	public synchronized int getGarrison() { return garrison; }
	public synchronized void setGarrison(int value) { garrison = value; }

	/**
	 * @see #garrisonCapacity
	 * @see #resetModules()
	 */
	private final int initialGarrisonCapacity;

	private int garrisonCapacity;
	public synchronized int getGarrisonCapacity() { return garrisonCapacity; }
	public synchronized void setGarrisonCapacity(int value) { garrisonCapacity = value; }

	/**
	 * @see #garrisonRegenerationRate
	 * @see #resetModules()
	 */
	private final int initialGarrisonRegenerationRate;

	private int garrisonRegenerationRate;
	public synchronized int getGarrisonRegenerationRate() { return garrisonRegenerationRate; }
	public synchronized void setGarrisonRegenerationRate(int value) { garrisonRegenerationRate = value; }

	private Army controllingArmy;
	public synchronized Army getControllingArmy() { return controllingArmy; }
	public synchronized void setControllingArmy(Army value) { controllingArmy = value; }

	private List<Module> modules;
	public List<Module> getModules() { return modules; }

	/**
	 * @see Module#modifyBuilding(Building)
	 */
	public void addModule(Module value) {
		value.modifyBuilding(this);

		modules.add(value);
	}

	/**
	 * Clears the module set of this building and resets its characteristics to
	 * their initial values.
	 *
	 * @see #initialGarrisonCapacity
	 * @see #initialGarrisonRegenerationRate
	 */
	public void resetModules() {
		modules.clear();

		setGarrisonCapacity(initialGarrisonCapacity);
		setGarrisonRegenerationRate(initialGarrisonRegenerationRate);
	}

	public Building(Point2D position, Army controllingArmy, int garrison, int garrisonCapacity, int garrisonRegenerationRate) {
		modules = new Vector<>();

		setPosition(position);
		setGarrison(garrison);
		setGarrisonCapacity(garrisonCapacity);
		setGarrisonRegenerationRate(garrisonRegenerationRate);
		setControllingArmy(controllingArmy);

		initialGarrisonRegenerationRate = garrisonRegenerationRate;
		initialGarrisonCapacity = garrisonCapacity;
	}

	/** @see Module#modifyOutgoingTroop(AttackTroop) */
	private void applyModulesOnOutgoingTroops(AttackTroop troop) {
		for (Module module : getModules()) {
			module.modifyOutgoingTroop(troop);
		}
	}

	/** @see Module#modifyIncomingTroop(AttackTroop) */
	private void applyModulesOnIncomingTroops(AttackTroop troop) {
		for (Module module : getModules()) {
			module.modifyIncomingTroop(troop);
		}
	}

	/**
	 * Split the garrison into two parts: one half remains inside
	 * this building whereas the rest is returned on the form of
	 * an {@link AttackTroop} object.
	 *
	 * @return An {@link AttackTroop} object on which this building's
	 * modules have already been applied on, or {@code null} if the
	 * garrison is not big enough to be split or if the target
	 * building is actually this building.
	 *
	 * @see #applyModulesOnOutgoingTroops(AttackTroop)
	 */
	public synchronized AttackTroop detachTroopTo(Building target) {
		if (target != this && getGarrison() > 1) {
			AttackTroop troops = new AttackTroop(this, target, getControllingArmy(), 1, getGarrison() / 2, 1);

			applyModulesOnOutgoingTroops(troops);

			setGarrison(getGarrison() - troops.getUnitCount());

			return troops; 
		}

		return null;
	}

	/**
	 * Applies this building's modules effects on the attacking troop
	 * then starts a battle between this building's garrison and the
	 * attacking troop. The survivors will keep the building on behalf
	 * of their {@link Army amry}.
	 * <p>
	 * If this building changes hands, all its modules are reset.
	 * <p>
	 * If this building and the attackers belong to the same army, this
	 * building's garrison is reinforced by the number of units of the
	 * incoming troop.
	 *
	 * @param troop The attackers.
	 *
	 * @see #applyModulesOnIncomingTroops(AttackTroop)
	 * @see #resetModules()
	 */
	public synchronized void handleAttack(AttackTroop troop) {
		if (controllingArmy == troop.getControllingArmy()) {
			setGarrison(getGarrison() + troop.getUnitCount());
		} else {
			applyModulesOnIncomingTroops(troop);

			setGarrison((int)(getGarrison() - troop.getUnitCount() * troop.getAttackPower()));

			if (getGarrison() < 0) {
				setControllingArmy(troop.getControllingArmy());

				resetModules();

				setGarrison(Math.abs(getGarrison()));
			}
		}
	}

	/**
	 * Makes this building generate units.
	 * <p>
	 * <b>If this building belongs to a {@link Army#neutral neutral army},
	 * the garrison won't regenerate.</b>
	 *
	 * @see #garrisonRegenerationRate
	 * @see #garrisonCapacity
	 */
	public synchronized void regenerateTroops() {
		if (getGarrison() < getGarrisonCapacity() && !getControllingArmy().isNeutral()) {
			setGarrison(getGarrison() + getGarrisonRegenerationRate());
		} else if (getGarrison() > getGarrisonCapacity()) {
			setGarrison(getGarrison() - (int)Math.max(1, Math.log((getGarrison() - getGarrisonCapacity() + 1) * 3)));
		}
	}

	/** {@inheritDoc} */
	@Override
	public void draw(Graphics2D g) {
		g.setColor(getControllingArmy().getColor());
		g.fillRect((int)getPosition().getX() - 10, (int)getPosition().getY() - 10, 20, 20);

		g.setColor(Color.DARK_GRAY);
		g.drawRect((int)getPosition().getX() - 10, (int)getPosition().getY() - 10, 20, 20);

		g.drawString(getGarrison() + "/" + getGarrisonCapacity(), (int)getPosition().getX() + 14, (int)getPosition().getY() + 4);
	}
}
