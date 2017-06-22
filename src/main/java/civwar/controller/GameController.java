package civwar.controller;

import java.awt.geom.Point2D;

import civwar.model.Building;
import civwar.model.Game;
import civwar.model.GameLoop;
import civwar.model.Module;

public class GameController {
	public GameController() {
		GameLoop.getInstance().start();
	}

	// TODO: Should I move this method to the civwar.model.Game class?
	private Building getBuildingByCollision(Point2D point) {
		for (Building building : Game.getInstance().getBuildings()) {
			if (
				   Math.abs(building.getPosition().getX() - point.getX()) <= 10
				&& Math.abs(building.getPosition().getY() - point.getY()) <= 10
			) {
				return building;
			}
		}

		return null;
	}

	/** @see Game#sendTroops(Building, Building) */
	public void sendTroops(Point2D source, Point2D target) {
		Building sourceBuilding = getBuildingByCollision(source);

		if (sourceBuilding == null) {
			return;
		}

		Building targetBuilding = getBuildingByCollision(target);

		if (targetBuilding == null) {
			return;
		}

		Game.getInstance().sendTroops(sourceBuilding, targetBuilding);
	}

	/** @see Game#applyModule(Module, Building) */
	public void applyModule(Module module, Point2D target) {
		Building targetBuilding = getBuildingByCollision(target);

		if (targetBuilding != null) {
			Game.getInstance().applyModule(module, targetBuilding);
		}
	}
}
