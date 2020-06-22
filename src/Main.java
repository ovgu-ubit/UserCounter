import java.io.File;

import javax.swing.JOptionPane;

import controller.CSV_Engine;
import model.Counter;
import view.Screen;

/**
 * User counter application to detect library visitors by their barcode.
 *
 *
 * @version 1.2.3
 * @author Sascha Bosse
 * @author Christian Schulz
 * @author Veit Köppen
 *
 */
public class Main {

	private final static String config_path = "./config.ini";
	private final static String registered_path = "./ous100_barcodes.csv";

	public static void main(String[] args) {
		File config_file = new File(Main.config_path);
		if (!config_file.exists()) {
			JOptionPane.showMessageDialog(null,
					"Konfiguration nicht gefunden!\r\n\r\n(Bitte prüfen Sie die Datei \"config.ini\" im Arbeitsverzeichnis.)");
			System.exit(1);
		}
		File registered_file = new File(Main.registered_path);
		if (!registered_file.exists()) {
			registered_file = null;
		}
		CSV_Engine csvEngine = new CSV_Engine(config_file, registered_file);
		Counter counter = new Counter(csvEngine);
		new Screen(csvEngine, counter);
	}
}
