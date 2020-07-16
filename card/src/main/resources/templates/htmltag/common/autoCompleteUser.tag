<script>
    var userNameAutoCompleteOption = {
        serviceUrl: '/user/listByKeyword.action',
        paramName: 'keyword',
        displayFieldName: 'realName',
        showNoSuggestionNotice: true,
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
                bs4pop.alert(result.message, {type: 'error'});
                return false;
            }
        },
        selectFn: function (suggestion) {
            $('#_userCode').text(suggestion.serialNumber);
            $('#userCode').val(suggestion.serialNumber);
        }
    };

</script>
