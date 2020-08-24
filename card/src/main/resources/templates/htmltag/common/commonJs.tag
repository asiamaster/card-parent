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
    
    /**打印信息 tableId展示的表格id  templateName 打印模板名称  需要向晓辉索取   url 加载打印数据的接口  自定义 */
    function print(tableId, templateName, url){
    	var id = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
   	 	if (id.length == 0) {
            $.modal.alertWarning("请至少选择一条记录");
            return;
        }
        if(typeof callbackObj != 'undefined'){
            window.printFinish=function(){
            }
            var paramStr ="";
            var data=loadPrintData(id, url);
            debugger;
            if(!data){
                return;
            }
            paramStr = JSON.stringify(data);
            console.log("打印信息--:"+paramStr);
            if(paramStr==""){
                return;
            }
            callbackObj.printDirect(paramStr,templateName);
        }else{
            bs4pop.alert("请检查打印的设备是否已连接", { type: "error" });
        }
    }
    //加载打印数据
    function loadPrintData(id,url){
        console.log("调用打印信息："+id);
        data = {};
    	data.id = id;
        $.ajax({
    	    type: "POST",
    	    url:url,
    	    data:JSON.stringify(data),
    	    contentType: "application/json; charset=utf-8",
    	    traditional:true,
    	    dataType:'json',
    	    async: false,
    	    error: function(result) {
    	    	$.modal.alertError(result.message);
    	    	return "";
    	    },
    	    success: function(result) {
    	    	debugger;
    	    	if(result.code=="200"){
    	    		return result.data;
    	    	}else{
    	    		$.modal.alertError(result.message);
    	    		return "";
    	    	}
    	    }
    	});	
    }
    
    function uuid() {
    	var s = [];
    	var hexDigits = "0123456789abcdef";
    	for (var i = 0; i < 36; i++) {
    		s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    	}
    	s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
    	s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
    	s[8] = s[13] = s[18] = s[23] = "-";

    	var uuid = s.join("");
    	return uuid;
    }

</script>
