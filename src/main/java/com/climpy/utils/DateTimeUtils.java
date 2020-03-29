package com.climpy.utils;

import java.util.concurrent.TimeUnit;

public class DateTimeUtils {
    public static long parseLong(String inputValue) {
        if (inputValue == null || inputValue.isEmpty())
            return -1L;
        long result = 0L;
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < inputValue.length(); i++) {
            char c = inputValue.charAt(i);
            if (Character.isDigit(c)) {
                number.append(c);
            } else {
                String string;
                if (Character.isLetter(c) && !(string = number.toString()).isEmpty()) {
                    result += convert(Integer.parseInt(string), c);
                    number = new StringBuilder();
                }
            }
        }
        return result;
    }

    private static long convert(int intValue, char unit) {
        switch (unit) {
            case 'y':
                return intValue * TimeUnit.DAYS.toMillis(365L);
            case 'M':
                return intValue * TimeUnit.DAYS.toMillis(30L);
            case 'd':
                return intValue * TimeUnit.DAYS.toMillis(1L);
            case 'h':
                return intValue * TimeUnit.HOURS.toMillis(1L);
            case 'm':
                return intValue * TimeUnit.MINUTES.toMillis(1L);
            case 's':
                return intValue * TimeUnit.SECONDS.toMillis(1L);
        }
        return -1L;
    }
}
