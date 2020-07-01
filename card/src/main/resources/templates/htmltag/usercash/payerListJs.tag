<script>
    var prefix = "${contextPath}/cash";
    $(function () {
        var options = {
            url: prefix + "/payerList.action"
        };
        $.table.init(options);
    });
</script>
