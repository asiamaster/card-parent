<script>
    var prefix = "${contextPath}/cash";
    $(function () {
        var options = {
            url: prefix + "/payeeList.action",
            singleSelect : true
        };
        $.table.init(options);
    });
</script>
