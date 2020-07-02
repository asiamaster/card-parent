<script>
    var prefix = "${contextPath}/cycle";
    $(function () {
        var options = {
            url: prefix + "/page.action"
        };
        $.table.init(options);
    });
</script>
