<script>
function statisticTotalAmount(action){
	var toolbar = $('.fixed-table-container');
	var divModel = '<div><h5>领款总金额：totalAmount</h5></div>';
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
            	divModel = divModel.replace("totalAmount", result.data+"");
            	toolbar.before(divModel);
            }else {
            	divModel = divModel.replace("totalAmount", 0+"");
            	toolbar.before(divModel);
            }
        }
    });
}
</script>
