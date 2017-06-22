package civwar.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import civwar.view.interfacing.Drawable;

/**
 * An attack troop is a moving bunch of units coming from a {@link Building building}
 * and heading to another. The latter, the <i>target</i> building, is meant to be attacked.
 *
 * @see Building#handleAttack(AttackTroop)
 * @see Building#detachTroopTo(Building) To create an attack troop.
 */
public class AttackTroop implements Drawable {
	private Building source;
	public synchronized Building getSource() { return source; }
	public synchronized void setSource(Building value) { source = value; }

	private Building target;
	public synchronized Building getTarget() { return target; }
	public synchronized void setTarget(Building value) { target = value; }

	private double sourceToTargetDistance;
	public synchronized double getSourceToTargetDistance() { return sourceToTargetDistance; }
	public synchronized void setSourceToTargetDistance(double value) { sourceToTargetDistance = value; }

	private double travelledDistance;
	public synchronized double getTravelledDistance() { return travelledDistance; }
	public synchronized void setTravelledDistance(double value) { travelledDistance = value; }

	/**
	 * The distance this troop can travel each time
	 * {@link #computeNextPosition()} is called.
	 *
	 * @see #computeNextPosition()
	 */
	private double speed;
	public synchronized double getSpeed() { return speed; }
	public synchronized void setSpeed(double value) { speed = value; }

	/** @see Building#handleAttack(AttackTroop) */
	private double attackPower;
	public synchronized double getAttackPower() { return attackPower; }
	public synchronized void setAttackPower(double value) { attackPower = value; }

	private int unitCount;
	public synchronized int getUnitCount() { return unitCount; }
	public synchronized void setUnitCount(int value) { unitCount = value; }

	private Point2D position;
	public synchronized Point2D getPosition() { return position; }
	public synchronized void setPosition(Point2D value) { position = value; }

	private Army controllingArmy;
	public synchronized Army getControllingArmy() { return controllingArmy; }
	public synchronized void setControllingArmy(Army value) { controllingArmy = value; }

	public AttackTroop(Building source, Building target, Army controllingArmy, double speed, int unitCount, double attackPower) {
		setSource(source);
		setTarget(target);
		setSpeed(speed);
		setUnitCount(unitCount);
		setAttackPower(attackPower);
		setControllingArmy(controllingArmy);

		setSourceToTargetDistance(Point2D.distance(
			getSource().getPosition().getX(), getSource().getPosition().getY(),
			getTarget().getPosition().getX(), getTarget().getPosition().getY())
		);

		setTravelledDistance(0);

		setPosition(new Point2D.Double(getSource().getPosition().getX(), getSource().getPosition().getY()));
	}

	/**
	 * Makes this troop move forward to it's {@link #target target} building.
	 *
	 * @see #position
	 * @see #speed
	 */
	public synchronized void computeNextPosition() {
		double directionAngle = Math.atan2(getTarget().getPosition().getY() - getSource().getPosition().getY(), getTarget().getPosition().getX() - getSource().getPosition().getX());

		setTravelledDistance(getSourceToTargetDistance() - Point2D.distance(
			getPosition().getX(), getPosition().getY(),
			getTarget().getPosition().getX(), getTarget().getPosition().getY()
		));

		getPosition().setLocation(
			getPosition().getX() + Math.cos(directionAngle) * getSpeed(),
			getPosition().getY() + Math.sin(directionAngle) * getSpeed()
		);
	}

	/** {@inheritDoc} */
	@Override
	public synchronized void draw(Graphics2D g) {
		g.setColor(getControllingArmy().getColor());
		g.fillOval((int)getPosition().getX() - 5, (int)getPosition().getY() - 5, 10, 10);

		g.setColor(Color.DARK_GRAY);
		g.drawOval((int)getPosition().getX() - 5, (int)getPosition().getY() - 5, 10, 10);

		g.drawString("" + getUnitCount(), (int)getPosition().getX() + 9, (int)getPosition().getY() + 4);
	}
}
