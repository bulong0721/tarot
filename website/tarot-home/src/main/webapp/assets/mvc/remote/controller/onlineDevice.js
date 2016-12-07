/**
 * Created by Martin on 2016/6/27.
 */
angular.module('myee', [])
    .controller('onlineDevice', onlineDevice);

/**
 * loggingCtrl - controller
 */
onlineDevice.$inject = ['$scope', 'cTables', 'cfromly'];

function onlineDevice($scope, cTables, cfromly) {
    var mgrData = {
        fields: [
            ],
        api: {
            read: '../admin/device/used/listOnlineDevice'
        }
    };

    cTables.initNgMgrCtrl(mgrData, $scope);
    $scope.where.queryObj={};

}