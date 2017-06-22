package civwar.view;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import civwar.Application;
import civwar.model.Module;

class ModuleView extends JPanel {
	private static final long serialVersionUID = 4465448906497923226L;

	private Module module;
	public Module getModule() { return module; }
	private void setModule(Module value) { module = value; }

	ModuleView(Module module) {
		setModule(module);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(120, 0));
		setBorder(BorderFactory.createEmptyBorder(0, 5, 20, 5));

		// Module icon.
		JLabel imageLabel = new JLabel();

		try {
			imageLabel.setIcon(new ImageIcon(Application.getInstance().getImageURL(module.getDepictingIconPath())));
		} catch (Exception e) {
			imageLabel.setText("No image.");
		}

		imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(imageLabel);

		// Module name.
		JLabel moduleNameLabel = new JLabel(module.getName().toUpperCase());

		moduleNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(moduleNameLabel);
	}
}
