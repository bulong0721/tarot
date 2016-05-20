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

function datatablesCtrl($scope, $compile, $uibModal, Constants) {
    var vm = this;
    $scope.where = {};
    $scope.dtInstance = null;

    $scope.dtColumns = [
        {data: 'name', title: '名称', width: 85, orderable: false},
        {data: 'login', title: '用户名', width: 60, orderable: false},
        {data: 'type', title: '账号类型', width: 55, orderable: false},
        {data: 'dateLeast', title: '最后登录时间', width: 70, orderable: false, align: 'center'},
        {data: 'ipLeast', title: '最后登录IP', width: 70, orderable: false},
        {data: 'phone', title: '电话号码', width: 65, orderable: false},
        {data: 'email', title: '电子邮件', width: 100, orderable: false},
        {data: 'active', title: '状态', width: 40, orderable: false},
        {title: '动作', width: 35, render: actionsHtml}
    ];

    $scope.edit = function() {
        var result = $uibModal.open({
            templateUrl: 'assets/views/modalContent.html',
            controller: 'modalInstanceCtrl',
            controllerAs: 'vm',
            resolve: {
                formData: function() {
                    return {
                        fields: vm.getFormFields()
                    }
                }
            }
        }).result;
    };

    vm.getFormFields = function() {
        return [
            {'key':'name','type':'input','templateOptions':{'label':'名称','placeholder':'名称'}},
            {'key':'login','type':'input','templateOptions':{'label':'用户名','placeholder':'用户名'}},
            {'key':'type','type':'input','templateOptions':{'label':'类型','placeholder':'类型'}}
        ];
    };

    //vm.edit = $scope.edit;

    function actionsHtml(data, type, full, meta) {
        return '<button class="btn btn-sm btn-warning" ng-click="edit()">' +
            '   <i class="fa fa-edit"></i>' +
            '</button>&nbsp;' +
            '<button class="btn btn-sm btn-danger" ng-click="edit()">' +
            '   <i class="fa fa-trash-o"></i>' +
            '</button>';
    }

    $scope.dtOptions = Constants.buildOption("/admin/users/paging.html", function (data) {
        angular.extend(data, $scope.where);
    }, function (row, data, dataIndex) {
        var elem = angular.element(row);
        var content = elem.contents();
        var scope = $scope;
        $compile(content)(scope);
    });

    $scope.search = function () {
        var api = $scope.dtInstance;
        if (api) {
            api.reloadData();
        }
    };
}

function modalInstanceCtrl($uibModalInstance, formData) {
    var vm = this;

    // function assignment
    vm.ok = ok;
    vm.cancel = cancel;

    // variable assignment
    vm.formData = formData;
    vm.originalFields = angular.copy(vm.formData.fields);

    // function definition
    function ok() {
        $uibModalInstance.close(vm.formData.model);
    }

    function cancel() {
        vm.formData.options.resetModel()
        $uibModalInstance.dismiss('cancel');
    };
}

function uiGridCtrl($scope) {
    $scope.where = {};

    $scope.data = [
        {name: 'xbl', gender: 1, company: 'myee', widgets: 15},
        {name: 'martin', gender: 1, company: 'myee', widgets: 15}
    ];

    var paginationOptions = {
        pageNumber: 1,
        pageSize: 25,
        sort: null
    };

    $scope.gridOptions = {
        paginationPageSizes: [25, 50, 75],
        paginationPageSize: 25,
        useExternalPagination: true,
        useExternalSorting: true,
        columnDefs: [
            {name: 'name', displayName: '名称', width: 85, orderable: false, className: 'dt-center'},
            {name: 'login', displayName: '用户名', width: 60, orderable: false},
            {name: 'type', displayName: '账号类型', width: 55, orderable: false},
            {name: 'dateLeast', displayName: '最后登录时间', width: 70, orderable: false, align: 'center'},
            {name: 'ipLeast', displayName: '最后登录IP', width: 70, orderable: false},
            {name: 'phone', displayName: '电话号码', width: 65, orderable: false},
            {name: 'email', displayName: '电子邮件', width: 100, orderable: false},
            {name: 'active', displayName: '状态', width: 40, orderable: false}
        ],
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.core.on.sortChanged($scope, function (grid, sortColumns) {
                if (sortColumns.length == 0) {
                    paginationOptions.sort = null;
                } else {
                    paginationOptions.sort = sortColumns[0].sort.direction;
                }
                getPage();
            });
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                paginationOptions.pageNumber = newPage;
                paginationOptions.pageSize = pageSize;
                getPage();
            });
        }
    };

    var getPage = function () {
        $scope.gridOptions.totalItems = 100;
        var firstRow = (paginationOptions.pageNumber - 1) * paginationOptions.pageSize;
        $scope.gridOptions.data = $scope.data;
    };

    getPage();
}

angular
    .module('inspinia')
    .controller('modalInstanceCtrl', modalInstanceCtrl)
    .controller('datatablesCtrl', datatablesCtrl)
    .controller('uiGridCtrl', uiGridCtrl)
    .controller('MainCtrl', MainCtrl);