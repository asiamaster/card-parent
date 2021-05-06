const etcApi = (function () {
    const prefix = "/etc";
    return {
        initTable: (queryParams) => {
            let options = {
                uniqueId: "id",
                url: prefix + "/page.action",
                sortName: "create_time",
                modalName: "已绑定车牌号列表",
                queryParams: function (params) {
                    let curParams = {
                        // 传递参数查询参数
                        rows: params.limit,
                            page: params.offset / params.limit + 1,
                        searchValue: params.search,
                        sort: params.sort,
                        order: params.order
                    };
                    return $.extend(curParams, queryParams);
                }
            };
            $.table.init(options);
        },
        initPageTable:()=>{
            let options = {
                uniqueId: "id",
                url: prefix + "/page.action",
                sortName: "create_time",
                modalName: "已绑定车牌号列表"
            };
            $.table.init(options);
        },
        getCardInfo: (cardNo) => {
            let url = prefix + "/cardInfo.action";
            return $.operate.http(url, "get", {cardNo});
        },
        //绑定
        bind: (cardInfo, plateNo, description) => {
            let url = prefix + "/bind.action";
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
