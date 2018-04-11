package model;

import java.time.LocalDate;

public class Booking {
	private final int id;
	private final Purchaser client;
        private final LocalDate date;
	
	public Booking(int id, Purchaser client, LocalDate date) {
		this.id = id;
		this.client = client;
                this.date = date;
	}
	
	public int getId() {
		return id;
	}
	
	public Purchaser getClient() {
		return client;
	}
	
	// Gæti verið nice fyrir gagnagrunninn að geta skilað
	// hér fylki af strengjum [id, client.getName(), client.getEmail...)
	
}
