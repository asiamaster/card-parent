<!--银行卡的客户信息-->
<script id="customerInfoTmpl" type="text/html">
    <input type="hidden" id="accountId" value="{{accountId}}">
    <div class="breadcrumb">
        基本信息
    </div>
    <div id="base-info" class="panel-heading" >
        <div class="row row-cols-6" >
            <div class="form-group col">
                <label class="info-column">卡号</label>
                <input  type="text" class="form-control" value="{{cardNo}}" readonly/>
            </div>
            <div class="form-group col">
                <label class="info-column">卡类别</label>
                <input  type="text" class="form-control" value="{{cardTypeText}}" readonly/>
            </div>
            <div class="form-group col">
                <label class="info-column">卡片状态</label>
                <input  type="text" class="form-control" value="{{cardStateText}}" readonly/>
            </div>
            <div class="form-group col">
                <label class="info-column">账户状态</label>
                <input  type="text" class="form-control" value="{{disabledStateText}}" readonly/>
            </div>
        </div>
    </div>
    <div class="breadcrumb">
        客户信息
    </div>
    <div  id="customerInfo" class="panel-heading">
        <div class="row row-cols-6">
            <div class="form-group col">
                <label class="info-column">客户编号</label>
                <input  type="text" class="form-control" value="{{customerCode}}" readonly/>
            </div>
            <div class="form-group col">
                <label class="info-column">客户名称</label>
                <input  type="text" class="form-control" value="{{customerName}}" readonly/>
            </div>
            <div class="form-group col">
                <label class="info-column">身份类型</label>
                <input  type="text" class="form-control" value="{{subTypeNames}}" readonly/>
            </div>
            <div class="form-group col">
                <label class="info-column">联系方式</label>
                <input  type="text" class="form-control" value="{{customerContactsPhone}}" readonly/>
            </div>
        </div>
    </div>
</script>
