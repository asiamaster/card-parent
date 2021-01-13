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

</script>
