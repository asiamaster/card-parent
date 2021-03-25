<script id="checkPassword" type="text/html">
    <form id="auth-check-form">
        <div class="form-group col">
            <input type="hidden" name="accountId" id="accountId"  value="{{fundAccountId}}">
            <label for="password" class="div_label">密码</label>
            <% if(isNotEmpty(allowInput) && allowInput == "1") {%>
                <input  type="password" id="password" maxlength=6 name="password">
            <% } else { %>
                <input  type="password" id="password" maxlength=6 name="password" readonly>
            <% } %>
            <button id="authBindPwdBtn" onclick="readFirmWithdrawPwd('bindPwd')" type="button" class="btn btn-primary">
                请输入密码
            </button>
        </div>
    </form>
</script>
<script>
    /**
     * 授权绑定,密码验证
     */
    function openAuthConfirmModal(){
    	var fundAccountId = $("#fundAccountId").val();
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

    /**
    *  打开市场账户修改密码弹出框
    * @author miaoguoxin
    * @date 2021/3/24
    */
    function openUpdatePwdModal(){
        let data = {
            merInfoName : $("#merInfoName").val(),
            fundAccountId:$("#fundAccountId").val()
        }
        let dia = bs4pop.dialog({
            title: '修改密码',// 对话框title
            content: bui.util.HTMLDecode(template('updatePwdModal',data)),
            width: '500px',// 宽度
            height: '450px',// 高度
            btns: [{
                label: '确定', className: 'btn-primary', onClick(e) {
                    if (!$.validate.form('update-pwd-form')) {
                        return false;
                    }
                    let url = "${contextPath}/firmWithdraw/updateFirmAccountPwd.action";
                    let data = {
                        oldPassword: $("#oldPassword").val(),
                        newPassword: $("#newPassword").val(),
                        fundAccountId: $('#fundAccountId').val()
                    };
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

        $("#update-pwd-form").validate({
            onkeyup: false,
            rules: {
                oldPassword: {
                    required: true
                },
                newPassword: {
                    required: true
                },
                secondConfirmPwd : {
                    required: true,
                    equalTo: "#newPassword"
                }
            },
            messages: {
                oldPassword: {
                    required: "请输入原密码"
                },
                newPassword: {
                    required: "请输入新密码"
                },
                secondConfirmPwd : {
                    required: "请确认密码",
                    equalTo: "两次输入密码不相同"
                }
            },
            focusCleanup: true
        });
    }


    $(document).on('click', '#pswBtn', function () {
        readPasswordKeyboardAsync();
    });
    // 连接密码键盘输入提款交易密码
    var nowPwdType = "tradePwd";
    function readFirmWithdrawPwd(pwdType){
        nowPwdType = pwdType;
        if (nowPwdType == "tradePwd") {
            $("#tradePwd").val("");
        } else if (nowPwdType == "bindPwd") {
            $('#password').val("");
        }

        CefSharp.BindObjectAsync("callbackObj");
        callbackObj.readPasswordKeyboardAsync();
    }

    // 该方法名为固定，C端回调
    function pswClientHandler(data){
        let json = JSON.parse(data);
        if (json.code == 0) {
        	if(nowPwdType == "tradePwd"){
        		$('#tradePwd').val(json.data);
        	}else if (nowPwdType == "bindPwd"){
        		$('#password').val(json.data);
        	}else if (nowPwdType == "u_oldPassword"){
                $('#oldPassword').val(json.data);
            }else if (nowPwdType == "u_newPassword"){
                $('#newPassword').val(json.data);
            }else if (nowPwdType == "u_confirmPwd"){
                $('#secondConfirmPwd').val(json.data);
            }
        } else {
            bs4pop.alert(json.message, {width:'350px',type: "error"});
            return false;
        }
    }
</script>
