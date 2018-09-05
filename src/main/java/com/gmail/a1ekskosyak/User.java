package com.gmail.a1ekskosyak;

import java.time.Clock;
import java.time.LocalDateTime;

public class User {
    private String email;
    private String name;
    private String password;
    private Clock clock;
    private LocalDateTime timestamp;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.clock = Clock.systemUTC();
        timestamp = LocalDateTime.now(clock);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        return timestamp != null ? timestamp.equals(user.timestamp) : user.timestamp == null;
    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return email + "\n"
                + name + "\n"
                + password + "\n"
                + timestamp;
    }
}
