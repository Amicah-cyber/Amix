package com.example.trainbook;

import java.util.Date;

public class user {
    private int id_number;
    private String Fullnames;
    private String Email;
    private int Mobile_number;
    private int Age;
    private String password;

    public user() {
    }

    public user(int id_number, String password){
    this.id_number = id_number;
    this.password = password;
}
public user(int id_number, String Fullnames, String Email, int Mobile_number, int Age, String password){
    this.id_number = id_number;
    this.Fullnames = Fullnames;
    this.Email = Email;
    this.Mobile_number = Mobile_number;
    this.Age = Age;
    this.password = password;
}

    public int getId_number() {
        return id_number;
    }

    public void setId_number(int id_number) {
        this.id_number = id_number;
    }

    public String getFullnames() {
        return Fullnames;
    }

    public void setFullnames(String fullnames) {
        Fullnames = fullnames;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getMobile_number() {
        return Mobile_number;
    }

    public void setMobile_number(int mobile_number) {
        Mobile_number = mobile_number;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
