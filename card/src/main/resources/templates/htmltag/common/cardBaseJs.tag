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

    function continuousReadCardNumber() {
        if (typeof (callbackObj) == "undefined") return;
        callbackObj.continueReadCardNumber();
    }

    function cardClientHandler(json) {
        let parse = JSON.parse(json);
        console.log(parse)
    }
</script>
