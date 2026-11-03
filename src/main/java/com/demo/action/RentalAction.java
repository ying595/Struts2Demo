package com.demo.action;

import com.demo.domain.Equipment;
import com.demo.domain.RentalOrder;
import com.demo.service.BusinessException;
import com.demo.service.EquipmentService;
import com.demo.service.RentalService;
import com.demo.util.DateUtil;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * 租赁 / 归还 Action。
 */
public class RentalAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    private final RentalService rentalService = new RentalService();
    private final EquipmentService equipmentService = new EquipmentService();

    private List<RentalOrder> orders;
    private List<Equipment> equipmentOptions;
    private RentalOrder order;

    private Long id;
    private Long equipmentId;
    private String keyword;
    private String status;
    private String customerName;
    private String phone;
    private String startDate;
    private String endDate;
    private String remark;

    private String returnDateText;
    private double damageFee;
    private String returnRemark;

    private int totalCount;
    private int bookedCount;
    private int rentingCount;
    private int returnedCount;

    public String list() {
        orders = rentalService.list(keyword, status);
        totalCount = rentalService.countAll();
        bookedCount = rentalService.countByStatus(RentalOrder.STATUS_BOOKED);
        rentingCount = rentalService.countByStatus(RentalOrder.STATUS_RENTING);
        returnedCount = rentalService.countByStatus(RentalOrder.STATUS_RETURNED);
        return "list";
    }

    public String create() {
        prepareForm();
        return "form";
    }

    public String save() {
        try {
            LocalDate start = DateUtil.parse(startDate);
            LocalDate end = DateUtil.parse(endDate);
            RentalOrder created = rentalService.rent(equipmentId, customerName, phone, start, end, remark);
            flash("success", "租赁单 " + created.getOrderNo() + " 创建成功，租金 ￥"
                    + created.getRentAmountText() + "，押金 ￥" + created.getDepositText());
            return SUCCESS;
        } catch (BusinessException | IllegalArgumentException e) {
            addActionError(e.getMessage());
            prepareForm();
            return "form";
        }
    }

    public String returnForm() {
        order = rentalService.get(id);
        if (order == null) {
            flash("error", "租赁单不存在");
            return "redirect";
        }
        if (RentalOrder.STATUS_RETURNED.equals(order.getStatus())) {
            flash("error", "订单 " + order.getOrderNo() + " 已归还");
            return "redirect";
        }
        if (returnDateText == null || returnDateText.trim().isEmpty()) {
            returnDateText = DateUtil.format(LocalDate.now());
        }
        return "return";
    }

    public String doReturn() {
        try {
            LocalDate actual = DateUtil.parse(returnDateText);
            RentalOrder done = rentalService.returnOrder(id, actual, damageFee, returnRemark);
            StringBuilder msg = new StringBuilder("订单 ").append(done.getOrderNo()).append(" 已归还");
            if (done.getLateDays() > 0) {
                msg.append("，逾期 ").append(done.getLateDays()).append(" 天，违约金 ￥").append(done.getLateFeeText());
            }
            if (done.getDamageFee() > 0) {
                msg.append("，损坏赔偿 ￥").append(done.getDamageFeeText());
            }
            flash("success", msg.toString());
            return SUCCESS;
        } catch (BusinessException | IllegalArgumentException e) {
            addActionError(e.getMessage());
            order = rentalService.get(id);
            if (order == null) {
                flash("error", "租赁单不存在");
                return "redirect";
            }
            return "return";
        }
    }

    public String cancel() {
        try {
            rentalService.cancel(id);
            flash("success", "订单已取消，器材档期已释放");
        } catch (BusinessException e) {
            flash("error", e.getMessage());
        }
        return SUCCESS;
    }

    private void prepareForm() {
        equipmentOptions = equipmentService.listRentable();
        Equipment selected = equipmentId == null ? null : equipmentService.get(equipmentId);
        if (selected != null) {
            equipmentId = selected.getId();
        }
        if (startDate == null || startDate.trim().isEmpty()) {
            startDate = DateUtil.format(LocalDate.now());
        }
        if (endDate == null || endDate.trim().isEmpty()) {
            endDate = DateUtil.format(LocalDate.now().plusDays(2));
        }
    }

    public List<String> getStatusOptions() {
        return Arrays.asList(RentalOrder.STATUS_BOOKED, RentalOrder.STATUS_RENTING,
                RentalOrder.STATUS_RETURNED, RentalOrder.STATUS_CANCELLED);
    }

    public List<RentalOrder> getOrders() {
        return orders;
    }

    public List<Equipment> getEquipmentOptions() {
        return equipmentOptions;
    }

    public RentalOrder getOrder() {
        return order;
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReturnDateText() {
        return returnDateText;
    }

    public void setReturnDateText(String returnDateText) {
        this.returnDateText = returnDateText;
    }

    public double getDamageFee() {
        return damageFee;
    }

    public void setDamageFee(double damageFee) {
        this.damageFee = damageFee;
    }

    public String getReturnRemark() {
        return returnRemark;
    }

    public void setReturnRemark(String returnRemark) {
        this.returnRemark = returnRemark;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getBookedCount() {
        return bookedCount;
    }

    public int getRentingCount() {
        return rentingCount;
    }

    public int getReturnedCount() {
        return returnedCount;
    }
}
