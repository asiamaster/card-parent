<script>
    $("#frozen-fund-form").validate({
        onkeyup: false,
        rules: {
            amount: {
                required: true,
                gt: 0.01
            },
            mark: {
                maxlength: 30
            },
        },
        messages: {
            amount: {
                required: "冻结金额必填",
                gt: "冻结金额至少{0}"
            },
            mark: {
                maxlength: "最多可以输入{0}个字符"
            }
        },
        focusCleanup: true
    });

    function limitNum(obj) {
        //只能输入两个小数
        obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');
        //只能输入大于0
        if (obj.value && obj.value <= 0) {
            obj.value = 0;
        }
    }


    //提交冻结资金操作
    function submitFrozen() {
        let requestData = {
            cardNo:${detail.cardAssociation.primary.cardNo!},
            accountId:${detail.cardAssociation.primary.accountId!},
            customerId:${detail.cardAssociation.primary.customerId!},
        };
        let url = '${contextPath}/fund/frozen.action';

        if (!$.validate.form('frozen-fund-form')) {
            return;
        }
        $.modal.confirm("确认冻结卡号【" + ${detail.cardAssociation.primary.cardNo!} +"】的资金吗?", function (sure) {
            if (!sure) {
                return;
            }
            let data = $.common.formToJSON('frozen-fund-form');
            $.operate.post(url, $.extend(requestData, data),function (result) {
                if (result.code == '200'){
                    $.tab.refresh()
                }
            });
        });
    }

    //表格隐藏、显示状态切换
    function showOrHide() {
        let $table = $("#table-div");
        if ($table.is(":hidden")) {
            $table.show();
            $.table.refresh()
        } else {
            $table.hide();
        }
    }

    //初始化表格
    $(() => {
        let options = {
            url: "${contextPath}/serial/business/page.action",
            sortName: "operate_time",
            modalName: "冻结资金记录"
        };
        $.table.init(options);
    });
</script>
