/**
 * Created by Ray.Fu on 2016/7/18.
 */

angular.module('myee', [])
    .controller('logListCtrl', logListCtrl);

/**
 * productUsedCtrl - controller
 */
logListCtrl.$inject = ['$scope', '$resource', 'Constants', 'cTables', 'cfromly', 'NgTableParams', '$q'];
function logListCtrl($scope, $resource, Constants, cTables, cfromly, NgTableParams, $q) {

    $scope.moduleChange = function($viewValue) {
        $resource('../admin/selfCheckLog/listFunction').query({moduleId:$viewValue== null ? '':$viewValue.value},{},function success(resp){
            var length = resp.length;
            console.log(1111)
            $scope.functionNames.splice(0, $scope.functionNames.length);
            for (var j = 0; j < length; j++) {
                $scope.functionNames.push({name: resp[j].name, value: resp[j].value});
            }
        });
    };

    var vm = this;
    var mgrData = {
        fields: [
        ],
        api: {
            read: '../admin/selfCheckLog/paging',
        }
    };

    //$scope.errorLevels = [];
    ///**
    // * 下拉列表查询错误等级
    // * **/
    //$resource('../admin/selfCheckLog/listErrorLevel').query({},{},function success(resp){
    //    var length = resp.length;
    //    $scope.errorLevels.splice(0, $scope.errorLevels.length);
    //    for (var j = 0; j < length; j++) {
    //        $scope.errorLevels.push({name: resp[j].name, value: resp[j].value});
    //    }
    //});

    $scope.moduleNames = [];
    /**
     * 下拉列表查询事件模块
     * **/
    $resource('../admin/selfCheckLog/listModule').query({},{},function success(resp){
        var length = resp.length;
        $scope.moduleNames.splice(0, $scope.moduleNames.length);
        for (var j = 0; j < length; j++) {
            $scope.moduleNames.push({name: resp[j].name, value: resp[j].value});
        }
    });

    $scope.functionNames = [];
    /**
     * 下拉列表查询功能模块
     * **/
    $resource('../admin/selfCheckLog/listFunction').query({},{},function success(resp){
        var length = resp.length;
        $scope.functionNames.splice(0, $scope.functionNames.length);
        for (var j = 0; j < length; j++) {
            $scope.functionNames.push({name: resp[j].name, value: resp[j].value});
        }
    });


    cTables.initNgMgrCtrl(mgrData, $scope);
}

