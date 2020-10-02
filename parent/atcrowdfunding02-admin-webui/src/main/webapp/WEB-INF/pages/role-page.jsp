<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh_CN">
<%@include file="/WEB-INF/pages/include-head.jsp" %>
<link rel="stylesheet" href="css/pagination.css"/>
<script type="text/javascript" src="jquery/jquery.pagination.js"></script>
<link rel="stylesheet" href="ztree/zTreeStyle.css"/>
<script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>
<%--导入js文件时不能加type="text/javascript"--%>
<script src="crowd/my-role.js"></script>
<script type="text/javascript">

    $(function () {
        // 为分页操作初始化数据
        window.pageNum = 1;
        window.pageSize = 5;
        window.keyword = "";
        // 使用my-role.js内的generatePage函数实现分页
        generatePage();

        $("#searchBtn").click(function () {
            window.keyword = $("#keywordInput").val();
            // 使用my-role.js内的generatePage函数实现分页
            generatePage();
        });

        // 4.点击新增按钮增加模态框
        $("#showAddModalBtn").click(function () {
            $("#addModal").modal("show");
        });

        // 5.给新增模态框中的保存按钮绑定单击响应函数
        $("#saveRoleBtn").click(function () {
            // 获取输入角色名称  #addModal:找到整个模态框 空格：表示在后代元素中查找 [name=roleName]：匹配name属性为roleName的元素
            var roleName = $.trim($("#addModal [name=roleName]").val());
            $.ajax({
                "url": "role/save.json",
                "type": "post",
                "data": {
                    "name": roleName
                },
                "dataType": "json",
                "success": function (response) {
                    var result = response.result;
                    if (result == "SUCCESS") {
                        // 需要在jqury下面引入layer.js
                        layer.msg("操作成功");
                        // 重新加载分页
                        window.pageNum = 99999999;
                        generatePage();
                    }
                    if (result == "FAILED") {
                        layer.msg("操作失败" + response.message);
                    }

                },
                "error": function (response) {
                    layer.msg(response.status + " " + response.statusText);

                }
            });

            // 关闭模态框
            $("#addModal").modal("hide");
            // 清理模态框
            $("#addModal [name=roleName]").val("")

        });

        /* 6.给页面上的“铅笔”按钮绑定单击函数，目的就是打开模态框
        // 这种方法只在起始页有效，翻页失效
         $(".pencilBtn").click(function () {
             $("#editModal").modal("show");
         });*/

        // 使用jqery对象的on（）函数解决问题
        // 首先找到“动态生成”的元素的“静态”元素
        // 参数1事件类型
        // 参数2选择器
        // 参数2事件的响应函数
        $("#rolePageBody").on("click", ".pencilBtn", function () {
            $("#editModal").modal("show");
            //获取表格中的角色名称
            var roleName = $(this).parent().prev().text();
            //获取表格中的角色id,来源my-role.js: var pencilBtn = "<button id='"+roleId+"' type='button' class='btn btn-primary btn-xs pencilBtn'><i class=' glyphicon glyphicon-pencil '></i></button>";
            //window使roleId变成全局变量
            window.roleId = this.id;
            $("#editModal [name=roleName]").val(roleName);
        });

        // 7.给更新模态框中的更新按钮绑定单击响应函数
        $("#updateRoleBtn").click(function () {
            // 获取参数
            var roleName = $("#editModal [name=roleName]").val();
            $.ajax({
                "url": "role/update.json",
                "type": "post",
                "data": {
                    "id": window.roleId,
                    "name": roleName
                },
                "dataType": "json",

                "success": function (response) {
                    var result = response.result;
                    if (result == "SUCCESS") {
                        // 需要在jqury下面引入layer.js
                        layer.msg("操作成功");
                        //重新加载分页
                        generatePage();
                    }
                    if (result == "FAILED") {
                        layer.msg("操作失败" + response.message);
                    }

                },
                "error": function (response) {
                    layer.msg(response.status + " " + response.statusText);

                }
            });
            // 关闭模态框
            $("#editModal").modal("hide");
        });


        // 8.点击确认模态框中的确认删除确认删除
        $("#removeRoleBtn").click(function () {
            // 从全局变量获取roleIdArray,转换为Json字符串
            var requestBody = JSON.stringify(window.roleIdArray);
            $.ajax({
                "url": "role/remove/by/role/id/array.json",
                "type": "post",
                "data": requestBody,
                "contentType": "application/json;charset=UTF-8",
                "dataType": "json",
                "success": function (response) {
                    var result = response.result;
                    if (result == "SUCCESS") {
                        // 需要在jqury下面引入layer.js
                        layer.msg("操作成功");
                        //重新加载分页

                        generatePage();
                    }
                    if (result == "FAILED") {
                        layer.msg("操作失败" + response.message);
                    }

                },
                "error": function (response) {

                    layer.msg(response.status + " " + response.statusText);

                }
            });
            $("#confirmModal").modal("hide");
        });

        /*// 使用jqery对象的on（）函数解决问题
        // 首先找到“动态生成”的元素的“静态”元素
        // 参数1事件类型
        // 参数2选择器
        // 参数2事件的响应函数
           9.给单条删除按钮绑定单击响应事件
        */
        $("#rolePageBody").on("click", ".removeBtn", function () {
            var roleName = $(this).parent().prev().text();
            //var id=this.id;
            //let roleName2 = roleName1;
            var roleArray = [{
                roleId: this.id,
                roleName: roleName
            }];

            showConfirmModal(roleArray);


        });

        // 10.全选框
        $("#summaryBox").click(function () {

            //获取当前多选框的状态
            var currentStatus = this.checked;

            //单选框状态改变 用当前多选框设置其他单选框的状态
            $(".itemBox").prop("checked", currentStatus);
        });

        //11.全选，全不选反向操作 单选框全选 多选框自动勾上
        $("#rolePageBody").on("click", ".itemBox", function () {
            //获取以选中的单选框数量
            var checkedBoxCount = $(".itemBox:checked").length;
            //获得全部单选框的数量
            var totalBoxCount = $(".itemBox").length;
            //使用二者比较的结果 如果相等 则多选框勾上
            $("#summaryBox").prop("checked", checkedBoxCount == totalBoxCount);
        });

        //12.批量删除
        $("#batchRemoveBtn").click(function () {
            var roleArray = [];
            //遍历
            $(".itemBox:checked").each(function () {
                var roleId = this.id;
                var roleName = $(this).parent().next().text();

                roleArray.push({
                    "roleId": roleId,   //前面是属性名 后面为属性值
                    "roleName": roleName
                });
            });
            if (roleArray.length == 0) {
                layer.msg("请至少选择一个角色进行删除");
                return;
            }
            if (roleArray.length == 5) {
                $("#summaryBox").prop("checked", false);
            }
            showConfirmModal(roleArray);
        });

        // 13.给分配权限按钮绑定单机响应函数
        $("#rolePageBody").on("click", ".checkBtn", function () {
            //把当前角色id存入全局变量
            window.roleId = this.id;
            $("#assignModal").modal("show");
            // 在模态框中显示Auth树形结构
            fillAuthTree();
        });

        // 14.role_auth提交分配数据
        $("#assignBtn").click(function () {

            // ①收集树形结构中被勾选的节点

            var authIdArray = [];
            // 【1】获取zTreeObj对象
            var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
            // 【2】获取被勾选的节点
            var checkedNodes = zTreeObj.getCheckedNodes();
            // 【3】遍历checkedNodes
            for(var i = 0; i < checkedNodes.length; i++) {
                var checkedNode = checkedNodes[i];
                var authId = checkedNode.id;
                authIdArray.push(authId);
            }

            // ②发送执行请求
            var requestBody = {
                "authIdArray": authIdArray,
                // 为了服务器接受统一，设置成数组
                "roleId": [window.roleId]
            };
            console.log(requestBody);
            // 转成Json字符串
            requestBody = JSON.stringify(requestBody);

            $.ajax({
                "url": "assign/do/role/assign/auth.json",
                "type": "post",
                "data": requestBody,
                "contentType": "application/json;charset=UTF-8",
                "dataType": "json",
                "success": function (response) {
                    var result = response.result;
                    if(result == "SUCCESS") {
                        // 需要在jqury下面引入layer.js
                        layer.msg("操作成功");
                    }
                    if(result == "FAILED") {
                        layer.msg("操作失败" + response.message);
                    }
                },
                "error":function (response) {
                    layer.msg(response.status + " " + response.statusText);
                }
            });
            $("#assignModal").modal("hide");
        });

    });
