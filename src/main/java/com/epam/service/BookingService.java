package com.epam.service;

import com.epam.domain.Auditorium;
import com.epam.domain.CinemaEvent;
import com.epam.domain.Ticket;
import com.epam.domain.User;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class BookingService extends JdbcDaoSupport {

    @Autowired
    private DiscountService discountService;

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    public BigDecimal getTicketsPrice(CinemaEvent event, Auditorium auditorium, LocalDateTime time, User user,
                                      List<Integer> seats) {
        BigDecimal discount = discountService.getDiscount(user, event, time, seats.size());
        BigDecimal price = event.getPrice().multiply(new BigDecimal(100).subtract(discount))
                .divide(new BigDecimal(100), RoundingMode.HALF_DOWN);
        //seats.stream().filter()
        if (event.getRating() == CinemaEvent.EventRatingEnum.HIGH) {
            price = price.divide(new BigDecimal("1.2"), RoundingMode.HALF_UP);
        }
        return price.divide(new BigDecimal(seats.size()), RoundingMode.HALF_UP);
    }

    public boolean bookTickets(List<Ticket> tickets) {
        AtomicBoolean freeTickets = new AtomicBoolean(true);
        tickets.forEach(ticket -> freeTickets.set(checkTicket(ticket)));
        if(freeTickets.get()) {
            tickets.forEach(ticket -> {
                String sql = "INSERT INTO public.ticket VALUES (?, ?, ?)";
                getJdbcTemplate().update(sql, ticket.getEventName(), ticket.getSeat(), ticket.getDate().toString());
            });
        }
        return freeTickets.get();
    }

    public List<Ticket> getPurchasedTicketsForEvent(CinemaEvent event, LocalDateTime time) {
        String sql = "select * from public.ticket where event_name = ? and date = ?";
        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql, new Object[]{event.getName(), time});
        List<Ticket> tickets = Lists.newArrayList();
        rows.forEach(row -> tickets.add(getTicket(row)));
        return tickets;
    }

    private boolean checkTicket(Ticket ticket) {
        String sql = "select * from public.ticket where event_name = ? and seat = ? and date = ?";
        return getJdbcTemplate().queryForRowSet(sql, new Object[]{ticket.getEventName(), ticket.getSeat(), ticket.getDate()}).first();
    }

    private Ticket getTicket(Map<String, Object> row) {
        Ticket ticket = new Ticket();
        ticket.setEventName((String) row.get("id"));
        ticket.setSeat((Integer) row.get("seat"));
        ticket.setDate(LocalDateTime.now());
        return ticket;
    }
}
