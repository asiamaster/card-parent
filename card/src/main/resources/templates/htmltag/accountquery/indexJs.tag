<script>
    var prefix = "${contextPath}/accountQuery";
    $(function() {
        var options = {
            url: prefix + "/page.action",
            createUrl: prefix + "/add.html",
            updateUrl: prefix + "/edit/{id}",
            removeUrl: prefix + "/remove",
            exportUrl: prefix + "/export",
            sortName: "card_create_time",
            modalName: "卡查询"
        };
        $.table.init(options);
    });
</script>
