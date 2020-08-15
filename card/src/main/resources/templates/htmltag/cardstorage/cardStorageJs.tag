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
    // 作废弹出层显示
    $("#voidBtn").click(function(){
    	let rows = $("#cardStorageTable").bootstrapTable('getSelections').length;
    	if(rows>0){
    		$("#voidModal").modal("show");
    	}else{
    		showError("您没有选择任何卡!");
    	}
    });
    
    // 作废
    $("#voidOkBtn").click(function(){
    	let cardNo = $.table.selectColumns("cardNo");
    	alert(cardNo);
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
                	showError("作废失败");
                }
            }
        });
    });
</script>