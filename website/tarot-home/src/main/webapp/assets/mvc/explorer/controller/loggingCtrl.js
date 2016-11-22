/**
 * Created by Martin on 2016/6/27.
 */
angular.module('myee', [])
    .controller('loggingCtrl', loggingCtrl);

/**
 * loggingCtrl - controller
 */
loggingCtrl.$inject = ['$scope', '$resource', 'cTables', 'cfromly'];

function loggingCtrl($scope, $resource, cTables, cfromly) {
    var mgrData = {
        fields: [
        ],
        api: {
            read: '../admin/pushLog/paging'
        }
    };

    cTables.initNgMgrCtrl(mgrData, $scope);
}