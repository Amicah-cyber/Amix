package com.example.trainbook;

import android.os.Bundle;

public interface CallbackInterface {
    String PRICE = "price";
    String COACH = "coach";

    String ID = "id";
    String NAME = "name";
    String EMAIL ="email";
    String MOBILE_NUMBER ="mobile";
    String AGE = "age";
    String PASSWORD = "password";
    String TRAIN_TYPE = "type";
    String ORIGIN = "origin";
    String DESTINATION = "destination";
    String CUSTOMER = "customer_id";
    String DDATE = "departure_date";





    void signUpSuccess();
    void firsttoSecond(Ticket ticket, int price);
    void ticketDetails(Ticket ticket);
    void viewTicket(Bundle bundle);

}
