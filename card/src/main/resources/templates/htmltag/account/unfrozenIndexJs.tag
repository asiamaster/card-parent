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
                	$.from.alertSuccess("解冻账户成功！");
                }else {
                	$.from.alertError("解冻账户失败！");
                }
            }
        });
    });
</script>
