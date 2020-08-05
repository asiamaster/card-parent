<script>
    $(function() {
        $(window).resize(function () {
            $('#grid').bootstrapTable('resetView')
        });
        //初始化表格 根据需求默认不查询数据 2020-08-05
        //$('#grid').bootstrapTable('refreshOptions', {url: '/serial/account/listPage.action'});
    });

    /**
     * 查询处理
     */
    function queryDataHandler() {
        $('#grid').bootstrapTable('refreshOptions', {pageNumber : 1, url: '/serial/account/listPage.action'});
    }

    /**
     * table参数组装
     * 可修改queryParams向服务器发送其余的参数
     * @param params
     */
    function queryParams(params) {
        let temp = {
            rows: params.limit,   //页面大小
            page: ((params.offset / params.limit) + 1) || 1, //页码
            sort: params.sort,
            order: params.order
        };
        return $.extend(temp, bui.util.bindGridMeta2Form('grid', 'queryForm'));
    }
</script>
