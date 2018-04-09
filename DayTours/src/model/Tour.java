package model;

public class Tour {
	private int id;
	private String title, location, duration, difficulty, itinirary, price;
	
	public Tour(String title, String location, String duration, String difficulty, String itinirary, int id, String price) {
		this.id = id;
		this.location = location;
		this.duration = duration;
		this.difficulty = difficulty;
		this.itinirary = itinirary;
                this.price = price;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getLocation() {
		return location;
	}

	public String getDuration() {
		return duration;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public String getItinirary() {
		return itinirary;
	}
        
        public String getPrice() {
		return price;
	}
}
