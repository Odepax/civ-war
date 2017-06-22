package civwar.view;

import java.awt.Dimension;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import civwar.model.Game;
import civwar.model.Module;

class ShopView extends JPanel {
	private static final long serialVersionUID = -4362441644002092272L;

	ShopView(MouseListener moduleApplicationListener) {
		super();

		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		setMinimumSize(new Dimension(200, 80));
		setPreferredSize(new Dimension(200, 80));

		add(Box.createHorizontalGlue());

		for (Module module : Game.getInstance().getModules()) {
			ModuleView moduleButton = new ModuleView(module);

			moduleButton.addMouseListener(moduleApplicationListener);

			add(moduleButton);
		}

		add(Box.createHorizontalGlue());
	}
}
