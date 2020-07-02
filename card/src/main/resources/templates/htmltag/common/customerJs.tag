<script>
    window.domain = 'diligrp.com';

    /************* 获取客户信息  start *****************/
    // 客户名称
    var customerNameQueryAutoCompleteOption = {
        serviceUrl: '/customer/listByKeyword.action',
        paramName: 'keyword',
        displayFieldName: 'code',
        showNoSuggestionNotice: true,
        minChars: 2,
        width: 'flex',
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

</script>