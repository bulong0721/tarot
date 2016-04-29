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
        colNames: ['操作', '名称', '用户', '电话号码', '电子邮件', '激活'],
        colModel: [
            {name:'myac',index:'', width:80, fixed:true, sortable:false, resize:false,
                formatter:'actions',
                formatoptions:{
                    keys:true,
                    delOptions:{recreateForm: true},
                    editformbutton:true,
                    editOptions:{recreateForm: true}
                }
            },
            {name: 'name', index: 'name', width: 85, editable: true},
            {name: 'login', index: 'login', width: 60, editable: true},
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
        colNames: ['操作', '门店ID', '门店名称', '省份', '城市', '区县','商圈','商场','地址','服务类型','联系电话','创建时间','状态'],
        colModel: [
            {name:'myac',index:'', width:80, fixed:true, sortable:false, resize:false,
                formatter:'actions',
                formatoptions:{
                    keys:true,
                    delOptions:{recreateForm: true},
                    editformbutton:true,
                    editOptions:{recreateForm: true}
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

angular
    .module('clover')
    .controller('indexCtrl', indexCtrl)
    .controller('storeCtrl', storeCtrl)
    .controller('userCtrl', userCtrl);