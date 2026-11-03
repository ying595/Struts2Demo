<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div class="page-head">
    <div>
        <h1>租赁订单</h1>
        <p class="sub">下单后自动占用器材档期；取机日当天起进入「租用中」，可在此完成归还。</p>
    </div>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/rental-form.action">新建租赁单</a>
</div>

<div class="summary">
    <div class="stat"><span>订单总数</span><strong><s:property value="totalCount"/></strong></div>
    <div class="stat accent"><span>租用中</span><strong><s:property value="rentingCount"/></strong></div>
    <div class="stat"><span>已预订</span><strong><s:property value="bookedCount"/></strong></div>
    <div class="stat"><span>已归还</span><strong><s:property value="returnedCount"/></strong></div>
</div>

<form class="card filter" method="get" action="${pageContext.request.contextPath}/rental-list.action">
    <input type="text" name="keyword" placeholder="搜索订单号 / 客户 / 器材 / 电话" value="<s:property value='keyword'/>">
    <select name="status">
        <option value="">全部状态</option>
        <s:iterator value="statusOptions" var="st">
            <option value="<s:property value='#st'/>" <s:if test="status == #st">selected</s:if>><s:property value="#st"/></option>
        </s:iterator>
    </select>
    <button type="submit" class="btn">筛选</button>
    <a class="btn" href="${pageContext.request.contextPath}/rental-list.action">重置</a>
</form>

<div class="card">
    <table class="grid">
        <thead>
        <tr>
            <th>订单号</th>
            <th>器材</th>
            <th>客户</th>
            <th>租期</th>
            <th>天数</th>
            <th>租金</th>
            <th>状态</th>
            <th class="op">操作</th>
        </tr>
        </thead>
        <tbody>
        <s:if test="orders.isEmpty()">
            <tr>
                <td colspan="8" class="empty">暂无租赁订单</td>
            </tr>
        </s:if>
        <s:iterator value="orders" var="o">
            <tr>
                <td>
                    <span class="mono"><s:property value="#o.orderNo"/></span>
                    <div class="muted"><s:property value="#o.createdAtText"/></div>
                </td>
                <td>
                    <strong><s:property value="#o.equipmentName"/></strong>
                    <div class="muted"><s:property value="#o.equipmentCode"/> · ￥<s:property value="#o.dailyRateText"/>/天</div>
                </td>
                <td>
                    <s:property value="#o.customerName"/>
                    <div class="muted"><s:property value="#o.phone"/></div>
                </td>
                <td>
                    <s:property value="#o.startDateText"/> ~ <s:property value="#o.endDateText"/>
                    <s:if test="#o.returnDate != null">
                        <div class="muted">实还：<s:property value="#o.returnDateText"/></div>
                    </s:if>
                </td>
                <td><s:property value="#o.days"/> 天</td>
                <td>
                    ￥<s:property value="#o.rentAmountText"/>
                    <s:if test="#o.extraFee > 0">
                        <div class="muted">另计 ￥<s:property value="#o.extraFeeText"/></div>
                    </s:if>
                </td>
                <td><span class="badge badge-<s:property value='#o.statusCss'/>"><s:property value="#o.status"/></span></td>
                <td class="op">
                    <s:if test="#o.active">
                        <a class="link" href="${pageContext.request.contextPath}/rental-return-form.action?id=<s:property value='#o.id'/>">归还</a>
                    </s:if>
                    <s:else>
                        <span class="muted">—</span>
                    </s:else>
                    <s:if test="#o.status == '已预订'">
                        <a class="link danger" href="${pageContext.request.contextPath}/rental-cancel.action?id=<s:property value='#o.id'/>"
                           onclick="return confirm('确认取消该预订？档期将被释放。');">取消</a>
                    </s:if>
                </td>
            </tr>
        </s:iterator>
        </tbody>
    </table>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
