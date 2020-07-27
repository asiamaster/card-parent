<script>
$("#unfrozen-account-form").validate({
    onkeyup: false,
    rules: {
        mark: {
            maxlength: 30
        }
    },
    messages: {
        mark: {
            maxlength: "最多可以输入{0}个字符"
        }
    },
    focusCleanup: true
});
	 function submitUnFrozen() {
			if (!$.validate.form('unfrozen-account-form')) {
				return;
			}
			var requestData = {
			            cardNo:${detail.cardAssociation.primary.cardNo!},
			            accountId:${detail.cardAssociation.primary.accountId!},
			            customerId:${detail.cardAssociation.primary.customerId!},
			        };
			let url = '${contextPath}/account/unfrozen.action';
			$.modal.confirm("确认解冻当前账户吗?", function (sure) {
	            if (!sure) {
	                return;
	            }
	            let data = $.common.formToJSON('unfrozen-account-form');
	            $.operate.post(url, $.extend(requestData, data));
	        });
			
/* 			$.ajax({
				type : 'POST',
				url : '${contextPath}/account/unfrozen.action',
				dataType : 'json',
				contentType : "application/json; charset=utf-8",
				traditional : true,
				data : JSON.stringify(data),
				success : function(result) {
					if (result.success) {
						$.from.alertSuccess("解冻账户成功！");
					} else {
						$.from.alertError("解冻账户失败！");
					}
				}
			}); */
		}
</script>
