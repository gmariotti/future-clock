package com.mariotti.developer.futureclock.models.database;

public class AlarmDbSchema {
    public static final class AlarmTable {
        public static final String NAME = "alarms";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TIME = "time";
            
            public static final String MONDAY = "monday";
            public static final String TUESDAY = "tuesday";
            public static final String WEDNESDAY = "wednesday";
            public static final String THURSDAY = "thursday";
            public static final String FRIDAY = "friday";
            public static final String SATURDAY = "saturday";
            public static final String SUNDAY = "sunday";

            private static final String TIMEZONE = "timezone";
            public static final String ACTIVE = "active";
        }
    }
}