<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 2021/7/8
  Time: 15:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录页面</title>
</head>
<body>
    <form action="/login" method="post">
        用户名:<input name="username"/><br/>
        密码:<input name="password" type="password"/><br/>
        日志：<input name="log"/><br/>
        <input type="submit"/>
    </form>
</body>
</html>
