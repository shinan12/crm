layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 计划项数据展示
     */
    var  tableIns = table.render({
        elem: '#cusDevPlanList',
        url : ctx+'/cus_dev_plan/list?sId='+$('[name="id"]').val(),
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "cusDevPlanListTable",
        cols : [[
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'编号',fixed:"true"},
            {field: 'planItem', title: '计划项',align:"center"},
            {field: 'exeAffect', title: '执行效果',align:"center"},
            {field: 'planDate', title: '执行时间',align:"center"},
            {field: 'createDate', title: '创建时间',align:"center"},
            {field: 'updateDate', title: '更新时间',align:"center"},
            {title: '操作',fixed:"right",align:"center", minWidth:150,templet:"#cusDevPlanListBar"}
        ]]
    });

    /**
     * 头部工具栏 监听事件
     */
    table.on('toolbar(cusDevPlans)',function (obj) {
        var checkStatus = table.checkStatus(obj.config.id);
        switch (obj.event) {
            case 'add':
                addUpdateCusDevPlanDialog();
                break;
            case 'success':
                updateDevResult(2);
                break;
            case 'failed':
                updateDevResult(3);
                break;
        };
    });
    //更新营销机会的开发状态
    function updateDevResult(devResult) {
        var id = $('[name="id"]').val()
        $.post(ctx+"/sale_chance/updateDevResult",{id:id,devResult:devResult},function (data) {
            if(data.code == 200) {
                //刷新页面
                parent.location.reload();
                //关闭
                tableIns.reload();
            } else {
                layer.msg(data.msg,{icon:5})
            }
        })
    }

    /**
     * 表格行 监听事件
     * saleChances为table标签的lay-filter 属性值
     */
    table.on('tool(cusDevPlans)',function (obj) {
        if(obj.event == "edit") {
            addUpdateCusDevPlanDialog(obj.data.id);
        } else if(obj.event == "del") {
            layer.confirm("确定删除当前数据？", {icon: 3, title: "开发计划管理"}, function (index) {
                $.post(ctx+"/cus_dev_plan/delete",{id:obj.data.id},function (data) {
                    if(data.code == 200) {
                        layer.close(index);
                        tableIns.reload();
                    } else {
                        layer.msg(data.msg,{icon: 5});
                    }
                })
            });
        }
    });
    function addUpdateCusDevPlanDialog(id) {
        var title = "<h2>计划项管理-添加计划项</h2>"
        var url = ctx + "/cus_dev_plan/toAddOrUpdatePage?sId="+$('[name="id"]').val();
        //修改操作有id值
        if(id) {

            title = "<h2>计划项管理-修改计划项</h2>"
            url += "&id=" + id;
        }

        layer.open({
            type:2,
            title:title,
            content:url,
            maxmin:true,
            area:["500px","300px"]
        });
    }

});
