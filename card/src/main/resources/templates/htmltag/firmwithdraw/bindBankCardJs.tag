<script>
	/**
	 * 初始化
	 */
	$(() => {

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
