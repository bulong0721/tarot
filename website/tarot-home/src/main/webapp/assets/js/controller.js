/**
 * Created by Martin on 2016/4/12.
 */
function indexCtrl($scope, ConstService) {
    $scope.tree = ConstService.routes;
}

function userCtrl($scope, ConstService) {
    $scope.patientdata = [];
    $scope.gridapi = {};

    $scope.config = {
        datatype: "local",
        colNames: ['名称', '用户', '电话号码', '电子邮件', '激活'],
        colModel: [
            {name: 'name', index: 'name', width: 80, editable: true},
            {name: 'login', index: 'login', width: 100},
            {name: 'phoneNumber', index: 'phone', width: 65, editable: true},
            {name: 'adminEmail', index: 'email', width: 100, editable: true},
            {name: 'activeStatusFlag', index: 'active', width: 40, align: 'center', editable: true}
        ],
        altRows: true,
        multiselect: true,
        viewrecords: true,
        rowNum: 10,
        rowList: [10, 20, 30],
        autowidth: true,
        caption: '用户管理',
        height: '100%'
    };

    $scope.modeldata = [
        {
            name: "徐步龙",
            login: "bulong0721",
            phoneNumber: "13818771662",
            adminEmail: "bulong0721@163.com",
            activeStatusFlag: true
        }
    ];

    $scope.loadrecord = function () {
        $scope.gridapi.insert($scope.modeldata);
    }
}

angular
    .module('clover')
    .controller('indexCtrl', indexCtrl)
    .controller('userCtrl', userCtrl);