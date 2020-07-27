<script>
    $("#unfrozenBtn").click(function(){
    	var accountId = $("#accountId").val();
    	$.ajax({
            type:'POST',
            url:'${contextPath}/account/unfrozen.action',
            dataType:'json',
            traditional:true,
            data: {
            	accountId:accountId
            },
            success:function(result) {
                if (result.success) {
                	showInfo("解冻账户成功");
                }else {
                	showError("解冻账户失败");
                }
            }
        });
    });
</script>
