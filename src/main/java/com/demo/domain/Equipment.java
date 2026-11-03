package com.demo.domain;

import com.demo.util.Fmt;

import java.io.Serializable;

/**
 * 器材（每一件都是独立编号的单品，便于排期占用到具体机器）。
 */
public class Equipment implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String STATUS_AVAILABLE = "可用";
    public static final String STATUS_RENTED = "租出中";
    public static final String STATUS_MAINTENANCE = "维护中";

    private Long id;
    private String code;
    private String name;
    private String category;
    private String brand;
    private double dailyRate;
    private double deposit;
    private String status = STATUS_AVAILABLE;
    private String remark;

    public boolean isAvailable() {
        return STATUS_AVAILABLE.equals(status);
    }

    public boolean isUnderMaintenance() {
        return STATUS_MAINTENANCE.equals(status);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(double dailyRate) {
        this.dailyRate = dailyRate;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDailyRateText() {
        return Fmt.moneyShort(dailyRate);
    }

    public String getDepositText() {
        return Fmt.moneyShort(deposit);
    }

    /** 供页面渲染徽章颜色 */
    public String getStatusCss() {
        if (STATUS_RENTED.equals(status)) {
            return "busy";
        }
        if (STATUS_MAINTENANCE.equals(status)) {
            return "warn";
        }
        return "ok";
    }
}
