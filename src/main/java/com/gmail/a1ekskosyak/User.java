package com.gmail.a1ekskosyak;

import java.time.Clock;
import java.time.LocalDateTime;

public class User {
    private String email;
    private String name;
    private String password;
    private int age;
    private Clock clock;
    private LocalDateTime timestamp;

    public User(String name, String email, String password, int age) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.clock = Clock.systemUTC();
        timestamp = LocalDateTime.now(clock);
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getAge() {
        return age;
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

        if (age != user.age) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (clock != null ? !clock.equals(user.clock) : user.clock != null) return false;
        return password != null ? password.equals(user.password) : user.password == null;
    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + age;
        return result;
    }

    @Override
    public String toString() {
        return "email: " + email + " " +
                "name: " + name + " " +
                "password: " + password + " " +
                "age: " + age + " " +
                "timestamp: " + timestamp;
    }
}
