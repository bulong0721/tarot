angular.module('myee', [])
    .controller('deviceUsedCtrl', deviceUsedCtrl);

/**
 * productUsedCtrl - controller
 */
deviceUsedCtrl.$inject = ['$scope', '$resource', 'Constants', 'cTables', 'cfromly', 'NgTableParams', '$q','cAlerts','toaster'];
function deviceUsedCtrl($scope, $resource, Constants, cTables, cfromly, NgTableParams, $q,cAlerts,toaster) {

    var iDatatable = 0, iEditor = 1;
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
            return $resource('./product/used/listByStoreId').get().$promise.then(function (data) {
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
                if (!selectedItems[id]) {
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

    $scope.goDeviceBindProductEditor = function (rowIndex) {
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
                    $scope.formBindData.model.bindShowName = '设备名称:' + (data.name || "") + ' | 门店名称:' + (Constants.thisMerchantStore.name || "") + ' | 主板编号:' + (data.boardNo || "") + ' | 设备号:' + (data.deviceNum || "");

                    //根据已关联的产品去勾选对应的checkbox
                    $scope.showCase.selectAll = false;
                    $scope.showCase.toggleAll(false, $scope.showCase.selected);//先取消所有checkbox的勾选状态
                    for (var value in data.productUsedList) {
                        //console.log("value"+value)
                        var productId = data.productUsedList[value].id;
                        $scope.showCase.selected[productId] = true;
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
                result.push(index);
            }
        });

        $resource('./device/used/bindProductUsed').save({
            'bindString': JSON.stringify(result),
            'deviceUsedId': $scope.formBindData.model.id
        }, {}, function (respSucc) {
            if (0 != respSucc.status) {
                $scope.toasterManage($scope.toastError,respSucc);
                return;
            }

            //用js离线刷新表格数据
            $scope.tableOpts.data[$scope.showCase.currentRowIndex].productUsedList = [];//先清空
            angular.forEach($scope.showCase.selected, function (data, index, array) {
                //data等价于array[index]
                if (data == true) {
                    var length = $scope.initalBindProductList.length;
                    for (i = 0; i < length; i++) {
                        var data2 = $scope.initalBindProductList[i];
                        if (data2.id == index) {
                            $scope.tableOpts.data[$scope.showCase.currentRowIndex].productUsedList.push({
                                id: index,
                                code: data2.code,
                                name: data2.name,
                                productNum: data2.productNum
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
            $scope.toasterManage($scope.toastError,respFail);
        });
    };

    //其他业务逻辑----------------------------


    function getDeviceList() {
        var data = $resource("./device/list").query();
        return data;
    }

    var mgrData = {
        fields: [
            {
                id: 'store.name',
                key: 'store.name',
                type: 'c_input',
                templateOptions: {disabled: true, label: '门店名称', placeholder: '门店名称'}
            },
            {key: 'name', type: 'c_input', templateOptions: {label: '名称', required: true, placeholder: '名称'}},
            {
                key: 'ifBatch',
                type: 'c_input',
                className:'formly-min-checkbox',
                templateOptions: {label: '批量新增', required: false, type: 'checkbox'},
                defaultValue: false,//不初始化就报错，设为false也报错？？_加了hideExpression就不会报错了，奇怪？？
                hideExpression: function ($viewValue, $modelValue, scope) {
                    if (scope.model.id) {
                        return true;   //修改时隐藏批量修改
                    } else {
                        return false;  //新增时显示批量修改
                    }
                }
            },
            {
                key: 'startNo',
                type: 'c_input',
                templateOptions: {label: '开始编号', required: true, placeholder: '开始编号'},
                hideExpression: '!model.ifBatch'
            },
            {
                key: 'endNo',
                type: 'c_input',
                templateOptions: {label: '结束编号', required: true, placeholder: '结束编号'},
                hideExpression: '!model.ifBatch'
            },
            {key: 'heartbeat', type: 'c_input', templateOptions: {label: '心跳', placeholder: '心跳'}},
            {key: 'boardNo', type: 'c_input', templateOptions: {label: '主板编号', placeholder: '主板编号',required: true}},
            {key: 'deviceNum', type: 'c_input', templateOptions: {label: '设备号', placeholder: '设备号'}},
            {key: 'description', type: 'c_input', templateOptions: {label: '描述', placeholder: '描述'}},
            {
                key: 'phone',
                type: 'c_input',
                templateOptions: {
                    type: 'text',
                    label: '手机号',
                    placeholder: '请输入11位手机号',
                    pattern:'^(1[3578][0-9]|14[0-7])[0-9]{8}$|(^((1[3578][0-9]|14[0-7])[0-9]{8},)*(1[3578][0-9]|14[0-7])[0-9]{8}$)'
                }
            },
            {
                key: 'device.id',
                type: 'c_select',
                className: 'c_select',
                templateOptions: {label: '选择设备类型', required: true, options: getDeviceList()}
            }

        ],
        api: {
            read: './device/used/paging',
            update: './device/used/update',
            delete: './device/used/delete',
            updateAttr: './device/used/attribute/save',
            deleteAttr: './device/used/attribute/delete',
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
                        $scope.toasterManage($scope.toastError,response);
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

                    $scope.toasterManage($scope.toastOperationSucc);
                    $scope.goDataTable();
                }, function saveFailed(response) {
                    $scope.toasterManage($scope.toastError,response);
                });
        }
    };

    //formly返回
    $scope.goDataTable = function () {
        $scope.activeTab = iDatatable;
        $scope.showBindEditor = false;
        $scope.showInfoEditor = false;
    };

    $scope.insertAttr = function (product) {
        if (!product.attributes) {
            product.attributes = [];
        }
        product.attributes.push({name: '', value: '', editing: true});
    };

    $scope.updateAttr = function (product, attr) {
        var xhr = $resource(mgrData.api.updateAttr);
        xhr.save({id: product.id}, attr).$promise.then(function (result) {
            if (0 != result.status) {
                $scope.toasterManage($scope.toastError,result);
                return;
            }
            $scope.toasterManage($scope.toastOperationSucc);
            attr.editing = false;
        });
    };

    $scope.deleteAttr = function (product, attr) {
        cAlerts.confirm('确定删除?',function(){
            //点击确定回调
            var xhr = $resource(mgrData.api.deleteAttr);
            xhr.save({id: product.id}, attr).$promise.then(function (result) {
                if (0 != result.status) {
                    $scope.toasterManage($scope.toastError,result);
                    return;
                }
                $scope.toasterManage($scope.toastDeleteSucc);
                var index = product.attributes.indexOf(attr);
                product.attributes.splice(index, 1);
            });
        },function(){
            //点击取消回调
        });
    };
}
