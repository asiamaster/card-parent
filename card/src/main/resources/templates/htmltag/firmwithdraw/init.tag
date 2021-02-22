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
            <button id="authBindPwdBtn" type="button" class="btn btn-primary">
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

    // 连接密码键盘输入提款交易密码
    function readFirmWithdrawPwd(){
        CefSharp.BindObjectAsync("callbackObj");
        callbackObj.readPasswordKeyboardAsync();
    }

    $(document).on('click', '#pswBtn', function () {
        readPasswordKeyboardAsync();
    });

    // 该方法名为固定，C端回调
    function pswClientHandler(data){
        let json = JSON.parse(data);
        if (json.code == 0) {
            $('#tradePwd').val(json.data);
            $('#password').val(json.data);
        } else {
            bs4pop.alert(json.message, {width:'350px',type: "error"});
            return false;
        }
    }
</script>
