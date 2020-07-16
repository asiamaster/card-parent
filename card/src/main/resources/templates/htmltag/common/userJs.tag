<script>
    window.domain = 'diligrp.com';
    /************* 获取用户信息  start *****************/
    // 客户名称
    let userNameQueryAutoCompleteOption = {
        serviceUrl: '/user/listByKeyword.action',
        paramName: 'keyword',
        displayFieldName: 'realName',
        showNoSuggestionNotice: true,
        width: 'flex',
        noSuggestionNotice: '无此用户, 请重新输入',
        transformResult: function (result) {
            if(result.success){
                let data = result.data;
                return {
                    suggestions: $.map(data, function (dataItem) {
                        return $.extend(dataItem, {
                                value: dataItem.userName + ' | ' + dataItem.realName
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

        }
    };
</script>
