package com.mariotti.developer.futureclock.test;

import android.test.suitebuilder.annotation.SmallTest;

import com.mariotti.developer.futureclock.models.Alarm;

import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class AlarmTest extends TestCase {

    @SmallTest
    public void testAlarmTimeString() {
        try {
            Calendar today = Calendar.getInstance();
            int days[] = new int[]{1, 2};
            Alarm alarm = new Alarm(UUID.randomUUID(), today, days, false);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            assertEquals(simpleDateFormat.format(today.getTime()), alarm.getTimeAsString());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception thrown");
        }
    }

    public void testAlarmExceptionThrown() {
        try {
            Alarm alarm = new Alarm(null, null, new int[]{1, 2}, false);
            fail("Exception not thrown");
        } catch (Exception e) {
            
        }
    }
}
