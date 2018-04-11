/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import database.Gagnagrunnur;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import model.Booking;
import model.Purchaser;


/**
 *
 * @author freyrdanielsson
 */
public class Book {
    
    private Gagnagrunnur db;
    
    public void setDb(Gagnagrunnur db) {
        this.db = db;
    }
    
    public int[] makeBooking(int id, String name, String email, int seatQt, LocalDate date) throws SQLException{
        int[] validation = validate(name, email, date);
        
        if(validation[0] == 1 || validation[1] == 1 || validation [2] == 1) {
            return validation;
        }
        
        
        
        Purchaser client = new Purchaser(name, email, seatQt);
        Booking book = new Booking(id, client, date);
        
        db.updateBooking(id, book, date);
        
        return validation;
        
    }
    
    private int[] validate(String name, String email, LocalDate date) {
        int[] validation = new int[3];
        Arrays.fill(validation, 0);
        
        
        if(name.matches(".*\\d+.*") || name.trim().equals("")) {
            validation[0] = 1;
        }
        
        if(!email.contains("@")){
            validation[1] = 1;
        }
        
        if(date == null) {
            validation[2] = 1;
        }
        
        return validation;
    }
    
}
