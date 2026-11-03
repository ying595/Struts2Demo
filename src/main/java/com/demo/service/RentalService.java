package com.demo.service;

import com.demo.domain.Equipment;
import com.demo.domain.RentalOrder;
import com.demo.store.DataStore;
import com.demo.util.DateUtil;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 租赁核心业务：下单（含档期冲突校验）、归还（含逾期费/损坏赔偿）、取消。
 */
public class RentalService {

    /** 逾期违约金系数：逾期每天按日租金的 1.5 倍计费 */
    public static final double LATE_FEE_RATE = 1.5;

    private final DataStore store = DataStore.get();

    public List<RentalOrder> list(String keyword, String status) {
        List<RentalOrder> result = new ArrayList<RentalOrder>();
        for (RentalOrder o : store.allOrders()) {
            if (keyword != null && !keyword.trim().isEmpty()) {
                String kw = keyword.trim().toLowerCase();
                if (!contains(o.getCustomerName(), kw) && !contains(o.getOrderNo(), kw)
                        && !contains(o.getEquipmentName(), kw) && !contains(o.getPhone(), kw)) {
                    continue;
                }
            }
            if (status != null && !status.trim().isEmpty() && !status.equals(o.getStatus())) {
                continue;
            }
            result.add(o);
        }
        result.sort(Comparator.comparing(RentalOrder::getId).reversed());
        return result;
    }

    public RentalOrder get(Long id) {
        return store.order(id);
    }

    /** 创建租赁单 */
    public synchronized RentalOrder rent(Long equipmentId, String customerName, String phone,
                                         LocalDate start, LocalDate end, String remark) {
        Equipment equipment = store.equipment(equipmentId);
        if (equipment == null) {
            throw new BusinessException("请选择要出租的器材");
        }
        if (equipment.isUnderMaintenance()) {
            throw new BusinessException("器材 " + equipment.getCode() + " 正在维护中，暂不可出租");
        }
        if (isBlank(customerName)) {
            throw new BusinessException("请填写客户名称");
        }
        if (isBlank(phone) || phone.trim().length() < 6) {
            throw new BusinessException("请填写有效的联系电话");
        }
        if (start == null || end == null) {
            throw new BusinessException("请选择租赁开始日期和结束日期");
        }
        if (end.isBefore(start)) {
            throw new BusinessException("结束日期不能早于开始日期");
        }
        if (start.isBefore(LocalDate.now())) {
            throw new BusinessException("开始日期不能早于今天（" + DateUtil.format(LocalDate.now()) + "）");
        }

        RentalOrder conflict = findConflict(equipmentId, start, end, null);
        if (conflict != null) {
            throw new BusinessException("档期冲突：器材 " + equipment.getCode() + " 已被订单 "
                    + conflict.getOrderNo() + "（" + conflict.getTermText() + "）占用");
        }

        RentalOrder order = new RentalOrder();
        order.setId(store.nextOrderId());
        order.setOrderNo(store.nextOrderNo(LocalDate.now()));
        order.setEquipmentId(equipmentId);
        order.setEquipmentCode(equipment.getCode());
        order.setEquipmentName(equipment.getName());
        order.setCustomerName(customerName.trim());
        order.setPhone(phone.trim());
        order.setStartDate(start);
        order.setEndDate(end);
        order.setDays((int) ChronoUnit.DAYS.between(start, end) + 1);
        order.setDailyRate(equipment.getDailyRate());
        order.setRentAmount(order.getDays() * equipment.getDailyRate());
        order.setDeposit(equipment.getDeposit());
        // 今天开始取机即进入租用中，未来日期则先占档期为已预订
        order.setStatus(start.isAfter(LocalDate.now()) ? RentalOrder.STATUS_BOOKED : RentalOrder.STATUS_RENTING);
        order.setRemark(remark == null ? "" : remark.trim());
        store.saveOrder(order);
        refreshEquipmentStatus(equipmentId);
        return order;
    }

