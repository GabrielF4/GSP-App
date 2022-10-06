package com.example.notespro;

import android.widget.EditText;

public class User {
    public String name, email, homeAdress, mobileNumber, dateOfBirth;

    public User() {

    }

    /* Konstruktor */
    public User(String name, String email, String homeAdress, String mobileNumber, String dateOfBirth) {
        this.name = name;
        this.email = email;
        this.homeAdress = homeAdress;
        this.mobileNumber = mobileNumber;
        this.dateOfBirth = dateOfBirth;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getHomeAdress() {
        return homeAdress;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }
}
