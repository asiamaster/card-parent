<script>
    // 初始化表格
    $(() => {
        let options = {
        	id: "unfrozenTable",
        	uniqueId: "id",
            url: "${contextPath}/serial/business/page.action",
            sortName: "operate_time",
            modalName: "冻结资金记录"
        };
        $.table.init(options);
    });
    
    $("#unfrozenBtn").click(function(){
    	// 选中行
    	var rows=$("#unfrozenTable").bootstrapTable('getSelections');
    	var tradeNos = new Array();
    	$.each(rows, function (index, item) {
            tradeNos[index] = item["tradeNo"];
       });
    	alert(JSON.stringify(tradeNos));
    	var accountId = $("#accountId").val();
    	$.ajax({
            type:'POST',
            url:'${contextPath}/fund/unfrozenRecord.action',
            dataType:'json',
            traditional:true,
            data: {
            	tradeNos:tradeNos,
            	accountId:accountId
            },
            success:function(result) {
                if (result.success) {
                	showInfo("操作成功");
                }else {
                	showError("解冻资金失败");
                }
            }
        });
    });
</script>
