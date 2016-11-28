/**
 * Created by mckay on 2016/11/23.
 */
angular.module('myee', [])
    .controller('camera', camera);

/**
 * camera - controller
 */
camera.$inject = ['$scope','cResource'];

function camera($scope,cResource) {
    var vm = $scope.vm = {
        machines:[],
        getMachine:function(val){
            console.log(val)
        }
    }
    //
    cResource.get('../admin/product/used/listByStoreId').then(function(resp){
        if(resp.rows && resp.rows.length>0){
            vm.machines = resp.rows;
        }
    });
}
