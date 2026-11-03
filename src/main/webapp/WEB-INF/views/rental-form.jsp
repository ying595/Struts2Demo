<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div class="page-head">
    <div>
        <h1>新建租赁单</h1>
        <p class="sub">选择器材与档期，系统会自动校验该器材是否已被其他订单占用。</p>
    </div>
    <a class="btn" href="${pageContext.request.contextPath}/rental-list.action">返回订单列表</a>
</div>

<div class="two-col">
    <form class="card" method="post" action="${pageContext.request.contextPath}/rental-save.action">
        <div class="form-grid">
            <div class="field full">
                <label>租赁器材 *</label>
                <select name="equipmentId" id="equipmentSelect" required>
                    <option value="">请选择器材</option>
                    <s:iterator value="equipmentOptions" var="opt">
                        <option value="<s:property value='#opt.id'/>"
                                data-rate="<s:property value='#opt.dailyRate'/>"
                                data-deposit="<s:property value='#opt.deposit'/>"
                                <s:if test="#opt.id == equipmentId">selected</s:if>>
                            <s:property value="#opt.code"/> · <s:property value="#opt.name"/>（￥<s:property value="#opt.dailyRateText"/>/天）
                        </option>
                    </s:iterator>
                </select>
                <span class="hint">维护中的器材不会出现在此列表</span>
            </div>
            <div class="field">
                <label>客户名称 *</label>
                <input type="text" name="customerName" value="<s:property value='customerName'/>" placeholder="例如：星野传媒 · 王磊" required>
            </div>
            <div class="field">
                <label>联系电话 *</label>
                <input type="text" name="phone" value="<s:property value='phone'/>" placeholder="13800000000" required>
            </div>
            <div class="field">
                <label>取机日期 *</label>
                <input type="date" name="startDate" id="startDate" value="<s:property value='startDate'/>" required>
            </div>
            <div class="field">
                <label>应还日期 *</label>
                <input type="date" name="endDate" id="endDate" value="<s:property value='endDate'/>" required>
            </div>
            <div class="field full">
                <label>拍摄用途 / 备注</label>
                <textarea name="remark" placeholder="项目名称、取还方式、配件需求等"><s:property value='remark'/></textarea>
            </div>
        </div>

        <div class="amount-preview" id="amountPreview">
            预计租期 <b id="previewDays">—</b> 天，租金 <b id="previewAmount">—</b>，押金 <b id="previewDeposit">—</b>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary">提交租赁单</button>
            <a class="btn" href="${pageContext.request.contextPath}/rental-list.action">取消</a>
        </div>
    </form>

    <aside class="card side">
        <h3>计费规则</h3>
        <div class="kv"><span>租金</span><strong>日租金 × 租期天数（含首尾）</strong></div>
        <div class="kv"><span>押金</span><strong>按器材档案中的押金收取</strong></div>
        <div class="kv"><span>逾期违约金</span><strong>日租金 × 1.5 / 天</strong></div>
        <div class="kv"><span>损坏赔偿</span><strong>归还时按实际情况录入</strong></div>
        <div class="notice">
            取机日当天起，订单进入「租用中」且器材状态自动变为租出中；未来日期的订单先以「已预订」占用档期，
            可在订单列表中取消。档期冲突会被直接拦截，避免同一台机器重复出租。
        </div>
    </aside>
</div>

<script>
    (function () {
        var select = document.getElementById('equipmentSelect');
        var start = document.getElementById('startDate');
        var end = document.getElementById('endDate');
        var daysEl = document.getElementById('previewDays');
        var amountEl = document.getElementById('previewAmount');
        var depositEl = document.getElementById('previewDeposit');

        function money(v) {
            return '￥' + (Math.round(v * 100) / 100).toFixed(2);
        }

        function update() {
            var opt = select.options[select.selectedIndex];
            var rate = opt && opt.getAttribute('data-rate') ? parseFloat(opt.getAttribute('data-rate')) : 0;
            var deposit = opt && opt.getAttribute('data-deposit') ? parseFloat(opt.getAttribute('data-deposit')) : 0;
            var days = 0;
            if (start.value && end.value) {
                var a = new Date(start.value + 'T00:00:00');
                var b = new Date(end.value + 'T00:00:00');
                days = Math.round((b - a) / 86400000) + 1;
                if (days < 1) {
                    days = 0;
                }
            }
            daysEl.textContent = days > 0 ? days : '—';
            amountEl.textContent = days > 0 && rate > 0 ? money(days * rate) : '—';
            depositEl.textContent = deposit > 0 ? money(deposit) : '—';
        }

        select.addEventListener('change', update);
        start.addEventListener('change', update);
        end.addEventListener('change', update);
        update();
    })();
</script>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
