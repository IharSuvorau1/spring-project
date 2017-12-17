package com.epam.domain;

import java.time.LocalDateTime;

public class Ticket {

    private String eventName;
    private Auditorium auditorium;
    private LocalDateTime date;
    private int seat;
    private User user;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Auditorium getAuditorium() {
        return auditorium;
    }

    public void setAuditorium(Auditorium auditorium) {
        this.auditorium = auditorium;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ticket ticket = (Ticket) o;

        if (seat != ticket.seat) return false;
        if (eventName != null ? !eventName.equals(ticket.eventName) : ticket.eventName != null) return false;
        if (auditorium != null ? !auditorium.equals(ticket.auditorium) : ticket.auditorium != null) return false;
        if (date != null ? !date.equals(ticket.date) : ticket.date != null) return false;
        return user != null ? user.equals(ticket.user) : ticket.user == null;
    }

    @Override
    public int hashCode() {
        int result = eventName != null ? eventName.hashCode() : 0;
        result = 31 * result + (auditorium != null ? auditorium.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + seat;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }
}
