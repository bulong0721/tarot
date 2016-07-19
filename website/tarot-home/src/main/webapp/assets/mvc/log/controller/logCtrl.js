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
    //绑定产品相关参数
    var vm = $scope.showCase = {};
    vm.selected = [];
    vm.selectAll = false;
    vm.toggleAll = toggleAll;
    vm.toggleOne = toggleOne;

    function initalBindProduct() {
        if ($scope.initalBindProductList) {//如果已经从后台读取过数据了，则不再访问后台获取列表
            var deferred = $q.defer();
            deferred.resolve($scope.initalBindProductList);
            return deferred.promise;
        } else {//第一次需要从后台读取列表，且只返回前10个数据
            return $resource('/admin/selfCheckLog/paging').get().$promise.then(function (data) {
                //初始化showCase.selected数组，给全选框用，让它知道应该全选哪些
                angular.forEach(data.rows, function (indexData, index, array) {
                    //indexData等价于array[index]
                    $scope.showCase.selected[indexData.id] = false;
                });
                $scope.initalBindProductList = data.rows;
                return data.rows;
            });
        }
    }

    function toggleAll(selectAll, selectedItems) {
        for (var id in selectedItems) {
            if (selectedItems.hasOwnProperty(id)) {
                selectedItems[id] = selectAll;
            }
        }
    }

    function toggleOne(selectedItems) {
        for (var id in selectedItems) {
            if (selectedItems.hasOwnProperty(id)) {
                if (!selectedItems[id]&& id!= $scope.currentId) {
                    vm.selectAll = false;
                    return;
                }
            }
        }
        vm.selectAll = true;
    }

    //绑定产品相关业务逻辑-----------------------------
    $scope.formBindData = {};
    $scope.showInfoEditor = false;
    $scope.showBindEditor = false;

    $scope.showCase.currentRowIndex = 0;

    $scope.processBindSubmit = function () {
        var result = [];

        angular.forEach($scope.showCase.selected, function (data, index, array) {
            //data等价于array[index]
            if (data == true) {
                if(index != $scope.currentId){
                    result.push(index);
                }
            }
        });
    };

    var mgrData = {
        fields: [],
        api: {
            read: 'selfCheckLog/paging',
        }
    };
    cTables.initNgMgrCtrl(mgrData, $scope);

    $scope.goEditorCustom = function (rowIndex) {
        $scope.goEditor(rowIndex);
        if (Constants.thisMerchantStore) {
            $scope.formData.model.store = {name: Constants.thisMerchantStore.name};
        }
        $scope.showBindEditor = false;
        $scope.showInfoEditor = true;
    };

    //formly提交
    $scope.processSubmit = function () {
        var formly = $scope.formData;
        if (formly.form.$valid) {
            //formly.options.updateInitialValue();//这句会报错
            var xhr = $resource(mgrData.api.update);
            xhr.save({
                autoStart: formly.model.startNo ? formly.model.startNo : "",
                autoEnd: formly.model.endNo ? formly.model.endNo : ""
            }, formly.model).$promise.then(function saveSuccess(response) {
                    if (0 != response.status) {
                        return;
                    }
                    //批量添加的数据添加到ngtables
                    angular.forEach(response.dataMap.updateResult, function (indexData, index, array) {
                        var data = indexData;
                        if ($scope.rowIndex < 0) {
                            $scope.tableOpts.data.splice(0, 0, data);
                        } else {
                            $scope.tableOpts.data.splice($scope.rowIndex, 1, data);
                        }
                    })

                    $scope.goDataTable();
                }, function saveFailed(response) {
                });
        }
    };

    //formly返回
    $scope.goDataTable = function () {
        $scope.showDataTable = true;
        $scope.showEditor = false;
        $scope.showBindEditor = false;
        $scope.showInfoEditor = false;
    };
}

