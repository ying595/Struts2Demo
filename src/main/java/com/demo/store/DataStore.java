package com.demo.store;

import com.demo.domain.Equipment;
import com.demo.domain.RentalOrder;
import com.demo.util.DateUtil;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 内存数据仓库（单例，应用启动即带演示数据）。
 * 如需接数据库，只需把这里的方法替换为 DAO 实现，Service/Action 无需改动。
 */
public final class DataStore {

    private static final DataStore INSTANCE = new DataStore();

    public static DataStore get() {
        return INSTANCE;
    }

    private final Map<Long, Equipment> equipments = new ConcurrentHashMap<Long, Equipment>();
    private final Map<Long, RentalOrder> orders = new ConcurrentHashMap<Long, RentalOrder>();
    private final AtomicLong equipmentSeq = new AtomicLong();
    private final AtomicLong orderSeq = new AtomicLong();
    private final AtomicLong orderNoSeq = new AtomicLong();

    private DataStore() {
        seed();
    }

    public long nextEquipmentId() {
        return equipmentSeq.incrementAndGet();
    }

    public long nextOrderId() {
        return orderSeq.incrementAndGet();
    }

    public String nextOrderNo(LocalDate date) {
        String suffix = String.format("%03d", orderNoSeq.incrementAndGet());
        return "R" + DateUtil.compact(date) + suffix;
    }

    public Collection<Equipment> allEquipments() {
        return equipments.values();
    }

    public Equipment equipment(Long id) {
        return id == null ? null : equipments.get(id);
    }

    public void saveEquipment(Equipment equipment) {
        equipments.put(equipment.getId(), equipment);
    }

    public void removeEquipment(Long id) {
        if (id != null) {
            equipments.remove(id);
        }
    }

    public Collection<RentalOrder> allOrders() {
        return orders.values();
    }

    public RentalOrder order(Long id) {
        return id == null ? null : orders.get(id);
    }

    public void saveOrder(RentalOrder order) {
        orders.put(order.getId(), order);
    }

    // ------------------------------------------------------------------
    // 演示数据
    // ------------------------------------------------------------------

    private void seed() {
        addEquipment("CAM-001", "索尼 FX3 全画幅电影机", "相机机身", "索尼", 480, 3000,
                Equipment.STATUS_RENTED, "含 XLR 手柄、2 块电池");
        addEquipment("CAM-002", "佳能 EOS R5 微单", "相机机身", "佳能", 260, 2000,
                Equipment.STATUS_AVAILABLE, "8K RAW，含充电器");
        addEquipment("LEN-001", "索尼 FE 24-70mm F2.8 GM II", "镜头", "索尼", 180, 2000,
                Equipment.STATUS_AVAILABLE, "人像/口播通用");
        addEquipment("LEN-002", "适马 18-35mm F1.8 Art", "镜头", "适马", 90, 800,
                Equipment.STATUS_AVAILABLE, "半画幅大光圈");
        addEquipment("LGT-001", "爱图仕 LS 600D 补光灯", "灯光", "爱图仕", 120, 1200,
                Equipment.STATUS_AVAILABLE, "含柔光箱");
        addEquipment("LGT-002", "南冠 PavoTube 30C 灯管", "灯光", "南冠", 60, 500,
                Equipment.STATUS_AVAILABLE, "两条套装");
        addEquipment("AUD-001", "罗德 Wireless GO II 无线麦", "收音", "罗德", 50, 600,
                Equipment.STATUS_AVAILABLE, "一拖二");
        addEquipment("AUD-002", "索尼 ECM-B1M 机头麦", "收音", "索尼", 45, 500,
                Equipment.STATUS_AVAILABLE, "数字音频接口");
        addEquipment("STB-001", "大疆 RS 4 Pro 稳定器", "稳定器", "大疆", 150, 1500,
                Equipment.STATUS_AVAILABLE, "含双手持配件");
        addEquipment("ACC-001", "曼富图 504X 液压三脚架", "其它", "曼富图", 40, 400,
                Equipment.STATUS_AVAILABLE, "纪录片常用");
        addEquipment("ACC-002", "五合一反光板 110cm", "其它", "神牛", 15, 100,
                Equipment.STATUS_MAINTENANCE, "支架损坏，待维修");

        LocalDate today = LocalDate.now();

        // 一条正在租用中的订单：让排期表与租出状态有数据
        addOrder(1L, "星野传媒 · 王磊", "13800001111",
                today.minusDays(2), today.plusDays(2), RentalOrder.STATUS_RENTING,
                null, 0, 0, "品牌短片拍摄，含备用电池");
        // 一条未来预订
        addOrder(5L, "拾光工作室 · 李婷", "13900002222",
                today.plusDays(3), today.plusDays(5), RentalOrder.STATUS_BOOKED,
                null, 0, 0, "直播间补光");
        // 一条历史已归还订单
        addOrder(3L, "微光影像 · 陈晨", "13700003333",
                today.minusDays(10), today.minusDays(7), RentalOrder.STATUS_RETURNED,
                today.minusDays(7), 0, 0, "婚礼跟拍");
    }

    private Equipment addEquipment(String code, String name, String category, String brand,
                                   double rate, double deposit, String status, String remark) {
        Equipment e = new Equipment();
        e.setId(nextEquipmentId());
        e.setCode(code);
        e.setName(name);
        e.setCategory(category);
        e.setBrand(brand);
        e.setDailyRate(rate);
        e.setDeposit(deposit);
        e.setStatus(status);
        e.setRemark(remark);
        equipments.put(e.getId(), e);
        return e;
    }

    private RentalOrder addOrder(Long equipmentId, String customer, String phone,
                                 LocalDate start, LocalDate end, String status,
                                 LocalDate returnDate, double damageFee, double lateFee, String remark) {
        Equipment e = equipments.get(equipmentId);
        RentalOrder o = new RentalOrder();
        o.setId(nextOrderId());
        o.setOrderNo(nextOrderNo(start));
        o.setEquipmentId(equipmentId);
        if (e != null) {
            o.setEquipmentCode(e.getCode());
            o.setEquipmentName(e.getName());
            o.setDailyRate(e.getDailyRate());
            o.setDeposit(e.getDeposit());
        }
        o.setCustomerName(customer);
        o.setPhone(phone);
        o.setStartDate(start);
        o.setEndDate(end);
        o.setDays((int) (end.toEpochDay() - start.toEpochDay()) + 1);
        o.setRentAmount(o.getDays() * o.getDailyRate());
        o.setStatus(status);
        o.setReturnDate(returnDate);
        o.setDamageFee(damageFee);
        o.setLateFee(lateFee);
        o.setRemark(remark);
        orders.put(o.getId(), o);
        return o;
    }
}
