<div class="form-group col-auto">
    <label for="startDate">${_labelText!}</label>
    <div class="form-inline">
        <div class="input-group">
            <input type="text" name="${_startDateParam!}" id="startDate" class="form-control date laydatetime laystart"
                   value="${startDate!,dateFormat='yyyy-MM-dd HH:mm:ss'}" />
            <div class="input-group-append">
                <label for="startDate" class="input-group-text fa fa-calendar"></label>
            </div>
        </div>&nbsp;&nbsp;至&nbsp;&nbsp;
        <div class="input-group">
            <input type="text" name="${_endDateParam!}" id="endDate" class="form-control date laydatetime layend"
                   value="${endDate!,dateFormat='yyyy-MM-dd HH:mm:ss'}" />
            <div class="input-group-append">
                <label for="endDate" class="input-group-text fa fa-calendar"></label>
            </div>
        </div>
    </div>
</div>

<script>
    //自动补全时分秒
    function autoCompleteDate(dateId, hours, minutes, seconds) {
        let dateInput = laydate.render({
            elem: '#' + dateId,
            type: 'datetime',
            ready: function (date) {
                lay.extend(dateInput.config.dateTime, {
                    hours: hours,
                    minutes: minutes,
                    seconds: seconds,
                });
            }
        });
    }

    $(() => {
        //开始日期
        autoCompleteDate("startDate", 0, 0, 0);
        //结束日期
        autoCompleteDate("endDate", 23, 59, 59);
        //初始化默认时间
        let $resetFlag = "true";
        <% if(isEmpty(_resetDate)) {%>
            $resetFlag="true";
        <% }else {%>
            $resetFlag=${_resetDate!};
        <% }%>
        if ($resetFlag === "true"){
            $.form.resetDate();
        }
    })

</script>
