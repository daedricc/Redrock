package com.climpy.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DurationUtil {
  private long value;
  
  public DurationUtil(long value) {
    this.value = value;
  }
  
  public static DurationUtil fromString(String source) {
    if (source.equalsIgnoreCase("perm") || source.equalsIgnoreCase("permanent"))
      return new DurationUtil(2147483647L); 
    long totalTime = 0L;
    boolean found = false;
    Matcher matcher = Pattern.compile("\\d+\\D+").matcher(source);
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
    return new DurationUtil(found ? (totalTime * 1000L) : -1L);
  }
  
  public long getValue() {
    return this.value;
  }
}
