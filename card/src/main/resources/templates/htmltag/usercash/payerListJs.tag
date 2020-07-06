<script>
    var prefix = "${contextPath}/cash";
    $(function () {
        var options = {
            url: prefix + "/payerList.action",
            singleSelect : true
        };
        $.table.init(options);
    });
</script>
