/**
 * Created by Martin on 2016/6/27.
 */
angular.module('myee', [])
    .controller('waitTokenCtrl', waitTokenCtrl);

/**
 * roleCtrl - controller
 */
waitTokenCtrl.$inject = ['$scope', 'cTables','cfromly','$resource'];

function waitTokenCtrl($scope, cTables,cfromly,$resource) {

    //$scope.waitStates = [];
    ///**
    // * 下拉列表状态
    // * **/
    //$resource('../data/queryWaitTokenState').query({},{},function success(resp){
    //    var length = resp.length;
    //    $scope.waitStates.splice(0, $scope.waitStates.length);
    //    for (var j = 0; j < length; j++) {
    //        $scope.waitStates.push({name: resp[j].name, value: resp[j].value});
    //    }
    //});


    var mgrOpts = {
        fields: [],
        api: {
            read: '../data/waittoken/paging',
        }
    };

    cTables.initNgMgrCtrl(mgrOpts, $scope);
}
