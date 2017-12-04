package com.epam.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public class CinemaEvent {

    private String id;
    private String name;
    private BigDecimal price = new BigDecimal("0.00");
    private EventRatingEnum rating;
    private Set<LocalDateTime> dates;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public EventRatingEnum getRating() {
        return rating;
    }

    public void setRating(EventRatingEnum rating) {
        this.rating = rating;
    }

    public Set<LocalDateTime> getDates() {
        return dates;
    }

    public void setDates(Set<LocalDateTime> dates) {
        this.dates = dates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CinemaEvent event = (CinemaEvent) o;

        if (id != null ? !id.equals(event.id) : event.id != null) return false;
        if (name != null ? !name.equals(event.name) : event.name != null) return false;
        if (price != null ? !price.equals(event.price) : event.price != null) return false;
        if (rating != event.rating) return false;
        return dates != null ? dates.equals(event.dates) : event.dates == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (rating != null ? rating.hashCode() : 0);
        result = 31 * result + (dates != null ? dates.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CinemaEvent{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", rating=" + rating +
                ", dates=" + dates +
                '}';
    }

    public enum EventRatingEnum {
        HIGH,
        MID,
        LOW
    }
}