    /** 归还器材 */
    public synchronized RentalOrder returnOrder(Long orderId, LocalDate returnDate,
                                                double damageFee, String returnRemark) {
        RentalOrder order = store.order(orderId);
        if (order == null) {
            throw new BusinessException("租赁单不存在");
        }
        if (RentalOrder.STATUS_RETURNED.equals(order.getStatus())) {
            throw new BusinessException("该订单已归还，无需重复操作");
        }
        if (RentalOrder.STATUS_CANCELLED.equals(order.getStatus())) {
            throw new BusinessException("该订单已取消，无法归还");
        }
        if (returnDate == null) {
            throw new BusinessException("请填写实际归还日期");
        }
        if (returnDate.isBefore(order.getStartDate())) {
            throw new BusinessException("归还日期不能早于取机日期 " + order.getStartDateText());
        }
        if (damageFee < 0) {
            throw new BusinessException("损坏赔偿金额不能为负数");
        }

        int lateDays = 0;
        double lateFee = 0;
        if (returnDate.isAfter(order.getEndDate())) {
            lateDays = (int) ChronoUnit.DAYS.between(order.getEndDate(), returnDate);
            lateFee = lateDays * order.getDailyRate() * LATE_FEE_RATE;
        }
        order.setReturnDate(returnDate);
        order.setLateDays(lateDays);
        order.setLateFee(round2(lateFee));
        order.setDamageFee(round2(damageFee));
        order.setReturnRemark(returnRemark == null ? "" : returnRemark.trim());
        order.setStatus(RentalOrder.STATUS_RETURNED);
        store.saveOrder(order);

        refreshEquipmentStatus(order.getEquipmentId());
        return order;
    }

    /** 取消未取机的预订 */
    public synchronized void cancel(Long orderId) {
        RentalOrder order = store.order(orderId);
        if (order == null) {
            throw new BusinessException("租赁单不存在");
        }
        if (!RentalOrder.STATUS_BOOKED.equals(order.getStatus())) {
            throw new BusinessException("只有「已预订」的订单可以取消，当前状态：" + order.getStatus());
        }
        order.setStatus(RentalOrder.STATUS_CANCELLED);
        store.saveOrder(order);
        refreshEquipmentStatus(order.getEquipmentId());
    }

    /** 查找与目标区间冲突的有效订单 */
    public RentalOrder findConflict(Long equipmentId, LocalDate from, LocalDate to, Long excludeOrderId) {
        for (RentalOrder o : store.allOrders()) {
            if (!o.getEquipmentId().equals(equipmentId)) {
                continue;
            }
            if (excludeOrderId != null && excludeOrderId.equals(o.getId())) {
                continue;
            }
            if (o.overlaps(from, to)) {
                return o;
            }
        }
        return null;
    }

    /** 器材当天是否被占用 */
    public RentalOrder findActiveOn(Long equipmentId, LocalDate date) {
        RentalOrder booked = null;
        for (RentalOrder o : store.allOrders()) {
            if (!o.getEquipmentId().equals(equipmentId) || !o.occupies(date)) {
                continue;
            }
            if (RentalOrder.STATUS_RENTING.equals(o.getStatus())) {
                return o;
            }
            booked = o;
        }
        return booked;
    }

    public int countByStatus(String status) {
        int count = 0;
        for (RentalOrder o : store.allOrders()) {
            if (status.equals(o.getStatus())) {
                count++;
            }
        }
        return count;
    }

    public int countAll() {
        return store.allOrders().size();
    }

    /** 根据是否存在「今天占用」的订单，回写器材状态 */
    public void refreshEquipmentStatus(Long equipmentId) {
        Equipment equipment = store.equipment(equipmentId);
        if (equipment == null || equipment.isUnderMaintenance()) {
            return;
        }
        equipment.setStatus(findActiveOn(equipmentId, LocalDate.now()) != null
                ? Equipment.STATUS_RENTED : Equipment.STATUS_AVAILABLE);
    }

    private double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    private boolean contains(String text, String keyword) {
        return text != null && text.toLowerCase().contains(keyword);
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
