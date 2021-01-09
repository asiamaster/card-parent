<script>
    window.domain = 'diligrp.com';

    /************* 获取客户信息  start *****************/
    // 客户名称
    var customerNameQueryAutoCompleteOption = {
        serviceUrl: '/customer/listByKeyword.action',
        width: '320px',
        paramName: 'keyword',
        displayFieldName: 'code',
        showNoSuggestionNotice: true,
        minChars: 2,
        noSuggestionNotice: '无此客户, 请重新输入',
        transformResult: function (result) {
            if(result.success){
                let data = result.data;
                return {
                    suggestions: $.map(data, function (dataItem) {
                        return $.extend(dataItem, {
                                value: dataItem.code + ' | ' + dataItem.name + ' | ' + dataItem.contactsPhone
                            }
                        );
                    })
                }
            }else{
                //bs4pop.alert(result.message, {type: 'error'});
                return false;
            }
        },
        selectFn: function (suggestion) {
            $('#show_customer_name').val(suggestion.name);
        }
    };

    function queryCustomerByCardNo(cardNo, domId, callback) {
        let targetDom = $('#' + domId);
        targetDom.val('');
        $('#hidden_account_id').val('');
        if (!cardNo || $.trim(cardNo).length !== 12) {
            customerCallback(callback, targetDom, false);
            return;
        }
        $.ajax({
            type: 'GET',
            url: '/accountQuery/singleWithoutValidate.action?cardNo=' + cardNo,
            dataType: 'json',
            success: function (result) {
                if (result.success) {
                    $('#hidden_account_id').val(result.data.accountId);
                    targetDom.val(result.data.customerName);
                    customerCallback(callback, targetDom, true)
                } else {
                    $('#hidden_account_id').val('');
                    targetDom.val('');
                    customerCallback(callback, targetDom, false)
                }
            },
            error: function () {
                customerCallback(callback, targetDom, false)
            }
        });
    }

    function customerCallback(callback, targetDom, success) {
        if (typeof callback == 'function') {
            callback(targetDom, success)
        }
    }
</script>
