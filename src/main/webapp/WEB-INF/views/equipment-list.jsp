<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div class="page-head">
    <div>
        <h1>器材台账</h1>
        <p class="sub">共 <s:property value="equipments.size()"/> 件器材，编号唯一，可按名称 / 编号 / 品牌检索。</p>
    </div>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/equipment-form.action">新增器材</a>
</div>

<form class="card filter" method="get" action="${pageContext.request.contextPath}/equipment-list.action">
    <input type="text" name="keyword" placeholder="搜索名称 / 编号 / 品牌" value="<s:property value='keyword'/>">
    <select name="category">
        <option value="">全部类别</option>
        <s:iterator value="categories" var="cat">
            <option value="<s:property value='#cat'/>" <s:if test="category == #cat">selected</s:if>><s:property value="#cat"/></option>
        </s:iterator>
    </select>
    <button type="submit" class="btn">筛选</button>
    <a class="btn" href="${pageContext.request.contextPath}/equipment-list.action">重置</a>
</form>

<div class="card">
    <table class="grid">
        <thead>
        <tr>
            <th>编号</th>
            <th>器材名称</th>
            <th>类别</th>
            <th>品牌</th>
            <th>日租金</th>
            <th>押金</th>
            <th>状态</th>
            <th class="op">操作</th>
        </tr>
        </thead>
        <tbody>
        <s:if test="equipments.isEmpty()">
            <tr>
                <td colspan="8" class="empty">没有符合条件的器材</td>
            </tr>
        </s:if>
        <s:iterator value="equipments" var="e">
            <tr>
                <td class="mono"><s:property value="#e.code"/></td>
                <td>
                    <strong><s:property value="#e.name"/></strong>
                    <div class="muted"><s:property value="#e.remark"/></div>
                </td>
                <td><s:property value="#e.category"/></td>
                <td><s:property value="#e.brand"/></td>
                <td>￥<s:property value="#e.dailyRateText"/></td>
                <td>￥<s:property value="#e.depositText"/></td>
                <td><span class="badge badge-<s:property value='#e.statusCss'/>"><s:property value="#e.status"/></span></td>
                <td class="op">
                    <a class="link" href="${pageContext.request.contextPath}/rental-form.action?equipmentId=<s:property value='#e.id'/>">发起租赁</a>
                    <a class="link" href="${pageContext.request.contextPath}/equipment-form.action?equipment.id=<s:property value='#e.id'/>">编辑</a>
                    <a class="link danger" href="${pageContext.request.contextPath}/equipment-delete.action?equipment.id=<s:property value='#e.id'/>"
                       onclick="return confirm('确认删除该器材？');">删除</a>
                </td>
            </tr>
        </s:iterator>
        </tbody>
    </table>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
