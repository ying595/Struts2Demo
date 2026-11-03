package com.demo.domain;

import com.demo.util.DateUtil;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 排期表中的一个日期格子（也用于表头日期列）。
 */
public class ScheduleCell implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDate date;
    private String dateText;
    private String weekText;
    private boolean weekend;
    private boolean today;
    private boolean occupied;
    private String label;
    private String cssClass;
    private String tooltip;

    public ScheduleCell() {
    }

    public ScheduleCell(LocalDate date) {
        this.date = date;
        this.dateText = DateUtil.monthDay(date);
        this.weekText = DateUtil.week(date);
        this.weekend = date.getDayOfWeek().getValue() >= 6;
        this.today = date.equals(LocalDate.now());
        this.label = "";
        this.cssClass = "cell-free";
        this.tooltip = dateText + " " + weekText + " 空闲";
    }

    public ScheduleCell occupancy(boolean occupied, String label, String cssClass, String tooltip) {
        this.occupied = occupied;
        this.label = label;
        this.cssClass = cssClass;
        this.tooltip = tooltip;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDateText() {
        return dateText;
    }

    public String getWeekText() {
        return weekText;
    }

    public boolean isWeekend() {
        return weekend;
    }

    public boolean isToday() {
        return today;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public String getLabel() {
        return label;
    }

    public String getCssClass() {
        return cssClass;
    }

    public String getTooltip() {
        return tooltip;
    }
}
