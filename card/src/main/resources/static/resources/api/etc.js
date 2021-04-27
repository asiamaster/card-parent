const etcApi = (function () {
    const prefix = "/etc";
    return {
        initTable: (accountId) => {
            let options = {
                uniqueId: "id",
                url: prefix + "/page.action",
                sortName: "modify_time",
                modalName: "已绑定车牌号列表",
                queryParams: function (params) {
                    return {
                        // 传递参数查询参数
                        rows: params.limit,
                        page: params.offset / params.limit + 1,
                        searchValue: params.search,
                        sort: params.sort,
                        order: params.order,
                        accountId
                    };
                }
            };
            $.table.init(options);
        },
        getCardInfo: (cardNo) => {
            let url = prefix + "/cardInfo.action";
            return $.operate.http(url, "get", {cardNo});
        },
        //绑定
        add: (cardInfo, plateNo, description) => {
            let url = prefix + "/add.action";
            let params = {
                accountId: cardInfo.accountId,
                cardNo: cardInfo.cardNo,
                customerId: cardInfo.customerId,
                tradePwd: cardInfo.tradePwd,
                plateNo,
                description,
            };
            return $.operate.http(url, "post", JSON.stringify(params));
        },
        //解绑
        unbind(cardInfo, id) {
            let url = prefix + "/unbind.action";
            let params = {
                accountId: cardInfo.accountId,
                cardNo: cardInfo.cardNo,
                customerId: cardInfo.customerId,
                id
            };
            return $.operate.http(url, "post", JSON.stringify(params));
        }
    }
}());
