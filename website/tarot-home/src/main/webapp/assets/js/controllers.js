/**
 * INSPINIA - Responsive Admin Theme
 *
 */

/**
 * MainCtrl - controller
 */
function MainCtrl() {

    this.userName = 'Martin.Xu';
    this.helloText = 'Welcome in SeedProject';
    this.descriptionText = 'It is an application skeleton for a typical AngularJS web app. You can use it to quickly bootstrap your angular webapp projects and dev environment for these projects.';

}

function datatablesCtrl($scope, DTOptionsBuilder, Constants) {
    $scope.where = {};
    $scope.dtInstance = null;

    $scope.dtColumns = [
        {data: 'name', title: '名称', width: 85, orderable: false, className: 'dt-center'},
        {data: 'login', title: '用户名', width: 60, orderable: false},
        {data: 'type', title: '账号类型', width: 55, orderable: false},
        {data: 'dateLeast', title: '最后登录时间', width: 70, orderable: false},
        {data: 'ipLeast', title: '最后登录IP', width: 70, orderable: false},
        {data: 'phone', title: '电话号码', width: 65, orderable: false},
        {data: 'email', title: '电子邮件', width: 100, orderable: false},
        {data: 'active', title: '状态', width: 40, orderable: false},
    ];

    $scope.dtOptions = Constants.buildOption("/admin/users/paging.html", function (data) {
        angular.extend(data, $scope.where);
    });

    $scope.search = function () {
        var api = $scope.dtInstance;
        if (api) {
            api.reloadData();
        }
    };
}

angular
    .module('inspinia')
    .controller('datatablesCtrl', datatablesCtrl)
    .controller('MainCtrl', MainCtrl);