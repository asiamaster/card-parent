<script>
    $("#frozen-account-form").validate({
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
    //提交冻结账户操作
    function submitFrozenAccount() {
        let requestData = {
            cardNo:${detail.cardAssociation.primary.cardNo!},
            accountId:${detail.cardAssociation.primary.accountId!},
            customerId:${detail.cardAssociation.primary.customerId!},
        };
        let url = '${contextPath}/account/frozen.action';

        if (!$.validate.form('frozen-account-form')) {
            return;
        }
        $.modal.confirm("注意：账户冻结后，所有功能业务均不可用，确认冻结当前账户吗?", function (sure) {
            if (!sure) {
                return;
            }
            let data = $.common.formToJSON('frozen-account-form');
            bui.util.debounce($.operate.post(url, $.extend(requestData, data), function (result) {
                if (result.code == web_status.SUCCESS) {
                	$("#frozen").attr("disabled", "disabled");
                	$("#disabledState").val("2");
                    $.tab.refresh()
                }
            }), 1000, true);
        });
    }

    //表格隐藏、显示状态切换
    function showOrHideFrozenAccountRecord() {
        let $table = $("#table-div-frozen-account");
        if ($table.is(":hidden")) {
            $table.show();
            let data = $.common.formToJSON('query-frozen-account');
            $.table.search("query-frozen-account","frozenAccuntTable",data);
           /*  let data = $.common.formToJSON('query-frozen-account');
            let options = {
                	id: "frozenAccuntTable",
                    url: "${contextPath}/serial/business/page.action",
                    sortName: "operate_time",
                    queryParams:data
                };
            $("#frozenAccuntTable").bootstrapTable('refresh', options); */
        } else {
            $table.hide();
        }
    }

    //初始化表格
    $(() => {
    	if($("#disabledState").val() == 2){
    		$("#frozen").attr("disabled", "disabled");
    	}
    	let data = $.common.formToJSON('query-frozen-account');
    	data.page=1;
    	data.rows=20;
    	data.order="DESC";
    	data.sort="operate_time";
        let options = {
        	id: "frozenAccuntTable",
            url: "${contextPath}/serial/business/page.action",
            modalName: "冻结账户记录",
            queryParams:data
        };
        $.table.init(options);
    });

</script>
