package com.epam.aspects;

import com.epam.domain.User;
import com.google.common.collect.Maps;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
public class DiscountAspect {

    private Map<String, Integer> userDiscount = Maps.newHashMap();
    private int discounts;

    @Before("execution(* *..DiscountService.getDiscount(..))")
    public void getEventsCount(JoinPoint joinPoint) {
        discounts++;
        User user = (User) joinPoint.getArgs()[0];
        userDiscount.merge(user.getEmail(), 1, (a, b) -> a + b);
        System.out.println("Number of discounts: " + discounts);
        System.out.println("Number of User discounts: " + userDiscount);
    }
}
