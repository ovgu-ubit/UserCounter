package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.time.LocalDateTime;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import controller.CSV_Engine;
import model.Counter;

public class Screen extends JFrame {

	public class ScreenListener implements WindowListener {

		private CSV_Engine csvEngine;

		public ScreenListener(CSV_Engine csvEngine) {
			this.csvEngine = csvEngine;
		}

		@Override
		public void windowActivated(WindowEvent e) {
		}

		@Override
		public void windowClosed(WindowEvent e) {
		}

		@Override
		public void windowClosing(WindowEvent e) {
			this.csvEngine.close();
			e.getWindow().dispose();
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
		}

		@Override
		public void windowIconified(WindowEvent e) {
		}

		@Override
		public void windowOpened(WindowEvent e) {
		}
	}

	public class ButtonListener implements ActionListener {

		protected Screen screen;
		protected CSV_Engine csvEngine;
		protected Counter counter;

		public ButtonListener(Screen screen, CSV_Engine csvEngine, Counter counter) {
			this.screen = screen;
			this.csvEngine = csvEngine;
			this.counter = counter;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			this.screen.label_info.setText("", false);
			this.screen.field_barcode.setEditable(true);
			this.screen.field_barcode.setText("");
			this.screen.field_barcode.requestFocusInWindow();
			this.screen.button_barcode.setEnabled(false);
			this.screen.button_barcode.setBackground(Color.GRAY);
			this.screen.button_in.setEnabled(false);
			this.screen.button_in.setBackground(Color.GRAY);
			this.screen.button_out.setEnabled(false);
			this.screen.button_out.setBackground(Color.GRAY);
			this.screen.checkUserCount(this.counter);
		}
	}

	public class BarcodeFieldListener implements KeyListener {

		private Screen screen;
		private Counter counter;

		public BarcodeFieldListener(Screen screen, Counter counter) {
			this.screen = screen;
			this.counter = counter;
		}

		@Override
		public void keyPressed(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
			String input = this.screen.field_barcode.getText().trim().toUpperCase();
			this.screen.field_barcode.setText(input);
			if (e.getKeyCode() == 10) {
				if (input.matches(this.counter.getBarcode_regex())) {
					this.screen.barcode = input;
					if (this.counter.isUserIn(input)) {
						this.screen.label_info.setText("<html>Info:<br />Nutzer ist bereits eingebucht.</html>", true,
								Color.ORANGE);
					} else {
						if (this.counter.isUserRegistered(input)) {
							this.screen.label_info.setText("", false);
						} else {
							this.screen.label_info.setText(
									"<html>Achtung:<br />Nutzer muss sich an der<br />Ausleihtheke melden.</html>",
									false);
						}
					}
					if (this.counter.getCurrentUsers() < this.counter.getMaximumUsers()) {
						this.screen.button_barcode.setEnabled(true);
						this.screen.button_barcode.setBackground(Color.LIGHT_GRAY);
						this.screen.button_in.setEnabled(true);
						this.screen.button_in.setBackground(Color.CYAN);
					}
					if (this.counter.getCurrentUsers() > 0) {
						this.screen.button_barcode.setEnabled(true);
						this.screen.button_barcode.setBackground(Color.LIGHT_GRAY);
						this.screen.button_out.setEnabled(true);
						this.screen.button_out.setBackground(Color.YELLOW);
					}
					this.screen.field_barcode.setEditable(false);
				} else {
					this.screen.label_info.setText("<html>Achtung:<br />Kein gültiger Barcode!</html>");
					this.screen.field_barcode.setText("");
					this.screen.field_barcode.requestFocusInWindow();
				}
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}
	}

	public class ExitButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}

	public class XButtonListener extends ButtonListener {

		public XButtonListener(Screen screen, CSV_Engine csvEngine, Counter counter) {
			super(screen, csvEngine, counter);
		}
	}

	public class InButtonListener extends ButtonListener {

		public InButtonListener(Screen screen, CSV_Engine csvEngine, Counter counter) {
			super(screen, csvEngine, counter);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// Model activities:
			this.counter.userIn(this.csvEngine.getDateState().format(LocalDateTime.now()), this.screen.barcode);
			this.counter.userRegister(this.screen.barcode);
			// Controller activities:
			this.csvEngine.writeCSV(this.counter);
			// View activities:
			super.actionPerformed(e);
		}
	}

	public class OutButtonListener extends ButtonListener {

		public OutButtonListener(Screen screen, CSV_Engine csvEngine, Counter counter) {
			super(screen, csvEngine, counter);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// Model activities:
			this.counter.userOut(this.csvEngine.getDateState().format(LocalDateTime.now()), this.screen.barcode);
			// Controller activities:
			this.csvEngine.writeCSV(this.counter);
			// View activities:
			super.actionPerformed(e);
		}
	}

