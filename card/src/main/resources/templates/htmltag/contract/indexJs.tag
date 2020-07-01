<script>
    var prefix = "${contextPath}/contract";
    $(function () {
        var options = {
            url: prefix + "/page.action",
            modalName: "合同查询"
        };
        $.table.init(options);
    });
</script>
