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
    if(cardNo == ""){
   		$.modal.alertError("请正确放置卡片位置");
   	}
   	if(cardNo == "-1"){
   		$.modal.alertError("读卡失败");
   	}
    return cardNo;
}


function readerPwd() {
    if (typeof (callbackObj) == "undefined") return;
    var pwd = callbackObj.readPWDkeyboard();
    return pwd;
}
</script>
