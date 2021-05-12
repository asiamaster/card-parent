const accountQueryApi = (function () {
    const prefix = "/accountQuery";
    return {
        getSingleByCardNo: (cardNo) => {
            let url = prefix + "/single.action";
            let param = {
                cardNo
            };
            return $.operate.http(url, "get", param);
        }
    }
}());
