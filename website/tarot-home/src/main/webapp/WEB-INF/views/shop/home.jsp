<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html ng-app="myee">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">

    <!-- Page title set in pageTitle directive -->
    <title page-title></title>

    <!-- Font awesome -->
    <link href="assets/font-awesome/css/font-awesome.css" rel="stylesheet">

    <!-- Bootstrap and Fonts -->
    <link href="assets/plugins/bootstrap/bootstrap.min.css" rel="stylesheet">

    <!-- ngTable css -->
    <link href="assets/plugins/ng-table/ng-table.css" rel="stylesheet">
    <!-- toastr css -->
    <link href="assets/plugins/toastr/toastr.min.css" rel="stylesheet">
    <!-- Main Inspinia CSS files -->
    <link href="assets/plugins/animate.css" rel="stylesheet">
    <link href="assets/plugins/style.css" rel="stylesheet">
</head>

<!-- ControllerAs syntax -->
<!-- Main controller with serveral data used in Inspinia theme on diferent view -->
<body ng-controller="mainCtrl as main">

<!-- Main view  -->
<div id="wrapper">
    <div ng-include="'assets/mvc/desktop/view/navigation_shop.html'"></div>
    <div id="page-wrapper" class="white-bg {{$state.current.name}}">
        <toaster-container></toaster-container>
        <div ng-include="'assets/mvc/desktop/view/topnavbar.html'"></div>
        <breadcrumb></breadcrumb>
        <div class="wrapper wrapper-content animated fadeIn"><div ui-view></div></div>
        <div ng-include="'assets/mvc/desktop/view/footer.html'"></div>
    </div>
    <div ng-include="'assets/mvc/desktop/view/right_sidebar.html'"></div>
</div>

<!-- jQuery and Bootstrap -->
<script src="assets/plugins/jquery/jquery-2.1.1.min.js"></script>
<script src="assets/plugins/jquery-ui/jquery-ui.min.js"></script>
<script src="assets/plugins/bootstrap/bootstrap.min.js"></script>

<!-- MetsiMenu -->
<script src="assets/plugins/metisMenu/jquery.metisMenu.js"></script>

<!-- SlimScroll -->
<script src="assets/plugins/slimscroll/jquery.slimscroll.min.js"></script>

<!-- Peace JS -->
<script src="assets/plugins/pace/pace.min.js"></script>

<!-- Custom and plugin javascript -->
<script src="assets/mvc/inspinia.js"></script>

<!-- Main Angular scripts-->
<script src="assets/plugins/angular/angular.min.js"></script>
<script src="assets/plugins/angular/angular-sanitize.min.js"></script>
<script src="assets/plugins/angular/angular-resource.min.js"></script>
<script src="assets/plugins/oclazyload/ocLazyLoad.min.js"></script>
<script src="assets/plugins/ui-router/angular-ui-router.min.js"></script>
<script src="assets/plugins/bootstrap/ui-bootstrap-tpls-2.0.0.min.js"></script>
<script src="assets/plugins/ng-table/ng-table.js"></script>
<script src="assets/plugins/toastr/toastr.min.js"></script>

<!-- Anglar App Script -->
<script src="assets/mvc/app.js"></script>
<script src="assets/mvc/config.js"></script>
<script src="assets/mvc/directives.js"></script>
<script src="assets/mvc/services.js"></script>
<script src="assets/mvc/controllers.js"></script>
<script src="assets/mvc/filters.js"></script>

<script>
    var baseUrl= ${downloadBase};
</script>
</body>
</html>
