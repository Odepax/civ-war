package civwar.view;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

import civwar.controller.GameController;
import civwar.model.Module;

public class GameView extends JPanel {
	private static final long serialVersionUID = 4375482311151482280L;

	public GameView(GameController gameController) {
		super(new BorderLayout());

		MouseListener moduleApplicationListener = new MouseAdapter() {
			private Module module;
			private Object currentView;

			@Override
			public void mouseEntered(MouseEvent e) {
				currentView = e.getSource();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (currentView instanceof ModuleView && module == null) {
					module = ((ModuleView)currentView).getModule();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (currentView instanceof BattlefieldView && module != null) {
					Point battlefieldViewLocation = ((BattlefieldView)currentView).getLocationOnScreen();

					gameController.applyModule(module, new Point2D.Double(
						e.getXOnScreen() - battlefieldViewLocation.getX(),
						e.getYOnScreen() - battlefieldViewLocation.getY())
					);
				}

				module = null;
			}
		};

		BattlefieldView battlefieldView = new BattlefieldView(gameController);

		battlefieldView.addMouseListener(moduleApplicationListener);

		JPanel battlefieldWrapper = new JPanel(new GridBagLayout());

		battlefieldWrapper.add(battlefieldView);

		add(battlefieldWrapper, BorderLayout.CENTER);
		add(new ShopView(moduleApplicationListener), BorderLayout.SOUTH);
	}
}
