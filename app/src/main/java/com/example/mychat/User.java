package com.example.mychat;

public class User {

    public String username;
    public String usersurname;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String usersurname, String email) {

        this.username = username;
        this.usersurname = usersurname;
        this.email = email;
    }
}

