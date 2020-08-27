<script>
function statisticTotalAmount(action){
	if($("#totalAmount")){
		$("#totalAmount").remove();
	}
	var name;
	if(action == 1){
		name = '领款';
	}else{
		name = '交款';
	}
	var toolbar = $('.fixed-table-toolbar');
	var divModel = '<div id="totalAmount"><h5>'+ name +'总金额：?</h5></div>';
	let data = $.common.formToJSON('queryForm');
	data.action=action;
	$.ajax({
        type:'POST',
        contentType:"application/json; charset=utf-8",
        url:'${contextPath}/cash/statistic.action',
        dataType:'json',
        traditional:true,
        data: JSON.stringify(data),
        success:function(result) {
            if (result.code == '200') {
            	divModel = divModel.replace("?", result.data+"");
            	toolbar.after(divModel);
            }else {
            	divModel = divModel.replace("?", 0+"");
            	toolbar.before(divModel);
            }
        }
    });
}
</script>
