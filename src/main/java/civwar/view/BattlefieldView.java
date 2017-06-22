package civwar.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import civwar.controller.GameController;
import civwar.model.AttackTroop;
import civwar.model.Building;
import civwar.model.Game;

class BattlefieldView extends JPanel {
	private static final long serialVersionUID = -2483480949642279052L;

	BattlefieldView(GameController gameController) {
		super();

		setMinimumSize(new Dimension(420, 400));
		setPreferredSize(new Dimension(420, 400));
		setBorder(BorderFactory.createDashedBorder(Color.LIGHT_GRAY, 1, 3, 2, true));

		addMouseListener(new MouseAdapter() {
			private Point2D source;

			@Override
			public void mousePressed(MouseEvent e) {
				if (source == null) {
					source = new Point2D.Double(e.getX(), e.getY());
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (source != null) {
					Point2D target = new Point2D.Double(e.getX(), e.getY());

					gameController.sendTroops(source, target);
				}

				source = null;
			}
		});

		Game.getInstance().addObserver((source, arg) -> {
			repaint();
		});
	}

	@Override
	protected void paintComponent(Graphics gr) {
		super.paintComponent(gr);

		Graphics2D g = (Graphics2D)gr;

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g.setStroke(new BasicStroke(2));
		g.setFont(new Font("Ubuntu", Font.BOLD, 12));
		// TODO: Maybe move those two lines above to Building.draw() and AttackTroop.draw() ?
		// -
		// Today, they're fine, but the render logic could be changed in the future to improve UX;
		// in that case, they should be moved... In the future...

		for (Building building : Game.getInstance().getBuildings()) {
			building.draw(g);
		}

		for (AttackTroop troop: Game.getInstance().getAttackTroops()) {
			troop.draw(g);
		}
	}
}
