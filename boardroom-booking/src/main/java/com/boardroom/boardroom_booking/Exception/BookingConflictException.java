package com.boardroom.boardroom_booking.Exception;

public class BookingConflictException extends RuntimeException{
    public BookingConflictException(String message){
        super(message);
    }
}
