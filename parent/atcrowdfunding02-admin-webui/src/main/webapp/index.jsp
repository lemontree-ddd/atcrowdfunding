<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/7/13 0013
  Time: 18:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <%-- http://localhost:8080/atcrowdfunding02_admin_webui_war_exploded/test/ssm.html --%>
    <base href="http://${pageContext.request.serverName }:${pageContext.request.serverPort }${pageContext.request.contextPath }/"/>
    <script type="text/javascript" src="jquery/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="static/layui/layui.all.js"></script>
    <script>

        $(function () {
            $("#btn2").click(function () {
                //alert("aaa");
                layer.msg("Layer的弹框");
            });

        });

    </script>

</head>
<body>

</body>
</html>
