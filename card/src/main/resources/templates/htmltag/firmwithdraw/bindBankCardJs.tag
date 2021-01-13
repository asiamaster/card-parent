<script>
	/**
	 * 初始化
	 */
	$(() => {

	});

    /**
	 * 授权绑定,密码验证
	 */
	$(document).on('click', '#authBindBtn', function () {
// let a = bui.util.debounce(saveOrUpdateHandler,1000,true);
        let dia = bs4pop.dialog({
            title: '授权绑定',// 对话框title
            content: bui.util.HTMLDecode(template('checkPassword', {})), // 对话框内容，可以是
            // string、element，$object
            width: '500px',// 宽度
            height: '200px',// 高度
            btns: [{
                label: '确定', className: 'btn-primary', onClick(e) {
                    let pwd = $("#password").val();
                    let accountId = $("#accountId").val();
                    let param = JSON.stringify({
                        loginPwd: pwd,
                        accountId: accountId
                    });
                    $.ajax({
                        type: 'POST',
                        url: '${contextPath}/firmwithdraw/checkPwd.action',
                        dataType: 'json',
                        contentType: "application/json; charset=utf-8",
                        data: param,
                        success: function (result) {
                            if (result.success) {
                                // 已绑定银行卡列表数据
                                bindBankCardTable();
                                $("#bankCardTableDiv").show();
                                return true;
                            } else {
                                bs4pop.alert(result.message, {type: 'error'});
                            }
                        }
                    });
                }
            }, {
                label: '取消', className: 'btn-secondary', onClick(e) {
                    return true;
                }
            }]
        });
    });

	/**
	 * 刷新
	 */
	$("#refreshBtn").click(function(){
		$.table.refresh();
    });

	/**
	 * 打开添加银行卡页面
	 */
	$("#openAddHtmlBtn").click(function(){
	    $.modal.openDefault("新增银行卡绑定",'${contextPath}/firmwithdraw/toAddBankCard.html','600px','80%');
    });

</script>
