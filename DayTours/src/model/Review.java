package model;

import java.sql.Date;
import java.time.LocalDate;

public class Review {
	private String name, review, email;
	private LocalDate date;
	
	public Review (String name, String review, String email, LocalDate date) {
		this.name = name;
		this.review = review;
		this.email = email;
		this.date = date;
	}
	
	public String getName() {
		return name;
	}
	
	public String getReview() {
		return review;
	}
	
	public String getEmail() {
		return email;
	}
	
	public LocalDate getDate() {
		return date;
	}
}
