<div class="form-group col-auto">
    <label for="startDate">${_labelText!}</label>
    <div class="form-inline">
        <div class="input-group">
            <input type="text" name="${_startDateParam!}" id="startDate" class="form-control date laydatetime laystart"
                   value="${startDate!,dateFormat='yyyy-MM-dd HH:mm:ss'}"/>
            <div class="input-group-append">
                <label for="startDate" class="input-group-text fa fa-calendar"></label>
            </div>
        </div>&nbsp;&nbsp;至&nbsp;&nbsp;
        <div class="input-group">
            <input type="text" name="${_endDateParam!}" id="endDate" class="form-control date laydatetime layend"
                   value="${endDate!,dateFormat='yyyy-MM-dd HH:mm:ss'}"/>
            <div class="input-group-append">
                <label for="endDate" class="input-group-text fa fa-calendar"></label>
            </div>
        </div>
    </div>
</div>
