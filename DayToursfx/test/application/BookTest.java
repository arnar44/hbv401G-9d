package application;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

class BookTest {
	
	private Book bookingClass;
	private int id;
	
	@Before
	public void setUp() {
		bookingClass = new Book();
		id = 1;
	}
	
	@After
	public void tearDown() {
		bookingClass = null;
	}

	@Test
	public void testName() {
		assertNull(bookingClass.makeBooking(id, "", "valid@email.com", 5));
		assertNull(bookingClass.makeBooking(id, 1, "valid@email.com", 5));
		assertNull(bookingClass.makeBooking(id, "e", "valid@email.com", 5));
		assertNull(bookingClass.makeBooking(id, false, "valid@email.com", 5));
		assertNotNull(bookingClass.makeBooking(id, "Name", "valid@email.com", 5));
	}

}
