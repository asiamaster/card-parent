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


</script>
