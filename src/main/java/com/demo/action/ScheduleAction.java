package com.demo.action;

import com.demo.domain.ScheduleView;
import com.demo.service.ScheduleService;

/**
 * 排期 Action。
 */
public class ScheduleAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    private final ScheduleService scheduleService = new ScheduleService();

    private ScheduleView view;
    private String start;
    private Integer days;

    public String index() {
        view = scheduleService.build(start, days);
        return SUCCESS;
    }

    public ScheduleView getView() {
        return view;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }
}
