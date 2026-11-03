package com.demo.service;

import com.demo.domain.Equipment;
import com.demo.domain.RentalOrder;
import com.demo.domain.ScheduleCell;
import com.demo.domain.ScheduleRow;
import com.demo.domain.ScheduleView;
import com.demo.util.DateUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 排期服务：把「器材 × 日期」的占用情况组织成可直接渲染的二维表。
 */
public class ScheduleService {

    public static final int DEFAULT_DAYS = 14;
    public static final int MAX_DAYS = 30;

    private final EquipmentService equipmentService = new EquipmentService();
    private final RentalService rentalService = new RentalService();

    public ScheduleView build(String startText, Integer days) {
        LocalDate start = DateUtil.parse(startText);
        if (start == null) {
            start = LocalDate.now();
        }
        int window = days == null ? DEFAULT_DAYS : days;
        window = Math.max(3, Math.min(MAX_DAYS, window));

        LocalDate end = start.plusDays(window - 1L);
        ScheduleView view = new ScheduleView();
        view.setWindowDays(window);
        view.setStartText(DateUtil.format(start));
        view.setEndText(DateUtil.format(end));
        view.setPrevStartText(DateUtil.format(start.minusDays(window)));
        view.setNextStartText(DateUtil.format(start.plusDays(window)));
        view.setTodayText(DateUtil.format(LocalDate.now()));

        List<ScheduleCell> header = new ArrayList<ScheduleCell>();
        for (int i = 0; i < window; i++) {
            header.add(new ScheduleCell(start.plusDays(i)));
        }
        view.setDays(header);

        List<ScheduleRow> rows = new ArrayList<ScheduleRow>();
        int totalBusy = 0;
        for (Equipment equipment : equipmentService.listAll()) {
            ScheduleRow row = new ScheduleRow();
            row.setEquipment(equipment);
            List<ScheduleCell> cells = new ArrayList<ScheduleCell>();
            int busyDays = 0;
            for (int i = 0; i < window; i++) {
                LocalDate date = start.plusDays(i);
                ScheduleCell cell = new ScheduleCell(date);
                if (equipment.isUnderMaintenance()) {
                    cell.occupancy(true, "维护", "cell-maint",
                            equipment.getCode() + " 维护中，暂不可排期");
                    busyDays++;
                } else {
                    RentalOrder order = rentalService.findActiveOn(equipment.getId(), date);
                    if (order != null) {
                        boolean renting = RentalOrder.STATUS_RENTING.equals(order.getStatus());
                        cell.occupancy(true, renting ? "已租" : "已订",
                                renting ? "cell-rented" : "cell-booked",
                                order.getOrderNo() + " · " + order.getCustomerName()
                                        + " · " + order.getTermText());
                        busyDays++;
                    }
                }
                cells.add(cell);
            }
            row.setCells(cells);
            row.setBusyDays(busyDays);
            row.setFreeRange("空闲 " + (window - busyDays) + " 天");
            totalBusy += busyDays;
            rows.add(row);
        }
        view.setRows(rows);
        view.setTotalBusySlots(totalBusy);
        return view;
    }
}
