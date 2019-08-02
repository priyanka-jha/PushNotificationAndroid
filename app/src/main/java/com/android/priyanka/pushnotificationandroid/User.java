package com.android.priyanka.pushnotificationandroid;

import java.io.Serializable;

//to map the data to be sent to firebase database
public class User implements Serializable {
    String email;
    String token;

    public User() {

    }

    public User(String email, String token) {
        this.email = email;
        this.token = token;
    }
}
