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
	var divModel = '<h6 id="totalAmount" class="float-left" style="height:60px;line-height:60px;margin-left:30px;"><span>'+ name +'总金额：?</span></h6>';
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
            	toolbar.append(divModel);
            }else {
            	divModel = divModel.replace("?", 0+"");
            	toolbar.append(divModel);
            }
        },
        error: function (result) {
        	divModel = divModel.replace("?", 0+"");
        	toolbar.append(divModel);
		}
    });
}
</script>
