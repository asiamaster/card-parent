<script>
    $("#frozen-fund-form").validate({
        onkeyup: false,
        rules: {
            mark: {
                maxlength: 30
            }
        },
        messages: {
            mark: {
                maxlength: "最多可以输入{0}个字符"
            }
        },
        focusCleanup: true
    });


    //提交冻结资金操作
    function submitFrozen() {
        let requestData = {
            cardNo:${detail.cardAssociation.primary.cardNo!},
            accountId:${detail.cardAssociation.primary.accountId!},
            customerId:${detail.cardAssociation.primary.customerId!},
        };
        let url = '${contextPath}/account/frozen.action';

        if (!$.validate.form('account-fund-form')) {
            return;
        }
        $.modal.confirm("确认冻结账户吗?", function (sure) {
            if (!sure) {
                return;
            }
            let data = $.common.formToJSON('frozen-account-form');
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
            modalName: "冻结账户记录"
        };
        $.table.init(options);
    });

</script>
