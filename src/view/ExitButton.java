package view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.border.LineBorder;

public class ExitButton extends JButton {

	private static final long serialVersionUID = 1L;

	public ExitButton(String text) {
		super(text);
		this.setFocusable(false);
		this.setBorder(new LineBorder(Color.BLACK));
		this.setFont(new Font(this.getFont().getName(), Font.BOLD, 24));
		this.setBackground(Color.GRAY);
		this.setFocusPainted(false);
	}
}
