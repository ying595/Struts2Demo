package com.demo.action;

import com.demo.domain.Equipment;
import com.demo.service.BusinessException;
import com.demo.service.EquipmentService;

import java.util.List;

/**
 * 器材台账 Action。
 */
public class EquipmentAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    private final EquipmentService equipmentService = new EquipmentService();

    private Equipment equipment = new Equipment();
    private List<Equipment> equipments;
    private List<String> categories;
    private String keyword;
    private String category;

    public String list() {
        equipments = equipmentService.list(keyword, category);
        categories = equipmentService.categories();
        return "list";
    }

    public String input() {
        categories = equipmentService.categories();
        if (equipment.getId() != null) {
            Equipment found = equipmentService.get(equipment.getId());
            if (found == null) {
                addActionError("器材不存在或已被删除");
                equipment = new Equipment();
            } else {
                equipment = found;
            }
        }
        return "form";
    }

    public String save() {
        categories = equipmentService.categories();
        if (hasErrors()) {
            return "form";
        }
        try {
            Equipment saved = equipmentService.save(equipment);
            flash("success", "器材 " + saved.getCode() + " 保存成功");
            return SUCCESS;
        } catch (BusinessException e) {
            addActionError(e.getMessage());
            return "form";
        }
    }

    public String delete() {
        try {
            equipmentService.delete(equipment.getId());
            flash("success", "器材已删除");
        } catch (BusinessException e) {
            flash("error", e.getMessage());
        }
        return SUCCESS;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public List<Equipment> getEquipments() {
        return equipments;
    }

    public List<String> getCategories() {
        return categories;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
