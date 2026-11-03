package com.demo.domain;

import com.demo.util.DateUtil;
import com.demo.util.Fmt;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 租赁单：一条订单对应一台器材在某个时间段内的占用。
 */
public class RentalOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String STATUS_BOOKED = "已预订";
    public static final String STATUS_RENTING = "租用中";
    public static final String STATUS_RETURNED = "已归还";
    public static final String STATUS_CANCELLED = "已取消";

    private Long id;
    private Long equipmentId;
    private String orderNo;
    private String equipmentCode;
    private String equipmentName;
    private String customerName;
    private String phone;
    private LocalDate startDate;
    private LocalDate endDate;
    private int days;
    private double dailyRate;
    private double rentAmount;
    private double deposit;
    private String status = STATUS_BOOKED;
    private LocalDate returnDate;
    private int lateDays;
    private double lateFee;
    private double damageFee;
    private String remark;
    private String returnRemark;
    private LocalDateTime createdAt = LocalDateTime.now();

    /** 该订单是否仍占用器材档期 */
    public boolean isActive() {
        return STATUS_BOOKED.equals(status) || STATUS_RENTING.equals(status);
    }

    /** 是否与给定区间重叠（闭区间） */
    public boolean overlaps(LocalDate from, LocalDate to) {
        return isActive() && !to.isBefore(startDate) && !from.isAfter(endDate);
    }

    /** 是否占用指定日期 */
    public boolean occupies(LocalDate date) {
        return isActive() && !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    public double getExtraFee() {
        return lateFee + damageFee;
    }

    public double getTotalAmount() {
        return rentAmount + getExtraFee();
    }

    public String getStartDateText() {
        return DateUtil.format(startDate);
    }

    public String getEndDateText() {
        return DateUtil.format(endDate);
    }

    public String getReturnDateText() {
        return DateUtil.format(returnDate);
    }

    public String getTermText() {
        return getStartDateText() + " ~ " + getEndDateText();
    }

    public String getRentAmountText() {
        return Fmt.money(rentAmount);
    }

    public String getDepositText() {
        return Fmt.money(deposit);
    }

    public String getLateFeeText() {
        return Fmt.money(lateFee);
    }

    public String getDamageFeeText() {
        return Fmt.money(damageFee);
    }

    public String getExtraFeeText() {
        return Fmt.money(getExtraFee());
    }

    public String getTotalAmountText() {
        return Fmt.money(getTotalAmount());
    }

    public String getDailyRateText() {
        return Fmt.moneyShort(dailyRate);
    }

    public String getCreatedAtText() {
        return createdAt == null ? "" : createdAt.toString().replace('T', ' ').substring(0, 16);
    }

    public String getStatusCss() {
        if (STATUS_RENTING.equals(status)) {
            return "busy";
        }
        if (STATUS_BOOKED.equals(status)) {
            return "warn";
        }
        if (STATUS_CANCELLED.equals(status)) {
            return "muted";
        }
        return "ok";
    }

    /** 距应还日期已逾期天数（用于归还页面提示） */
    public long getOverdueDaysUntilToday() {
        LocalDate today = LocalDate.now();
        if (endDate == null || !today.isAfter(endDate)) {
            return 0;
        }
        return ChronoUnit.DAYS.between(endDate, today);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(double dailyRate) {
        this.dailyRate = dailyRate;
    }

    public double getRentAmount() {
        return rentAmount;
    }

    public void setRentAmount(double rentAmount) {
        this.rentAmount = rentAmount;
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

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public int getLateDays() {
        return lateDays;
    }

    public void setLateDays(int lateDays) {
        this.lateDays = lateDays;
    }

    public double getLateFee() {
        return lateFee;
    }

    public void setLateFee(double lateFee) {
        this.lateFee = lateFee;
    }

    public double getDamageFee() {
        return damageFee;
    }

    public void setDamageFee(double damageFee) {
        this.damageFee = damageFee;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReturnRemark() {
        return returnRemark;
    }

    public void setReturnRemark(String returnRemark) {
        this.returnRemark = returnRemark;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
