package com.demo.service;

import com.demo.domain.Equipment;
import com.demo.store.DataStore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 器材台账服务：增删改查 + 状态维护。
 */
public class EquipmentService {

    public static final List<String> CATEGORIES = Arrays.asList(
            "相机机身", "镜头", "灯光", "收音", "稳定器", "其它");

    private final DataStore store = DataStore.get();

    public List<Equipment> list(String keyword, String category) {
        List<Equipment> result = new ArrayList<Equipment>();
        for (Equipment e : store.allEquipments()) {
            if (keyword != null && !keyword.trim().isEmpty()) {
                String kw = keyword.trim().toLowerCase();
                boolean hit = contains(e.getName(), kw) || contains(e.getCode(), kw) || contains(e.getBrand(), kw);
                if (!hit) {
                    continue;
                }
            }
            if (category != null && !category.trim().isEmpty() && !category.equals(e.getCategory())) {
                continue;
            }
            result.add(e);
        }
        result.sort(Comparator.comparing(Equipment::getCode));
        return result;
    }

    public List<Equipment> listAll() {
        return list(null, null);
    }

    public List<Equipment> listRentable() {
        List<Equipment> result = new ArrayList<Equipment>();
        for (Equipment e : store.allEquipments()) {
            if (!e.isUnderMaintenance()) {
                result.add(e);
            }
        }
        result.sort(Comparator.comparing(Equipment::getCode));
        return result;
    }

    public List<String> categories() {
        return CATEGORIES;
    }

    public Equipment get(Long id) {
        return store.equipment(id);
    }

    /** 新增或更新器材 */
    public Equipment save(Equipment form) {
        if (form == null) {
            throw new BusinessException("器材信息不能为空");
        }
        checkRequired(form);
        Equipment target;
        if (form.getId() == null) {
            target = new Equipment();
            target.setId(store.nextEquipmentId());
        } else {
            target = store.equipment(form.getId());
            if (target == null) {
                throw new BusinessException("器材不存在或已被删除");
            }
        }
        // 编号唯一性校验
        for (Equipment other : store.allEquipments()) {
            if (!other.getId().equals(target.getId()) && other.getCode().equalsIgnoreCase(form.getCode().trim())) {
                throw new BusinessException("器材编号 " + form.getCode() + " 已存在");
            }
        }
        target.setCode(form.getCode().trim());
        target.setName(form.getName().trim());
        target.setCategory(form.getCategory());
        target.setBrand(form.getBrand() == null ? "" : form.getBrand().trim());
        target.setDailyRate(form.getDailyRate());
        target.setDeposit(form.getDeposit());
        target.setStatus(form.getStatus() == null || form.getStatus().trim().isEmpty()
                ? Equipment.STATUS_AVAILABLE : form.getStatus());
        target.setRemark(form.getRemark() == null ? "" : form.getRemark().trim());
        store.saveEquipment(target);
        return target;
    }

    public void delete(Long id) {
        Equipment e = store.equipment(id);
        if (e == null) {
            throw new BusinessException("器材不存在");
        }
        for (com.demo.domain.RentalOrder o : store.allOrders()) {
            if (o.getEquipmentId().equals(id) && o.isActive()) {
                throw new BusinessException("器材 " + e.getCode() + " 还有未完成的租赁单，无法删除");
            }
        }
        store.removeEquipment(id);
    }

    public void updateStatus(Long id, String status) {
        Equipment e = store.equipment(id);
        if (e != null) {
            e.setStatus(status);
        }
    }

    private void checkRequired(Equipment form) {
        if (isBlank(form.getCode())) {
            throw new BusinessException("请填写器材编号，例如 CAM-003");
        }
        if (isBlank(form.getName())) {
            throw new BusinessException("请填写器材名称");
        }
        if (isBlank(form.getCategory())) {
            throw new BusinessException("请选择器材类别");
        }
        if (form.getDailyRate() <= 0) {
            throw new BusinessException("日租金必须大于 0");
        }
        if (form.getDeposit() < 0) {
            throw new BusinessException("押金不能为负数");
        }
    }

    private boolean contains(String text, String keyword) {
        return text != null && text.toLowerCase().contains(keyword);
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
