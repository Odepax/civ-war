package civwar;

import java.awt.Dimension;

import javax.swing.JFrame;

import civwar.controller.GameController;
import civwar.view.GameView;
import civwar.view.TutorialView;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = -3880026026104218593L;

	public MainWindow() {
		setTitle("Civ War Java");
		setMinimumSize(new Dimension(320, 280));
		setPreferredSize(new Dimension(600, 520));

		setContentPane(new TutorialView(this::launchGame));
	}

	/**
	 * Changes the content of the window ( from splash screen tutorial to game view ) and
	 * starts the game loop.
	 */
	public void launchGame() {
		setContentPane(new GameView(new GameController()));

		revalidate();
		repaint();
	}
}
