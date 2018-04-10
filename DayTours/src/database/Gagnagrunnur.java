/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Booking;
import model.Purchaser;
import model.Review;
import model.Trip;

/**
 *
 * @author Arnar
 */
public class Gagnagrunnur {
    
    private Connection conn;
    
    public Gagnagrunnur () {
        conn = null;
    }
    
    /**
     * Fall sem skilar tengingu við gagnagrunn, gegnum JDBC
     * @return
     * @throws SQLException 
     */
    private Connection connect () throws SQLException  {
        if(conn == null){
            try {
                Class.forName("org.sqlite.JDBC");
                String url = System.getProperty("user.dir")+"/src/database/DayTours.db";
                conn = DriverManager.getConnection("jdbc:sqlite:"+url);
            }
            catch( Exception e ){
                System.out.println("Error connecting to database");
            }
        }
        return conn;
    }
    
    private Boolean update(PreparedStatement pstmt){
        try{
            int updates = pstmt.executeUpdate();
            return updates > 0;
        } catch(SQLException e){
            System.out.println("Uppfærsla á gagnagrunni tókst ekki");
            System.out.println(e);
            return false;
        }
    }
    
    public void closeConn() throws SQLException{
        if(conn != null) conn.close();
    }
    
    public ResultSet searchTrips(String [] params) throws SQLException {
        // Búa til tengingu, ef hún er null þá skilum við null því ekki tókst að tengjast við gagnagrunn
        conn = connect();
        if(conn == null) return null;
        
        //Þetta verður sirka svona, sjáum til hverju við viljum leita af
        // Sækja verðbilið
        String[] price = params[0].split("-");
        String stmt = "SELECT * FROM tours WHERE price BETWEEN ? AND ? AND location = ? AND difficulty = ?";
        PreparedStatement pstmt = conn.prepareStatement(stmt);
        
        pstmt.setString(1, price[0]);
        pstmt.setString(2, price[1]);
        pstmt.setString(3, params[1]);
        pstmt.setString(4, params[2]);
        
        ResultSet trips = pstmt.executeQuery();
        return trips;
    }
    
    public Boolean createReview(int id, Review review) throws SQLException{
        // tengjust og skilum null ef ekki tókst að tengjast
        conn = connect();
        if(conn == null) return null;
        
        String stmt = "INSERT INTO reviews (tourID, name, email, review, accepted) VALUES (?,?,?,?,0)";
        PreparedStatement pstmt = conn.prepareStatement(stmt);
        
        pstmt.setInt(1, id);
        pstmt.setString(2, review.getName());
        pstmt.setString(3, review.getEmail());
        pstmt.setString(4, review.getReview());
        
        Boolean result = update(pstmt);
        // skila true ef það tókst að setja inn review, annars false
        return result;
    }
    
    public Boolean updateBooking(int id, Booking booking) throws SQLException{
        // tengjust og skilum null ef ekki tókst að tengjast
        conn = connect();
        if(conn == null) return null;
        
        String stmt = "INSERT INTO bookings (Purchaser_name, Purchaser_email, tourId, seats) VALUES (?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(stmt);
        
        Purchaser purchaser = booking.getClient();
        
        pstmt.setString(1, purchaser.getName());
        pstmt.setString(2, purchaser.getEmail());
        pstmt.setInt(3, id);
        pstmt.setInt(4, purchaser.getSeatQt());
        
        Boolean result = update(pstmt);
        // skila true ef það tókst að setja inn review, annars false
        return result;
    }
    
    public Boolean createTrip(Trip trip) throws SQLException{
        // tengjust og skilum null ef ekki tókst að tengjast
        conn = connect();
        if(conn == null) return null;
        
        String stmt = "INSERT INTO tours (title, price, location, duration, difficulty, description) VALUES (?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(stmt);
                
        pstmt.setString(1, trip.getTitle());
        pstmt.setString(2, trip.getPrice());
        pstmt.setString(3, trip.getLocation());
        pstmt.setString(4, trip.getDuration());
        pstmt.setString(5, trip.getDifficulty());
        pstmt.setString(6, trip.getItinirary());
        
        Boolean result = update(pstmt);
        // skila true ef það tókst að setja inn review, annars false
        return result;
    }
   
