package com.demo.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 排期视图数据：表头日期 + 每台器材的占用行。
 */
public class ScheduleView implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<ScheduleCell> days = new ArrayList<ScheduleCell>();
    private List<ScheduleRow> rows = new ArrayList<ScheduleRow>();
    private String startText;
    private String endText;
    private String prevStartText;
    private String nextStartText;
    private String todayText;
    private int windowDays;
    private int totalBusySlots;

    public List<ScheduleCell> getDays() {
        return days;
    }

    public void setDays(List<ScheduleCell> days) {
        this.days = days;
    }

    public List<ScheduleRow> getRows() {
        return rows;
    }

    public void setRows(List<ScheduleRow> rows) {
        this.rows = rows;
    }

    public String getStartText() {
        return startText;
    }

    public void setStartText(String startText) {
        this.startText = startText;
    }

    public String getEndText() {
        return endText;
    }

    public void setEndText(String endText) {
        this.endText = endText;
    }

    public String getPrevStartText() {
        return prevStartText;
    }

    public void setPrevStartText(String prevStartText) {
        this.prevStartText = prevStartText;
    }

    public String getNextStartText() {
        return nextStartText;
    }

    public void setNextStartText(String nextStartText) {
        this.nextStartText = nextStartText;
    }

    public String getTodayText() {
        return todayText;
    }

    public void setTodayText(String todayText) {
        this.todayText = todayText;
    }

    public int getWindowDays() {
        return windowDays;
    }

    public void setWindowDays(int windowDays) {
        this.windowDays = windowDays;
    }

    public int getTotalBusySlots() {
        return totalBusySlots;
    }

    public void setTotalBusySlots(int totalBusySlots) {
        this.totalBusySlots = totalBusySlots;
    }
}
