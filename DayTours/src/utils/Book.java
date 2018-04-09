/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import database.Gagnagrunnur;
import java.sql.SQLException;
import java.util.Arrays;
import model.Booking;
import model.Purchaser;


/**
 *
 * @author freyrdanielsson
 */
public class Book {
    
    public int[] makeBooking(int id, String name, String email, int seatQt) throws SQLException{
        int[] validation = validate(name, email);
        
        if(validation[0] == 1 || validation[1] == 0) {
            return validation;
        }
        
        
        
        Purchaser client = new Purchaser(name, email, seatQt);
        Booking book = new Booking(id, client);
        
        Gagnagrunnur db = new Gagnagrunnur();
        
        db.updateBooking(id, book);
        
        return validation;
        
    }
    
    private int[] validate(String name, String email) {
        int[] validation = new int[2];
        Arrays.fill(validation, 0);
        
        if(name.matches(".*\\d+.*") || name.trim().equals("")) {
            validation[0] = 1;
        }
        
        if(!email.contains("@")){
            validation[1] = 1;
        }
        
        return validation;
    }
    
}