    public Boolean deleteTrip(int id) throws SQLException{
        // tengjust og skilum null ef ekki tókst að tengjast
        conn = connect();
        if(conn == null) return null;
        
        String stmt = "DELETE FROM tours WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(stmt);        
        
        pstmt.setInt(1, id);
        
        Boolean result = update(pstmt);
        // skila true ef það tókst að setja inn review, annars false
        return result;
    }
    
    public Boolean deleteReviews(int id, int tourId) throws SQLException{
        // tengjust og skilum null ef ekki tókst að tengjast
        conn = connect();
        if(conn == null) return null;
        
        String stmt = "DELETE FROM reviews WHERE id = ? AND tourId = ?";
        PreparedStatement pstmt = conn.prepareStatement(stmt);        
        
        pstmt.setInt(1, id);
        pstmt.setInt(2, tourId);
        
        Boolean result = update(pstmt);
        // skila true ef það tókst að setja inn review, annars false
        return result;
    }
    
    /**
     * Samþykkja eitt review
     * @param id
     * @return
     * @throws SQLException 
     */
    public Boolean confirmReview(int id) throws SQLException{
        // tengjust og skilum null ef ekki tókst að tengjast
        conn = connect();
        if(conn == null) return null;
        
        String stmt = "UPDATE reviews SET accepted = 1 WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(stmt);        
        
        pstmt.setInt(1, id);
        
        Boolean result = update(pstmt);
        // skila true ef það tókst að setja inn review, annars false
        return result;
    }
    /**
     * Samþykkja öll review
     * @param id
     * @return
     * @throws SQLException 
     */
    public Boolean confirmAllReviews() throws SQLException{
        // tengjust og skilum null ef ekki tókst að tengjast
        conn = connect();
        if(conn == null) return null;
        
        String stmt = "UPDATE reviews SET accepted = 1";
        PreparedStatement pstmt = conn.prepareStatement(stmt);        
                
        Boolean result = update(pstmt);
        // skila true ef það tókst að setja inn review, annars false
        return result;
    }
    
    public ResultSet getTrip(int id) throws SQLException{
        // tengjust og skilum null ef ekki tókst að tengjast
        conn = connect();
        if(conn == null) return null;
        
        String stmt = "SELECT * FROM tours WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(stmt);        
        
        pstmt.setInt(1, id);
        
        ResultSet trip = pstmt.executeQuery();
        return trip;
    }
    
    /**
     * Get accepted reviews
     * @param id
     * @return
     * @throws SQLException 
     */
    public ResultSet getReviews(int id) throws SQLException{
        // tengjust og skilum null ef ekki tókst að tengjast
        conn = connect();
        if(conn == null) return null;
        
        String stmt = "SELECT * FROM reviews WHERE tourId = ? AND accepted = 1";
        PreparedStatement pstmt = conn.prepareStatement(stmt);        
        
        pstmt.setInt(1, id);
        
        ResultSet trip = pstmt.executeQuery();
        return trip;
    }
    
    /**
     * Sækir reviews sem hafa ekki verið samþykkt
     * @param id
     * @return
     * @throws SQLException 
     */
    public ResultSet getAdminReviews() throws SQLException{
        // tengjust og skilum null ef ekki tókst að tengjast
        conn = connect();
        if(conn == null) return null;
        
        String stmt = "SELECT * FROM reviews WHERE accepted = 0";
        PreparedStatement pstmt = conn.prepareStatement(stmt);
        
        ResultSet trip = pstmt.executeQuery();
        return trip;
    }
    
    public ResultSet getUser(String username, String password) throws SQLException{
        // tengjust og skilum null ef ekki tókst að tengjast
        conn = connect();
        if(conn == null) return null;
        
        String stmt = "SELECT * FROM users WHERE username = ? AND password = ?";
        PreparedStatement pstmt = conn.prepareStatement(stmt);        
        
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        
        ResultSet trip = pstmt.executeQuery();
        return trip;
    }
    
    public ResultSet getTrips() throws SQLException{
        // tengjust og skilum null ef ekki tókst að tengjast
        conn = connect();
        if(conn == null) return null;
        
        String stmt = "SELECT * FROM tours";
        PreparedStatement pstmt = conn.prepareStatement(stmt);
        
        ResultSet trips = pstmt.executeQuery();
        
        return trips;
    }
    
}
