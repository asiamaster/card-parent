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
    function continuousReadCardNumber(callbackFunc) {
        if (typeof (callbackObj) == "undefined") return;
        callbackObj.continueReadCardNumber();
        if (typeof callbackFunc == "function") {
            callbackFunc();
        }
    }

    //关闭连续读卡
    function closeContinuousReadCard() {
        if (typeof (callbackObj) == "undefined") return;
        callbackObj.closeContinueReadCard();
    }

    //检查卡状态,true关闭,false开启
    function checkCardStatus() {
        if (typeof (callbackObj) == "undefined") return;
        return  callbackObj.checkCardStatus();
    }
</script>
