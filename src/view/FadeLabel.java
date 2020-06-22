package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.Timer;

public class FadeLabel extends JLabel {

	public class FadeListener implements ActionListener {

		private FadeLabel fadeLabel;
		private double alpha;

		public FadeListener(FadeLabel fadeLabel) {
			this.fadeLabel = fadeLabel;
			this.init(false, null);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			double stepSize = 1.0 / this.fadeLabel.fadeStepSize;
			this.setAlpha(this.getAlpha() - stepSize);
			if (this.getAlpha() < stepSize) {
				this.fadeLabel.setText("", false);
			} else {
				this.fadeLabel.setForegroundAlpha(this.getAlpha());
			}
		}

		private void init(boolean fadeout, Color color) {
			this.setAlpha(1.0);
			if (color != null) {
				this.fadeLabel.setForeground(color);
				this.fadeLabel.setForegroundAlpha(this.getAlpha());
			}
			if (this.fadeLabel.timer != null) {
				if (fadeout) {
					this.fadeLabel.timer.restart();
				} else {
					this.fadeLabel.timer.stop();
				}
			}
		}

		private void setAlpha(double alpha) {
			this.alpha = alpha;
		}

		private double getAlpha() {
			return this.alpha;
		}
	}

	private static final long serialVersionUID = 1L;
	private Timer timer;
	private FadeListener fadeListener = new FadeListener(this);
	private int fadeDuration = 2000;
	private int fadeStepSize = 50;
	private Color defaultColor = Color.RED;

	public FadeLabel(int horizontalAlignment, int timeout) {
		super("", horizontalAlignment);
		this.setAlignmentX(CENTER_ALIGNMENT);
		this.setAlignmentY(CENTER_ALIGNMENT);
		this.timer = new Timer(timeout, this.fadeListener);
		this.timer.setDelay(this.fadeDuration / this.fadeStepSize);
	}

	@Override
	public void setText(String text) {
		this.setText(text, true);
	}

	public void setText(String text, Color color) {
		this.setText(text, true, color);
	}

	public void setText(String text, boolean fadeout) {
		this.setText(text, fadeout, this.defaultColor);
	}

	public void setText(String text, boolean fadeout, Color color) {
		super.setText(text);
		if (this.fadeListener != null) {
			this.fadeListener.init(fadeout, color);
		}
	}

	public void setTimeout(int timeout) {
		this.timer.setInitialDelay(timeout);
	}

	private void setForegroundAlpha(double alpha) {
		Color color = this.getForeground();
		this.setForeground(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) Math.round(alpha * 255)));
	}
}
