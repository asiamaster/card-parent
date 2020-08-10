<script>
function readerIDCard() {
    if (typeof (callbackObj) == "undefined") return;
    var idCard = callbackObj.readIDCard();
    idCard = eval("(" + idCard + ")");
    return idCard;
}

function readerCardNumber() {
    if (typeof (callbackObj) == "undefined") return;
    var cardNo = callbackObj.readCardNumber();
    return cardNo;
}


function readerPwd() {
    if (typeof (callbackObj) == "undefined") return;
    var pwd = callbackObj.readPWDkeyboard();
    return pwd;
}
</script>
