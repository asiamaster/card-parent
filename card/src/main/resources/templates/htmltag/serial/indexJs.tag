<script>
    $(function() {
        $(window).resize(function () {
            $('#grid').bootstrapTable('resetView')
        });
        //初始化表格 根据需求默认不查询数据 2020-08-05
        //$('#grid').bootstrapTable('refreshOptions', {url: '/serial/account/listPage.action'});
    });

    function customerQueryCallback(dom, success) {
        if (success) {
            $("#includeSlave").removeAttr("disabled");
        }else {
            $("#includeSlave").attr("disabled", "disabled");
        }
    }

    /**
     * 查询处理
     */
    function queryDataHandler() {
        $('#grid').bootstrapTable('refreshOptions', {pageNumber : 1, url: '/serial/account/listPage.action'});
        //查询操作金额合计
        $('#span_operate_amount').html('');
        $.ajax({
            type:'POST',
            url:'/serial/account/countOperateAmount.action',
            dataType:'json',
            data:bui.util.bindGridMeta2Form('grid', 'queryForm'),
            success:function(ret) {
                if (ret.code === '200') {
                    $('#span_operate_amount').html('发生金额合计：' + ret.data);
                } else {
                    $('#span_operate_amount').html('发生金额合计：0');
                }
            },
            error:function() {
                $('#span_operate_amount').html('发生金额合计：0');
            }
        });
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

    /**
     * 用于导出  当数据为空时给出信息提示
     */
    function exportExcel() {
        let totalRows = $('#grid').bootstrapTable("getOptions").totalRows;
        if (totalRows === 0) {
            showWarning("暂无数据，无法导出");
            return;
        }
        bui.util.doExport("grid", "queryForm");
    }
</script>
