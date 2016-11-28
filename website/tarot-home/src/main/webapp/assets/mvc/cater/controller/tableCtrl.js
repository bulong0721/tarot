/**
 * Created by Martin on 2016/6/27.
 */
angular.module('myee', [])
    .controller('tableMgrCtrl', tableMgrCtrl);

/**
 * roleCtrl - controller
 */
tableMgrCtrl.$inject = ['$scope', '$resource', 'cTables', 'cfromly', 'Constants', 'NgTableParams', '$q', 'cAlerts', '$filter'];

function tableMgrCtrl($scope, $resource, cTables, cfromly, Constants, NgTableParams, $q, cAlerts, $filter) {

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
                    $scope.formBindData.model.bindShowName = '餐桌名称:' + (data.name || "") + ' | 门店名称:' + (Constants.thisMerchantStore.name || "");

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
                //$scope.showBatchBindEditor = false;
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

        $resource('./catering/table/bindDeviceUsed').save({
            'bindString': JSON.stringify(result),
            'tableId': $scope.formBindData.model.id
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

    //批量一对一绑定设备//20160817还没想好如何关联--------------------------------------
    //var bindData = {
    //    fields: [
    //        {
    //            key: 'startNo',
    //            type: 'c_input',
    //            templateOptions: {label: '开始编号', required: true, placeholder: '开始编号'},
    //        },
    //        {
    //            key: 'endNo',
    //            type: 'c_input',
    //            templateOptions: {label: '结束编号', required: true, placeholder: '结束编号'},
    //        },
    //        {
    //            key: 'startDevice',
    //            type: 'c_input',
    //            templateOptions: {label: '起始设备', required: true, placeholder: '起始编号'},
    //        },
    //        {
    //            key: 'endDevice',
    //            type: 'c_input',
    //            templateOptions: {label: '结束编号', required: true, placeholder: '结束编号'},
    //        }
    //    ],
    //    api: {
    //        update: '../admin/catering/table/batchBindDeviceUsed',
    //    }
    //};
    ////formly配置项
    //$scope.formBatchBindData = {
    //    fields: bindData.fields
    //};
    //$scope.formBatchBindData.bindShowName = "将按照序号顺序一对一关联";
    //$scope.showBatchBindEditor = false;
    //$scope.goBatchBindEditor = function (rowIndex) {
    //    console.log(123)
    //    console.log($scope.formBatchBindData)
    //    $scope.activeTab = iEditor;
    //    $scope.showInfoEditor = false;
    //    $scope.showBindEditor = false;
    //    $scope.showBatchBindEditor = true;
    //};
    //
    //$scope.processBatchBindSubmit = function () {
    //
    //};
    //其他业务逻辑----------------------------

    var typeOpts = $resource('./catering/type/options').query();
    var zoneOpts = $resource('./catering/zone/options').query();

    var mgrData = $scope.mgrData = {
        fields: [
            {
                key: 'name',
                type: 'c_input',
                className: 'c_formly_line',
                templateOptions: {label: '餐桌名称', required: true, placeholder: '餐桌名称,50字以内', maxlength: 50,isSearch:true}
            },
            {
                key: 'scanCode',
                type: 'c_input',
                className: 'c_formly_line',
                templateOptions: {
                    label: '餐桌码',
                    placeholder: '输入000-200之间的3位数字,批量添加时将自动+1递增',
                    pattern: '(([01]{1}[0-9]{2})|([2]{1}[0]{2}))$',
                    maxlength: 3,
                    isSearch:true
                }
            },
            {
                key: 'textId',
                type: 'c_input',
                className: 'c_formly_line',
                templateOptions: {label: 'ERP ID', placeholder: '小超人点菜用,10字以内', maxlength: 10,isSearch:true}
            },
            {
                key: 'description',
                type: 'c_textarea',
                ngModelAttrs: {
                    style: {attribute: 'style'}
                },
                templateOptions: {label: '描述', placeholder: '描述,255字以内', rows: 10, style: 'max-width:500px',maxlength:255}
            },
            {
                key: 'tableType.id',
                type: 'c_select',
                className: 'c_formly_line c_select',
                templateOptions: {
                    label: '桌型',
                    valueProp: 'id',
                    options: typeOpts,
                    required: true,
                    placeholder: '桌型',
                    isSearch:true
                }
            },
            {
                key: 'tableZone.id',
                type: 'c_select',
                className: 'c_formly_line c_select',
                templateOptions: {label: '区域', valueProp: 'id', options: zoneOpts, required: true, placeholder: '区域',isSearch:true}
            },
            {
                key: 'ifBatch',
                type: 'c_input',
                className: 'formly-min-checkbox',
                templateOptions: {label: '批量新增餐桌', required: false, type: 'checkbox'},
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
                templateOptions: {
                    label: '餐桌开始编号',
                    required: true,
                    placeholder: '8位数字内,将添加到餐桌名称后',
                    pattern: '^[0-9]*$',
                    maxlength: 8
                },
                hideExpression: '!model.ifBatch'
            },
            {
                key: 'endNo',
                type: 'c_input',
                templateOptions: {
                    label: '餐桌结束编号',
                    required: true,
                    placeholder: '8位数字内,大于等于餐桌开始编号,将添加到餐桌名称后',
                    pattern: '^[0-9]*$',
                    maxlength: 8
                },
                hideExpression: '!model.ifBatch'
            },
            {
                key: 'ifBatchBind',
                type: 'c_input',
                className: 'formly-min-checkbox',
                templateOptions: {label: '批量一对一新增并关联设备', required: false, type: 'checkbox'},
                defaultValue: false,//不初始化就报错，设为false也报错？？_加了hideExpression就不会报错了，奇怪？？
                hideExpression: '!model.ifBatch'
            },
            {
                key: 'startDeviceUsedNo',
                type: 'c_input',
                templateOptions: {
                    label: '设备起始编号',
                    required: true,
                    placeholder: '8位数字内,将添加到设备名称后,批量自增+1',
                    pattern: '^[0-9]*$',
                    maxlength: 8
                },
                hideExpression: '!model.ifBatchBind || !model.ifBatch'
            },
            {
                key: 'dU.name',
                type: 'c_input',
                templateOptions: {label: '名称', required: true, placeholder: '名称,60字以内',maxlength: 60},
                hideExpression: '!model.ifBatchBind || !model.ifBatch'
            },
            {
                key: 'dU.heartbeat',
                type: 'c_input',
                templateOptions: {label: '心跳', placeholder: '心跳,60字以内',maxlength: 60},
                hideExpression: '!model.ifBatchBind || !model.ifBatch'
            },
            {
                key: 'dU.boardNo',
                type: 'c_input',
                templateOptions: {label: '主板编号', placeholder: '主板编号,60字以内',maxlength: 60, required: true},
                hideExpression: '!model.ifBatchBind || !model.ifBatch'
            },
            {
                key: 'dU.deviceNum',
                type: 'c_input',
                templateOptions: {label: '设备号', placeholder: '设备号,60字以内',maxlength: 60},
                hideExpression: '!model.ifBatchBind || !model.ifBatch'
            },
            {
                key: 'dU.description',
                type: 'c_input',
                templateOptions: {label: '描述', placeholder: '描述,255字以内',maxlength: 255},
                hideExpression: '!model.ifBatchBind || !model.ifBatch'
            },
            {
                key: 'dU.phone',
                type: 'c_input',
                templateOptions: {
                    type: 'text',
                    label: '手机号',
                    placeholder: '请输入11位手机号',
                    pattern: '^(1[3578][0-9]|14[0-7])[0-9]{8}$|(^((1[3578][0-9]|14[0-7])[0-9]{8},)*(1[3578][0-9]|14[0-7])[0-9]{8}$)'
                },
                hideExpression: '!model.ifBatchBind || !model.ifBatch'
            },
            {
                key: 'dU.device.id',
                type: 'c_select',
                className: 'c_select',
                templateOptions: {label: '设备类型', required: true, options: getDeviceList()},
                hideExpression: '!model.ifBatchBind || !model.ifBatch'
            }
        ],
        api: {
            read: './catering/table/paging',
            update: './catering/table/save',
            delete: './catering/table/delete'
        }
    };

    function getDeviceList() {
        var data = $resource("./device/list").query();
        return data;
    }

    cTables.initNgMgrCtrl(mgrData, $scope);

    $scope.goEditorCustom = function (rowIndex) {
        $scope.goEditor(rowIndex);
        if (Constants.thisMerchantStore) {
            $scope.formData.model.store = {name: Constants.thisMerchantStore.name};
        }
        $scope.showBindEditor = false;
        $scope.showInfoEditor = true;
        //$scope.showBatchBindEditor = false;
    };

    //formly提交
    $scope.processSubmit = function () {
        var formly = $scope.formData;
        if (formly.form.$valid) {
            $scope.disableSubmit = true;
            //formly.options.updateInitialValue();//这句会报错
            //console.log(formly.model.deviceUsed)
            //console.log(formly.model)
            //console.log(({deviceUsed:formly.model.deviceUsed? formly.model.deviceUsed : {name:""}}));
            var xhr = $resource(mgrData.api.update);
            xhr.save({
                autoStart: formly.model.startNo ? formly.model.startNo : "",
                autoEnd: formly.model.endNo ? formly.model.endNo : "",
                autoDUStart: formly.model.startDeviceUsedNo ? formly.model.startDeviceUsedNo : "",
                dUString: formly.model.dU ? formly.model.dU : ""
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

                    $scope.toasterManage($scope.toastOperationSucc);
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
        //$scope.showBatchBindEditor = false;
    };

}