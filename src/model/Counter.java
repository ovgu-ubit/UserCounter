package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import controller.CSV_Engine;

// model for current state of users
public class Counter {

	private int currentUsers;
	private int maximumUsers;
	private double threshold;
	private String barcode_regex;

	private ArrayList<String[]> state; // current state as a list of events (timestamp,
										// barcode In, barcode Out)
	private Set<String> presentUsers;
	private Set<String> registeredUsers;

	public Counter(CSV_Engine csvEngine) {
		csvEngine.initCounter(this);
	}

	public void init(int maxUsers, double threshold, String barcode_regex) {
		this.setCurrentUsers(0);
		this.setMaximumUsers(maxUsers);
		this.setThreshold(threshold);
		this.setBarcode_regex((barcode_regex == null || barcode_regex.isEmpty()) ? "^.*$" : barcode_regex);

		this.setState(new ArrayList<String[]>());
		this.setPresentUsers(new HashSet<String>((int) (this.getMaximumUsers() * 1.333) + 1));
		this.setRegisteredUsers(null);
	}

	public void fill(Set<String> registeredUsers) {

	}

	// records a user entry into UB
	public void userIn(String timestamp, String barcode) { // could be boolean to get the information if there has been
															// an error in log
		this.setCurrentUsers(this.getCurrentUsers() + 1);
		this.getPresentUsers().add(barcode);
		this.getState().add(new String[] { timestamp, barcode, "" });
	}

	// records a user exit into UB
	public void userOut(String timestamp, String barcode) {
		this.setCurrentUsers(this.getCurrentUsers() - 1);
		this.getPresentUsers().remove(barcode);
		this.getState().add(new String[] { timestamp, "", barcode });
	}

	public void userRegister(String barcode) {
		if (this.getRegisteredUsers() != null) {
			this.getRegisteredUsers().add(barcode);
		}
	}

	/**
	 * checks if a user is going in or out by counting former entries and exits
	 * 
	 * @param timestamp when did user barcode been read
	 * @param barcode   user barcode that has been scanned
	 * @return if the user is going in (false) or out (true)
	 */
	public boolean isUserIn(String barcode) {
		return getPresentUsers().contains(barcode);
	}

	public boolean isUserRegistered(String barcode) {
		if (this.getRegisteredUsers() == null) {
			return true;
		}
		return this.getRegisteredUsers().contains(barcode);
	}

	public int getCurrentUsers() {
		return this.currentUsers;
	}

	public void setCurrentUsers(int currentUsers) {
		this.currentUsers = currentUsers;
	}

	public int getMaximumUsers() {
		return this.maximumUsers;
	}

	public void setMaximumUsers(int maximumUsers) {
		this.maximumUsers = maximumUsers;
	}

	public double getThreshold() {
		return this.threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public String getBarcode_regex() {
		return this.barcode_regex;
	}

	public void setBarcode_regex(String barcode_regex) {
		this.barcode_regex = barcode_regex;
	}

	public ArrayList<String[]> getState() {
		return this.state;
	}

	public void setState(ArrayList<String[]> state) {
		this.state = state;
	}

	public void addState(String[] state) {
		this.state.add(state);
	}

	public Set<String> getPresentUsers() {
		return this.presentUsers;
	}

	public void setPresentUsers(Set<String> presentUsers) {
		this.presentUsers = presentUsers;
	}

	public Set<String> getRegisteredUsers() {
		return this.registeredUsers;
	}

	public void setRegisteredUsers(Set<String> registeredUsers) {
		this.registeredUsers = registeredUsers;
	}
}
