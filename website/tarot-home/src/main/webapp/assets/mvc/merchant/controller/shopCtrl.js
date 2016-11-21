angular.module('myee', [])
    .controller('merchantShopCtrl', merchantShopCtrl);

/**
 * merchantShopCtrl - controller
 */
merchantShopCtrl.$inject = ['$scope', 'Constants', 'cTables', 'cfromly', '$resource', 'NgTableParams', '$q'];
function merchantShopCtrl($scope, Constants, cTables, cfromly, $resource, NgTableParams, $q) {

    var iDatatable = 0, iEditor = 1;
    var mgrData = {
        fields: [
            {
                id: 'merchant.name',
                key: 'merchant.name',
                type: 'c_input',
                templateOptions: {disabled: true, label: '商户名称', placeholder: '商户名称'}
            },
            {
                key: 'name',
                type: 'c_input',
                templateOptions: {label: '名称', required: true, placeholder: '门店名称,60字以内',maxlength:60}
            },
            {
                key: 'address.province.id',
                type: 'c_select',
                className: 'c_select',
                templateOptions: {
                    label: '省份',
                    options: Constants.provinces,

                },
            },
            {
                key: 'address.city.id',
                type: 'c_select',
                className: 'c_select',
                templateOptions: {
                    label: '城市',
                    options: Constants.citys,
                },
                controller: function ($scope, Constants) {
                    $scope.$watch('model.address.province.id', function (newValue, oldValue, theScope) {
                        if (newValue !== oldValue) {
                            // logic to reload this select's options asynchronusly based on state's value (newValue)
                            if ($scope.model[$scope.options.key] && oldValue) {
                                // reset this select
                                $scope.model[$scope.options.key] = '';
                            }
                            // Reload options
                            Constants.getCitysByProvince(newValue, $scope);
                        }
                    });

                }
            },
            {
                key: 'address.county.id',
                type: 'c_select',
                className: 'c_select',
                templateOptions: {label: '区县', options: Constants.districts},
                controller: function ($scope, Constants) {
                    $scope.$watch('model.address.city.id', function (newValue, oldValue, theScope) {
                        if (newValue !== oldValue) {
                            // logic to reload this select's options asynchronusly based on state's value (newValue)
                            if ($scope.model[$scope.options.key] && oldValue) {
                                // reset this select
                                $scope.model[$scope.options.key] = '';
                            }
                            // Reload options
                            Constants.getDistrictsByCity(newValue);
                        }
                    });

                }
            },
            {
                key: 'address.circle.id',
                type: 'c_select',
                className: 'c_select',
                templateOptions: {label: '商圈', options: Constants.circles}
            },
            {
                key: 'address.mall.id',
                type: 'c_select',
                className: 'c_select',
                templateOptions: {label: '商场', options: Constants.malls}
            },
            {key: 'address.address', type: 'c_input', templateOptions: {label: '地址', placeholder: '地址,255字以内',maxlength:255}},
            {
                key: 'phone',
                type: 'c_input',
                templateOptions: {
                    label: '联系电话',
                    placeholder: '联系电话,11位手机或固话,20字以内',
                    maxlength:20,
                    pattern: '^(1[3578][0-9]|14[0-7])[0-9]{8}$|(^((1[3578][0-9]|14[0-7])[0-9]{8},)*(1[3578][0-9]|14[0-7])[0-9]{8}$)|(^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$)'
                }
            },
            {key: 'code', type: 'c_input', templateOptions: {label: '门店码', required: true, placeholder: '门店码,字母和数字的组合,100字以内',maxlength:100,pattern:'^[a-zA-Z0-9_]*$'}},
        ],
        api: {
            read: './merchantStore/pagingByMerchant',
            update: './merchantStore/save',
            delete: './merchantStore/delete'
        }
    };
    cTables.initNgMgrCtrl(mgrData, $scope);

    $scope.goEditorCustom = function (rowIndex) {
        $scope.goEditor(rowIndex);
        if (Constants.thisMerchant) {
            $scope.formData.model.merchant = {name: Constants.thisMerchant.name};
        }
        $scope.showBindEditor = false;
        $scope.showInfoEditor = true;
    };

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
            return $resource('../admin/merchantStore/getAllStoreExceptSelf').get().$promise.then(function (data) {
                //console.log(data.rows)
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
                if (!selectedItems[id] && id != $scope.currentId) {
                    vm.selectAll = false;
                    return;
                }
            }
        }
        vm.selectAll = true;
    }

    $scope.formBindData = {};
    $scope.showInfoEditor = false;
    $scope.showBindEditor = false;
    $scope.activeTab = iDatatable;
    $scope.showCase.currentRowIndex = 0;
    $scope.thisStoreId = Constants.thisMerchantStore.id;

    $scope.goShopBindEditor = function (rowIndex) {

        initalBindProduct().then(function () {
            //tables获取数据,获取该门店下设备可绑定的所有产品
            $scope.tableBindOpts = new NgTableParams({}, {
                counts: [],
                dataset: $scope.initalBindProductList
            });

            $scope.tableBindOpts.reload().then(function () {
                $scope.addNew = true;
                if ($scope.tableOpts && rowIndex > -1) {
                    $scope.showCase.currentRowIndex = rowIndex;//记录当前选择的行，以备后续更新该行数据
                    var data = $scope.tableOpts.data[rowIndex];
                    $scope.formBindData.model = data;
                    $scope.currentId = data.id;
                    //console.log(data)
                    //console.log(Constants.thisMerchantStore)
                    $scope.formBindData.model.bindShowName = '门店名称:' + (data.name || "") + ' | 商户名称:' + (data.merchant.name || "");

                    //根据已关联的产品去勾选对应的checkbox
                    $scope.showCase.selectAll = false;
                    $scope.showCase.toggleAll(false, $scope.showCase.selected);//先取消所有checkbox的勾选状态
                    for (var value in data.bindStores) {
                        //console.log("value"+value)
                        var storeId = data.bindStores[value].id;
                        $scope.showCase.selected[storeId] = true;
                        $scope.showCase.toggleOne($scope.showCase.selected);//判断全选框是否要被checked
                    }

                    $scope.addNew = false;
                    $scope.rowIndex = rowIndex;
                } else {
                    $scope.formBindData.model = {};
                }
                $scope.activeTab = iEditor;
                $scope.showInfoEditor = false;
                $scope.showBindEditor = true;
            });
        });
    };


    $scope.processBindSubmit = function () {
        var result = [];

        angular.forEach($scope.showCase.selected, function (data, index, array) {
            //data等价于array[index]
            if (data == true) {
                if (index != $scope.currentId) {
                    result.push(index);
                }
            }
        });

        $resource('../api/sale/corp/bindShop').save({
            'bindString': JSON.stringify(result),
            'merchantId': $scope.formBindData.model.id
        }, {}, function (respSucc) {
            if (0 != respSucc.status) {
                $scope.toasterManage($scope.toastError, respSucc);
                return;
            }

            //用js离线刷新表格数据
            $scope.tableOpts.data[$scope.showCase.currentRowIndex].bindStores = [];//先清空
            angular.forEach($scope.showCase.selected, function (data, index, array) {
                //data等价于array[index]
                if (data == true) {
                    var length = $scope.initalBindProductList.length;
                    for (i = 0; i < length; i++) {
                        var data2 = $scope.initalBindProductList[i];
                        //console.log(data2)
                        if (data2.id == index) {
                            $scope.tableOpts.data[$scope.showCase.currentRowIndex].bindStores.push({
                                id: index,
                                name: data2.name,
                                merchant: data2.merchant,
                            });
                            break;
                        }
                    }
                }
            });

            $scope.toasterManage($scope.toastOperationSucc);
            $scope.goDataTable();
        }, function (respFail) {
            //console.log(respFail);
            $scope.toasterManage($scope.toastError, respFail);
        });
    };

    $scope.tips = "*管理当前切换的门店所属商户下的所有门店";
}