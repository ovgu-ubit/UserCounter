package view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class UserPanel extends JPanel {

	public class UserLabel extends JLabel {

		private static final long serialVersionUID = 1L;

		public UserLabel() {
			super();
			this.setFont(new Font(this.getFont().getName(), Font.BOLD, 48));
		}
	}

	private static final long serialVersionUID = 1L;
	private UserLabel label_userCount = new UserLabel();

	public UserPanel() {
		super();
		this.setBorder(new LineBorder(Color.BLACK));
		this.add(this.label_userCount);
	}

	public void setText(String text) {
		this.label_userCount.setText(text);
	}
}
