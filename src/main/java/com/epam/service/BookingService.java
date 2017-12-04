package com.epam.service;

import com.epam.domain.Auditorium;
import com.epam.domain.CinemaEvent;
import com.epam.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private DiscountService discountService;

    public BigDecimal getTicketsPrice(CinemaEvent event, Auditorium auditorium, LocalDateTime time, User user,
                                      List<Integer> seats) {
        BigDecimal discount = discountService.getDiscount(user, event, time, seats.size());
        BigDecimal price = event.getPrice().multiply(new BigDecimal(100).subtract(discount))
                .divide(new BigDecimal(100), RoundingMode.HALF_DOWN);
        //seats.stream().filter()
        if (event.getRating() == CinemaEvent.EventRatingEnum.HIGH) {
            price = price.divide(new BigDecimal("1.2"), BigDecimal.ROUND_HALF_UP);
        }
        return price;
    }
}
