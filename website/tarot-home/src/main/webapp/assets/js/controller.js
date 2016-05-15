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

    $scope.config = {
        datatype: "json",
        url: "/admin/files/list.html",
        colNames: ['资源路径', '修改时间', '文件类型', '大小'],
        colModel: [
            {name: 'name', index: 'name', width: 80, editable: true},
            {name: 'modified', index: 'modified', width: 45, editable: true},
            {name: 'type', index: 'type', width: 30, editable: true},
            {name: 'size', index: 'size', width: 40, editable: true}
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
        prmNames: {
            page: "page",    // 表示请求页码的参数名称
            rows: "rows",    // 表示请求行数的参数名称
            sort: "sidx", // 表示用于排序的列名的参数名称
            order: "sord", // 表示采用的排序方式的参数名称
            search: "_search", // 表示是否是搜索请求的参数名称
            nd: "nd", // 表示已经发送请求的次数的参数名称
            id: "id", // 表示当在编辑数据模块中发送数据时，使用的id的名称
            oper: "oper"
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