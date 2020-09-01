<script>
    /** 错误消息提示框 */
    function showError(message) {
        bs4pop.alert(message, {type : "error"});
    }

    /** 提示消息弹出框 */
    function showInfo(message) {
        bs4pop.alert(message, {type : "info"});
    }

    /** 警示消息框 */
    function showWarning(message) {
        bs4pop.alert(message, {type : "warning"});
    }

    function getNowFormatDate(nian,yue,ti) {
        var date = new Date();
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var currentdate = year + nian + month + yue + strDate + ti;
        return currentdate;
    }
    
    function printData(data,templateName){
    	if(typeof callbackObj != 'undefined'){
            window.printFinish=function(){
            }
            if(!data){
                return;
            }
            console.log("打印信息--:"+ data);
            if(data==""){
                return;
            }
            callbackObj.printDirect(data,templateName);
        }else{
            bs4pop.alert("请检查打印的设备是否已连接", { type: "error" });
        }
    }
    
    function randomString(len) {
    	len = len || 16;
	  　　var $chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';    /****默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1****/
	 　　var maxPos = $chars.length;
	  　　var pwd = '';
	  　　for (i = 0; i < len; i++) {
	  　　　　pwd += $chars.charAt(Math.floor(Math.random() * maxPos));
	 　　}
	 　　return pwd;
    }

</script>
