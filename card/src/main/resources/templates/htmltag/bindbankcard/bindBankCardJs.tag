<script>
	/**
	 * 初始化
	 */
	$(() => {

	});

    /**
	 * 授权绑定,密码验证
	 */
	$(document).on('click', '#authBindBtn', function () {
// let a = bui.util.debounce(saveOrUpdateHandler,1000,true);
        let dia = bs4pop.dialog({
            title: '授权绑定',// 对话框title
            content: bui.util.HTMLDecode(template('checkPassword', {})), // 对话框内容，可以是
            // string、element，$object
            width: '500px',// 宽度
            height: '200px',// 高度
            btns: [{
                label: '确定', className: 'btn-primary', onClick(e) {
                    let pwd = $("#password").val();
                    let accountId = $("#accountId").val();
                    let param = JSON.stringify({
                        loginPwd: pwd,
                        accountId: accountId
                    });
                    $.ajax({
                        type: 'POST',
                        url: '${contextPath}/bindBankCard/checkPwd.action',
                        dataType: 'json',
                        contentType: "application/json; charset=utf-8",
                        data: param,
                        success: function (result) {
                            if (result.success) {
                                // 已绑定银行卡列表数据
                                bindBankCardTable();
                                $("#bankCardTableDiv").show();
                                return true;
                            } else {
                                bs4pop.alert(result.message, {type: 'error'});
                            }
                        }
                    });
                }
            }, {
                label: '取消', className: 'btn-secondary', onClick(e) {
                    return true;
                }
            }]
        });
    });

	/**
	 * 刷新
	 */
	$("#refreshBtn").click(function(){
		$.table.refresh();
    });

	/**
	 * 打开添加银行卡页面
	 */
	$("#openAddHtmlBtn").click(function(){
	    $.modal.openDefault("新增银行卡绑定",'${contextPath}/bindBankCard/toAddBankCard.html','600px','100%');
    });

	/**
	 * 打开添加银行卡页面
	 */
	$("#openAddHtmlBtn2").click(function(){
		dia = bs4pop.dialog({
            title: 'iframe新增',// 对话框title
            content: '${contextPath}/customer/preSave.html?', // 对话框内容，可以是
																// string、element，$object
            width: '80%',// 宽度
            height: '900px',// 高度
            isIframe: true,// 默认是页面层，非iframe
            // 按钮放在父页面用此处的 btns 选项。也可以放在页面里直接在页面写div。
            /*
			 * btns: [{label: '取消',className: 'btn-secondary',onClick(e,
			 * $iframe){ } }, {label: '确定',className: 'btn-primary',onClick(e,
			 * $iframe){ let diaWindow = $iframe[0].contentWindow;
			 * bui.util.debounce(diaWindow.saveOrUpdateHandler,1000,true)()
			 * return false; } }]
			 */
        });
    });

	/**
	 * 已绑定的银行卡列表
	 */
	function bindBankCardTable(){
		let accountId = $("#accountId").val();
		let options = {
		    	id: "bankCardTable",
		    	uniqueId: "id",
		        url: "${contextPath}/bindBankCard/queryList.action",
		        sortName: "create_time",
		        modalName: "绑定卡列表",
		        method: "get"
		};
		$.table.init(options);
	}


    /**
     * 卡号查询
     */
     $('[name="cardNo"]').keydown(function(e){
         $('#authBindBtn').hide();
         if (e.keyCode == 13) {
             let cardNo = $(this).val();
             getCardCustomerInfo(cardNo);
         }
     });


	/**
	 * 读卡号
	 */
    function readCard() {
        let result = readerCardNumber();
        if (!result.success) {
            $.modal.alertWarning(result.message);
            return false;
        }
        let cardNo = result.data;
        if ($.common.isEmpty(cardNo)) {
            parent.$.modal.alertWarning("请将卡片放置在读卡器上");
            return false;
        }
        $("#cardNo").val(cardNo);
        getCardCustomerInfo(cardNo)
    }

    // 获取园区卡号的用户信息
    function getCardCustomerInfo(cardNo){
        $.ajax({
            type: 'get',
            url: '/bindBankCard/queryCard.action',
            data: {cardNo},
            async: false,
            dataType: "json",
            success: function (res) {
            	if(res.success){
            		localStorage.accountId = res.data.cardInfo.accountId;
	                localStorage.fundAccountId = res.data.cardInfo.accountId;
	                localStorage.customerCode = res.data.cardInfo.customerCode;
	                localStorage.customerName = res.data.cardInfo.customerName;
	                localStorage.cardNo = res.data.cardInfo.cardNo;
	                $('#authBindBtn').show();
	                $('#cardInfoDiv').html(bui.util.HTMLDecode(template('customerInfoTmpl', res.data.cardInfo)));
            	}else{
            		$('#cardInfoDiv').html(res.message);
            	}
            },
            error: function (error) {
                $('#cardInfoDiv').html(error);
            }
        })
    }
    
    function operFormatter(value, row, index, field) {
        return '<a href="javascript:unBind('+row["id"]+');">解绑</a> ';
    }
    
    // 解绑银行卡
    function unBind(id) {
    	 alert(id);
    	 var url = "${contextPath}/bindBankCard/unBind.action";
    	 var data={id:id};
	   	 $.operate.post(url, data, function (){
	 	   $.table.refresh();
	   	 });
//        $.ajax({
//            type: 'post',
//            url: '${contextPath}/bindBankCard/unBind.action',
//            data: {id:id},
//            async: false,
//            dataType: "json",
//            success: function (res) {
//               if(res.success){
//            	   bs4pop.alert("操作成功", {type: 'info'});
//            	   $.table.refresh();
//               }else{
//            	   bs4pop.alert(res.message, {type: 'error'});
//               }
//            },
//            error: function (error) {
//                $('#bankNamePersonal').val('');
//                $.modal.alertWarning(error);
//            }
//        })
    }
</script>
