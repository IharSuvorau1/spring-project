package com.epam.aspects;

import com.epam.domain.CinemaEvent;
import com.epam.domain.Ticket;
import com.google.common.collect.Maps;
import javaemul.internal.annotations.UncheckedCast;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Aspect
@Component
public class CounterAspect {

    private Map<String, Integer> eventsCount = Maps.newHashMap();
    private Map<String, Integer> pricesCount = Maps.newHashMap();
    private Map<String, Integer> ticketsCount = Maps.newHashMap();

    @Before("execution(* *..EventService.getByName(..))")
    public void getEventsCount(JoinPoint joinPoint) {
        String eventName = (String) joinPoint.getArgs()[0];
        eventsCount.merge(eventName, 1, (a, b) -> a + b);
        System.out.println("Event was accessed by name: " + eventsCount);
    }

    @Before("execution(* *..BookingService.getTicketsPrice(..))")
    public void getPricesCount(JoinPoint joinPoint) {
        String eventName = ((CinemaEvent) joinPoint.getArgs()[0]).getName();
        pricesCount.merge(eventName, 1, (a, b) -> a + b);
        System.out.println("Event prices were querie: " + pricesCount);
    }

    @Before("execution(* *..BookingService.bookTickets(..))")
    public void getTikectCount(JoinPoint joinPoint) {
        List<Ticket> tickets = (List<Ticket>) joinPoint.getArgs()[0];
        Map<String, List<Ticket>> ticketMap = tickets.stream().collect(Collectors.groupingBy(Ticket::getEventName));
        ticketMap.keySet().forEach(key -> ticketsCount.merge(key, ticketMap.get(key).size(), (a, b) -> a + b));
        System.out.println("Event tickets were booked: " + ticketsCount);
    }
}
