<script>
    // 初始化表格
    $(() => {
        let options = {
            id: "unfrozenTable",
            uniqueId: "id",
            url: "${contextPath}/fund/unfrozenRecord.action",
            sortName: "operate_time",
            modalName: "冻结资金记录"
        };
        $.table.init(options);
    });

    // 解冻确认弹出层显示
    $("#unfrozenBtn").click(function () {
        let frozenIds = $.table.selectColumns("frozenId");
        if (frozenIds.length == 0) {
            $.modal.alertWarning("请至少选择一条记录！");
            return;
        }
        let params = {
            frozenIds: frozenIds,
            accountId:${account.accountId!}
        };
        let url = "${contextPath}/fund/unfrozenFundModal.html?" + $.common.jsonObj2UrlParams(params)
        $.modal.openDefault("解冻说明", url, '60%', '400')
    });

</script>
