<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html ng-app="inspinia">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">

    <!-- Page title set in pageTitle directive -->
    <title page-title></title>

    <!-- Font awesome -->
    <link href="assets/font-awesome/css/font-awesome.css" rel="stylesheet">

    <!-- Bootstrap and Fonts -->
    <link href="assets/css/bootstrap.min.css" rel="stylesheet">

    <!-- Main Inspinia CSS files -->
    <link href="assets/css/animate.css" rel="stylesheet">
    <link id="loadBefore" href="assets/css/style.css" rel="stylesheet">
</head>

<!-- ControllerAs syntax -->
<!-- Main controller with serveral data used in Inspinia theme on diferent view -->
<body ng-controller="mainCtrl as main">

<!-- Main view  -->
<div id="wrapper">
    <div ng-include="'assets/mvc/desktop/view/navigation.html'"></div>
    <div id="page-wrapper" class="gray-bg {{$state.current.name}}">
        <div ng-include="'assets/mvc/desktop/view/topnavbar.html'"></div>
        <breadcrumb></breadcrumb>
        <div class="wrapper wrapper-content animated fadeIn"><div ui-view></div></div>
        <div ng-include="'assets/mvc/desktop/view/footer.html'"></div>
    </div>
    <div ng-include="'assets/mvc/desktop/view/right_sidebar.html'"></div>
</div>

<!-- jQuery and Bootstrap -->
<script src="assets/js/jquery/jquery-2.1.1.min.js"></script>
<script src="assets/js/plugins/jquery-ui/jquery-ui.js"></script>
<script src="assets/js/bootstrap/bootstrap.min.js"></script>

<!-- MetsiMenu -->
<script src="assets/js/plugins/metisMenu/jquery.metisMenu.js"></script>

<!-- SlimScroll -->
<script src="assets/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

<!-- Peace JS -->
<script src="assets/js/plugins/pace/pace.min.js"></script>

<!-- Custom and plugin javascript -->
<script src="assets/js/inspinia.js"></script>

<!-- Main Angular scripts-->
<script src="assets/js/angular/angular.min.js"></script>
<script src="assets/js/angular/angular-sanitize.js"></script>
<script src="assets/js/plugins/oclazyload/dist/ocLazyLoad.min.js"></script>
<script src="assets/js/ui-router/angular-ui-router.min.js"></script>
<script src="assets/js/angular/angular-resource.min.js"></script>
<script src="assets/js/bootstrap/ui-bootstrap-tpls-1.1.2.min.js"></script>

<!-- Anglar App Script -->
<script src="assets/js/app.js"></script>
<script src="assets/js/config.js"></script>
<script src="assets/js/directives.js"></script>
<script src="assets/js/services.js"></script>
<script src="assets/js/controllers.js"></script>
<script src="assets/js/filters.js"></script>

</body>
</html>
