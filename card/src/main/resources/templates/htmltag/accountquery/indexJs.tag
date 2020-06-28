<script>
    var prefix = "${contextPath}/accountQuery";
    $(function () {
        var options = {
            url: prefix + "/page.action",
            sortName: "card_create_time",
            modalName: "卡查询"
        };
        $.table.init(options);
    });

    function cardNoFormatter(value, row, index, field) {
        return '<a class="" href="javascript:void(0);">' + insertSpace(value) + '</a> ';
    }

    /*每隔4位空格一下*/
    function insertSpace(value) {
        var out = [];
        for (let i = 0; i < value.length; i++) {
            out.push(value.charAt(i))
            if (i === 0) {
                continue;
            }
            if (i % 4 === 0) {
                out.push(" ")
            }
        }
        return out.join("");
    }
</script>
