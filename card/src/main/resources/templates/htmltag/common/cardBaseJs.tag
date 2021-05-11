<script>
    function readerIDCard() {
        if (typeof (callbackObj) == "undefined") return;
        let result = callbackObj.readIDCard();

        //idCard = eval("(" + idCard + ")");
        //return idCard;
        return JSON.parse(result);
    }

    //有消息提示的读卡
    function readCardWithMsg() {
        let result = readerCardNumber();
        if (!result.success) {
            $.modal.alertWarning(result.message);
            return;
        }
        let cardNo = result.data;
        if ($.common.isEmpty(cardNo)) {
            parent.$.modal.alertWarning("请将卡片放置在读卡器上");
            return;
        }
        return cardNo;
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
    /**
     * 读取密码
     * @returns
     */
    function readPasswordKeyboardAsync() {
        if (typeof (callbackObj) == "undefined") return;
        callbackObj.readPasswordKeyboardAsync();
    }

    //读取银行卡
    function readerBank() {
        if (typeof (callbackObj) == "undefined") return false;
        return callbackObj.readBankCardNumber();
    }

</script>
