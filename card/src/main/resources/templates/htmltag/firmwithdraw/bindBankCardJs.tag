<script>
	/**
	 * 初始化
	 */
	$(function () {
		$("#fundAccountId").val(0);
		initTable();
	});
	
	var vue = new Vue({
		el:'#v',
		data() {
			return {
				firmInfo: {
					firm: {},
					merInfo: {
						profitAccount: 0
					}
				}
			}
		},
		mounted: function(){
			
		},
		methods : {
			// 获取市场帐户详情
			getFirmAccountInfo(){
				var firmId = $("#firmAccount").val();
				if(firmId == ""){
					Object.assign(this.$data, this.$options.data());
					$("#fundAccountId").val(0); // 手动设置，VUE绑定的值没有及时更新
					$.table.refresh();
					return;
				}
				$.ajax({
					type: "GET",
					data: {
						firmId : firmId
					},
					url: '${contextPath}/firmWithdraw/getFirmInfo.action',
					success: function (ret) {
						if (ret.success){
							vue.firmInfo = ret.data;
							$("#fundAccountId").val(vue.firmInfo.merInfo.profitAccount); // 手动设置，VUE绑定的值没有及时更新
                        	$.table.refresh();
                        	$('#withdrawTableDiv').show();
						}
					}
				});
			},
			// 密码验证添加银行卡
			openAuthConfirmModal(){
		    	var fundAccountId = $("#fundAccountId").val();
		    	if(fundAccountId == 0){
		    		bs4pop.alert("请选择市场账户", {type: 'warning'});
		    		return;
		    	}
		        let dia = bs4pop.dialog({
		            title: '授权绑定',// 对话框title
		            content: bui.util.HTMLDecode(template('checkPassword', {"fundAccountId":fundAccountId})), // 对话框内容，可以是
		            // string、element，$object
		            width: '500px',// 宽度
		            height: '200px',// 高度
		            btns: [{
		                label: '确定', className: 'btn-primary', onClick(e) {
		                    let requestData = $.common.formToJSON("auth-check-form");
		                    $.ajax({
		                        type: 'POST',
		                        url: '${contextPath}/firmWithdraw/authCheck.action',
		                        dataType: 'json',
		                        contentType: "application/json; charset=utf-8",
		                        data: JSON.stringify(requestData),
		                        success: function (result) {
		                            if (result.success) {
		                                dia.hide();
		                                localStorage.firmFundAccountId = $('#fundAccountId').val();
		                                $.modal.openDefault("新增银行卡绑定",'${contextPath}/firmWithdraw/toAddBankCard.html','600px','100%');
		                            } else {
		                                bs4pop.alert(result.message, {type: 'error'});
		                            }
		                        }
		                    });
		                    return false;
		                }
		            }, {
		                label: '取消', className: 'btn-secondary', onClick(e) {
		                    return true;
		                }
		            }]
		        });
		    }
		}
	})
	
	// 初始化绑定列表
	function initTable(){
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

	// 连接密码键盘调整密码
    function readPwd(){
    	CefSharp.BindObjectAsync("callbackObj");
        callbackObj.readPasswordKeyboardAsync();
    }
    
    // 该方法名为固定，C端回调
    function pswClientHandler(data){
        var json = JSON.parse(data);
        if (json.code == 0) {
             $('#password').val(json.data);
             $('#unbindModalPassword').val(json.data);
        } else {
            bs4pop.alert(json.message, {width:'350px',type: "error"});
            return false;
        }
    }
    

	/**
	 * 刷新
	 */
	$("#refreshBtn").click(function(){
		$.table.refresh();
    });
    
	
	// 列表解绑 操作
    function operFormatter(value, row, index, field) {
        return "<a href='javascript:openUnBindModal("+JSON.stringify(row)+");'>解绑</a>";
    }
    
    
    function openUnBindModal(row){
    	var id = row["id"];
    	let dia = bs4pop.dialog({
            title: '解除绑定',// 对话框title
            content: bui.util.HTMLDecode(template('unbindModal', row)),
            width: '400px',// 宽度
            height: '420px',// 高度
            btns: [{
                label: '确定', className: 'btn-primary', onClick(e) {
                    var url = "${contextPath}/firmWithdraw/unBind.action";
                    var password = $("#unbindModalPassword").val();
                    if(password == ""){
                    	bs4pop.alert("请输入密码", {type: 'error'});
                    	return false;
                    }
                    let fundAccountId = $('#fundAccountId').val();
	               	var data={id:id, loginPwd:password, fundAccountId:fundAccountId};
	           	   	$.operate.post(url, data, function (result){
	           	   		if(result.success){
	           	   			dia.hide();
	           	   		}
	           	   	});
	           	   	return false;
                }
            }, {
                label: '取消', className: 'btn-secondary', onClick(e) {
                    return true;
                }
            }]
        });
    }
    
    function tableRefresh(){
    	$.table.refresh();
    }
    
    // 解绑银行卡
    function unBind(id) {
    	alert(0);
    	 var url = "${contextPath}/bindBankCard/unBind.action";
    	 var data={id:id};
	   	 $.operate.post(url, data, function (){
	 	   $.table.refresh();
	   	 });
    }
    
    function noteFormatter(value, row, index, field) {
        if ($.common.isEmpty(value)){
            return value;
        }
        return '<span data-toggle="tooltip" data-placement="top" title="" data-original-title="'+value+'">'+value+'</span>'
    }
</script>
