package com.reptracker.android.models;

import androidx.annotation.NonNull;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class User {

    private String id;
    private String email;
    private String firstname;
    private String lastname;
    private String password;

    public User() {
    }

    public User(String email, String firstname, String lastname, String password) {
        super();
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NonNull
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("firstname", firstname).append("lastname", lastname).append("email", email).append("password", password).toString();
    }

}