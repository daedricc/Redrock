package com.climpy.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateTimeUtils {
  public static String getDateToLong(String longValue, String pattern, Locale locale) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, locale);
    Date date = new Date(longValue);
    return simpleDateFormat.format(date);
  }
  
  public static String formatToMMSS(int secondsValue) {
    int millis = secondsValue * 1000;
    int sec = millis / 1000 % 60;
    int min = millis / 60000 % 60;
    int hr = millis / 3600000 % 24;
    return ((hr > 0) ? String.format("%02d", new Object[] { Integer.valueOf(hr) }) : "") + String.format("%02d:%02d", new Object[] { Integer.valueOf(min), Integer.valueOf(sec) });
  }
  
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
  
  public static String formatToDateBreakdown(Calendar fromDate, Calendar toDate) {
    boolean future = false;
    if (toDate.equals(fromDate))
      return "şimdi";
    if (toDate.after(fromDate))
      future = true; 
    StringBuilder stringBuilder = new StringBuilder();
    String[] names = { 
        "y", "yıl", "ay", "ay", "g", "gün", "saat", "saat", "dakika", "dakika",
        "saniye", "saniye" };
    int[] types = { 1, 2, 5, 11, 12, 13 };
    int accuracy = 0;
    for (int i = 0; i < types.length && accuracy <= 2; i++) {
      int diff = dateDiff(types[i], fromDate, toDate, future);
      if (diff > 0) {
        accuracy++;
        stringBuilder.append(" ").append(diff).append(" ").append(names[i * 2 + ((diff > 1) ? 1 : 0)]);
      } 
    } 
    return (stringBuilder.length() == 0) ? "şimdi" : stringBuilder.toString().trim();
  }
  
  public static String formatDurationWords(long durationMillis, boolean suppressLeadingZeroElements, boolean suppressTrailingZeroElements) {
    String duration = DurationFormatUtils.formatDuration(durationMillis, "d' g'H' saat 'm' dakika 's' saniye'");
    if (suppressLeadingZeroElements) {
      duration = " " + duration;
      String tmp = StringUtils.replaceOnce(duration, " 0 g", "0 gün");
      if (tmp.length() != duration.length()) {
        duration = tmp;
        tmp = StringUtils.replaceOnce(tmp, " 0 saat", "");
        if (tmp.length() != duration.length()) {
          tmp = StringUtils.replaceOnce(tmp, " 0 dakika", "");
          duration = tmp;
          if (tmp.length() != tmp.length())
            duration = StringUtils.replaceOnce(tmp, " 0 saniye", ""); 
        } 
      } 
      if (duration.length() != 0)
        duration = duration.substring(1); 
    } 
    if (suppressTrailingZeroElements) {
      String tmp = StringUtils.replaceOnce(duration, " 0 saniye", "");
      if (tmp.length() != duration.length()) {
        duration = tmp;
        tmp = StringUtils.replaceOnce(tmp, " 0 dakika", "");
        if (tmp.length() != duration.length()) {
          duration = tmp;
          tmp = StringUtils.replaceOnce(tmp, " 0 saat", "");
          if (tmp.length() != duration.length())
            duration = StringUtils.replaceOnce(tmp, " 0 g", "0 gün");
        } 
      } 
    } 
    duration = " " + duration;
    duration = StringUtils.replaceOnce(duration, " 1 saniye", " 1 saniye");
    duration = StringUtils.replaceOnce(duration, " 1 dakika", " 1 dakika");
    duration = StringUtils.replaceOnce(duration, " 1 saat", " 1 saat");
    duration = StringUtils.replaceOnce(duration, " 1 g", " 1 gün");
    return duration.trim();
  }
  
  private static int dateDiff(int type, Calendar fromDate, Calendar toDate, boolean future) {
    int diff = 0;
    long savedDate;
    for (savedDate = fromDate.getTimeInMillis(); (future && !fromDate.after(toDate)) || (!future && !fromDate.before(toDate)); diff++) {
      savedDate = fromDate.getTimeInMillis();
      fromDate.add(type, future ? 1 : -1);
    } 
    diff--;
    fromDate.setTimeInMillis(savedDate);
    return diff;
  }
  
  public static Long parseTimeToString(String time) {
    if (time.equalsIgnoreCase("permanent") || time.equalsIgnoreCase("perm"))
      return Long.valueOf(-1L); 
    long totalTime = 0L;
    boolean found = false;
    Matcher matcher = Pattern.compile("\\d+\\D+").matcher(time);
    while (matcher.find()) {
      String string = matcher.group();
      Long value = Long.valueOf(Long.parseLong(string.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[0]));
      String type = string.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[1];
      switch (type) {
        case "s":
          totalTime += value.longValue();
          found = true;
        case "m":
          totalTime += value.longValue() * 60L;
          found = true;
        case "h":
          totalTime += value.longValue() * 60L * 60L;
          found = true;
        case "d":
          totalTime += value.longValue() * 60L * 60L * 24L;
          found = true;
        case "w":
          totalTime += value.longValue() * 60L * 60L * 24L * 7L;
          found = true;
        case "M":
          totalTime += value.longValue() * 60L * 60L * 24L * 30L;
          found = true;
        case "y":
          totalTime += value.longValue() * 60L * 60L * 24L * 365L;
          found = true;
      } 
    } 
    return !found ? null : Long.valueOf(totalTime * 1000L);
  }
  
  public static String setIntegerCountdownFormat(Integer intValue) {
    int remainder = intValue.intValue() * 1000;
    int seconds = remainder / 1000 % 60;
    int minutes = remainder / 60000 % 60;
    int hours = remainder / 3600000 % 24;
    return ((hours > 0) ? String.format("%02d:", new Object[] { Integer.valueOf(hours) }) : "") + 
      String.format("%02d:%02d", new Object[] { Integer.valueOf(minutes), Integer.valueOf(seconds) });
  }
  
  public static String setLongCountdownFormat(Long longValue) {
    if (longValue.longValue() < TimeUnit.MINUTES.toMillis(1L))
      return getDecimalFormat().format(longValue.longValue() / 1000.0D) + "s"; 
    return DurationFormatUtils.formatDuration(longValue.longValue(), (
        (longValue.longValue() >= TimeUnit.HOURS.toMillis(1L)) ? "HH:" : "") + "mm:ss");
  }
  
  private static DecimalFormat getDecimalFormat() {
    return new DecimalFormat("0.0");
  }
}
