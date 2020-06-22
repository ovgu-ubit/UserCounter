package view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.border.LineBorder;

public class UserButton extends JButton {

	private static final long serialVersionUID = 1L;

	public UserButton(String text) {
		super(text);
		this.setFocusable(false);
		this.setBorder(new LineBorder(Color.BLACK));
		this.setFont(new Font(this.getFont().getName(), Font.BOLD, 48));
		this.setBackground(Color.GRAY);
		this.setEnabled(false);
		this.setFocusPainted(false);
	}
}
