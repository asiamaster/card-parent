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

    function queryCustomerByCardNo(cardNo, domId) {
        $('#' + domId).val('')
        $('#hidden_account_id').val('');
        if (!cardNo || $.trim(cardNo).length !== 12) {
            return;
        }
        $.ajax({
            type:'GET',
            url:'/accountQuery/singleWithoutValidate.action?cardNo=' + cardNo,
            dataType:'json',
            success:function(result) {
                if (result.success) {
                    $('#hidden_account_id').val(result.data.accountId);
                    $('#' + domId).val(result.data.customerName);
                } else {
                    $('#hidden_account_id').val('');
                    $('#' + domId).val('');
                }
            },
            error:function(){

            }
        });
    }

</script>
