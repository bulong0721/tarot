/**
 * Created by mckay on 2016/11/23.
 */
angular.module('myee', [])
    .controller('camera', camera);

/**
 * camera - controller
 */
camera.$inject = ['$scope','$resource'];

function camera($scope,$resource) {
    var vm = $scope.vm = {
        machines:[],
        getMachine:function(val){
            console.log(val)
        }
    }
    //
    $resource('../admin/product/used/listByStoreId').get({}, function (resp) {
        if(resp.status == '0'){
            if(resp.rows && resp.rows.length>0){
                vm.machines = resp.rows;
            }
        }
    });
}
