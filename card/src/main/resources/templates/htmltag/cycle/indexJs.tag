<script>
    var prefix = "${contextPath}/cycle";
    $(function () {
        var options = {
            url: prefix + "/page.action",
            singleSelect : true
        };
        $.table.init(options);
    });
</script>
