package model;

public class Purchaser {

	private String name, email;
	private int seatQt;
	
	public Purchaser (String name, String email, int seatQt) {
		this.name = name;
		this.email = email;
		this.seatQt = seatQt;
	}

	public String getName() {
		return name;
	}


	public String getEmail() {
		return email;
	}


	public int getSeatQt() {
		return seatQt;
	}
}
