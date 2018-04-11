package model;

public class Trip {
	private int id;
	private String title, location, duration, difficulty, itinirary, price, meet, pickup, availability, category;
	
	public Trip(String title, String location, String duration, String difficulty,
                String itinirary, int id, String price, String meet, String pickup,
                String availability, String category) {
		this.id = id;
		this.location = location;
		this.duration = duration;
		this.difficulty = difficulty;
		this.itinirary = itinirary;
                this.price = price;
                this.title = title;
                this.meet = meet;
                this.availability = availability;
                this.pickup = pickup;
                this.category = category;
	}

    public String getMeet() {
        return "Meet on location: " + meet;
    }

    public String getPickup() {
        return "Pickup: " + pickup;
    }

    public String getAvailability() {
        return "Available: " + availability;
    }

    public String getCategory() {
        return "Type: " + category;
    }

    public int getId() {
	return id;
    }

    public String getTitle() {
	return title;
    }

    public String getLocation() {
        return "Departure: " + location;
    }

    public String getDuration() {
    	return "Duration: " + duration;
    }

    public String getDifficulty() {
    	return "Level: " + difficulty;
    }

    public String getItinirary() {
    	return itinirary;
    }
        
    public String getPrice() {
	return "Price: " + price + " ISK";
    }
}
