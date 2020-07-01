<script>
    var prefix = "${contextPath}/cash";
    $(function () {
        var options = {
            url: prefix + "/payeeList.action"
        };
        $.table.init(options);
    });
</script>
