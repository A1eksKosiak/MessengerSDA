package com.gmail.a1ekskosyak;

import java.time.Clock;
import java.time.LocalDateTime;

public class Chat {
    private String name;
    private boolean isPrivate;
    private String password;
    private LocalDateTime timestamp;
    private Clock clock;

    public Chat(String name) {
        this.name = name;
        this.isPrivate = false;
        this.timestamp = LocalDateTime.now(clock);
    }

    public Chat(String name, String password) {
        this.name = name;
        this.isPrivate = true;
        this.password = password;
        this.timestamp = LocalDateTime.now(clock);
    }

    public String getName() {
        return name;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
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

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chat)) return false;

        Chat chat = (Chat) o;

        if (isPrivate != chat.isPrivate) return false;
        if (!name.equals(chat.name)) return false;
        return password != null ? password.equals(chat.password) : chat.password != null;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (isPrivate ? 1 : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
