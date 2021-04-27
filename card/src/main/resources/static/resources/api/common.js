const commonApi = (function () {
    const prefix = "/appCommon";
    return {
        checkPwd: (accountId, password) => {
            let url = prefix + "/checkTradePwd.action";
            let param = {
                password, accountId
            };
            return $.operate.http(url, "post", JSON.stringify(param));
        }
    }
}());
