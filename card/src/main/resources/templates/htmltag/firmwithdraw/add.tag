<script>
    $(document).on('#withdrawalBtn', 'click', function () {
        submitHandler()
    })

    // 保存绑定的银行卡信息
    function submitHandler(e) {
        let formId = 'withdrawalForm';
        if (!$.validate.form(formId)) {
            return false;
        }
        let url = "${contextPath}/";

        let data = $.extend($.common.formToJSON(formId));
        $.operate.post(url, data);
    }

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
                        url: '${contextPath}/firmwithdraw/checkPwd.action',
                        dataType: 'json',
                        contentType: "application/json; charset=utf-8",
                        data: param,
                        success: function (result) {
                            if (result.success) {

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
    $(document).on('click', '#pswBtn', function () {
        readPasswordKeyboardAsync();
    })
   function pswClientHandler(data){
       var json = JSON.parse(data);
       if (json.code == 0) {
           $('#pswBtn').val(json.data);
       } else {
           bs4pop.alert(json.message, {width:'350px',type: "error"});
           return false;
       }
   }*/

</script>
