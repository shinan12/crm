layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
    /**
     * 营销机会列表展示
     */
    var tableIns = table.render({
        elem: '#saleChanceList', // 表格绑定的ID
        url : ctx + '/sale_chance/list', // 访问数据的地址
        cellMinWidth : 95,
        page : true, // 开启分页
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "saleChanceListTable",
        cols : [[
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'编号',fixed:"true"},
            {field: 'chanceSource', title: '机会来源',align:"center"},
            {field: 'customerName', title: '客户名称', align:'center'},
            {field: 'cgjl', title: '成功几率', align:'center'},
            {field: 'overview', title: '概要', align:'center'},
            {field: 'linkMan', title: '联系人', align:'center'},
            {field: 'linkPhone', title: '联系电话', align:'center'},
            {field: 'description', title: '描述', align:'center'},
            {field: 'createMan', title: '创建人', align:'center'},
            {field: 'createDate', title: '创建时间', align:'center'},
            {field: 'uname', title: '指派人', align:'center'},
            {field: 'assignTime', title: '分配时间', align:'center'},
            {field: 'state', title: '分配状态', align:'center',templet:function(d)
                {
                    return formatterState(d.state);
                }},
            {field: 'devResult', title: '开发状态',
                align:'center',templet:function (d) {
                    return formatterDevResult(d.devResult);
                }},
            {title: '操作', templet:'#saleChanceListBar',fixed:"right",align:"center", minWidth:150}
    ]]
});
    /**
     * 绑定搜索按钮的点击事件
     */
    $(".search_btn").click(function () {
        table.reload('saleChanceListTable', {
            where: { //设定异步数据接口的额外参数，任意设
                customerName: $("input[name='customerName']").val(), // 客户名
                createMan: $("input[name='createMan']").val(), // 创建人
                state: $("#state").val() // 状态
            }
            ,page: {
                curr: 1 // 重新从第 1 页开始
            }
        }); // 只重载数据
    });

     /**
     * 格式化分配状态
     * 0 - 未分配
     * 1 - 已分配
     * 其他 - 未知
     * @param state
     * @returns {string}
     */
    function formatterState(state){
        if(state==0) {
            return "<div style='color: yellow'>未分配</div>";
        } else if(state==1) {
            return "<div style='color: green'>已分配</div>";
        } else {
            return "<div style='color: red'>未知</div>";
        }
    }

    function formatterDevResult(value){
        if(value == 0) {
            return "<div style='color: yellow'>未开发</div>";
        } else if(value==1) {
            return "<div style='color: #00FF00;'>开发中</div>";
        } else if(value==2) {
            return "<div style='color: #00B83F'>开发成功</div>";
        } else if(value==3) {
            return "<div style='color: red'>开发失败</div>";
        } else {
            return "<div style='color: #af0000'>未知</div>"
        }
    }

    function openAddUpdateSaleChanceDialog(id) {
        var title =  "<h2>营销机会管理 - 机会添加</h2>";
        var url = ctx + "/sale_chance/toAddUpdatePage";
        if(id) {
            title = "<h2>营销机会管理-机会更新</h2>"
            url = url + "?id=" + id;
        }
        layer.open({
            type: 2,
            title:title,
            content:url,
            area:['500px','620px'],
            maxmin:true
        });

    }
    /**
    * 表格行 监听事件
    * saleChances为table标签的lay-filter 属性值
    */
    table.on('tool(saleChances)',function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if(layEvent == 'edit') {
            var id = data.id;
            console.log("sdfa:"+id)
            openAddUpdateSaleChanceDialog(id);
        }
    });

    /**
     * 头部工具栏 监听事件
     */
    table.on('toolbar(saleChances)',function (obj) {
        var checkStatus = table.checkStatus(obj.config.id);
        switch (obj.event) {
            case 'add':
                layer.msg('添加');
                openAddUpdateSaleChanceDialog();
                break;
            case 'del':
                layer.msg('删除');
                break;
        }
    })
});