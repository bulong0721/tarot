/**
 * Created by Martin on 2016/6/27.
 */
angular.module('myee', [])
    .controller('dUMonitorMgrCtrl', dUMonitorMgrCtrl);

/**
 * deviceUsedMonitorCtrl - controller
 */
dUMonitorMgrCtrl.$inject = ['$scope', 'cTables', 'cfromly'];

function dUMonitorMgrCtrl($scope, cTables, cfromly) {

    $scope.tips = "*新建的管理员账号将绑定当前所切换的门店";
}