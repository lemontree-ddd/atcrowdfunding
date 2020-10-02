// 模态框中Auth树形结构显示
function fillAuthTree(){

    // 1.发送Ajax请求查询Auth数据
    var ajaxReturn = $.ajax({
        "url":"assign/get/all/auth.json",
        "type":"post",
        "dataType":"json",
        "async":false, //同步

    });
    //响应函数结果中中获取Auth的json数据
    if (ajaxReturn.status!=200){
        layer.msg("出错，"+ajaxReturn.status+" "+ajaxReturn.statusText)
        return;
    }
        // 2.从响应结果中获取Auth的json数据
        var authList = ajaxReturn.responseJSON.data;

        // 3.创建json对象存储zTree所做的设置
        // 交给ztree组装
        var setting = {
            "data": {
                "simpleData": {
                    //开启简单json功能
                    "enable": true,
                    //设置父节点 使用categoryId属性关联父节点，不用默认的pId
                    "pIdKey": "categoryId"
                },
                "key": {
                    // 使用title属性显示节点名称，不用默认的name作为属性名
                    "name": "title"
                }
            },
            "check":{
                "enable":true
            }
        };

    // 4.生成初始化树结构 <ul id="authTreeDemo" class="ztree"></ul>
    // ,不要写成.
    $.fn.zTree.init($("#authTreeDemo"),setting,authList);

    //默认节点展开
    var zTreeObj=$.fn.zTree.getZTreeObj("authTreeDemo");
    zTreeObj.expandAll(true);
    // 查询已分配的Auth的id组成的数组
    ajaxReturn=$.ajax({
        "url":"assign/get/assign/auth/id/by/role/id.json",
        "type":"post",
        "data":{
            "roleId":window.roleId
        },
        "dataType":"json",
        "async":false
});
    if (ajaxReturn.status!=200){
        layer.msg("出错，"+ajaxReturn.status+" "+ajaxReturn.statusText);
        return;
    }
    // 从响应结果中获取authArray
    var authArray=ajaxReturn.responseJSON.data;

    //alert(authArray);把对应的树形结构勾选
    // 遍历一下数组
    for (var i = 0;i < authArray.length; i++){
        var authId = authArray[i];
        // 根据id查询树形结构中的对应节点
        var treeNode = zTreeObj.getNodeByParam("id",authId);
        // 将treeNode设置为被勾选
        var checked = true;//节点勾选
        var checkTypeFlag = false;//不联动，避免父节点勾选，回显多选内容
        zTreeObj.checkNode(treeNode,checked,checkTypeFlag);
    }

}

// 声明专门的函数显示模态框  用于删除的模态框
function showConfirmModal(roleArray) {
    $("#confirmModal").modal("show");
    // 清除旧的数据
    $("#roleNameDiv").empty();
    //在全局变量范围创建数组用来存放角色id
    window.roleIdArray=[];
    // 遍历roleArray数组
    for (var i = 0; i < roleArray.length; i++) {
        var role = roleArray[i];
        var roleName = role.roleName;
        $("#roleNameDiv").append(roleName + "<br/>");
        var roleId = role.roleId;

        //调用数组对象的push()方法存入新的元素
        window.roleIdArray.push(roleId);
    }
}
//实验表明roleArray数据创建失败
// function showConfirmModal(id,roleName) {
//     $("#confirmModal").modal("show");
//     $("#roleNameDiv").empty();
//     window.roleIdArray=[];
//         $("#roleNameDiv").append(roleName+"<br/>");
//         window.roleIdArray.push(id);
//
// }


//执行分页，生成页面效果
function generatePage() {
    // 1.获取分页数据
    var pageInfo=getPageInfoRemote();
    // 2.填充表格
    fillTableBody(pageInfo);
}

