<script>
    $(function () {
        let options = {
            uniqueId: "id",
            url: "${contextPath}/accountQuery/page.action",
            sortName: "card_create_time",
            modalName: "卡查询"
        };
        $.table.init(options);
    });

    function redirectToDetail() {
        let selectedCardNo = $.table.selectColumns("cardNo");
        if ($.common.isEmpty(selectedCardNo)) {
            $.modal.alertWarning("请至少选中一行");
            return
        }
        let params = {
            cardNo: selectedCardNo,
            accountId: $.table.selectColumns("accountId"),
            accountPkId: $.table.selectColumns("accountPkId"),
            cardPkId: $.table.selectColumns("cardPkId")
        };
        let urlParams = $.common.jsonObj2UrlParams(params);
        window.location.href = "${contextPath}/accountQuery/detailTab.html?" + urlParams
    }

    function cardNoFormatter(value, row, index, field) {
        return '<div class="" style="color: #007BFF" href="javascript:void(0);">' + value + '</div> ';
    }

    /*每隔4位空格一下*/
    function insertSpace(value) {
        let out = [];
        for (let i = 0; i < value.length; i++) {
            out.push(value.charAt(i))
            if ((i + 1) % 4 === 0) {
                out.push(" ")
            }
        }
        return out.join("");
    }

    function readCard() {
        let result = readerCardNumber();
        if (!result.success) {
            $.modal.alertWarning(result.message);
            return;
        }
        let cardNo = result.data;
        if ($.common.isEmpty(cardNo)) {
            parent.$.modal.alertWarning("请将卡片放置在读卡器上");
            return;
        }
        $("#cardNo").val(cardNo)
    }
</script>
