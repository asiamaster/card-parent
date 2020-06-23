<script>
    var cardType = {
        "10": "主卡",
        "20": "副卡"
    };
    var cardState = {
        "1": "正常",
        "2": "锁定",
        "3": "挂失",
        "4": "退还"
    };
    var list = {
        "cardType": cardType,
        "cardState": cardState
    };

    function valueFormatter(value, row, index, filed) {
        return list[filed][value];
    }
</script>
