<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>木爷终端管理系统 | 用户登录</title>
    <link href="assets/plugins/bootstrap/bootstrap.min.css" rel="stylesheet">
    <link href="assets/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link href="assets/plugins/animate.css" rel="stylesheet">
    <link href="assets/plugins/style.css" rel="stylesheet">
</head>
<body class="gray-bg loginBg">
    <div class="middle-box text-center loginscreen animated fadeInDown">
        <div class="loginMain">
            <h3>木爷终端管理系统</h3>
            <p class="client-status info"><i class="fa fa-coffee"></i>请输入您的登录信息.</p>
            ${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}
            <form class="m-t" role="form" method="post" action="../shop/j_spring_security_check">
                <div class="form-group">
                    <input type="text" name="username" class="form-control" placeholder="用户名" required="">
                </div>
                <div class="form-group">
                    <input type="password" name="password" class="form-control" placeholder="密码" required="">
                </div>
                <div class="form-group">
                    <input type="text" name="securityCode" class="form-control" placeholder="验证码" required="">
                </div>
                <button type="submit" class="btn btn-primary btn-block">登录</button>
            </form>
            <p class="m-t">
                <small>上海木爷机器人技术有限公司 &copy; 2016</small>
            </p>
        </div>
    </div>
    <!-- Mainly scripts -->
    <script src="assets/plugins/jquery/jquery-2.1.1.min.js"></script>
    <script src="assets/plugins/bootstrap/bootstrap.min.js"></script>
</body>
</html>