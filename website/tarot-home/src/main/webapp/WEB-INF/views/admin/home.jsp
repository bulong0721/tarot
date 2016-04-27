<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html ng-app="clover">

<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>木爷终端管理系统</title>
    <meta name="description" content=""/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="assets/font-awesome/4.2.0/css/font-awesome.min.css"/>
    <!-- page specific plugin styles -->
    <link rel="stylesheet" href="assets/css/jquery-ui.min.css" />
    <link rel="stylesheet" href="assets/css/datepicker.min.css" />
    <link rel="stylesheet" href="assets/css/ui.jqgrid.min.css" />
    <!-- text fonts -->
    <link rel="stylesheet" href="assets/fonts/fonts.googleapis.com.css"/>
    <!-- ace styles -->
    <link rel="stylesheet" href="assets/css/ace.min.css" class="ace-main-stylesheet" id="main-ace-style"/>
    <!--[if lte IE 9]>
    <link rel="stylesheet" href="assets/css/ace-part2.min.css" class="ace-main-stylesheet"/>
    <![endif]-->
    <!--[if lte IE 9]>
    <link rel="stylesheet" href="assets/css/ace-ie.min.css"/>
    <![endif]-->
    <!-- ace settings handler -->
    <script src="assets/js/ace/ace-extra.min.js"></script>
    <!--[if lte IE 8]>
    <script src="assets/js/ace/html5shiv.min.js"></script>
    <script src="assets/js/ace/respond.min.js"></script>
    <![endif]-->
</head>

<!-- ControllerAs syntax -->
<!-- Main controller with serveral data used in Inspinia theme on diferent view -->
<body>
<!-- Wrapper-->
<div class="no-skin">
    <!-- Navigation -->
    <div ng-include="'assets/views/topnavbar.html'"></div>

    <!-- Page wraper -->
    <!-- ng-class with current state name give you the ability to extended customization your view -->
    <div class="main-container" id="main-container">
        <!-- Page wrapper -->
        <div id="sidebar" class="sidebar responsive" ng-include="'assets/views/navigation.html'"></div>

        <!-- Main view  -->
        <div class="main-content" ui-view></div>

        <!-- Footer -->
        <div class="footer" ng-include="'assets/views/footer.html'"></div>
    </div>
    <!-- End page wrapper-->
</div>
<!-- End wrapper-->

<!-- jQuery and Bootstrap -->
<script src="assets/js/ace/jquery.2.1.1.min.js"></script>

<!-- bootstrap scripts -->
<script src="assets/js/ace/bootstrap.min.js"></script>

<!-- ace scripts -->
<script src="assets/js/ace/ace-elements.min.js"></script>
<script src="assets/js/ace/ace.min.js"></script>

<!-- Angular scripts-->
<script src="assets/js/angular/angular.min.js"></script>
<script src="assets/js/ui-router/angular-ui-router.js"></script>
<script src="assets/js/ng-resource/angular-resource.min.js"></script>

<!-- Angular plugins-->
<script src="assets/js/ace/bootstrap-datepicker.min.js"></script>
<script src="assets/js/ace/jquery.jqGrid.min.js"></script>
<script src="assets/js/ace/grid.locale-cn.js"></script>
<script src="assets/js/plugins/angular-jqgrid.js"></script>

<!-- Anglar App Script -->
<script src="assets/js/app.js"></script>
<script src="assets/js/service.js"></script>
<script src="assets/js/config.js"></script>
<script src="assets/js/directive.js"></script>
<script src="assets/js/controller.js"></script>

</body>
</html>
