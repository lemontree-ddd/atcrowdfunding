<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh_CN">
<%@include file="/WEB-INF/pages/include-head.jsp" %>
<%--//zTree引入--%>
<link rel="stylesheet" href="ztree/zTreeStyle.css"/>
<script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="crowd/my-menu.js"></script>
<script type="text/javascript">

    $(function () {

        // 调用专门封装好的函数初始化树形结构 生产json数据
        generateTree();
        // 给添加子节点按钮绑定单击响应函数
        $("#treeDemo").on("click", ".addBtn", function () {
            window.pid = this.id;
            $("#menuAddModal").modal("show");
            return false;
        });

        // 给添加子节点的模态框中的保存按钮绑定单击响应函数
        $("#menuSaveBtn").click(function () {

            // 收集表单项中用户输入的数据
            var name = $.trim($("#menuAddModal [name=name]").val());
            var url = $.trim($("#menuAddModal [name=url]").val());
            // 单选按钮要定位到“被选中”的，不然选哪个都是默认第一个
            var icon = $("#menuAddModal [name=icon]:checked").val();
            // 发送Ajax请求
            $.ajax({
                "url": "menu/save.json",
                "type": "post",
                "data": {
                    "pid": window.pid,
                    "name": name,
                    "url": url,
                    "icon": icon
                },
                "dataType": "json",
                "success": function (response) {
                    var result = response.result;
                    if (result == "SUCCESS") {
                        // 需要在jqury下面引入layer.js
                        layer.msg("操作成功");

                        //重新加载分页, 注意要在确认服务器完成操作后再刷新
                        // 否则又可能刷新不到最新的数据，因为这里是异步
                        generateTree();
                    }
                    if (result == "FAILED") {
                        layer.msg("操作失败" + response.message);
                    }

                },
                "error": function (response) {
                    layer.msg(response.status + " " + response.statusText);

                }
            });
            //关闭模态框
            $("#menuAddModal").modal("hide");

            //清空表单
            //click内部无，相当于用户点击了一下
            $("#menuResetBtn").click();
        });

        // 给编辑按钮绑定单击响应函数
        $("#treeDemo").on("click", ".editBtn", function () {

            // 将当前节点的id保存到全局变量
            window.id = this.id;
            $("#menuEditModal").modal("show");
            //获取zTreeObj对象
            var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");
            //根据id查询对象
            var key = "id";
            // 用来搜索节点的属性值
            var value = window.id;

            var currentNode = zTreeObj.getNodeByParam(key, value);
            //回显表单数据
            $("#menuEditModal [name=name]").val(currentNode.name);
            $("#menuEditModal [name=url]").val(currentNode.url);

            //radio回显的本质是把value属性和currentNode.icon一致的radio选中
            $("#menuEditModal [name=icon]").val([currentNode.icon]);
            return false;
        });

        // 点击模态框中的更新响应函数
        $("#menuEditBtn").click(function () {
            //收集表单数据
            var name = $("#menuEditModal [name=name]").val();
            var url = $("#menuEditModal [name=url]").val();
            var icon = $("#menuEditModal [name=icon]:checked").val();

            //发送ajax请求
            $.ajax({
                "url": "menu/update.json",
                "type": "post",
                "data": {
                    "id": window.id,
                    "name": name,
                    "url": url,
                    "icon": icon
                },
                "dataType": "json",
                "success": function (response) {
                    var result = response.result;
                    if (result == "SUCCESS") {
                        // 需要在jqury下面引入layer.js
                        layer.msg("操作成功");
                        //重新加载分页
                        generateTree();
                    }
                    if (result == "FAILED") {
                        layer.msg("操作失败" + response.message);
                    }

                },
                "error": function (response) {
                    layer.msg(response.status + " " + response.statusText);

                }
            });
            //关闭模态框
            $("#menuEditModal").modal("hide");

        });

        //"x"删除按钮绑定单机响应函数 使用treeDemo 原因：动态
        $("#treeDemo").on("click", ".removeBtn", function () {
            //根据id查询json对象
            window.id = this.id;
            $("#menuConfirmModal").modal("show");
            //获取zTreeObj对象
            var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");

            var key = "id";
            var value = window.id
            var currentNode = zTreeObj.getNodeByParam(key, value);
            $("#removeNodeSpan").html("【<i class='" + currentNode.icon + "'></i>" + currentNode.name + "】");
            return false;
        });

        // 给确认模态框中的Ok按钮绑定响应函数
        $("#confirmBtn").click(function () {

            $.ajax({
                "url": "menu/remove.json",
                "type": "post",
                "data": {
                    "id": window.id,

                },
                "dataType": "json",
                "success": function (response) {
                    var result = response.result;
                    if (result == "SUCCESS") {
                        // 需要在jqury下面引入layer.js
                        layer.msg("操作成功");
                        //重新加载分页
                        generateTree();
                    }
                    if (result == "FAILED") {
                        layer.msg("操作失败" + response.message);
                    }

                },
                "error": function (response) {
                    layer.msg(response.status + " " + response.statusText);

                }
            });
            $("#menuConfirmModal").modal("hide");

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
                <div class="panel-heading"><i class="glyphicon glyphicon-th-list"></i> 权限菜单列表
                    <div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i
                            class="glyphicon glyphicon-question-sign"></i></div>
                </div>
                <div class="panel-body">
                    <%--ztree动态节点依附的静态节点--%>
                    <ul id="treeDemo" class="ztree"></ul>
                </div>
            </div>
        </div>

    </div>
</div>
</div>
<%@include file="/WEB-INF/pages/modal-menu-add.jsp"%>
<%@include file="/WEB-INF/pages/modal-menu-edit.jsp"%>
<%@include file="/WEB-INF/pages/modal-menu-confirm.jsp"%>


</body>
</html>
