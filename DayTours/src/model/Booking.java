package model;

public class Booking {
	private int id;
	private Purchaser client;
	
	public Booking(int id, Purchaser client) {
		this.id = id;
		this.client = client;
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
