<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>木爷终端管理系统 | 管理员登录</title>
    <link href="assets/plugins/bootstrap/bootstrap.min.css" rel="stylesheet">
    <link href="assets/plugins/style.css" rel="stylesheet">
</head>
<body class="gray-bg loginBg">
    <div class="middle-box text-center loginscreen animated fadeInDown">
        <div class="loginMain">
            <h3>木爷终端管理系统</h3>
            <p class="client-status info"><i class="fa fa-coffee"></i>请输入您的登录信息.</p>
            <div class="text-danger" id="error"> ${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}</div>
            <form class="m-t" role="form" method="post" action="../admin/j_spring_security_check">
                <div class="form-group">
                    <input type="text" name="username" class="form-control" placeholder="用户名" required="">
                </div>
                <div class="form-group">
                    <input type="password" name="password" class="form-control" placeholder="密码" required="">
                </div>
                <div class="form-group input-group">
                    <input type="text" name="securityCode" id="securityCode" class="form-control" placeholder="验证码" required="">
                    <div class="input-group-btn">
                        <img style="height:22px;" id="codeImg" alt="点击更换" title="点击更换" src="" />
                    </div>
                </div>
                <div class="form-group"><button type="submit" class="btn btn-primary btn-block">登录</button></div>
            </form>
            <p class="m-t">
                <small>上海木爷机器人技术有限公司 &copy; 2016</small>
            </p>
        </div>
    </div>
<!-- Mainly scripts -->
<script src="assets/plugins/jquery/jquery-2.1.1.min.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        changeCode();
        $("#codeImg").bind("click", changeCode);

        $("form").submit(function(){
            var form = $(this);
            $.ajax({
                type: 'GET',
                url: 'security/code/validate?securityCode='+$('#securityCode').val(),
                success: function(res){
                    if(res){
                        form.unbind('submit').submit();
                        return true;
                    }else{
                        $('#error').html('验证码错误')
                    }
                }
            });
            return false;
        });
        $(document).keyup(function(event) {
            if (event.keyCode == 13) {
                $("#to-recover").trigger("click");
            }
        });

        function changeCode() {
            $("#codeImg").attr("src", "../admin/security/code");
        }
    });
</script>
</body>
</html>