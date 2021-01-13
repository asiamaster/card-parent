<script>

    $(()=>{
        $('[name="name"]').val(localStorage.customerName);
    })

    // 保存绑定的银行卡信息
    function submitHandler(e) {
        let formId = 'bankAddForm';
        if (!$.validate.form(formId)) {
            return false;
        }
        var  text=$('#openingBankNum option:selected').text();
        $("#openingBank").val(text);
//        alert(JSON.stringify($("#openingBankNum")));
        let url = "${contextPath}/bindBankCard/addBind.action";
        let customerInfo = {
            accountId: localStorage.accountId,
            fundAccountId: localStorage.fundAccountId,
            customerCode: localStorage.customerCode,
            customerName: localStorage.customerName,
            cardNo: localStorage.cardNo,
        }
        let data = $.extend(customerInfo, $.common.formToJSON(formId));
        $.operate.post(url, data);
    }

    // 开户行输入自动查询
    var openingBankAutocompleteOption = {
        width: '100%',
        language: 'zh-CN',
        minimumInputLength: 1,
        ajax: {
            type:'post',
            contentType: 'application/json',
            url: '${contextPath}/bindBankCard/getOpeningBankName.action',
            delay: 500,
            data: function (params) {
                return JSON.stringify({
                    keyword: params.term
                })
            },
            processResults: function (result) {
                if(result.success){
                    let data = result.data;
                    return {
                        results: $.map(data, function (dataItem) {
                            dataItem.text = dataItem.bankName;
                            dataItem.id = dataItem.bankNo;
                            return dataItem;
                        })
                    };
                }else{
                    bs4pop.alert(result.message, {type: 'error'});
                    return;
                }
            }
        }
    }

    // 账户类型交互
    $('#bankAccountType').change(function () {
        if($(this).val() == 1) {
            $('[data-account-type="1"]').show();
            $('[data-account-type="2"]').hide();
        } else {
            $('[data-account-type="1"]').hide();
            $('[data-account-type="2"]').show();
        }
    })

    // 刷银行卡并获取银行卡名称
    $('#bankReader').on('click', function () {
        let bankInfo = JSON.parse(readerBank());
        if(bankInfo.code == '0') {
            $('#bankNo').val(bankInfo.data);
            getBankInfo(bankInfo.data);

        } else {
            $.modal.alertWarning(bankInfo.message);
        }
    })

    // 输入银行卡并获取银行卡名称
    $('#bankNo').on('keydown', function (event) {
        if ( $(this).val().length > 14 &&  event.keyCode  == 13) {
            getBankInfo($(this).val());
        }
    })

    // 获取银行卡名称
    function getBankInfo(cardNo) {
        $.ajax({
            type: 'get',
            url: '${contextPath}/bindBankCard/getBankInfo.action?cardNo='+ cardNo,
            // data: {cardNo},
            async: false,
            dataType: "json",
            success: function (res) {
                $('#bankNamePersonal').val(res.data.channelName);
                $('#bankType').val(res.data.channelId);
            },
            error: function (error) {
                $('#bankNamePersonal').val('');
                $.modal.alertWarning(error);
            }
        })
    }


</script>