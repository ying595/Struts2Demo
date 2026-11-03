<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<s:if test="order == null">
    <div class="card">
        <h1>订单不存在</h1>
        <p class="sub">该租赁单可能已被删除，请返回订单列表重新选择。</p>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/rental-list.action">返回订单列表</a>
    </div>
</s:if>
<s:else>

<div class="page-head">
    <div>
        <h1>器材归还</h1>
        <p class="sub">订单 <s:property value="order.orderNo"/> · 填写实际归还日期后，系统自动结算逾期违约金。</p>
    </div>
    <a class="btn" href="${pageContext.request.contextPath}/rental-list.action">返回订单列表</a>
</div>

<div class="two-col">
    <form class="card" method="post" action="${pageContext.request.contextPath}/rental-return.action" id="returnForm"
          data-end="<s:property value='order.endDateText'/>" data-rate="<s:property value='order.dailyRate'/>">
        <input type="hidden" name="id" value="<s:property value='order.id'/>">

        <div class="form-grid">
            <div class="field">
                <label>实际归还日期 *</label>
                <input type="date" name="returnDateText" id="returnDate" value="<s:property value='returnDateText'/>" required>
                <span class="hint">应还日期：<s:property value="order.endDateText"/></span>
            </div>
            <div class="field">
                <label>损坏赔偿（元）</label>
                <input type="number" name="damageFee" id="damageFee" value="<s:property value='damageFee'/>" min="0" step="0.01">
                <span class="hint">无损坏填 0</span>
            </div>
            <div class="field full">
                <label>归还备注</label>
                <textarea name="returnRemark" placeholder="器材成色、缺失配件、清洁情况等"><s:property value='returnRemark'/></textarea>
            </div>
        </div>

        <div class="amount-preview" id="feePreview">
            逾期 <b id="previewLateDays">0</b> 天，违约金 <b id="previewLateFee">￥0.00</b>，本次结算合计 <b id="previewTotal">￥0.00</b>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary">确认归还</button>
            <a class="btn" href="${pageContext.request.contextPath}/rental-list.action">取消</a>
        </div>
    </form>

    <aside class="card side">
        <h3>订单信息</h3>
        <div class="kv"><span>订单号</span><strong><s:property value="order.orderNo"/></strong></div>
        <div class="kv"><span>客户</span><strong><s:property value="order.customerName"/></strong></div>
        <div class="kv"><span>联系电话</span><strong><s:property value="order.phone"/></strong></div>
        <div class="kv"><span>器材</span><strong><s:property value="order.equipmentCode"/> · <s:property value="order.equipmentName"/></strong></div>
        <div class="kv"><span>租期</span><strong><s:property value="order.termText"/>（<s:property value="order.days"/> 天）</strong></div>
        <div class="kv"><span>日租金</span><strong>￥<s:property value="order.dailyRateText"/></strong></div>
        <div class="kv"><span>已计租金</span><strong>￥<s:property value="order.rentAmountText"/></strong></div>
        <div class="kv"><span>押金</span><strong>￥<s:property value="order.depositText"/></strong></div>
        <div class="kv"><span>当前状态</span><strong><s:property value="order.status"/></strong></div>
        <s:if test="order.overdueDaysUntilToday > 0">
            <div class="notice">该订单已超过应还日期 <s:property value="order.overdueDaysUntilToday"/> 天，请核对逾期费用。</div>
        </s:if>
    </aside>
</div>

<script>
    (function () {
        var form = document.getElementById('returnForm');
        var endText = form.getAttribute('data-end');
        var rate = parseFloat(form.getAttribute('data-rate') || '0');
        var dateInput = document.getElementById('returnDate');
        var damageInput = document.getElementById('damageFee');
        var daysEl = document.getElementById('previewLateDays');
        var feeEl = document.getElementById('previewLateFee');
        var totalEl = document.getElementById('previewTotal');

        function money(v) {
            return '￥' + (Math.round(v * 100) / 100).toFixed(2);
        }

        function update() {
            var lateDays = 0;
            if (dateInput.value && endText) {
                var actual = new Date(dateInput.value + 'T00:00:00');
                var due = new Date(endText + 'T00:00:00');
                if (actual > due) {
                    lateDays = Math.round((actual - due) / 86400000);
                }
            }
            var lateFee = lateDays * rate * 1.5;
            var damage = parseFloat(damageInput.value || '0');
            if (isNaN(damage) || damage < 0) {
                damage = 0;
            }
            daysEl.textContent = lateDays;
            feeEl.textContent = money(lateFee);
            totalEl.textContent = money(lateFee + damage);
        }

        dateInput.addEventListener('change', update);
        damageInput.addEventListener('input', update);
        update();
    })();
</script>

</s:else>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
