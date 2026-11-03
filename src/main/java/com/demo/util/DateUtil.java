package com.demo.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * 日期工具：统一使用 yyyy-MM-dd 文本与 LocalDate 互转。
 */
public final class DateUtil {

    public static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter COMPACT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final String[] WEEK = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

    private DateUtil() {
    }

    public static LocalDate parse(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(text.trim(), FMT);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("日期格式不正确，应为 yyyy-MM-dd：" + text);
        }
    }

    public static String format(LocalDate date) {
        return date == null ? "" : FMT.format(date);
    }

    public static String compact(LocalDate date) {
        return date == null ? "" : COMPACT.format(date);
    }

    public static String week(LocalDate date) {
        return date == null ? "" : WEEK[date.getDayOfWeek().getValue() - 1];
    }

    public static String monthDay(LocalDate date) {
        return date == null ? "" : String.format("%02d-%02d", date.getMonthValue(), date.getDayOfMonth());
    }
}
