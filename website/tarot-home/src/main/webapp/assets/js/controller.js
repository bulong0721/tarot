/**
 * Created by Martin on 2016/4/12.
 */
function indexCtrl($scope, ConstService) {
    $scope.tree = ConstService.routes;
}

function userCtrl($scope, ConstService) {
    $scope.api = {};

    $scope.config = {
        datatype: "json",
        url: "/admin/users/paging.html",
        colNames: ['操作', '名称', '用户名', '账号类型', '最后登录时间', '最后登录IP', '电话号码', '电子邮件', '状态'],
        colModel: [
            {
                name: 'myac', index: '', width: 80, fixed: true, sortable: false, resize: false,
                formatter: 'actions',
                formatoptions: {
                    keys: true,
                    delOptions: {recreateForm: true},
                    editformbutton: true,
                    editOptions: {recreateForm: true}
                }
            },
            {name: 'name', index: 'name', width: 85, editable: true},
            {name: 'login', index: 'login', width: 60, editable: true},
            {name: 'userType', index: 'userType', width: 55, editable: true, sortable: false},
            {name: 'dateLeast', index: 'dateLeast', width: 70, editable: false, sortable: false},
            {name: 'ipLeast', index: 'ipLeast', width: 70, editable: false, sortable: false},
            {name: 'phone', index: 'phone', width: 65, editable: true},
            {name: 'email', index: 'email', width: 100, editable: true},
            {name: 'active', index: 'active', width: 40, editable: true}
        ],
        multiselect: true,
        viewrecords: true,
        rowNum: 10,
        rowList: [10, 20, 30],
        autowidth: true
    };

    $scope.myData = [
        {
            name: "徐步龙",
            login: "bulong0721",
            phoneNumber: "13818771662",
            adminEmail: "bulong0721@163.com",
            activeStatusFlag: true
        }
    ];

    $scope.loadrecord = function () {
        $scope.api.loadData($scope.myData);
    }
}

function storeCtrl($scope, ConstService) {
    $scope.api = {};

    $scope.config = {
        datatype: "json",
        url: "/admin/users/paging.html",
        colNames: ['操作', '门店ID', '门店名称', '省份', '城市', '区县', '商圈', '商场', '地址', '服务类型', '联系电话', '创建时间', '状态'],
        colModel: [
            {
                name: 'myac', index: '', width: 80, fixed: true, sortable: false, resize: false,
                formatter: 'actions',
                formatoptions: {
                    keys: true,
                    delOptions: {recreateForm: true},
                    editformbutton: true,
                    editOptions: {recreateForm: true}
                }
            },
            {name: 'storeId', index: 'name', width: 45, editable: true},
            {name: 'name', index: 'login', width: 70, editable: true},
            {name: 'province', index: 'phone', width: 50, editable: true},
            {name: 'city', index: 'email', width: 50, editable: true},
            {name: 'county', index: 'active', width: 50, editable: true},
            {name: 'area', index: 'active', width: 60, editable: true},
            {name: 'market', index: 'active', width: 60, editable: true},
            {name: 'address', index: 'active', width: 85, editable: true},
            {name: 'serviceType', index: 'active', width: 60, editable: true},
            {name: 'phone', index: 'active', width: 65, editable: true},
            {name: 'created', index: 'active', width: 65, editable: true},
            {name: 'state', index: 'active', width: 45, editable: true}
        ],
        multiselect: true,
        viewrecords: true,
        rowNum: 10,
        rowList: [10, 20, 30],
        autowidth: true
    };
}

function explorerCtrl($scope, ConstService) {
    $scope.api = {};

    $scope.search = function() {
        alert('test search');
    };

    $scope.config = {
        datatype: "json",
        url: "/admin/files/list.html",
        colNames: ['路径', '资源名称', '选择', '操作', '操作1', '修改时间', '文件类型', '大小'],
        colModel: [
            {name: 'id', index: 'id', width: 10, editable: false, key:true, hidden:true },
            {name: 'name', index: 'name', width: 80, editable: false},
            {name: 'check', index: 'check', width: 75, editable: true, align: 'center', fixed: true, formatter: 'checkbox', formatoptions: {disabled: false} },
            {
                name: 'myac', index: '', width: 120, fixed: true, sortable: false, resize: false, align: 'center',
                formatter: 'actions',
                formatoptions: {
                    keys: true,
                    delOptions: {recreateForm: true},
                    editformbutton: false,
                    editOptions: {recreateForm: false}
                }
            },
            {
                name: 'myac1', index: '', width: 120, fixed: true, sortable: false, resize: false, align: 'center',
                formatter: function(value, opts, row) {
                    return '<button class="btn btn-sm btn-danger" type="button" ng-click="search()"><i class="fa fa-search"></i><span class="bold">搜索</span></button>';
                }
            },
            {name: 'mtime', index: 'mtime', width: 45, editable: false, align: 'center', formatter: ConstService.dateFormatter},
            {name: 'type', index: 'type', width: 30, editable: false, align: 'center',},
            {name: 'size', index: 'size', width: 30, editable: false, align: 'center', formatter: ConstService.sizeFormatter}
        ],
        treeGrid: true,
        treeGridModel: 'adjacency',
        treeIcons: {
            plus: 'ace-icon fa fa-caret-right bigger-160',
            minus: 'ace-icon fa fa-caret-down bigger-160',
            leaf: 'ace-icon fa fa-file'
        },
        loadonce: false,
        ExpandColClick: true,
        ExpandColumn: 'name',
        mtype: "POST",
        treeReader: {
            level_field: 'level',
            leaf_field: 'leaf',
            expanded_field: 'expanded',
            parent_id_field: 'parent',
        },
        height: 'auto',
        autowidth: true
    };
}

angular
    .module('clover')
    .controller('indexCtrl', indexCtrl)
    .controller('storeCtrl', storeCtrl)
    .controller('explorerCtrl', explorerCtrl)
    .controller('userCtrl', userCtrl);