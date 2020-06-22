package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import org.ini4j.Wini;

import model.Counter;
import view.Screen;

// class for reading and writing the model from/to CSV
public class CSV_Engine {

	public final static String version = "1.2.3";

	private FileWriter fw;
	private Wini ini; // package ini4j for reading ini files
	private BufferedReader br; // file reader for list of unregistered users
	private File csv; // location of the current CSV file
	private String sep; // separator for CSV
	private String header; // header line for CSV
	private DateTimeFormatter dateFile = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // format of CSV file name
	private DateTimeFormatter dateState = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // format of timestamps

	public CSV_Engine(File config, File registered) {
		try {
			this.ini = new Wini(config);
			this.br = (registered != null) ? new BufferedReader(new FileReader(registered)) : null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// init the model either as empty or read from CSV
	public void initCounter(Counter counter) {
		// Init model config from config.ini file
		counter.init(this.ini.get("users", "max_user", int.class),
				this.ini.get("users", "threshold_yellow", double.class),
				this.ini.get("users", "barcode_regex", String.class));
		this.sep = this.ini.get("csv_file", "sep");
		this.header = this.ini.get("csv_file", "header").replaceAll(";", this.sep) + "\n";

		// Load already registered users list for comparison
		try {
			if (this.br != null) {
				Set<String> registeredUsers = new HashSet<String>();
				String registeredUser = null;
				while ((registeredUser = this.br.readLine()) != null) {
					registeredUser = registeredUser.trim().toUpperCase();
					if (registeredUser.matches(counter.getBarcode_regex())) {
						registeredUsers.add(registeredUser);
					}
				}
				this.br.close();
				counter.setRegisteredUsers(registeredUsers);
			}
		} catch (IOException e) {
			counter.setRegisteredUsers(null);
		}

		// Prepare log output csv file from config.ini file
		try {
			String path = this.ini.get("csv_file", "path");
			(new File(path)).mkdirs();
			this.csv = new File("./" + path + "/" + this.dateFile.format(LocalDateTime.now()) + ".csv");
			if (!this.csv.exists()) { // first execution of the day, initialize
				this.csv.createNewFile();
				this.fw = new FileWriter(this.csv, true);
				this.fw.append(this.header);
			} else { // resuming operation after interruption => read CSV file and restore current
						// state
				this.getCurrentState(counter);
				this.fw = new FileWriter(this.csv, true);
			}
			this.fw.flush(); // or close()?
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initScreen(Screen screen) {
		screen.init(this.ini.get("ui", "fade_timeout", int.class));
	}

	// read current model state from CSV
	private void getCurrentState(Counter counter) {
		try {
			FileReader fr = new FileReader(this.csv);
			BufferedReader br = new BufferedReader(fr);
			counter.setCurrentUsers(0);
			String line;
			while ((line = br.readLine()) != null) {
				if (line.compareTo(this.header.replace("\n", "")) == 0) // if line equals header than no state change is
																		// recorded
					continue;
				String[] data = line.split(this.sep, -1); // -1 indicates that empty Strings should be read
				if (data[1].compareTo("") != 0) { // entry has been recorded
					counter.userIn(data[0], data[1]);
				}
				if (data[2].compareTo("") != 0) { // exit has been recorded
					counter.userOut(data[0], data[2]);
				}
			}
			br.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public DateTimeFormatter getDateState() {
		return this.dateState;
	}

	public void setDateState(DateTimeFormatter dateState) {
		this.dateState = dateState;
	}

	public void writeCSV(Counter counter) {
		String[] state = counter.getState().get(counter.getState().size() - 1);
		try {
			this.fw.append(String.join(this.sep, state) + "\n");
			this.fw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			this.fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
