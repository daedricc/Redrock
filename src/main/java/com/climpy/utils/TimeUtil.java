package com.climpy.utils;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtil {
  private TimeUtil() {
    throw new RuntimeException("Cannot instantiate a utility class.");
  }
  
  public static String millisToTimer(long millis) {
    long seconds = millis / 1000L;
    if (seconds > 3600L)
      return String.format("%02d:%02d:%02d", new Object[] { Long.valueOf(seconds / 3600L), Long.valueOf(seconds % 3600L / 60L), Long.valueOf(seconds % 60L) }); 
    return String.format("%02d:%02d", new Object[] { Long.valueOf(seconds / 60L), Long.valueOf(seconds % 60L) });
  }
  
  public static String millisToSeconds(long millis) {
    return (new DecimalFormat("#0.0")).format(((float)millis / 1000.0F));
  }
  
  public static String dateToString(Date date, String s) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.getTime().toString();
  }
  
  public static Timestamp addDuration(long duration) {
    return truncateTimestamp(new Timestamp(System.currentTimeMillis() + duration));
  }
  
  public static Timestamp truncateTimestamp(Timestamp timestamp) {
    if (timestamp.toLocalDateTime().getYear() > 2037)
      timestamp.setYear(2037); 
    return timestamp;
  }
  
  public static Timestamp addDuration(Timestamp timestamp) {
    return truncateTimestamp(new Timestamp(System.currentTimeMillis() + timestamp.getTime()));
  }
  
  public static Timestamp fromMillis(long millis) {
    return new Timestamp(millis);
  }
  
  public static Timestamp getCurrentTimestamp() {
    return new Timestamp(System.currentTimeMillis());
  }
  
  public static String millisToRoundedTime(long millis) {
    millis++;
    long seconds = millis / 1000L;
    long minutes = seconds / 60L;
    long hours = minutes / 60L;
    long days = hours / 24L;
    long weeks = days / 7L;
    long months = weeks / 4L;
    long years = months / 12L;
    if (years > 0L)
      return String.valueOf(years) + " year" + ((years == 1L) ? "" : "s");
    if (months > 0L)
      return String.valueOf(months) + " month" + ((months == 1L) ? "" : "s"); 
    if (weeks > 0L)
      return String.valueOf(weeks) + " week" + ((weeks == 1L) ? "" : "s"); 
    if (days > 0L)
      return String.valueOf(days) + " day" + ((days == 1L) ? "" : "s"); 
    if (hours > 0L)
      return String.valueOf(hours) + " hour" + ((hours == 1L) ? "" : "s"); 
    if (minutes > 0L)
      return String.valueOf(minutes) + " minute" + ((minutes == 1L) ? "" : "s"); 
    return String.valueOf(seconds) + " second" + ((seconds == 1L) ? "" : "s");
  }
  
  public static long parseTime(String time) {
    long totalTime = 0L;
    boolean found = false;
    Matcher matcher = Pattern.compile("\\d+\\D+").matcher(time);
    while (matcher.find()) {
      String s = matcher.group();
      Long value = Long.valueOf(Long.parseLong(s.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[0]));
      String s2 = s.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[1];
      String str1;
      switch ((str1 = s2).hashCode()) {
        case 77:
          if (!str1.equals("M"))
            continue; 
          totalTime += value.longValue() * 60L * 60L * 24L * 30L;
          found = true;
        case 100:
          if (!str1.equals("d"))
            continue; 
          totalTime += value.longValue() * 60L * 60L * 24L;
          found = true;
        case 104:
          if (!str1.equals("h"))
            continue; 
          totalTime += value.longValue() * 60L * 60L;
          found = true;
        case 109:
          if (!str1.equals("m"))
            continue; 
          totalTime += value.longValue() * 60L;
          found = true;
        case 115:
          if (!str1.equals("s"))
            continue; 
          totalTime += value.longValue();
          found = true;
        case 119:
          if (!str1.equals("w"))
            continue; 
          totalTime += value.longValue() * 60L * 60L * 24L * 7L;
          found = true;
        case 121:
          if (!str1.equals("y"))
            continue; 
          totalTime += value.longValue() * 60L * 60L * 24L * 365L;
          found = true;
      } 
    } 
    return found ? (totalTime * 1000L) : -1L;
  }
  
  private static long convert(int value, char unit) {
    switch (unit) {
      case 'y':
        return value * TimeUnit.DAYS.toMillis(365L);
      case 'M':
        return value * TimeUnit.DAYS.toMillis(30L);
      case 'd':
        return value * TimeUnit.DAYS.toMillis(1L);
      case 'h':
        return value * TimeUnit.HOURS.toMillis(1L);
      case 'm':
        return value * TimeUnit.MINUTES.toMillis(1L);
      case 's':
        return value * TimeUnit.SECONDS.toMillis(1L);
    } 
    return -1L;
  }
  
  public static long parse(String input) {
    if (input == null || input.isEmpty())
      return -1L; 
    long result = 0L;
    StringBuilder number = new StringBuilder();
    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);
      if (Character.isDigit(c)) {
        number.append(c);
      } else {
        String str;
        if (Character.isLetter(c) && !(str = number.toString()).isEmpty()) {
          result += convert(Integer.parseInt(str), c);
          number = new StringBuilder();
        } 
      } 
    } 
    return result;
  }
}
