package com.epam.service;

import com.epam.domain.CinemaEvent;
import com.epam.domain.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DiscountService {

    private List<DiscountStrategy> discountStrategies;

    public BigDecimal getDiscount(User user, CinemaEvent event, LocalDateTime date, int ticketCount) {
        BigDecimal birthday =
                discountStrategies.stream().map(discountStrategy -> discountStrategy.checkBirthdayDiscount(user, date))
                        .max(BigDecimal::compareTo).get();
        BigDecimal ticket = discountStrategies.stream().map(discountStrategy ->
                discountStrategy.checkNumberOfTicketsDiscount(ticketCount)).max(BigDecimal::compareTo).get();
        return birthday.compareTo(ticket) > 0 ? birthday : ticket;
    }

    public class DiscountStrategy {

        public BigDecimal checkBirthdayDiscount(User user, LocalDateTime date) {
            int day = date.getDayOfYear();
            return day <= user.getBirthday().getDayOfYear() + 5 && day > user.getBirthday().getDayOfYear() - 5
                    ? new BigDecimal(5) : BigDecimal.ZERO;
        }

        public BigDecimal checkNumberOfTicketsDiscount(int ticketCount) {
            BigDecimal value = new BigDecimal(ticketCount / 10).setScale(0, RoundingMode.HALF_DOWN);
            return value.divide(new BigDecimal(2), 0, RoundingMode.HALF_DOWN).divide(
                    new BigDecimal(ticketCount), RoundingMode.HALF_DOWN).multiply(new BigDecimal(100));
        }
    }
}
