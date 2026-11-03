<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div class="page-head">
    <div>
        <h1>档期排期</h1>
        <p class="sub">
            <s:property value="view.startText"/> 至 <s:property value="view.endText"/>，
            共 <s:property value="view.rows.size()"/> 台器材、占用 <s:property value="view.totalBusySlots"/> 个「器材·天」。
        </p>
    </div>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/rental-form.action">新建租赁单</a>
</div>

<div class="card">
    <div class="toolbar">
        <a class="btn btn-sm" href="${pageContext.request.contextPath}/schedule.action?start=<s:property value='view.prevStartText'/>&amp;days=<s:property value='view.windowDays'/>">← 上一周期</a>
        <a class="btn btn-sm" href="${pageContext.request.contextPath}/schedule.action?days=<s:property value='view.windowDays'/>">回到今天</a>
        <a class="btn btn-sm" href="${pageContext.request.contextPath}/schedule.action?start=<s:property value='view.nextStartText'/>&amp;days=<s:property value='view.windowDays'/>">下一周期 →</a>
        <span class="spacer"></span>
        <form class="filter" method="get" action="${pageContext.request.contextPath}/schedule.action">
            <input type="hidden" name="start" value="<s:property value='view.startText'/>">
            <select name="days">
                <option value="7" <s:if test="view.windowDays == 7">selected</s:if>>7 天</option>
                <option value="14" <s:if test="view.windowDays == 14">selected</s:if>>14 天</option>
                <option value="21" <s:if test="view.windowDays == 21">selected</s:if>>21 天</option>
                <option value="30" <s:if test="view.windowDays == 30">selected</s:if>>30 天</option>
            </select>
            <button type="submit" class="btn btn-sm">应用</button>
        </form>
    </div>

    <div class="legend">
        <span><i class="free"></i>空闲</span>
        <span><i class="booked"></i>已预订</span>
        <span><i class="rented"></i>已租出</span>
        <span><i class="maint"></i>维护中</span>
        <span>鼠标悬停格子可查看订单与客户信息</span>
    </div>

    <div class="schedule-wrap">
        <table class="schedule">
            <thead>
            <tr>
                <th class="eq">器材 / 日期</th>
                <s:iterator value="view.days" var="d">
                    <th class="<s:if test='#d.weekend'>weekend</s:if> <s:if test='#d.today'>today</s:if>">
                        <span class="d"><s:property value="#d.dateText"/></span>
                        <span class="w"><s:property value="#d.weekText"/></span>
                    </th>
                </s:iterator>
            </tr>
            </thead>
            <tbody>
            <s:if test="view.rows.isEmpty()">
                <tr>
                    <td class="eq">暂无器材</td>
                    <td class="cell cell-free" colspan="<s:property value='view.windowDays'/>">请先在器材台账中添加器材</td>
                </tr>
            </s:if>
            <s:iterator value="view.rows" var="row">
                <tr>
                    <td class="eq">
                        <span class="code"><s:property value="#row.equipment.code"/> · <s:property value="#row.equipment.category"/></span>
                        <span class="name"><s:property value="#row.equipment.name"/></span>
                        <span class="free">出租 <s:property value="#row.busyDays"/> 天 · <s:property value="#row.freeRange"/></span>
                    </td>
                    <s:iterator value="#row.cells" var="c">
                        <td class="cell <s:property value='#c.cssClass'/> <s:if test='#c.today'>cell-today</s:if>"
                            title="<s:property value='#c.tooltip'/>">
                            <s:property value="#c.label"/>
                        </td>
                    </s:iterator>
                </tr>
            </s:iterator>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
