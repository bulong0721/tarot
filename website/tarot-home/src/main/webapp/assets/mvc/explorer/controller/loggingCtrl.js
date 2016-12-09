/**
 * Created by Martin on 2016/6/27.
 */
angular.module('myee', [])
    .controller('loggingCtrl', loggingCtrl);

/**
 * loggingCtrl - controller
 */
loggingCtrl.$inject = ['$scope', 'cTables', 'cfromly'];

function loggingCtrl($scope, cTables, cfromly) {
    var mgrData = {
        fields: [
            ],
        api: {
            read: '../admin/pushLog/paging'
        }
    };

    cTables.initNgMgrCtrl(mgrData, $scope);
    $scope.where.queryObj={};

}