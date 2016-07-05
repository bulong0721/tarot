angular.module('inspinia', [])
    .controller('deviceUsedCtrl', deviceUsedCtrl);

/**
 * productUsedCtrl - controller
 */
deviceUsedCtrl.$inject = ['$scope', '$resource', 'Constants', 'cTables','cfromly'];
function deviceUsedCtrl($scope, $resource, Constants, cTables,cfromly) {
    //绑定产品相关参数
    var vm = $scope.showCase = {};
    vm.selected = {};
    vm.selectAll = false;
    vm.toggleAll = toggleAll;
    vm.toggleOne = toggleOne;

    var titleHtml = '<input type="checkbox" ng-model="showCase.selectAll" ng-click="showCase.toggleAll(showCase.selectAll, showCase.selected)">';

    //vm.dtBindOptions = angular.extend(
    //    {
    //        ajax: {
    //            url: '/product/used/listByStoreId',
    //            dataSrc: "rows",
    //            data: function (data, $scope) {
    //                angular.extend(data, $scope.where);
    //            }
    //        },
    //        createdRow: function (row, data, dataIndex) {
    //            $compile(angular.element(row).contents())($scope);
    //        },
    //        headerCallback: function (header) {
    //            if (!vm.headerCompiled) {
    //                // Use this headerCompiled field to only compile header once
    //                vm.headerCompiled = true;
    //                $compile(angular.element(header).contents())($scope);
    //            }
    //        }
    //    }, Constants.bindProductOptions
    //);
    //
    //vm.dtBindColumns = [
    //    DTColumnBuilder.newColumn(null).withTitle(titleHtml).notSortable()
    //        .renderWith(function (data, type, full, meta) {
    //            vm.selected[full.id] = false;
    //            return '<input type="checkbox" ng-model="showCase.selected[' + data.id + ']" ng-click="showCase.toggleOne(showCase.selected)">';
    //        }),
    //    {data: 'id', visible: false},
    //    {data: 'code', title: '产品编号', width: 60, sortable: true},
    //    {data: 'name', title: '产品名称', width: 60, sortable: true},
    //    {data: 'productNum', title: '产品版本', width: 60, sortable: true}
    //];

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
    $scope.showDetailEditor = false;
    $scope.showBindEditor = false;

    $scope.showCase.currentRowIndex = 0;

    $scope.goDeviceBindProductEditor = function (rowIndex) {
        $scope.addNew = true;

        if ($scope.dtApi && rowIndex > -1) {
            $scope.showCase.currentRowIndex = rowIndex;//记录当前选择的行，以备后续更新该行数据

            var data = $scope.dtApi.DataTable.row(rowIndex).data();
            $scope.formBindData.model = data;
            $scope.formBindData.model.bindShowName = '设备名称:' + data.name + ' | 门店名称:' + data.store.name + ' | 主板编号:' + data.boardNo + ' | 设备号:' + data.deviceNum;

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
        $scope.showDataTable = false;
        $scope.showEditor = true;
        $scope.showInfoEditor = false;
        $scope.showDetailEditor = false;
        $scope.showBindEditor = true;

    };

    $scope.processBindSubmit = function () {
        var result = [];
        angular.forEach($scope.showCase.selected, function (data, index, array) {
            //data等价于array[index]
            if (data == true) {
                result.push(index);
            }
        });

        $resource('/deviceUsed/bindProductUsed').save({
            'bindString': JSON.stringify(result),
            'deviceUsedId': $scope.formBindData.model.id
        }, {}, function (respSucc) {
            if (0 != respSucc.status) {
                return;
            }

            //用js离线刷新表格数据
            $scope.dtApi.DataTable.row($scope.showCase.currentRowIndex).data().productUsedList = [];//先清空
            angular.forEach($scope.showCase.selected, function (data, index, array) {
                //data等价于array[index]
                if (data == true) {
                    $scope.dtApi.DataTable.row($scope.showCase.currentRowIndex).data().productUsedList.push({id: index});
                }
            });

            $scope.goDataTable();
        }, function (respFail) {
            console.log(respFail);
        });
    };

    //其他业务逻辑----------------------------


    function getDeviceList() {
        var data = $resource("/device/list").query();
        return data;
    }

    var mgrData = {
        fields: [
            {
                'id': 'store.name',
                'key': 'store.name',
                'type': 'c_input',
                'templateOptions': {'disabled': true, 'label': '门店名称', 'placeholder': '门店名称'}
            },
            {'key': 'name', 'type': 'c_input','templateOptions': {'label': '名称', required: true, 'placeholder': '名称'}},
            {
                'key': 'ifBatch',
                'type': 'c_input',
                'templateOptions': {'label': '批量新增', required: false, 'type': 'checkbox'},
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
                'key': 'startNo',
                'type': 'c_input',
                'templateOptions': {'label': '开始编号', required: false, 'placeholder': '开始编号'},
                hideExpression: '!model.ifBatch'
            },
            {
                'key': 'endNo',
                'type': 'c_input',
                'templateOptions': {'label': '结束编号', required: false, 'placeholder': '结束编号'},
                hideExpression: '!model.ifBatch'
            },
            {'key': 'heartbeat', 'type': 'c_input', 'templateOptions': {'label': '心跳', 'placeholder': '心跳'}},
            {'key': 'boardNo', 'type': 'c_input', 'templateOptions': {'label': '主板编号', 'placeholder': '主板编号'}},
            {'key': 'deviceNum', 'type': 'c_input', 'templateOptions': {'label': '设备号', 'placeholder': '设备号'}},
            {'key': 'description', 'type': 'c_input', 'templateOptions': {'label': '描述', 'placeholder': '描述'}},
            {
                'key': 'device.id',
                'type': 'c_select',
                'templateOptions': {'label': '选择设备类型', required: true, 'options': getDeviceList()}
            }

        ],
        api: {
            read: '/deviceUsed/paging',
            update: '/deviceUsed/update',
            updateAttr: '/deviceUsed/attribute/save',
            deleteAttr: '/deviceUsed/attribute/delete',
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
        $scope.showDetailEditor = true;
    };

    //formly提交
    $scope.processSubmit = function () {
        var formly = $scope.formData;
        if (formly.form.$valid) {
            //formly.options.updateInitialValue();//这句会报错
            var xhr = $resource(mgrData.api.update);
            xhr.save({
                autoStart: formly.model.startNo ? formly.model.startNo : null ,
                autoEnd: formly.model.endNo ? formly.model.endNo : null
            }, formly.model).$promise.then(function saveSuccess(response) {
                if (0 != response.status) {
                    return;
                }
                //批量添加的数据添加到ngtables
                angular.forEach(response.dataMap.updateResult,function(indexData,index,array){
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
        $scope.showDetailEditor = false;
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
            attr.editing = false;
        });
    };

    $scope.deleteAttr = function (product, attr) {
        var xhr = $resource(mgrData.api.deleteAttr);
        xhr.save({id: product.id}, attr).$promise.then(function (result) {
            var index = product.attributes.indexOf(attr);
            product.attributes.splice(index, 1);
        });
    };
}