//远程访问服务器端程序获取pageInfo数据
function getPageInfoRemote() {
    // 调用$.ajax()函数发送请求并接受$.ajax()函数的返回值
    var ajaxResult = $.ajax({
        "url": "role/get/page/info.json",
        "type": "post",
        "data": {
            "pageNum": window.pageNum,
            "pageSize": window.pageSize,
            "keyword": window.keyword
        },
        "async": false,   //改成同步调用
        "dataType": "json"
    });
    console.log(ajaxResult);
    // 判断当前响应状态码是否为 200
    var statusCode = ajaxResult.status;
    // 如果当前响应状态码不是 200，说明发生了错误或其他意外情况，显示提示消息，让当前函数停止执行
    if (statusCode != 200) {
        layer.msg("失败！响应状态码=" + statusCode + " 说明信息=" + ajaxResult.statusText);
        return null;
    }
    console.log(ajaxResult);
    // 如果响应状态码是 200，说明请求处理成功，获取pageInfo
    var resultEntity = ajaxResult.responseJSON;
    // 从 resultEntity 中获取 result 属性
    var result = resultEntity.result;
    // 判断 result 是否成功
    if (result == "FAILED") {
        layer.msg(resultEntity.message);
        return null;
    }
    // 确认 result 为成功后获取
    var pageInfo = resultEntity.data;
    // 返回 pageInfo

    return pageInfo;

}

//填充表格
function fillTableBody(pageInfo) {
    // 清除 tbody 中的旧的内容
    $("#rolePageBody").empty();
    // 这里清空是为了让没有搜索结果时不显示页码导航条
    $("#Pagination").empty();
    // 判断 pageInfo 对象是否有效
    if (pageInfo == null || pageInfo == undefined || pageInfo.list == null || pageInfo.list.length == 0) {
        $("#rolePageBody").append("<tr><td colspan='4' align='center'>抱歉！没有查询到您搜索的数据！</td></tr>");
        return;
    }
    // 使用 pageInfo 的 list 属性填充 tbody
    for(var i = 0; i < pageInfo.list.length; i++) {
        var role = pageInfo.list[i];
        var roleId = role.id;
        var roleName = role.name;
        var numberTd = "<td>" + (i + 1) + "</td>";
        var checkboxTd = "<td><input id='"+roleId+"' class='itemBox' type='checkbox'></td>";
        var roleNameTd = "<td>" + roleName + "</td>";
        var checkBtn = "<button id='"+roleId+"' type='button' class='checkBtn btn btn-success btn-xs'><i class=' glyphicon glyphicon-check'></i></button>";
        //通过button标签的id属性（别的属性也行）把roleId值传递到button按钮的单击响应函数中，在单击响应函数中使用this.id
        var pencilBtn = "<button id='"+roleId+"' type='button' class='btn btn-primary btn-xs pencilBtn'><i class=' glyphicon glyphicon-pencil '></i></button>";
        var removeBtn = "<button id='"+roleId+"' type='button' class='btn btn-danger btn-xs removeBtn'><i class=' glyphicon glyphicon-remove '></i></button>";
        var buttonTd = "<td>" + checkBtn + " " + pencilBtn + " " + removeBtn + "</td>";
        var tr = "<tr>" + numberTd + checkboxTd + roleNameTd + buttonTd + "</tr>";
        $("#rolePageBody").append(tr);
    }

    // 生成分页导航条
    generateNavigator(pageInfo);

}
//生成分页页码导航条
function generateNavigator(pageInfo) {
    //总记录数
    var totalRecord=pageInfo.total;
    var properties = {
        num_edge_entries: 3, //边缘页数
        num_display_entries: 5, //主体页数
        callback: paginationCallBack,
        items_per_page:pageInfo.pageSize, //每页显示1项
        current_page: pageInfo.pageNum-1,//Pagination内部使用pageIndex来管理页面
        prev_text:"上一页",
        next_text:"下一页"
    }
    $("#Pagination").pagination(totalRecord,properties)
}
function paginationCallBack(pageIndex,jQuery) {
    //根据pageIndex计算pageNum
    window.pageNum = pageIndex+1;
    //调用分页函数
    generatePage();
   //由于每个按钮都是超链接，所以这里取消超链接的默认行为
    return false;
}