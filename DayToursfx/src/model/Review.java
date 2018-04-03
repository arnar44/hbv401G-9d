package model;

import java.sql.Date;

public class Review {
	private String name, review, email;
	private Date date;
	
	public Review (String name, String review, String email, Date date) {
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
	
	public Date getDate() {
		return date;
	}
}
