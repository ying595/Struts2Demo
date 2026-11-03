<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div class="page-head">
    <div>
        <h1><s:if test="equipment.id == null">新增器材</s:if><s:else>编辑器材</s:else></h1>
        <p class="sub">编号建议按「类别缩写-序号」命名，例如 CAM-003、LEN-003。</p>
    </div>
    <a class="btn" href="${pageContext.request.contextPath}/equipment-list.action">返回台账</a>
</div>

<form class="card" method="post" action="${pageContext.request.contextPath}/equipment-save.action">
    <input type="hidden" name="equipment.id" value="<s:property value='equipment.id'/>">

    <div class="form-grid">
        <div class="field">
            <label>器材编号 *</label>
            <input type="text" name="equipment.code" value="<s:property value='equipment.code'/>" placeholder="CAM-003" required>
        </div>
        <div class="field">
            <label>器材名称 *</label>
            <input type="text" name="equipment.name" value="<s:property value='equipment.name'/>" placeholder="索尼 A7S3 机身" required>
        </div>
        <div class="field">
            <label>类别 *</label>
            <select name="equipment.category" required>
                <option value="">请选择类别</option>
                <s:iterator value="categories" var="cat">
                    <option value="<s:property value='#cat'/>" <s:if test="equipment.category == #cat">selected</s:if>><s:property value="#cat"/></option>
                </s:iterator>
            </select>
        </div>
        <div class="field">
            <label>品牌</label>
            <input type="text" name="equipment.brand" value="<s:property value='equipment.brand'/>" placeholder="索尼 / 佳能 / 大疆">
        </div>
        <div class="field">
            <label>日租金（元）*</label>
            <input type="number" name="equipment.dailyRate" value="<s:property value='equipment.dailyRate'/>" min="0.01" step="0.01" required>
            <span class="hint">超过应还日期归还将按日租金的 1.5 倍/天计收违约金</span>
        </div>
        <div class="field">
            <label>押金（元）</label>
            <input type="number" name="equipment.deposit" value="<s:property value='equipment.deposit'/>" min="0" step="0.01">
        </div>
        <div class="field">
            <label>状态</label>
            <select name="equipment.status">
                <option value="可用" <s:if test="equipment.status == '可用'">selected</s:if>>可用</option>
                <option value="租出中" <s:if test="equipment.status == '租出中'">selected</s:if>>租出中</option>
                <option value="维护中" <s:if test="equipment.status == '维护中'">selected</s:if>>维护中</option>
            </select>
            <span class="hint">租出中 / 可用状态会在下单、归还时自动回写</span>
        </div>
        <div class="field full">
            <label>备注</label>
            <textarea name="equipment.remark" placeholder="配件、成色、注意事项等"><s:property value='equipment.remark'/></textarea>
        </div>
    </div>

    <div class="form-actions">
        <button type="submit" class="btn btn-primary">保存</button>
        <a class="btn" href="${pageContext.request.contextPath}/equipment-list.action">取消</a>
    </div>
</form>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
