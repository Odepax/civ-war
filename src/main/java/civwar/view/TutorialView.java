package civwar.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import civwar.Application;

public class TutorialView extends JPanel {
	private static final long serialVersionUID = -1289931207286103170L;

	/**
	 * @param then The action to call after the tutorial is complete.
	 */
	public TutorialView(Runnable then) {
		super(new BorderLayout(20, 0));

		JLabel imageLabel = new JLabel();

		try {
			imageLabel.setIcon(new ImageIcon(Application.getInstance().getImageURL("tutorial/1-drag-your-troops-to-attack.png")));
		} catch (Exception e) {
			imageLabel.setText("TIP: Drag your troops from a building to another to attack.");
		}

		JPanel centerPanel = new JPanel(new GridBagLayout());

		centerPanel.add(imageLabel);

		JButton continueButton = new JButton("CONTINUE");

		continueButton.setForeground(Color.DARK_GRAY);
		continueButton.setBackground(Color.WHITE);
		continueButton.setFocusPainted(false);

		continueButton.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
			BorderFactory.createEmptyBorder(10, 10, 10, 10)
		));

		continueButton.addActionListener(new ActionListener() {
			// TODO: Use a CardLayout here...
			// -
			// This implementation is sufficient here because there are only 2 slides.
			// But if we need more in the future, this could be a better option than
			// producing spaghetti code.
			private static final int SLIDE_COUNT = 2;

			private int slide = 0;

			@Override
			public void actionPerformed(ActionEvent e) {
				++slide;

				if (slide == SLIDE_COUNT) {
					SwingUtilities.invokeLater(then);
				} else {
					imageLabel.setIcon(null);
					imageLabel.setText(null);

					try {
						imageLabel.setIcon(new ImageIcon(Application.getInstance().getImageURL("tutorial/2-drag-modules-to-buff.png")));
					} catch (Exception error) {
						imageLabel.setText("TIP: Drag a module from the shop to one of your buildings to apply its bonus.");
					}
				}
			}
		});

		JPanel southPanel = new JPanel();

		southPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		southPanel.add(continueButton);

		add(centerPanel, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
	}
}
