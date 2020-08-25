<script>
    // 初始化表格
    $(() => {
        let options = {
        	id: "cardStorageTable",
        	uniqueId: "id",
            url: "${contextPath}/cardStorage/queryCardStorageList.action",
            sortName: "create_time",
            modalName: "卡库存列表"
        };
        $.table.init(options);
    });
    
    // 根据选中行状态，对作废按钮进行禁用或启用
    $("#cardStorageTable").on('check.bs.table', function (e, row, $element) {
    	let state = $.table.selectColumns("state");
    	// USED("在用", 1), ACTIVE("激活", 2), VOID("作废", 3), UNACTIVATE("未激活", 4)
    	let activeCode = ${@com.dili.card.type.CardStorageState.ACTIVE.getCode()};
    	let unactivateCode = ${@com.dili.card.type.CardStorageState.UNACTIVATE.getCode()};
    	if(state == activeCode || state == unactivateCode){
    		$("#voidBtn").removeAttr("disabled", "disabled");
    	}else{
    		$("#voidBtn").attr("disabled", "disabled");
    	}
    });
    
    // 作废弹出层显示
    $("#voidBtn").click(function(){
    	let rows = $("#cardStorageTable").bootstrapTable('getSelections').length;
    	if(rows>0){
    		$("#voidModal").modal("show");
    	}else{
    		showError("您没有选择任何卡!");
    	}
    });
    
    // 作废操作
    $("#voidOkBtn").click(function(){
    	let cardNo = $.table.selectColumns("cardNo");
    	$.ajax({
            type:'POST',
            url:'${contextPath}/cardStorage/cardVoid.action',
            dataType:'json',
            traditional:true,
            data: {
            	cardNo:cardNo
            },
            success:function(result) {
                if (result.success) {
                	showInfo("操作成功");
                }else {
                	showError(result.message);
                }
            }
        });
    });
</script>