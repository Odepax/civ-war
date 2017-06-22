package civwar.model;

import javax.swing.Timer;

/**
 * The game loop is a top level entity of this program.
 * Its role includes the following periodical tasks:
 *
 * - It's the game loop that periodically triggers the view refreshing;
 * - It makes the buildings' garrison regenerate;
 * - It makes the troops move and apply collisions with the buildings
 *   to simulate the arrival of the troops and the attacks.
 */
public final class GameLoop {
	private final static GameLoop singleton = new GameLoop();

	public static GameLoop getInstance() {
		return singleton;
	}

	private long loopCount;
	private Timer loopTimer;

	private GameLoop() {
		loopCount = 0;

		loopTimer = new Timer(40, (e) -> {
			// TODO: Time dilation factor?

			if (++loopCount % 50 == 0) {
				Game.getInstance().regenerateTroops(); // Every 2 seconds.
			}

			Game.getInstance().makeAttackTroopsMove(); // 25 FPS.
			Game.getInstance().notifyObservers();
			// NOTE: `if ([...].hasChanged())` is automatic.
		});
	}

	/** Starts the game loop. */
	public void start() {
		if (!loopTimer.isRunning()) {
			if (loopCount == 0) {
				loopTimer.start();
			} else {
				loopTimer.restart();
			}
		}
	}

	/** Stops the game loop, suspending its periodical activities. */
	public void stop() {
		if (loopTimer.isRunning()) {
			loopTimer.stop();
		}
	}
}
