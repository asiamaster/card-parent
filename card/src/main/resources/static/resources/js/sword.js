// 当前table相关信息
table = {
    config: {},
    // 当前实例配置
    options: {},
    // 设置实例配置
    set: function (id) {
        if ($.common.getLength(table.config) > 1) {
            var tableId = $.common.isEmpty(id) ? $(event.currentTarget).parents(".bootstrap-table").find(".table").attr("id") : id;
            if ($.common.isNotEmpty(tableId)) {
                table.options = table.get(tableId);
            }
        }
    },
    // 获取实例配置
    get: function (id) {
        return table.config[id];
    },
    // 记住选择实例组
    rememberSelecteds: {},
    // 记住选择ID组
    rememberSelectedIds: {}
};

//当前tab标签信息
tab = {
    tabMap: new Map(),
    options: {}
};

(function ($) {
    $.extend({
        _tree: {},
        bttTable: {},
        // 表格封装处理
        table: {
            // 初始化表格参数
            init: function (options) {
                let defaults = {
                    id: "grid",
                    type: 0, // 0 代表bootstrapTable 1代表bootstrapTreeTable
                    height: undefined,
                    sidePagination: "server",
                    sortName: "",
                    sortOrder: "desc",
                    pagination: true,
                    pageSize: 10,
                    pageList: [10, 20, 50],
                    toolbar: "toolbar",
                    striped: true,
                    escape: false,
                    firstLoad: true,
                    showFooter: false,
                    search: false,
                    showSearch: true,
                    showPageGo: false,
                    showRefresh: false,
                    showColumns: false,
                    showToggle: false,
                    showExport: false,
                    clickToSelect: true,
                    singleSelect: false,
                    mobileResponsive: true,
                    rememberSelected: false,
                    fixedColumns: false,
                    fixedNumber: 0,
                    rightFixedColumns: false,
                    rightFixedNumber: 0,
                    queryParams: $.table.queryParams,
                    onClickRow: $.table.onClickRow,
                    rowStyle: {},
                    showLoading: true,
                };
                var options = $.extend(defaults, options);
                table.options = options;
                table.config[options.id] = options;
                $.table.initEvent();
                if (options.showLoading) {
                    $.modal.loading("loading...");
                }
                $('#' + options.id).bootstrapTable({
                    id: options.id,
                    url: options.url,                                   // 请求后台的URL（*）
                    contentType: "application/x-www-form-urlencoded",   // 编码类型
                    method: 'post',                                     // 请求方式（*）
                    cache: false,                                       // 是否使用缓存
                    height: options.height,                             // 表格的高度
                    striped: options.striped,                           // 是否显示行间隔色
                    sortable: true,                                     // 是否启用排序
                    sortStable: false,                                   // 设置为 true 将获得稳定的排序
                    sortName: options.sortName,                         // 排序列名称
                    sortOrder: options.sortOrder,                       // 排序方式  asc 或者 desc
                    pagination: options.pagination,                     // 是否显示分页（*）
                    pageNumber: 1,                                      // 初始化加载第一页，默认第一页
                    pageSize: options.pageSize,                         // 每页的记录行数（*）
                    pageList: options.pageList,                         // 可供选择的每页的行数（*）
                    firstLoad: options.firstLoad,                       // 是否首次请求加载数据，对于数据较大可以配置false
                    escape: options.escape,                             // 转义HTML字符串
                    showFooter: options.showFooter,                     // 是否显示表尾
                    iconSize: 'outline',                                // 图标大小：undefined默认的按钮尺寸 xs超小按钮sm小按钮lg大按钮
                    toolbar: '#' + options.toolbar,                     // 指定工作栏
                    sidePagination: options.sidePagination,             // server启用服务端分页client客户端分页
                    search: options.search,                              // 是否显示搜索框功能
                    searchText: options.searchText,                     // 搜索框初始显示的内容，默认为空
                    showSearch: options.showSearch,                     // 是否显示检索信息
                    showPageGo: options.showPageGo,               		// 是否显示跳转页
                    showRefresh: options.showRefresh,                   // 是否显示刷新按钮
                    showColumns: options.showColumns,                   // 是否显示隐藏某列下拉框
                    showToggle: options.showToggle,                     // 是否显示详细视图和列表视图的切换按钮
                    showExport: options.showExport,                     // 是否支持导出文件
                    uniqueId: options.uniqueId,                         // 唯 一的标识符
                    clickToSelect: options.clickToSelect,				// 是否启用点击选中行
                    singleSelect: options.singleSelect,                 // 是否单选checkbox
                    mobileResponsive: options.mobileResponsive,         // 是否支持移动端适配
                    detailView: options.detailView,                     // 是否启用显示细节视图
                    onClickRow: options.onClickRow,                     // 点击某行触发的事件
                    onDblClickRow: options.onDblClickRow,               // 双击某行触发的事件
                    onClickCell: options.onClickCell,                   // 单击某格触发的事件
                    onDblClickCell: options.onDblClickCell,             // 双击某格触发的事件
                    onEditableSave: options.onEditableSave,             // 行内编辑保存的事件
                    onExpandRow: options.onExpandRow,                   // 点击详细视图的事件
                    rememberSelected: options.rememberSelected,         // 启用翻页记住前面的选择
                    fixedColumns: options.fixedColumns,                 // 是否启用冻结列（左侧）
                    fixedNumber: options.fixedNumber,                   // 列冻结的个数（左侧）
                    rightFixedColumns: options.rightFixedColumns,       // 是否启用冻结列（右侧）
                    rightFixedNumber: options.rightFixedNumber,         // 列冻结的个数（右侧）
                    onReorderRow: options.onReorderRow,                 // 当拖拽结束后处理函数
                    queryParams: options.queryParams,                   // 传递参数（*）
                    rowStyle: options.rowStyle,                         // 通过自定义函数设置行样式
                    columns: options.columns,                           // 显示列信息（*）
                    responseHandler: $.table.responseHandler,           // 在加载服务器发送来的数据之前处理函数
                    onLoadSuccess: $.table.onLoadSuccess,               // 当所有数据被加载时触发处理函数
                    onLoadError: $.table.onLoadError,
                    exportOptions: options.exportOptions,               // 前端导出忽略列索引
                    detailFormatter: options.detailFormatter,           // 在行下面展示其他数据列表
                    formatLoadingMessage: () => {
                        return "";
                    }
                });
            },
            // 获取实例ID，如存在多个返回#id1,#id2 delimeter分隔符
            getOptionsIds: function (separator) {
                var _separator = $.common.isEmpty(separator) ? "," : separator;
                var optionsIds = "";
                $.each(table.config, function (key, value) {
                    optionsIds += "#" + key + _separator;
                });
                return optionsIds.substring(0, optionsIds.length - 1);
            },
            // 查询条件
            queryParams: function (params) {
                let curParams = {
                    // 传递参数查询参数
                    rows: params.limit,
                    page: params.offset / params.limit + 1,
                    searchValue: params.search,
                    sort: params.sort,
                    order: params.order
                };
                let currentId = $.common.isEmpty(table.options.formId) ? 'queryForm' : table.options.formId;
                return $.extend(curParams, $.common.formToPairValue(currentId));
            },
            //单击事件
            onClickRow: function (row, $element, field) {
                // //判断是否已选中
                // if ($($element).hasClass("changeColor")) {
                //     //已选中则移除 当前行的class='changeColor'
                //     $($element).removeClass('changeColor');
                // } else
                // {
                //     //未点击则，为当前行添加 class='changeColor'
                //     $($element).addClass('changeColor');
                // }
            },
            // 请求获取数据后处理回调函数
            responseHandler: function (res) {
                if (typeof table.options.responseHandler == "function") {
                    table.options.responseHandler(res);
                }

                if (res.code == 200) {
                    if ($.common.isNotEmpty(table.options.sidePagination) && table.options.sidePagination == 'client') {
                        return res.rows;
                    } else {
                        if ($.common.isNotEmpty(table.options.rememberSelected) && table.options.rememberSelected) {
                            var column = $.common.isEmpty(table.options.uniqueId) ? table.options.columns[1].field : table.options.uniqueId;
                            $.each(res.data, function (i, row) {
                                row.state = $.inArray(row[column], table.rememberSelectedIds[table.options.id]) !== -1;
                            })
                        }
                        return {rows: res.rows, total: res.total};
                    }
                } else {
                    $.modal.alertWarning(res.message);
                    return {rows: [], total: 0};
                }

            },
            // 初始化事件
            initEvent: function () {
                // 实例ID信息
                var optionsIds = $.table.getOptionsIds();
                // 监听事件处理
                var TABLE_EVENTS = "all.bs.table click-cell.bs.table dbl-click-cell.bs.table click-row.bs.table dbl-click-row.bs.table sort.bs.table check.bs.table uncheck.bs.table onUncheck check-all.bs.table uncheck-all.bs.table check-some.bs.table uncheck-some.bs.table load-success.bs.table load-error.bs.table column-switch.bs.table page-change.bs.table search.bs.table toggle.bs.table show-search.bs.table expand-row.bs.table collapse-row.bs.table refresh-options.bs.table reset-view.bs.table refresh.bs.table"
                $(optionsIds).on(TABLE_EVENTS, function () {
                    table.set($(this).attr("id"));
                });
                // 选中、取消、全部选中、全部取消（事件）
                $(optionsIds).on("check.bs.table check-all.bs.table uncheck.bs.table uncheck-all.bs.table", function (e, rows , $element) {
                    // 复选框分页保留保存选中数组
                    var rowIds = $.table.affectedRowIds(rows);
                    if ($.common.isNotEmpty(table.options.rememberSelected) && table.options.rememberSelected) {
                        func = $.inArray(e.type, ['check', 'check-all']) > -1 ? 'union' : 'difference';
                        var selectedIds = table.rememberSelectedIds[table.options.id];
                        if ($.common.isNotEmpty(selectedIds)) {
                            table.rememberSelectedIds[table.options.id] = _[func](selectedIds, rowIds);
                        } else {
                            table.rememberSelectedIds[table.options.id] = _[func]([], rowIds);
                        }
                        var selectedRows = table.rememberSelecteds[table.options.id];
                        if ($.common.isNotEmpty(selectedRows)) {
                            table.rememberSelecteds[table.options.id] = _[func](selectedRows, rows);
                        } else {
                            table.rememberSelecteds[table.options.id] = _[func]([], rows);
                        }

                    }
                });
                // 加载成功、选中、取消、全部选中、全部取消（事件）
                $(optionsIds).on("check.bs.table uncheck.bs.table check-all.bs.table uncheck-all.bs.table load-success.bs.table", function () {
                    var toolbar = table.options.toolbar;
                    var uniqueId = table.options.uniqueId;
                    // 工具栏按钮控制
                    var rows = $.common.isEmpty(uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(uniqueId);
                    // 非多个禁用
                    $('#' + toolbar + ' .multiple').toggleClass('disabled', !rows.length);
                    // 非单个禁用
                    $('#' + toolbar + ' .single').toggleClass('disabled', rows.length != 1);
                });
                // 图片预览事件
                $(optionsIds).off("click").on("click", '.img-circle', function () {
                    var src = $(this).attr('src');
                    var target = $(this).data('target');
                    var height = $(this).data('height');
                    var width = $(this).data('width');
                    if ($.common.equals("self", target)) {
                        layer.open({
                            title: false,
                            type: 1,
                            closeBtn: true,
                            shadeClose: true,
                            area: ['auto', 'auto'],
                            content: "<img src='" + src + "' height='" + height + "' width='" + width + "'/>"
                        });
                    } else if ($.common.equals("blank", target)) {
                        window.open(src);
                    }
                });
                // 单击tooltip事件
                $(optionsIds).on("click", '.tooltip-show', function () {
                    var target = $(this).data('target');
                    var input = $(this).prev();
                    if ($.common.equals("copy", target)) {
                        input.select();
                        document.execCommand("copy");
                    } else if ($.common.equals("open", target)) {
                        parent.bs4pop.alert(input.val(),
                            {type: 'success'}
                        );
                    }
                });
            },
            // 当所有数据被加载时触发
            onLoadSuccess: function (data) {
                if (typeof table.options.onLoadSuccess == "function") {
                    table.options.onLoadSuccess(data);
                }
                // 浮动提示框特效
                $("[data-toggle='tooltip']").tooltip();
                $.modal.closeLoading();
            },
            onLoadError: function (status, data) {
                $.modal.closeLoading();
            },
            // 表格销毁
            destroy: function (tableId) {
                var currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
                $("#" + currentId).bootstrapTable('destroy');
            },
            // 序列号生成
            serialNumber: function (index, tableId) {
                var currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
                var tableParams = $("#" + currentId).bootstrapTable('getOptions');
                var pageSize = tableParams.pageSize;
                var pageNumber = tableParams.pageNumber;
                return pageSize * (pageNumber - 1) + index + 1;
            },
            // 列超出指定长度浮动提示 target（copy单击复制文本 open弹窗打开文本）
            tooltip: function (value, length, target) {
                var _length = $.common.isEmpty(length) ? 20 : length;
                var _text = "";
                var _value = $.common.nullToStr(value);
                var _target = $.common.isEmpty(target) ? 'copy' : target;
                if (_value.length > _length) {
                    _text = _value.substr(0, _length) + "...";
                    _value = _value.replace(/\'/g, "&apos;");
                    _value = _value.replace(/\"/g, "&quot;");
                    var actions = [];
                    actions.push($.common.sprintf('<input id="tooltip-show" style="opacity: 0;position: absolute;z-index:-1" type="text" value="%s"/>', _value));
                    actions.push($.common.sprintf('<a href="###" class="tooltip-show" data-toggle="tooltip" data-target="%s" title="%s">%s</a>', _target, _value, _text));
                    return actions.join('');
                } else {
                    _text = _value;
                    return _text;
                }
            },
            // 搜索-默认第一个form
            search: function (formId, tableId, data) {
                table.set(tableId);
                var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                var params = $.common.isEmpty(tableId) ? $("#" + table.options.id).bootstrapTable('getOptions') : $("#" + tableId).bootstrapTable('getOptions');
                params.queryParams = function (params) {
                    var search = $.common.formToJSON(currentId);
                    if ($.common.isNotEmpty(data)) {
                        $.each(data, function (key) {
                            search[key] = data[key];
                        });
                    }
                    search.pageSize = params.limit;
                    search.pageNum = params.offset / params.limit + 1;
                    search.searchValue = params.search;
                    search.orderByColumn = params.sort;
                    search.isAsc = params.order;
                    console.log(search)
                    return search;
                }

                if ($.common.isNotEmpty(tableId)) {
                    $("#" + tableId).bootstrapTable('refresh', params);
                } else {
                    $("#" + table.options.id).bootstrapTable('refresh', params);
                }
                data = {};
            },
            // 导出数据
            exportExcel: function (formId) {
                table.set();
                bui.util.doExport(table.options.id, formId)
            },
            // 刷新表格
            refresh: function (tableId) {
                let currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
                $("#" + currentId).bootstrapTable('refresh', {
                    silent: true
                });
            },
            // 查询表格指定列值
            selectColumns: function (column) {
                var rows = $.map($("#" + table.options.id).bootstrapTable('getSelections'), function (row) {
                    return row[column];
                });
                if ($.common.isNotEmpty(table.options.rememberSelected) && table.options.rememberSelected) {
                    var selectedRows = table.rememberSelecteds[table.options.id];
                    if ($.common.isNotEmpty(selectedRows)) {
                        rows = $.map(table.rememberSelecteds[table.options.id], function (row) {
                            return row[column];
                        });
                    }
                }
                return $.common.uniqueFn(rows);
            },
            // 获取当前页选中或者取消的行ID
            affectedRowIds: function (rows) {
                var column = $.common.isEmpty(table.options.uniqueId) ? table.options.columns[1].field : table.options.uniqueId;
                var rowIds;
                if ($.isArray(rows)) {
                    rowIds = $.map(rows, function (row) {
                        return row[column];
                    });
                } else {
                    rowIds = [rows[column]];
                }
                return rowIds;
            },
            // 查询表格首列值
            selectFirstColumns: function () {
                var rows = $.map($("#" + table.options.id).bootstrapTable('getSelections'), function (row) {
                    return row[table.options.columns[1].field];
                });
                if ($.common.isNotEmpty(table.options.rememberSelected) && table.options.rememberSelected) {
                    var selectedRows = table.rememberSelecteds[table.options.id];
                    if ($.common.isNotEmpty(selectedRows)) {
                        rows = $.map(selectedRows, function (row) {
                            return row[table.options.columns[1].field];
                        });
                    }
                }
                return $.common.uniqueFn(rows);
            },
            // 显示表格指定列
            showColumn: function (column, tableId) {
                var currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
                $("#" + currentId).bootstrapTable('showColumn', column);
            },
            // 隐藏表格指定列
            hideColumn: function (column, tableId) {
                var currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
                $("#" + currentId).bootstrapTable('hideColumn', column);
            }
        },
        //tab标签页操作
        tab: {
            init: (options) => {
                let defaults = {
                    id: 'tab',
                    prefixUrl: '',
                    activeId: undefined,
                    params: {}
                };
                options = $.extend(defaults, options);
                tab.options = options;
                let hasActiveId = $.common.isNotEmpty(options.activeId);
                //初始化页签资源
                let urlParams = $.common.jsonObj2UrlParams(options.params);
                $('#' + options.id).find('a').each((i, ele) => {
                    let id = $(ele).attr('id');
                    let uri = $(ele).attr('uri');
                    let href = $(ele).attr('href');
                    let isActive = false;
                    //如果没有指定初始激活的id，那就默认第一个元素元素
                    if (!hasActiveId && i == 0) {
                        isActive = true;
                    }
                    if (hasActiveId && options.activeId == id) {
                        isActive = true;
                    }
                    tab.tabMap.set(id, {
                        id: id,
                        url: options.prefixUrl + uri + "?" + urlParams,
                        href: href,
                        isActive: isActive
                    })
                });

                //加载已active的选项卡
                for (let item of tab.tabMap.values()) {
                    if (item.isActive) {
                        //激活选项卡
                        let $ele = $('#' + item.id);
                        $ele.addClass('active');
                        $(item.href).addClass('show active');

                        $(item.href).load(item.url);
                        //加载过一次不再重复加载
                        item.hasLoad = true;
                        break;
                    }
                }
            },
            //选项卡切换加载数据
            onChange: (callback) => {
                let id = '#' + tab.options.id + ' a[data-toggle="tab"]';
                $(id).on('shown.bs.tab', (e) => {
                    let oldId = e.relatedTarget.getAttribute("id");
                    let relatedTargetTab = tab.tabMap.get(oldId);
                    relatedTargetTab.isActive = false;
                    let newId = e.target.getAttribute("id");
                    let targetTab = tab.tabMap.get(newId);
                    targetTab.isActive = true;
                    //加载过一次不再重复加载
                    if (!targetTab.hasLoad) {
                        $(targetTab.href).load(targetTab.url);
                        targetTab.hasLoad = true;
                    }

                    if (typeof callback == 'function') {
                        callback(targetTab, relatedTargetTab)
                    }
                });
            },
            refresh:function (id) {
                if ($.common.isEmpty(id)){
                    for (let item of tab.tabMap.values()) {
                        if (item.isActive) {
                            id = item.id;
                            break;
                        }
                    }
                }
                let targetTab = tab.tabMap.get(id);
                $(targetTab.href).children().remove();
                $(targetTab.href).load(targetTab.url);
            }
        },
        // 表格树封装处理
        treeTable: {
            // 初始化表格
            init: function (options) {
                var defaults = {
                    id: "bootstrap-tree-table",
                    type: 1, // 0 代表bootstrapTable 1代表bootstrapTreeTable
                    height: 0,
                    rootIdValue: null,
                    ajaxParams: {},
                    toolbar: "toolbar",
                    striped: false,
                    expandColumn: 1,
                    showSearch: true,
                    showRefresh: true,
                    showColumns: true,
                    expandAll: true,
                    expandFirst: true
                };
                var options = $.extend(defaults, options);
                table.options = options;
                table.config[options.id] = options;
                $.bttTable = $('#' + options.id).bootstrapTreeTable({
                    code: options.code,                                 // 用于设置父子关系
                    parentCode: options.parentCode,                     // 用于设置父子关系
                    type: 'post',                                       // 请求方式（*）
                    url: options.url,                                   // 请求后台的URL（*）
                    data: options.data,                                 // 无url时用于渲染的数据
                    ajaxParams: options.ajaxParams,                     // 请求数据的ajax的data属性
                    rootIdValue: options.rootIdValue,                   // 设置指定根节点id值
                    height: options.height,                             // 表格树的高度
                    expandColumn: options.expandColumn,                 // 在哪一列上面显示展开按钮
                    striped: options.striped,                           // 是否显示行间隔色
                    bordered: false,                                    // 是否显示边框
                    toolbar: '#' + options.toolbar,                     // 指定工作栏
                    showSearch: options.showSearch,                     // 是否显示检索信息
                    showRefresh: options.showRefresh,                   // 是否显示刷新按钮
                    showColumns: options.showColumns,                   // 是否显示隐藏某列下拉框
                    expandAll: options.expandAll,                       // 是否全部展开
                    expandFirst: options.expandFirst,                   // 是否默认第一级展开--expandAll为false时生效
                    columns: options.columns,                           // 显示列信息（*）
                    responseHandler: $.treeTable.responseHandler        // 当所有数据被加载时触发处理函数
                });
            },
            // 条件查询
            search: function (formId) {
                var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                var params = $.common.formToJSON(currentId);
                $.bttTable.bootstrapTreeTable('refresh', params);
            },
            // 刷新
            refresh: function () {
                $.bttTable.bootstrapTreeTable('refresh');
            },
            // 查询表格树指定列值
            selectColumns: function (column) {
                var rows = $.map($.bttTable.bootstrapTreeTable('getSelections'), function (row) {
                    return row[column];
                });
                return $.common.uniqueFn(rows);
            },
            // 请求获取数据后处理回调函数，校验异常状态提醒
            responseHandler: function (res) {
                if (typeof table.options.responseHandler == "function") {
                    table.options.responseHandler(res);
                }
                if (res.code != undefined && res.code != 0) {
                    $.modal.alertWarning(res.msg);
                    return [];
                } else {
                    return res;
                }
            },
        },
        // 表单封装处理
        form: {
            // 表单重置
            reset: function (formId, tableId) {
                table.set(tableId);
                var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                $("#" + currentId)[0].reset();
                if (table.options.type == table_type.bootstrapTable) {
                    if ($.common.isEmpty(tableId)) {
                        $("#" + table.options.id).bootstrapTable('refresh');
                    } else {
                        $("#" + tableId).bootstrapTable('refresh');
                    }
                } else if (table.options.type == table_type.bootstrapTreeTable) {
                    if ($.common.isEmpty(tableId)) {
                        $("#" + table.options.id).bootstrapTreeTable('refresh', []);
                    } else {
                        $("#" + tableId).bootstrapTreeTable('refresh', []);
                    }
                }
            },
            // 获取选中复选框项
            selectCheckeds: function (name) {
                var checkeds = "";
                $('input:checkbox[name="' + name + '"]:checked').each(function (i) {
                    if (0 == i) {
                        checkeds = $(this).val();
                    } else {
                        checkeds += ("," + $(this).val());
                    }
                });
                return checkeds;
            },
            // 获取选中下拉框项
            selectSelects: function (name) {
                var selects = "";
                $('#' + name + ' option:selected').each(function (i) {
                    if (0 == i) {
                        selects = $(this).val();
                    } else {
                        selects += ("," + $(this).val());
                    }
                });
                return selects;
            }
        },
        // 弹出层封装处理
        modal: {
            // 显示图标
            icon: function (type) {
                var icon = "";
                if (type == modal_status.WARNING) {
                    icon = 0;
                } else if (type == modal_status.SUCCESS) {
                    icon = 1;
                } else if (type == modal_status.FAIL) {
                    icon = 2;
                } else {
                    icon = 3;
                }
                return icon;
            },
            // 消息提示
            msg: function (content, type) {
                bs4pop.notice(content, {position: 'leftcenter', type: type})
            },
            // 错误消息
            msgError: function (content) {
                $.modal.msg(content, modal_status.FAIL);
            },
            // 成功消息
            msgSuccess: function (content) {
                $.modal.msg(content, modal_status.SUCCESS);
            },
            // 警告消息
            msgWarning: function (content) {
                $.modal.msg(content, modal_status.WARNING);
            },
            // 弹出提示
            alert: function (content, type) {
                bs4pop.alert(content,
                    {type: type}
                );
            },
            // 消息提示并刷新父窗体
            msgReload: function (msg, type) {
                layer.msg(msg, {
                        icon: $.modal.icon(type),
                        time: 500,
                        shade: [0.1, '#8F8F8F']
                    },
                    function () {
                        $.modal.reload();
                    });
            },
            // 错误提示
            alertError: function (content) {
                $.modal.alert(content, modal_status.FAIL);
            },
            // 成功提示
            alertSuccess: function (content) {
                $.modal.alert(content, modal_status.SUCCESS);
            },
            // 警告提示
            alertWarning: function (content) {
                $.modal.alert(content, modal_status.WARNING);
            },
            // 关闭全部窗体
            closeAll: function () {
                layer.closeAll();
            },
            // 确认窗体
            confirm: function (content, callBack) {
                bs4pop.confirm(content, {title: "确认提示"}, callBack);
            },
            // 弹出层指定宽度
            open: function (title, url, width, height, isFrame) {
                //如果是移动端，就使用自适应大小弹窗
                if ($.common.isMobile()) {
                    width = 'auto';
                    height = 'auto';
                }
                if ($.common.isEmpty(title)) {
                    title = false;
                }
                if ($.common.isEmpty(url)) {
                    url = "/404.html";
                }
                if ($.common.isEmpty(width)) {
                    width = 800;
                }
                if ($.common.isEmpty(height)) {
                    height = ($(window).height() - 50);
                }
                if ($.common.isEmpty(isFrame)) {
                    isFrame = true;
                }
                bs4pop.dialog({
                    title: title,//对话框title
                    content: url, //对话框内容
                    width: width,//宽度
                    height: height,//高度
                    isIframe: isFrame,//默认是页面层，非iframe
                });
            },
            // 弹出层指定参数选项
            openOptions: function (options) {
                let defaults = {
                    title: "系统窗口",//对话框title
                    content: "/404.html",//对话框内容
                    width: '50%',//宽度
                    height: '60%',//高度
                    isIframe: true,//默认是页面层，非iframe
                };
                options = $.extend(defaults, options);
                var _btn = ['<i class="fa fa-check"></i> 确认', '<i class="fa fa-close"></i> 关闭'];
                // if ($.common.isEmpty(options.yes)) {
                //     options.yes = function (index, layero) {
                //         options.callBack(index, layero);
                //     }
                // }
                bs4pop.dialog(options);
            },
            // 禁用按钮
            disable: function () {
                var doc = window.top == window.parent ? window.document : window.parent.document;
                $("a[class*=layui-layer-btn]", doc).addClass("layer-disabled");
            },
            // 启用按钮
            enable: function () {
                var doc = window.top == window.parent ? window.document : window.parent.document;
                $("a[class*=layui-layer-btn]", doc).removeClass("layer-disabled");
            },
            // 打开遮罩层
            loading: function (message) {
                bui.loading.show(message);
            },
            // 关闭遮罩层
            closeLoading: function () {
                // setTimeout(function(){
                //     $.unblockUI();
                // }, 50);
                bui.loading.hide();
            },
            // 重新加载
            reload: function () {
                parent.location.reload();
            }
        },
        // 操作封装处理
        operate: {
            // 提交数据
            submit: function (url, type, dataType, data, callback) {
                let config = {
                    url: url,
                    type: type,
                    contentType: "application/json; charset=utf-8",
                    dataType: dataType,
                    data: data,
                    beforeSend: () => {
                        $.modal.loading("正在处理中，请稍后...");
                    },
                    success: (result) => {
                        if (typeof callback == "function") {
                            callback(result);
                        }
                        $.operate.ajaxSuccess(result);
                    },
                    error: (status, result) => {
                        $.modal.closeLoading();
                    }
                };
                //非get请求都转json
                if (!$.common.equalsIgnoreCase("get", type)) {
                    config.contentType = "application/json; charset=utf-8";
                    config.data = JSON.stringify(data)
                }
                $.ajax(config)
            },
            // post请求传输
            post: function (url, data, callback) {
                $.operate.submit(url, "post", "json", data, callback);
            },
            // get请求传输
            get: function (url, callback) {
                $.operate.submit(url, "get", "json", "", callback);
            },
            removeWithTitle: function (id, width, height,modalName) {
                table.set();
                let _url = $.operate.removeUrl(id);
                if ($.common.isEmpty(_url)){
                    return;
                }
                let _width = $.common.isEmpty(width) ? "800" : width;
                let _height = $.common.isEmpty(height) ? ($(window).height() - 50) : height;
                //如果是移动端，就使用自适应大小弹窗
                if ($.common.isMobile()) {
                    _width = 'auto';
                    _height = 'auto';
                }
                let options = {
                    title: modalName,
                    width: _width,
                    height: _height,
                    content: _url,
                    btns: [{
                        label: '确定', className: 'btn btn-primary', onClick(e, $iframe) {
                            try {
                                return $iframe[0].contentWindow.submitHandler(e)
                            } catch (ex) {
                                return false;
                            }
                        }
                    },{
                        label: '取消', className: 'btn btn-secondary', onClick(e, $iframe) {
                            try {
                                return $iframe[0].contentWindow.cancelHandler(e)
                            } catch (ex) {
                                return true;
                            }
                        }
                    }]
                };
                $.modal.openOptions(options);
            },
            // 详细访问地址
            removeUrl: function (id) {
                var url = "/404.html";
                if ($.common.isNotEmpty(id)) {
                    url = table.options.removeUrl.replace("{id}", id);
                } else {
                    var id = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
                    if (id.length == 0) {
                        $.modal.alertWarning("请至少选择一条记录");
                        return;
                    }
                    url = table.options.removeUrl.replace("{id}", id);
                }
                return url;
            },
            // 详细信息
            detailToPage: function (id) {
                table.set();
                let _url = $.operate.detailUrl(id);
                if ($.common.isEmpty(_url)){
                    return;
                }
                let urlLastNum = _url.substring(_url.lastIndexOf("/") + 1);
                let urlLast=_url.substring(0,_url.lastIndexOf("/")) + "?id=" + urlLastNum;
                alert(urlLast);
                window.location.href=urlLast;
            },
            // 详细信息
            detail: function (id, width, height) {
                table.set();
                let _url = $.operate.detailUrl(id);
                if ($.common.isEmpty(_url)){
                    return;
                }
                let _width = $.common.isEmpty(width) ? "800" : width;
                let _height = $.common.isEmpty(height) ? ($(window).height() - 50) : height;
                //如果是移动端，就使用自适应大小弹窗
                if ($.common.isMobile()) {
                    _width = 'auto';
                    _height = 'auto';
                }
                let options = {
                    title: table.options.modalName,
                    width: _width,
                    height: _height,
                    content: _url,
                    btns: [{label: '关闭', className: 'btn-secondary', onClick(e) {

                        }}]
                };
                $.modal.openOptions(options);
            },
            // 详细访问地址
            detailUrl: function (id) {
                var url = "/404.html";
                if ($.common.isNotEmpty(id)) {
                    url = table.options.detailUrl.replace("{id}", id);
                } else {
                    var id = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
                    if (id.length == 0) {
                        $.modal.alertWarning("请至少选择一条记录");
                        return;
                    }
                    url = table.options.detailUrl.replace("{id}", id);
                }
                return url;
            },
            // 删除信息
            remove: function () {
                table.set();
                var row = $.map($("#" + table.options.id).bootstrapTable('getSelections'), function (row) {
                    return row;
                });
                if (row.length == 0) {
                    $.modal.alertWarning("请至少一条数据");
                    return;
                }
                $.modal.confirm("确定删除该条信息吗？", function (sure) {
                    if (!sure) {
                        return;
                    }
                    var data = {"id": row[0].id};
                    $.operate.submit(table.options.removeUrl, "post", "json", data);
                });

            },
            // 批量删除信息
            removeAll: function () {
                table.set();
                var rows = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
                if (rows.length == 0) {
                    $.modal.alertWarning("请至少选择一条记录");
                    return;
                }
                $.modal.confirm("确认要删除选中的" + rows.length + "条数据吗?", function () {
                    var url = table.options.removeUrl;
                    var data = {"ids": rows.join()};
                    $.operate.submit(url, "post", "json", data);
                });
            },
            // 清空信息
            clean: function () {
                table.set();
                $.modal.confirm("确定清空所有" + table.options.modalName + "吗？", function () {
                    var url = table.options.cleanUrl;
                    $.operate.submit(url, "post", "json", "");
                });
            },
            // 添加信息
            addWithTitleAndSelect: function (id, width, height, modalName) {
                table.set();
                let _url = $.operate.addUrlSelect(id);
                if ($.common.isEmpty(_url)){
                    return;
                }
                let _width = $.common.isEmpty(width) ? "800" : width;
                let _height = $.common.isEmpty(height) ? ($(window).height() - 50) : height;
                //如果是移动端，就使用自适应大小弹窗
                if ($.common.isMobile()) {
                    _width = 'auto';
                    _height = 'auto';
                }
                let options = {
                    title: modalName,
                    width: _width,
                    height: _height,
                    content: _url,
                    btns: [{
                        label: '确定', className: 'btn btn-primary', onClick(e, $iframe) {
                            try {
                                return $iframe[0].contentWindow.submitHandler(e)
                            } catch (ex) {
                                return false;
                            }
                        }
                    },{
                        label: '取消', className: 'btn btn-secondary', onClick(e, $iframe) {
                            try {
                                return $iframe[0].contentWindow.cancelHandler(e)
                            } catch (ex) {
                                return true;
                            }
                        }
                    }]
                };
                $.modal.openOptions(options);
            },
            // 添加下一步信息
            addWithTitleAndNext: function (url, width, height, modalName) {
                table.set();
                let _url = url;
                if ($.common.isEmpty(url)){
                	_url = $.operate.addUrl(url);
                }
                let options = {
                    title: modalName,
                    width: width,
                    height: height,
                    content: _url,
                    btns: [{
                        label: '下一步', className: 'btn btn-primary container_center', onClick(e, $iframe) {
                            try {
                                return $iframe[0].contentWindow.submitHandler(e)
                            } catch (ex) {
                                console.log(ex);
                                return false;
                            }
                        }
                    },{
                        label: '取消', className: 'btn btn-secondary container_center', onClick(e, $iframe) {
                            try {
                                return $iframe[0].contentWindow.cancelHandler(e)
                            } catch (ex) {
                                return true;
                            }
                        }
                    }]
                };
                $.modal.openOptions(options);
            },
            // 添加信息
            addWithTitle: function (id, width, height, modalName) {
                table.set();
                let options = {
                    title: modalName,
                    width: width,
                    height: height,
                    content: $.operate.addUrl(id),
                    btns: [{
                        label: '确定', className: 'btn btn-primary', onClick(e, $iframe) {
                            try {
                                return $iframe[0].contentWindow.submitHandler(e)
                            } catch (ex) {
                                console.log(ex);
                                return false;
                            }
                        }
                    },{
                        label: '取消', className: 'btn btn-secondary', onClick(e, $iframe) {
                            try {
                                return $iframe[0].contentWindow.cancelHandler(e)
                            } catch (ex) {
                                return true;
                            }
                        }
                    }]
                };
                $.modal.openOptions(options);
            },
            // 添加信息
            add: function (id, width, height) {
            	$.operate.addWithTitle(id, width, height, '添加' + table.options.modalName);
            },
            // 添加访问地址
            addUrl: function (id) {
                var url = $.common.isEmpty(id) ? table.options.createUrl.replace("{id}", "") : table.options.createUrl.replace("{id}", id);
                return url;
            },
            // 添加访问地址
            addUrlSelect: function (id) {
            	 var url = "/404.html";
                 if ($.common.isNotEmpty(id)) {
                     url = table.options.createUrl.replace("{id}", id);
                 } else {
                     var id = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
                     if (id.length == 0) {
                         $.modal.alertWarning("请至少选择一条记录");
                         return;
                     }
                     url = table.options.createUrl.replace("{id}", id);
                 }
                 return url;
            },
            // 修改信息
            editWithTitle: function (id, width, height, modalName) {
            	table.set();
            	let _url = $.operate.editUrl(id);
                if ($.common.isEmpty(_url)){
                    return;
                }
                let _width = $.common.isEmpty(width) ? "800" : width;
                let _height = $.common.isEmpty(height) ? ($(window).height() - 50) : height;
                //如果是移动端，就使用自适应大小弹窗
                if ($.common.isMobile()) {
                    _width = 'auto';
                    _height = 'auto';
                }
                let options = {
                    title: modalName,
                    width: _width,
                    height: _height,
                    content: _url,
                    btns: [{
                        label: '确定', className: 'btn btn-primary', onClick(e, $iframe) {
                            try {
                                return $iframe[0].contentWindow.submitHandler(e)
                            } catch (ex) {
                                return false;
                            }
                        }
                    },{
                        label: '取消', className: 'btn btn-secondary', onClick(e, $iframe) {
                            try {
                                return $iframe[0].contentWindow.cancelHandler(e)
                            } catch (ex) {
                                return true;
                            }
                        }
                    }]
                };
                $.modal.openOptions(options);
            },
            // 修改信息
            edit: function (id) {
                table.set();
                if ($.common.isEmpty(id) && table.options.type == table_type.bootstrapTreeTable) {
                    var row = $("#" + table.options.id).bootstrapTreeTable('getSelections')[0];
                    if ($.common.isEmpty(row)) {
                        $.modal.alertWarning("请至少选择一条记录");
                        return;
                    }
                    var url = table.options.updateUrl.replace("{id}", row[table.options.uniqueId]);
                    $.modal.open("修改" + table.options.modalName, url);
                } else {
                    $.modal.open("修改" + table.options.modalName, $.operate.editUrl(id));
                }
            },
            // 修改访问地址
            editUrl: function (id) {
                var url = "/404.html";
                if ($.common.isNotEmpty(id)) {
                    url = table.options.updateUrl.replace("{id}", id);
                } else {
                    var id = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
                    if (id.length == 0) {
                        $.modal.alertWarning("请至少选择一条记录");
                        return;
                    }
                    url = table.options.updateUrl.replace("{id}", id);
                }
                return url;
            },
            // 保存信息 刷新表格
            save: function (url, data, callback) {
                   let config = {
                       url: url,
                       type: "post",
                       dataType: "json",
                       contentType: "application/json; charset=utf-8",
                       data: data,
                       beforeSend: function () {
                           $.modal.loading("正在处理中，请稍后...");
                          // $.modal.disable();
                       },
                       success: function(result) {
                           if (typeof callback == "function") {
                               callback(result);
                           }
                           $.operate.successCallback(result);
                       },
                       error: function (status, result) {
                           $.modal.hide();
                       }
                   };
                   $.ajax(config)
            },
            // 保存信息 弹出提示框
            saveModal: function (url, data, callback) {
                var config = {
                    url: url,
                    type: "post",
                    dataType: "json",
                    data: data,
                    beforeSend: function () {
                        $.modal.loading("正在处理中，请稍后...");
                    },
                    success: function (result) {
                        if (typeof callback == "function") {
                            callback(result);
                        }
                        if (result.code == web_status.SUCCESS) {
                            $.modal.alertSuccess(result.msg)
                        } else if (result.code == web_status.WARNING) {
                            $.modal.alertWarning(result.msg)
                        } else {
                            $.modal.alertError(result.msg);
                        }
                        $.modal.closeLoading();
                    }
                };
                $.ajax(config)
            },
            // 保存结果弹出msg刷新table表格
            ajaxSuccess: function (result) {
                $.modal.closeLoading();
                if (result.code == web_status.SUCCESS && table.options.type == table_type.bootstrapTable) {
                    $.modal.alertSuccess(result.message);
                    $.table.refresh();
                } else if (result.code == web_status.SUCCESS && table.options.type == table_type.bootstrapTreeTable) {
                    $.modal.alertSuccess(result.message);
                    $.treeTable.refresh();
                    $.common.isEmpty()
                } else if (result.code == web_status.SUCCESS && table.option == undefined) {
                    $.modal.alertSuccess(result.message)
                } else if (result.code == web_status.WARNING) {
                    $.modal.alertWarning(result.message)
                } else {
                    $.modal.alertError(result.message);
                }

            },
            // 成功结果提示msg（父窗体全局更新）
            saveSuccess: function (result) {
                if (result.code == web_status.SUCCESS) {
                    $.modal.msgReload("保存成功,正在刷新数据请稍后……", modal_status.SUCCESS);
                } else if (result.code == web_status.WARNING) {
                    $.modal.alertWarning(result.msg)
                } else {
                    $.modal.alertError(result.msg);
                }
                $.modal.closeLoading();
            },
            // 成功回调执行事件（父窗体静默更新）
            successCallback: function (result) {
                if (result.code == web_status.SUCCESS) {
                    //let parent = window.parent;
                    if (parent.table.options.type == table_type.bootstrapTable) {
                        parent.$.modal.msgSuccess(result.message);
                        parent.$.table.refresh();
                    } else if (parent.table.options.type == table_type.bootstrapTreeTable) {
                        parent.$.modal.msgSuccess(result.message);
                        parent.$.treeTable.refresh();
                    } else {
                        $.modal.msgReload("保存成功,正在刷新数据请稍后……", modal_status.SUCCESS);
                    }
                } else if (result.code == web_status.WARNING) {
                    $.modal.alertWarning(result.message)
                } else {
                    $.modal.alertError(result.message);
                }
                $.modal.closeLoading();
               // $.modal.enable();
            },
        },
        // 校验封装处理
        validate: {
            // 判断返回标识是否唯一 false 不存在 true 存在
            unique: function (value) {
                if (value == "0") {
                    return true;
                }
                return false;
            },
            // 表单验证
            form: function (formId) {
                var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                return $("#" + currentId).validate().form();
            },
            // 重置表单验证（清除提示信息）
            reset: function (formId) {
                var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                return $("#" + currentId).validate().resetForm();
            }
        },
        // 树插件封装处理
        tree: {
            _option: {},
            _lastValue: {},
            // 初始化树结构
            init: function (options) {
                var defaults = {
                    id: "tree",                    // 属性ID
                    expandLevel: 0,                // 展开等级节点
                    view: {
                        selectedMulti: false,      // 设置是否允许同时选中多个节点
                        nameIsHTML: true           // 设置 name 属性是否支持 HTML 脚本
                    },
                    check: {
                        enable: false,             // 置 zTree 的节点上是否显示 checkbox / radio
                        nocheckInherit: true,      // 设置子节点是否自动继承
                    },
                    data: {
                        key: {
                            title: "title"         // 节点数据保存节点提示信息的属性名称
                        },
                        simpleData: {
                            enable: true           // true / false 分别表示 使用 / 不使用 简单数据模式
                        }
                    },
                };
                var options = $.extend(defaults, options);
                $.tree._option = options;
                // 树结构初始化加载
                var setting = {
                    callback: {
                        onClick: options.onClick,                      // 用于捕获节点被点击的事件回调函数
                        onCheck: options.onCheck,                      // 用于捕获 checkbox / radio 被勾选 或 取消勾选的事件回调函数
                        onDblClick: options.onDblClick                 // 用于捕获鼠标双击之后的事件回调函数
                    },
                    check: options.check,
                    view: options.view,
                    data: options.data
                };
                $.get(options.url, function (data) {
                    var treeId = $("#treeId").val();
                    tree = $.fn.zTree.init($("#" + options.id), setting, data);
                    $._tree = tree;
                    var nodes = tree.getNodesByParam("level", options.expandLevel - 1);
                    for (var i = 0; i < nodes.length; i++) {
                        tree.expandNode(nodes[i], true, false, false);
                    }
                    var node = tree.getNodesByParam("id", treeId, null)[0];
                    $.tree.selectByIdName(treeId, node);
                });
            },
            // 搜索节点
            searchNode: function () {
                // 取得输入的关键字的值
                var value = $.common.trim($("#keyword").val());
                if ($.tree._lastValue == value) {
                    return;
                }
                // 保存最后一次搜索名称
                $.tree._lastValue = value;
                var nodes = $._tree.getNodes();
                // 如果要查空字串，就退出不查了。
                if (value == "") {
                    $.tree.showAllNode(nodes);
                    return;
                }
                $.tree.hideAllNode(nodes);
                // 根据搜索值模糊匹配
                $.tree.updateNodes($._tree.getNodesByParamFuzzy("name", value));
            },
            // 根据Id和Name选中指定节点
            selectByIdName: function (treeId, node) {
                if ($.common.isNotEmpty(treeId) && treeId == node.id) {
                    $._tree.selectNode(node, true);
                }
            },
            // 显示所有节点
            showAllNode: function (nodes) {
                nodes = $._tree.transformToArray(nodes);
                for (var i = nodes.length - 1; i >= 0; i--) {
                    if (nodes[i].getParentNode() != null) {
                        $._tree.expandNode(nodes[i], true, false, false, false);
                    } else {
                        $._tree.expandNode(nodes[i], true, true, false, false);
                    }
                    $._tree.showNode(nodes[i]);
                    $.tree.showAllNode(nodes[i].children);
                }
            },
            // 隐藏所有节点
            hideAllNode: function (nodes) {
                var tree = $.fn.zTree.getZTreeObj("tree");
                var nodes = $._tree.transformToArray(nodes);
                for (var i = nodes.length - 1; i >= 0; i--) {
                    $._tree.hideNode(nodes[i]);
                }
            },
            // 显示所有父节点
            showParent: function (treeNode) {
                var parentNode;
                while ((parentNode = treeNode.getParentNode()) != null) {
                    $._tree.showNode(parentNode);
                    $._tree.expandNode(parentNode, true, false, false);
                    treeNode = parentNode;
                }
            },
            // 显示所有孩子节点
            showChildren: function (treeNode) {
                if (treeNode.isParent) {
                    for (var idx in treeNode.children) {
                        var node = treeNode.children[idx];
                        $._tree.showNode(node);
                        $.tree.showChildren(node);
                    }
                }
            },
            // 更新节点状态
            updateNodes: function (nodeList) {
                $._tree.showNodes(nodeList);
                for (var i = 0, l = nodeList.length; i < l; i++) {
                    var treeNode = nodeList[i];
                    $.tree.showChildren(treeNode);
                    $.tree.showParent(treeNode)
                }
            },
            // 获取当前被勾选集合
            getCheckedNodes: function (column) {
                var _column = $.common.isEmpty(column) ? "id" : column;
                var nodes = $._tree.getCheckedNodes(true);
                return $.map(nodes, function (row) {
                    return row[_column];
                }).join();
            },
            // 不允许根父节点选择
            notAllowParents: function (_tree) {
                var nodes = _tree.getSelectedNodes();
                if (nodes.length == 0) {
                    $.modal.msgError("请选择节点后提交");
                    return false;
                }
                for (var i = 0; i < nodes.length; i++) {
                    if (nodes[i].level == 0) {
                        $.modal.msgError("不能选择根节点（" + nodes[i].name + "）");
                        return false;
                    }
                    if (nodes[i].isParent) {
                        $.modal.msgError("不能选择父节点（" + nodes[i].name + "）");
                        return false;
                    }
                }
                return true;
            },
            // 不允许最后层级节点选择
            notAllowLastLevel: function (_tree) {
                var nodes = _tree.getSelectedNodes();
                for (var i = 0; i < nodes.length; i++) {
                    if (!nodes[i].isParent) {
                        $.modal.msgError("不能选择最后层级节点（" + nodes[i].name + "）");
                        return false;
                    }
                }
                return true;
            },
            // 隐藏/显示搜索栏
            toggleSearch: function () {
                $('#search').slideToggle(200);
                $('#btnShow').toggle();
                $('#btnHide').toggle();
                $('#keyword').focus();
            },
            // 折叠
            collapse: function () {
                $._tree.expandAll(false);
            },
            // 展开
            expand: function () {
                $._tree.expandAll(true);
            }
        },
        // 通用方法封装处理
        common: {
            // 判断字符串是否为空
            isEmpty: function (value) {
                if (value == null || this.trim(value) == "") {
                    return true;
                }
                return false;
            },
            // 判断一个字符串是否为非空串
            isNotEmpty: function (value) {
                return !$.common.isEmpty(value);
            },
            // 空对象转字符串
            nullToStr: function (value) {
                if ($.common.isEmpty(value)) {
                    return "-";
                }
                return value;
            },
            // 是否显示数据 为空默认为显示
            visible: function (value) {
                if ($.common.isEmpty(value) || value == true) {
                    return true;
                }
                return false;
            },
            //form表单转json
            form2JsonString: function (formId) {
                var paramArray = $('#' + formId).serializeArray();
                /*请求参数转json对象*/
                var jsonObj = {};
                $(paramArray).each(function () {
                    jsonObj[this.name] = this.value;

                });
                // json对象再转换成json字符串
                return JSON.stringify(jsonObj);
            },
            //json对象转url参数
            jsonObj2UrlParams: (jsonObj) => {
                try {
                    let tempArr = [];
                    for (let i in jsonObj) {
                        let key = encodeURIComponent(i);
                        let value = encodeURIComponent(jsonObj[i]);
                        tempArr.push(key + '=' + value);
                    }
                    return tempArr.join('&');
                } catch (err) {
                    return '';
                }
            },
            // 空格截取
            trim: function (value) {
                if (value == null) {
                    return "";
                }
                return value.toString().replace(/(^\s*)|(\s*$)|\r|\n/g, "");
            },
            // 比较两个字符串（大小写敏感）
            equals: function (str, that) {
                return str == that;
            },
            // 比较两个字符串（大小写不敏感）
            equalsIgnoreCase: function (str, that) {
                return String(str).toUpperCase() === String(that).toUpperCase();
            },
            // 将字符串按指定字符分割
            split: function (str, sep, maxLen) {
                if ($.common.isEmpty(str)) {
                    return null;
                }
                var value = String(str).split(sep);
                return maxLen ? value.slice(0, maxLen - 1) : value;
            },
            // 字符串格式化(%s )
            sprintf: function (str) {
                var args = arguments, flag = true, i = 1;
                str = str.replace(/%s/g, function () {
                    var arg = args[i++];
                    if (typeof arg === 'undefined') {
                        flag = false;
                        return '';
                    }
                    return arg;
                });
                return flag ? str : '';
            },
            // 指定随机数返回
            random: function (min, max) {
                return Math.floor((Math.random() * max) + min);
            },
            // 判断字符串是否是以start开头
            startWith: function (value, start) {
                var reg = new RegExp("^" + start);
                return reg.test(value)
            },
            // 判断字符串是否是以end结尾
            endWith: function (value, end) {
                var reg = new RegExp(end + "$");
                return reg.test(value)
            },
            // 数组去重
            uniqueFn: function (array) {
                var result = [];
                var hashObj = {};
                for (var i = 0; i < array.length; i++) {
                    if (!hashObj[array[i]]) {
                        hashObj[array[i]] = true;
                        result.push(array[i]);
                    }
                }
                return result;
            },
            // 数组中的所有元素放入一个字符串
            join: function (array, separator) {
                if ($.common.isEmpty(array)) {
                    return null;
                }
                return array.join(separator);
            },
            //表单转键值对
            formToPairValue: function(formId){
                let json = {};
                let form = "#" + formId;
                $.each($(form).serializeArray(), function (i, field) {
                    if ($.common.isEmpty(field.value)) {
                        return
                    }
                    if (json[field.name]) {
                        json[field.name] += ("," + field.value);
                    } else {
                        json[field.name] = field.value;
                    }
                });
                return json;
            },
            // 获取form下所有的字段并转换为json对象
            formToJSON: function (formId) {
                let json =  $.common.formToPairValue(formId);

                let jQuery = $("#" + formId).find('input,textarea,hidden');
                for (let i = 0; i < jQuery.length; i++) {
                    let name = jQuery[i].getAttribute("name");
                    let formatter = jQuery[i].getAttribute("val-formatter");
                    if ($.common.isEmpty(json[name])) {
                        continue;
                    }
                    //通过在标签上加val-formatter指定方法名，可以对数据进行格式化
                    if ($.common.isNotEmpty(formatter)) {
                        json[name] = eval(formatter + "(" + json[name] + ")");
                    }
                }
                return json;
            },
            // 获取obj对象长度
            getLength: function (obj) {
                let count = 0;
                for (var i in obj) {
                    if (obj.hasOwnProperty(i)) {
                        count++;
                    }
                }
                return count;
            },
            // 判断移动端
            isMobile: function () {
                return navigator.userAgent.match(/(Android|iPhone|SymbianOS|Windows Phone|iPad|iPod)/i);
            },
            //获取url参数
            getQueryVariable: function (variable) {
                var query = window.location.search.substring(1);
                var vars = query.split("&");
                for (var i = 0; i < vars.length; i++) {
                    var pair = vars[i].split("=");
                    if (pair[0] == variable) {
                        return pair[1];
                    }
                }
                return "";
            }
        }
    });
})(jQuery);

/** 表格类型 */
table_type = {
    bootstrapTable: 0,
    bootstrapTreeTable: 1
};

/** 消息状态码 */
web_status = {
    SUCCESS: 200,
    FAIL: 500,
    WARNING: 301
};

/** 弹窗状态码 */
modal_status = {
    SUCCESS: "success",
    FAIL: "error",
    WARNING: "warning"
};
