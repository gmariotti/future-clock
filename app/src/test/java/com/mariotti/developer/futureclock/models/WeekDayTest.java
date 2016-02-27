package com.mariotti.developer.futureclock.models;

import org.junit.Assert;
import org.junit.Test;

import com.mariotti.developer.futureclock.models.WeekDay;

public class WeekDayTest {

    @Test
    public void reorderDayTest() {
        int days[] = {
                1, 5, 9, 9, -3, 3, 3, 2, 7, 110, -11
        };

        int expected[] = {1, 2, 3, 5, 7};

        Assert.assertArrayEquals(expected, WeekDay.INSTANCE.reorderDays(days));
    }
}
