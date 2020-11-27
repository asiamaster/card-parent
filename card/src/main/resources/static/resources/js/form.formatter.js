/**
 * @return {number}
 */
function yuanToFen(val) {
    if (val == undefined) {
        return 0;
    }
    return parseFloat(val) * 100;
}

function limitNum(obj) {
    //只能输入两个小数
    obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');
    //只能输入大于0
    if (obj.value && obj.value < 0) {
        obj.value = 0;
    }
}

/**
* 允许有符号的数字
* @author miaoguoxin
* @date 2020/11/27
*/
function limitNumWithSigned(obj) {
    if (obj.value.length < 2){
        return;
    }
    if (containsNumber(obj.value)){
        obj.value = 0;
    }
    obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');
}

function containsNumber(str) {
    var b = /^[0-9]+.?[0-9]*$/;
    return b.test(str);
}
