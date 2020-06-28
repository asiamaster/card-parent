package com.dili.card.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 简单的日期格式化工具
 */
public class DateUtil {

    private static final Map<String, DateTimeFormatter> formatterMap = new HashMap<>();

    /**
     * 获取当前日期
     * @return
     */
    public static LocalDate nowDate() {
        return LocalDate.now(ZoneId.systemDefault());
    }

    /**
     * 获取当前日期时间
     * @return
     */
    public static LocalDateTime nowDateTime() {
        return LocalDateTime.now(ZoneId.systemDefault());
    }

    /**
     * 格式化日期
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDate(LocalDate date, String pattern) {
        DateTimeFormatter formatter = getFormatter(pattern);
        return formatter.format(date);
    }

    /**
     * 格式化当前日期
     * @param pattern
     * @return
     */
    public static String formatDate(String pattern) {
        DateTimeFormatter formatter = getFormatter(pattern);
        return formatter.format(nowDate());
    }

    /**
     * 格式化日期时间
     * @param dateTime
     * @param pattern
     * @return
     */
    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        DateTimeFormatter formatter = getFormatter(pattern);
        return formatter.format(dateTime);
    }

    /**
     * 格式化当前日期时间
     * @param pattern
     * @return
     */
    public static String formatDateTime(String pattern) {
        DateTimeFormatter formatter = getFormatter(pattern);
        return formatter.format(nowDateTime());
    }

    /**
     * 解析日期时间
     * @param dateTimeStr
     * @param pattern
     * @return
     */
    public static LocalDateTime parseDateTime(String dateTimeStr, String pattern) {
        DateTimeFormatter formatter = getFormatter(pattern);
        return LocalDateTime.parse(dateTimeStr, formatter);
    }

    /**
     * 解析日期
     * @param dateStr
     * @param pattern
     * @return
     */
    public static LocalDate parseDate(String dateStr, String pattern) {
        DateTimeFormatter formatter = getFormatter(pattern);
        return LocalDate.parse(dateStr, formatter);
    }

    /**
     * 获取日期格式器
     * @param pattern
     * @return
     */
    private static DateTimeFormatter getFormatter(String pattern) {
        DateTimeFormatter formatter = formatterMap.get(pattern);
        if(formatter == null) {
            formatter = DateTimeFormatter.ofPattern(pattern);
            formatterMap.put(pattern, formatter);
        }
        return formatter;
    }
}
