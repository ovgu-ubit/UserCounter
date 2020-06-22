package view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class ClockLabel extends JLabel {

	public class ClockListener implements ActionListener {

		private ClockLabel label;

		public ClockListener(ClockLabel label) {
			this.label = label;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			this.label.setCurrentTime();
		}
	}

	private static final long serialVersionUID = 1L;
	private Timer timer = new Timer(1000, new ClockListener(this));

	public ClockLabel() {
		super("", SwingConstants.CENTER);
		this.timer.start();
		this.setCurrentTime();
		this.setFont(new Font(this.getFont().getName(), Font.BOLD, 64));
	}

	public void setCurrentTime() {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		this.setText(format.format(new Date()));
	}
}
