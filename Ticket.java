package com.example.trainbook;

import java.util.Date;

public class Ticket {
    private int id;
    private String type;
    private String coach;
    private String origin;
    private String destination;
    private int customerId;
    private Date date;
    private String nationality;
    private int mobile;

    public Ticket() {
    }

    public Ticket(int id, String type, String coach, String origin, String destination, int customerId, Date date, String nationality, int mobile) {
        this.id = id;
        this.type = type;
        this.coach = coach;
        this.origin = origin;
        this.destination = destination;
        this.customerId = customerId;
        this.date = date;
        this.nationality = nationality;
        this.mobile = mobile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public int getMobile() {
        return mobile;
    }

    public void setMobile(int mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "type:"+getType() +"coach:"+getCoach()+"origin:"+getOrigin()+"destination"+getDestination()+
        "customer:"+String.valueOf(getCustomerId())+"date:"+String.valueOf(getDate())+"nationality"+getNationality()+"mobile:"+String.valueOf(getMobile());
    }
}
