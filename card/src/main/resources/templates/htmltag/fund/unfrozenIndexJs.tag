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
    $("#unfrozenBtn").click(function(){
    	let rows = $("#unfrozenTable").bootstrapTable('getSelections').length;
    	if(rows>0){
    		$("#unfrozenModal").modal("show");
    	}else{
    		showError("您没有选择任何记录!");
    	}
    });
    
    // 解冻
    $("#unfrozenOkBtn").click(function(){
    	// 获取选中行
    	var rows=$("#unfrozenTable").bootstrapTable('getSelections');
    	var frozenIds = new Array();
    	$.each(rows, function (index, item) {
    		frozenIds[index] = item["frozenId"];
       });
    	var accountId = $("#accountId").val();
    	var remark = $("#remark").val();
    	$.ajax({
            type:'POST',
            url:'${contextPath}/fund/unfrozen.action',
            dataType:'json',
            traditional:true,
            data: {
            	frozenIds: frozenIds,
            	accountId: accountId,
            	remark: remark
            },
            success:function(result) {
                if (result.success) {
                	showInfo("操作成功");
                	$('#unfrozenTable').bootstrapTable('refresh');
                }else {
                	showError("解冻资金失败："+result.message);
                }
            }
        });
    });
</script>
