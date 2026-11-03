package com.demo.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 排期表中的一行：一台器材 + 窗口内每天的占用情况。
 */
public class ScheduleRow implements Serializable {

    private static final long serialVersionUID = 1L;

    private Equipment equipment;
    private List<ScheduleCell> cells = new ArrayList<ScheduleCell>();
    private int busyDays;
    private String freeRange;

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public List<ScheduleCell> getCells() {
        return cells;
    }

    public void setCells(List<ScheduleCell> cells) {
        this.cells = cells;
    }

    public int getBusyDays() {
        return busyDays;
    }

    public void setBusyDays(int busyDays) {
        this.busyDays = busyDays;
    }

    public String getFreeRange() {
        return freeRange;
    }

    public void setFreeRange(String freeRange) {
        this.freeRange = freeRange;
    }
}
