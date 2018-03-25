package model;

public class Booking {
	private int id;
	private Purchaser client;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Purchaser getClient() {
		return client;
	}
	public void setClient(Purchaser client) {
		this.client = client;
	}
	
	// Gæti verið nice fyrir gagnagrunninn að geta skilað
	// hér fylki af strengjum [id, client.getName(), client.getEmail...)
	
}
