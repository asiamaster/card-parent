<script>
	/**
	 * 初始化
	 */
	$(() => {
		
	});

	/**
	 * 授权绑定
	 */
	$("#authBindBtn").click(function(){
		bindBankCardTable();
		$("#bankCardTableDiv").show();
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
		var dia = bs4pop.dialog({
            title: 'iframe新增',//对话框title
            content: '${contextPath}/customer/toAddBankCard.html?', //对话框内容，可以是 string、element，$object
            width: '80%',//宽度
            height: '700px',//高度
            isIframe: true,//默认是页面层，非iframe
            //按钮放在父页面用此处的 btns 选项。也可以放在页面里直接在页面写div。
            /*btns: [{label: '取消',className: 'btn-secondary',onClick(e, $iframe){

                }
            }, {label: '确定',className: 'btn-primary',onClick(e, $iframe){
                    let diaWindow = $iframe[0].contentWindow;
                    bui.util.debounce(diaWindow.saveOrUpdateHandler,1000,true)()
                    return false;
                }
            }]*/
        });
    });
	
	/**
	 * 打开添加银行卡页面
	 */
	$("#openAddHtmlBtn2").click(function(){
		dia = bs4pop.dialog({
            title: 'iframe新增',//对话框title
            content: '${contextPath}/customer/preSave.html?', //对话框内容，可以是 string、element，$object
            width: '80%',//宽度
            height: '700px',//高度
            isIframe: true,//默认是页面层，非iframe
            //按钮放在父页面用此处的 btns 选项。也可以放在页面里直接在页面写div。
            /*btns: [{label: '取消',className: 'btn-secondary',onClick(e, $iframe){

                }
            }, {label: '确定',className: 'btn-primary',onClick(e, $iframe){
                    let diaWindow = $iframe[0].contentWindow;
                    bui.util.debounce(diaWindow.saveOrUpdateHandler,1000,true)()
                    return false;
                }
            }]*/
        });
    });
	
	/**
	 * 已绑定的银行卡列表
	 */
	function bindBankCardTable(){
		let accountId = $("#accountId").val(); 
		alert(accountId);
		let options = {
		    	id: "bankCardTable",
		    	uniqueId: "id",
		        url: "${contextPath}/bindBankCard/queryList.action",
		        sortName: "create_time",
		        modalName: "绑定卡列表",
		        method: "get",
		        queryParams: {accountId:accountId}
		};
		$.table.init(options);
	}

	/**
	 * 读卡号
	 */ 
    function readCard() {
// let result = readerCardNumber();
// if (!result.success) {
// $.modal.alertWarning(result.message);
// return;
// }
// let cardNo = result.data;
// if ($.common.isEmpty(cardNo)) {
// parent.$.modal.alertWarning("请将卡片放置在读卡器上");
// return;
// }
    	let cardNo = '888810032992';
        alert(cardNo);
        $("#cardNo").val(cardNo);
        $("#queryCardInfoForm").submit();
    }
</script>