	private static final long serialVersionUID = 1L;
	private static final int fade_timeout = 10000;
	private String barcode;

	private JLabel label_barcode = new JLabel("Barcode:");
	private FadeLabel label_info = new FadeLabel(SwingConstants.CENTER, Screen.fade_timeout);
	private ClockLabel label_clock = new ClockLabel();
	private JLabel label_userCount = new JLabel("Anzahl Nutzer (Maximale Anzahl):", SwingConstants.CENTER);
	private JLabel label_version = new JLabel(CSV_Engine.version, SwingConstants.RIGHT);
	private JTextField field_barcode = new JTextField();
	private ExitButton button_barcode = new ExitButton("X");
	private UserButton button_in = new UserButton("Rein");
	private UserButton button_out = new UserButton("Raus");
	private ExitButton button_exit = new ExitButton("X");
	private UserPanel panel_userCount = new UserPanel();

	public Screen(CSV_Engine csvEngine, Counter counter) {
		super("Fullscreen");

		this.getContentPane().setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setUndecorated(true);
		this.setResizable(false);
		this.setLayout(null);
		this.pack();

		this.initComponents(csvEngine, counter);
		csvEngine.initScreen(this);
		this.addWindowListener(new ScreenListener(csvEngine));
	}

	public void init(int timeout) {
		this.label_info.setTimeout(timeout);
	}

	// Initialize and add all components to main panel:
	private void initComponents(CSV_Engine csvEngine, Counter counter) {
		int screenWidth = this.getWidth();
		int screenHeight = this.getHeight();
		int buttonWidth = (screenWidth / 2) - 400;
		int buttonHeight = screenHeight - 400;

		this.label_barcode.setBounds(((screenWidth - 200) / 2) - 60, 100, 60, 40);
		this.label_info.setBounds((screenWidth - 400) / 2, 120, 400, 160);
		this.label_info.setFont(new Font(this.getFont().getName(), Font.BOLD, 24));
		this.label_clock.setBounds((screenWidth - 320) / 2, 360, 320, 80);
		this.label_userCount.setBounds(((screenWidth - 320) / 2), screenHeight - 420, 320, 40);
		this.label_userCount.setFont(new Font(this.getFont().getName(), Font.BOLD, 18));
		this.label_version.setBounds(screenWidth - 100, screenHeight - 40, 90, 40);
		this.label_version.setFont(new Font(this.getFont().getName(), Font.BOLD, 18));
		this.field_barcode.setBounds((screenWidth - 200) / 2, 100, 200, 40);
		this.field_barcode.addKeyListener(new BarcodeFieldListener(this, counter));
		this.button_barcode.setBounds(((screenWidth - 200) / 2) + 200, 100, 50, 40);
		this.button_barcode.setEnabled(false);
		this.button_barcode.addActionListener(new XButtonListener(this, csvEngine, counter));
		this.button_in.setBounds(((screenWidth - buttonWidth) / 2) - buttonWidth, 100, buttonWidth, buttonHeight);
		this.button_in.addActionListener(new InButtonListener(this, csvEngine, counter));
		this.button_out.setBounds(((screenWidth - buttonWidth) / 2) + buttonWidth, 100, buttonWidth, buttonHeight);
		this.button_out.addActionListener(new OutButtonListener(this, csvEngine, counter));
		this.button_exit.setBounds(screenWidth - 50, 0, 50, 40);
		this.button_exit.addActionListener(new ExitButtonListener());
		this.panel_userCount.setBounds(((screenWidth - 320) / 2), screenHeight - 380, 320, 80);
		this.checkUserCount(counter);

		this.add(this.label_barcode);
		this.add(this.label_info);
		this.add(this.label_clock);
		this.add(this.label_userCount);
		this.add(this.label_version);
		this.add(this.field_barcode);
		this.add(this.button_barcode);
		this.add(this.button_in);
		this.add(this.button_out);
		this.add(this.button_exit);
		this.add(this.panel_userCount);

		this.field_barcode.requestFocusInWindow();
		this.setVisible(true);
	}

	private void checkUserCount(Counter counter) {
		this.panel_userCount.setText(
				String.valueOf(counter.getCurrentUsers()) + " (" + String.valueOf(counter.getMaximumUsers()) + ")");
		if (counter.getCurrentUsers() >= counter.getMaximumUsers()) {
			this.panel_userCount.setBackground(Color.RED);
		} else if (counter.getCurrentUsers() >= Math.floor(counter.getMaximumUsers() * counter.getThreshold())) {
			this.panel_userCount.setBackground(Color.YELLOW);
		} else {
			this.panel_userCount.setBackground(Color.GREEN);
		}
	}
}
