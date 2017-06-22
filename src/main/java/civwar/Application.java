package civwar;

import java.net.URL;

import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Main class. It just initializes and launches the GUI and the game loop,
 * feeding them with the view-model.
 */
public final class Application {
	private final static Application singleton = new Application();

	public static Application getInstance() {
		return singleton;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			Application.getInstance().start();
		});
	}

	private MainWindow mainWindow;

	private Application() {}

	/** Initializes and displays the GUI civwar window. */
	private void start() {
		if (mainWindow == null) {
			mainWindow = new MainWindow();

			mainWindow.setSize(600, 500);
			mainWindow.setLocationRelativeTo(null);
			mainWindow.setVisible(true);
			mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		}
	}

	/**
	 * Used to load images from the disk.
	 *
	 * @param imagePath The image name to load.
	 *
	 * @return The URL pointing to the desired resource.
	 */
	public URL getImageURL(String imagePath) {
		return getClass().getResource("/res/image/" + imagePath);
	}
}
