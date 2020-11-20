<script>
    function readerIDCard() {
        if (typeof (callbackObj) == "undefined") return;
        let result = callbackObj.readIDCard();

        //idCard = eval("(" + idCard + ")");
        //return idCard;
        return JSON.parse(result);
    }

    function readerCardNumber() {
        if (typeof (callbackObj) == "undefined") return;
        let resultJson = callbackObj.readCardNumber();
        return JSON.parse(resultJson);
    }

    //开启连续读卡
    function continuousReadCardNumber() {
        if (typeof (callbackObj) == "undefined") return;
        callbackObj.continueReadCardNumber();
    }

    //关闭连续读卡
    function closeContinuousReadCard() {
        if (typeof (callbackObj) == "undefined") return;
        callbackObj.closeContinueReadCard();
    }
</script>
