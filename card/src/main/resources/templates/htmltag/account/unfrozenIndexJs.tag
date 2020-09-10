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
	 function submitUnFrozenAccount() {
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
	            bui.util.debounce($.operate.post(url, $.extend(requestData, data), function (result) {
	                if (result.code == web_status.SUCCESS) {
	                	$("#unfrozen").attr("disabled", "disabled");
	                	$("#disabledState2").val("1");
	                    $.tab.refresh();
	                }
	            }), 1000, true)
	        });
		}
	 
	 $(() => {
	    	if($("#disabledState2").val() == 1){
	    		$("#unfrozen").attr("disabled", "disabled");
	    	}
	    });

</script>
