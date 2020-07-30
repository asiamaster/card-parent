<script>
function statisticTotalAmount(action){
	$("#totalAmount").remove();
	var toolbar = $('.fixed-table-container');
	var divModel = '<div id="totalAmount"><h5>领款总金额：?</h5></div>';
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
            	toolbar.before(divModel);
            }else {
            	divModel = divModel.replace("?", 0+"");
            	toolbar.before(divModel);
            }
        }
    });
}
</script>
