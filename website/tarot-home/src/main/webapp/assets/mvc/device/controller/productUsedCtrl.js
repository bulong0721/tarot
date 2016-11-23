angular.module('myee', [])
    .controller('productUsedCtrl', productUsedCtrl);

/**
 * productUsedCtrl - controller
 */
productUsedCtrl.$inject = ['$scope', '$resource', 'Constants', 'cTables', 'cfromly', 'NgTableParams', '$q', 'cAlerts', 'toaster', '$filter'];
function productUsedCtrl($scope, $resource, Constants, cTables, cfromly, NgTableParams, $q, cAlerts, toaster, $filter) {

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
            return $resource('./device/used/listByStoreId').get().$promise.then(function (data) {
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

    //绑定设备相关业务逻辑-----------------------------
    $scope.formBindData = {};
    $scope.showInfoEditor = false;
    $scope.showBindEditor = false;

    $scope.showCase.currentRowIndex = 0;

    $scope.filterBindOptions = function () {
        var deferred = $q.defer();
        //tables获取数据,获取该门店下可绑定的所有设备
        $scope.tableBindOpts = new NgTableParams({}, {
            counts: [],
            dataset: $filter('filter')($scope.initalBindProductList, $scope.showCase.nameFilter || "")
        });
        $scope.loadByInit = true;
        $scope.tableOpts.page(1);
        $scope.tableBindOpts.reload();
        deferred.resolve($scope.tableBindOpts);
        return deferred.promise;
    }

    $scope.goDeviceBindProductEditor = function (rowIndex) {
        initalBindProduct().then(function () {
            $scope.filterBindOptions().then(function () {
                $scope.addNew = true;

                if ($scope.tableOpts && rowIndex > -1) {
                    $scope.showCase.currentRowIndex = rowIndex;//记录当前选择的行，以备后续更新该行数据

                    var data = $scope.tableOpts.data[rowIndex];
                    $scope.formBindData.model = data;
                    //console.log(data)
                    //console.log(Constants.thisMerchantStore)
                    $scope.formBindData.model.bindShowName = '设备组名称:' + (data.name || "") + ' | 门店名称:' + (Constants.thisMerchantStore.name || "") + ' | 设备组编号:' + (data.boardNo || "") + ' | 设备组版本:' + (data.deviceNum || "");

                    //根据已关联的产品去勾选对应的checkbox
                    $scope.showCase.selectAll = false;
                    $scope.showCase.toggleAll(false, $scope.showCase.selected);//先取消所有checkbox的勾选状态
                    for (var value in data.deviceUsedList) {
                        //console.log("value"+value)
                        var productId = data.deviceUsedList[value].id;
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

        $resource('./product/used/bindDeviceUsed').save({
            'bindString': JSON.stringify(result),
            'productUsedId': $scope.formBindData.model.id
        }, {}, function (respSucc) {
            if (0 != respSucc.status) {
                $scope.toasterManage($scope.toastError, respSucc);
                return;
            }

            //用js离线刷新表格数据
            $scope.tableOpts.data[$scope.showCase.currentRowIndex].deviceUsedList = [];//先清空
            angular.forEach($scope.showCase.selected, function (data, index, array) {
                //data等价于array[index]
                if (data == true) {
                    var length = $scope.initalBindProductList.length;
                    for (i = 0; i < length; i++) {
                        var data2 = $scope.initalBindProductList[i];
                        if (data2.id == index) {
                            $scope.tableOpts.data[$scope.showCase.currentRowIndex].deviceUsedList.push({
                                id: index,
                                name: data2.name,
                                boardNo: data2.boardNo,
                                deviceNum: data2.deviceNum
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

    //其他业务逻辑----------------------------

    var productOpts = Constants.productOpts;

    var mgrData = $scope.mgrData = {
        fields: [
            {
                id: 'store.name',
                key: 'store.name',
                type: 'c_input',
                templateOptions: {disabled: true, label: '门店名称', placeholder: '门店名称',isSearch:true}
            },
            {
                key: 'ifBatch',
                type: 'c_input',
                className: 'formly-min-checkbox',
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
                key: 'code',
                type: 'c_input',
                templateOptions: {
                    label: '编号',
                    required: true,
                    placeholder: '设备组编号,100字以内',
                    maxlength: 100,
                    isSearch:true
                },
                hideExpression: 'model.ifBatch'
            },
            {
                key: 'startNo',
                type: 'c_input',
                templateOptions: {
                    label: '开始组编号',
                    required: true,
                    placeholder: '开始组编号,8位数字以内',
                    pattern: '^[0-9]*$',
                    maxlength: 8
                },
                hideExpression: '!model.ifBatch'
            },
            {
                key: 'endNo',
                type: 'c_input',
                templateOptions: {
                    label: '结束组编号',
                    required: true,
                    placeholder: '结束组编号,8位数字以内,大于等于开始组编号',
                    pattern: '^[0-9]*$',
                    maxlength: 8
                },
                hideExpression: '!model.ifBatch'
            },
            {
                key: 'type',
                type: 'c_select',
                className: 'c_select',
                templateOptions: {
                    label: '名称',
                    required: true,
                    placeholder: '设备组名称',
                    valueProp: 'type',
                    labelProp: 'friendlyType',
                    options: productOpts,
                    isSearch:true
                }
            },
            {
                key: 'productNum',
                type: 'c_input',
                templateOptions: {
                    label: '版本',
                    required: true,
                    placeholder: '设备组版本,100字以内',
                    maxlength: 100,
                    isSearch:true
                }
            },
            {
                key: 'description',
                type: 'c_textarea',
                ngModelAttrs: {
                    style: {attribute: 'style'}
                },
                templateOptions: {label: '描述', placeholder: '描述,255字以内', rows: 10, style: 'max-width:500px',maxlength:255}
            }
        ],
        api: {
            read: './product/used/paging',
            update: './product/used/save',
            delete: './product/used/delete',
            updateAttr: './product/attribute/save',
            deleteAttr: './product/attribute/delete',
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
            $scope.disableSubmit = true;
            //formly.options.updateInitialValue();//这句会报错
            var xhr = $resource(mgrData.api.update);
            formly.model.code = formly.model.code?formly.model.code:(formly.model.code | formly.model.startNo);//保证编号不为空，后台才能正常，虽然用不到
            xhr.save({
                autoStart: formly.model.startNo ? formly.model.startNo : "",
                autoEnd: formly.model.endNo ? formly.model.endNo : ""
            }, formly.model).$promise.then(function saveSuccess(response) {
                    $scope.disableSubmit = false;
                    if (0 != response.status) {
                        $scope.toasterManage($scope.toastError, response);
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

                    $scope.toasterManage($scope.toastOperationSucc,response);
                    $scope.goDataTable();
                }, function saveFailed(response) {
                    $scope.toasterManage($scope.toastError, response);
                });
        }
    };

    //formly返回
    $scope.goDataTable = function () {
        $scope.activeTab = iDatatable;
        $scope.showBindEditor = false;
        $scope.showInfoEditor = false;
    };

    cTables.initAttrNgMgr(mgrData, $scope);
}