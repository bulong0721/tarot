/**
 * Created by Martin on 2016/6/27.
 */
angular.module('myee', [])
    .controller('dUMonitorMgrCtrl', dUMonitorMgrCtrl);

/**
 * dUMonitorMgrCtrl - controller
 */
dUMonitorMgrCtrl.$inject = ['$scope','$resource'];

function dUMonitorMgrCtrl($scope,$resource) {

    //Summary
    $resource('../admin/remoteMonitor/deviceUsed/summary?deviceUsedId=1').get({}, function (resp) {
        var r = resp.rows[0];
        $scope.summary = {
            deviceUsed:r.deviceUsed,
            //appInfoList:r.appInfoList,
            metricInfoList:r.metricInfoList
        }
        console.log($scope.summary)
    });

    //Metrics
    $resource('../admin/remoteMonitor/deviceUsed/metrics?deviceUsedId=1&period=&metricsKeyString=').get({}, function (resp) {
        var r = resp.rows[0];
        $scope.metric = {
            deviceUsed:r.deviceUsed,
            metricInfoList:r.metricInfoList,
            appInfoList:r.appInfoList,
            summaryUsed:r.summaryUsed
        }
        console.log($scope.metric)
    });

}