package civwar.model;



import java.awt.geom.Point2D;

public class BuildingFactory {
	private final static BuildingFactory singleton = new BuildingFactory();

	public static BuildingFactory getInstance() {
		return singleton;
	}

	/** @see Building */
	public Building createHouse(int x, int y, Army controllingArmy, int garrison) {
		return new Building(new Point2D.Double(x, y), controllingArmy, garrison, 10, 1);
	}

	/** @see Building */
	public Building createHouse(int x, int y) {
		return createHouse(x, y, Army.NEUTRAL, 5);
	}

	/** @see Building */
	public Building createHouse(int x, int y, Army controllingArmy) {
		return createHouse(x, y, controllingArmy, 5);
	}

	/** @see Building */
	public Building createHouse(int x, int y, int garrison) {
		return createHouse(x, y, Army.NEUTRAL, garrison);
	}

	/** @see Building */
	public Building createTower(int x, int y, Army controllingArmy, int garrison) {
		return new Building(new Point2D.Double(x, y), controllingArmy, garrison, 20, 1);
	}

	/** @see Building */
	public Building createTower(int x, int y) {
		return createTower(x, y, Army.NEUTRAL, 10);
	}

	/** @see Building */
	public Building createTower(int x, int y, Army controllingArmy) {
		return createTower(x, y, controllingArmy, 10);
	}

	/** @see Building */
	public Building createTower(int x, int y, int garrison) {
		return createTower(x, y, Army.NEUTRAL, garrison);
	}

	/** @see Building */
	public Building createCitadel(int x, int y, Army controllingArmy, int garrison) {
		return new Building(new Point2D.Double(x, y), controllingArmy, garrison, 60, 2);
	}

	/** @see Building */
	public Building createCitadel(int x, int y) {
		return createCitadel(x, y, Army.NEUTRAL, 30);
	}

	/** @see Building */
	public Building createCitadel(int x, int y, Army controllingArmy) {
		return createCitadel(x, y, controllingArmy, 30);
	}

	/** @see Building */
	public Building createCitadel(int x, int y, int garrison) {
		return createCitadel(x, y, Army.NEUTRAL, garrison);
	}
}
