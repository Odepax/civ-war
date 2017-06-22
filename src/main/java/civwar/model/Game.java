package civwar.model;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Vector;

/**
 * This object is a singleton facade usd to manipulate the other objects of
 * the model layer. The methods it exposes implement the features of the game,
 * and are meant to be used by the {@link GameLoop game loop ( as a system )} and
 * the {@link civwar.controller.GameController view controllers ( representatives
 * of the end user )}.
 */
public final class Game extends Observable {
	private final static Game singleton = new Game();

	public static Game getInstance() {
		return singleton;
	}

	private List<Building> buildings;
	public List<Building> getBuildings() { return buildings; }
	private void addBuilding(Building value) { buildings.add(value); }

	private List<AttackTroop> attackTroops;
	public List<AttackTroop> getAttackTroops() { return attackTroops; }
	private void addAttackTroop(AttackTroop value) { attackTroops.add(value); }

	private List<Module> modules;
	public List<Module> getModules() { return modules; }
	private void addModule(Module value) { modules.add(value); }

	private Game() {
		// TODO: Find another way to instantiate modules...
		// -
		// Through files. It's planned to be done later, with map editor...
		buildings = new Vector<>();
		attackTroops = new Vector<>();
		modules = new Vector<>();

		addModule(new Module("Self Defense", "Decreases the power of the suffered attacks.") {
			@Override
			public void modifyIncomingTroop(AttackTroop troop) {
				troop.setAttackPower(troop.getAttackPower() - 0.2);
			}
		});

		addModule(new Module("Rage", "Increases the attack power of the garrison.") {
			@Override
			public void modifyOutgoingTroop(AttackTroop troop) {
				troop.setAttackPower(troop.getAttackPower() + 0.2);
			}
		});

		addModule(new Module("Arriba Arriba", "Increases the speed of the outgoing troops.") {
			@Override
			public void modifyOutgoingTroop(AttackTroop troop) {
				troop.setSpeed(troop.getSpeed() + 0.2);
			}
		});

		addModule(new Module("Guest Room", "Improves troops capacity.") {
			@Override
			public void modifyBuilding(Building building) {
				building.setGarrisonCapacity(building.getGarrisonCapacity() + 10);
			}
		});

		addModule(new Module("Baby Boom", "Improves troops regeneration.") {
			@Override
			public void modifyBuilding(Building building) {
				building.setGarrisonRegenerationRate(building.getGarrisonRegenerationRate() + 1);
			}
		});

		addModule(new Module("Group Work", "The more units in an attack troop, the greater its attack power.") {
			@Override
			public void modifyOutgoingTroop(AttackTroop troop) {
				troop.setAttackPower(troop.getAttackPower() + Math.log(troop.getUnitCount()) / 4);
			}
		});

		reset();
	}

	public void reset() {
		getBuildings().clear();
		getAttackTroops().clear();

		// TODO: Get rid of this big initialization phase, somehow, at the end.
		// -
		// Hum... Maybe when there will be a map editor, and a way to read the map data from a file...
		// Whose format would be to determine.
		Army blueArmy = new Army("Blues", Color.CYAN);
		Army redArmy = new Army("Reds", Color.ORANGE);

		addBuilding(BuildingFactory.getInstance().createTower(040, 200, blueArmy));
		addBuilding(BuildingFactory.getInstance().createTower(360, 200, redArmy));

		addBuilding(BuildingFactory.getInstance().createCitadel(200, 160));
		addBuilding(BuildingFactory.getInstance().createCitadel(200, 240));

		addBuilding(BuildingFactory.getInstance().createHouse(160, 040));
		addBuilding(BuildingFactory.getInstance().createHouse(240, 040));
		addBuilding(BuildingFactory.getInstance().createHouse(160, 360));
		addBuilding(BuildingFactory.getInstance().createHouse(240, 360));
		// NOTE: End of "this big initialization phase".

		setChanged();
	}

	/** @see Building#regenerateTroops() */
	void regenerateTroops() {
		for (Building building : getBuildings()) {
			building.regenerateTroops();
		}

		setChanged();
	}

	/**
	 * Makes the attack troops deployed on the battlefield move. Also applies the collisions
	 * with buildings to simulate the arrival of troops to their target.
	 *
	 * @see AttackTroop#computeNextPosition()
	 * @see Building#handleAttack(AttackTroop)
	 */
	void makeAttackTroopsMove() {
		if (getAttackTroops().size() != 0) {
			for (Iterator<AttackTroop> i = getAttackTroops().iterator(); i.hasNext(); ) {
				AttackTroop troops = i.next();

				troops.computeNextPosition();

				if (troops.getTravelledDistance() >= troops.getSourceToTargetDistance() - 10) {
					troops.getTarget().handleAttack(troops);

					i.remove();
				}
			}

			setChanged();
		}
	}

	/** @see Building#detachTroopTo(Building) */
	public void sendTroops(Building source, Building target) {
		AttackTroop troop = source.detachTroopTo(target);

		if (troop != null) {
			addAttackTroop(troop);

			setChanged();
		}
	}

	public void applyModule(Module module, Building target) {
		target.addModule(module);

		setChanged();
	}
}
