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
                <div style="float: left;">
                    <i><img style="height:22px;" id="codeImg" alt="点击更换"
                            title="点击更换" src="" /></i>
                </div>
                <%--<span><a onclick="securityCodeCheck();"--%>
                <%--class="btn btn-primary btn-block" id="to-recover">登录</a></span>--%>
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
    <!-- 访问两个接口说明(注意：接口admin和shop分别对应不同的admin\login.jsp,shop\login.jsp，两个jsp都需要改)
         1.进入界面与刷新界面或点击验证码图片时，访问../admin/security/code或../shop/security/code获取验证码图片
         2.当用户点击登录按钮时，访问../admin/security/code/validate?securityCode=输入的验证码
           或../shop/security/code/validate?securityCode=输入的验证码，获取用户输入的验证码是否正确，正确返回true否则返回false
         3.当验证码输入错误时，清空用户名密码错误信息
         -->
    <script type="text/javascript">
        $(document).ready(function() {
            changeCode();
            $("#codeImg").bind("click", changeCode);
        });

        $(document).keyup(function(event) {
            if (event.keyCode == 13) {
                $("#to-recover").trigger("click");
            }
        });

        function changeCode() {
            $("#codeImg").attr("src", "../shop/security/code");
        }

        function securityCodeCheck(){
            if(check()){

            }
        }

        function check() {
//    ../shop/security/code/validate?securityCode=输入的验证码
        }
    </script>
</body>
</html>