</script>
<body>

<%@include file="/WEB-INF/pages/include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@include file="/WEB-INF/pages/include-sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input id="keywordInput" class="form-control has-success" type="text"
                                       placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button id="searchBtn" type="button" class="btn btn-warning"><i
                                class="glyphicon glyphicon-search"></i> 查询
                        </button>
                    </form>
                    <button id="batchRemoveBtn" type="button" class="btn btn-danger"
                            style="float:right;margin-left:10px;"><i
                            class=" glyphicon glyphicon-remove"></i> 删除
                    </button>
                    <button type="button" id="showAddModalBtn" class="btn btn-primary" style="float:right;">
                        <i class="glyphicon glyphicon-plus"></i> 新增
                    </button>

                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr>
                                <th width="30">#</th>
                                <th width="30"><input id="summaryBox" type="checkbox"></th>
                                <th>名称</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody id="rolePageBody"></tbody>
                            <tfoot>
                            <tr>
                                <td colspan="6" align="center">
                                    <div id="Pagination" class="pagination"><!-- 这里显示分页 --></div>

                                </td>
                            </tr>

                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>
</div>
<%@include file="/WEB-INF/pages/modal-role-add.jsp" %>
<%@include file="/WEB-INF/pages/modal-role-edit.jsp" %>
<%@include file="/WEB-INF/pages/modal-role-confirm.jsp" %>
<%@include file="/WEB-INF/pages/modal-role-assign-auth.jsp" %>

</body>
</html>
