/**
 * Created by Ray.Fu on 2016/12/19.
 */

angular.module('myee', [])
    .controller('receiptPrintedCtrl', receiptPrintedCtrl);

/**
 * receiptPrintedCtrl - controller
 */
receiptPrintedCtrl.$inject = ['$scope', 'cResource', 'Constants', 'cTables', 'cfromly', 'NgTableParams', '$q', 'cAlerts', 'toaster', '$filter'];
function receiptPrintedCtrl($scope, cResource, Constants, cTables, cfromly, NgTableParams, $q, cAlerts, toaster, $filter) {

    var iDatatable = 0, iEditor = 5, iBind = 2;
    $scope.previewButtonName = false;
    var vm = $scope.showCase = {};
    vm.selected = [];
    vm.selectAll = false;
    vm.toggleAll = toggleAll;
    vm.toggleOne = toggleOne;
    $scope.formBindData = {};

    var mgrModuleNameData = $scope.mgrModuleNameData = {
        fields: [
            {
                key: 'moduleName',
                type: 'c_input',
                className: 'c_input',
                templateOptions: {
                    label: '模板名',
                    required: true,
                    placeholder: '模板名',
                    maxlength: 8
                }
            },
            {
                key: 'receiptType',
                type: 'c_select',
                className: 'c_select',
                templateOptions: {
                    label: '模板类型',required: true, options: [{name:'抽奖', value:'1'},{name:'排号', value:'2'}]
                }
            },
            {
                key: 'description',
                type: 'c_textarea',
                className: 'c_textarea',
                templateOptions: {label: '描述', placeholder: '描述', rows: 10, style: 'max-width:500px',maxlength:255}
            }
        ],
        api: {
            read: './configuration/receiptPrinted/paging',
            update: './configuration/receiptPrinted/update',
            delete: './configuration/receiptPrinted/delete',
            listItems: './configuration/receiptPrintedItem/list'
        },
        constant: {
            FILE_NAME_WITH_EXTERN: 2
        }
    };

    /* 跳转编辑或新增小票模板页面*/
    $scope.goEditReceiptPrinted = function (rowIndex) {
        if (rowIndex > -1) {
            var data = $scope.tableOpts.data[rowIndex];
            $scope.formDataReceiptModule.model = angular.copy(data);
            $scope.rowIndex = rowIndex;
            if (!$scope.formDataReceiptModule.model.attributes) {
                $scope.formDataReceiptModule.model.attributes = [];
            }
            var goItems = [];
            if (data.itemList == undefined || data.itemList.length == 0) {
                cResource.get(mgrModuleNameData.api.listItems, {receiptPrintedId : data.id}).then(function(resp){
                    if (!$scope.formDataReceiptModule.model.attributes) {
                        $scope.formDataReceiptModule.model.attributes = [];
                    }
                    $scope.formDataReceiptModule.model.attributes = [];
                    angular.forEach(resp.dataMap.itemList, function (indexData, index, array) {
                        goItems.push({
                            align : indexData.align,
                            content : indexData.content,
                            font : indexData.font,
                            isBold : indexData.bold,
                            isNewline : indexData.newline,
                            isUnderline : indexData.underline,
                            itemType : indexData.itemType,
                            size : indexData.size,
                            editing : false,
                            show : true
                        });
                    });
                    $scope.formDataReceiptModule.model.attributes = goItems;
                });
            } else {
                angular.forEach(data.itemList, function (indexData, index, array) {
                    goItems.push({
                        align : indexData.align,
                        content : indexData.content,
                        font : indexData.font,
                        isBold : indexData.bold,
                        isNewline : indexData.newline,
                        isUnderline : indexData.underline,
                        itemType : indexData.itemType,
                        size : indexData.size,
                        editing : false,
                        show : true
                    });
                });
            }

            $scope.formDataReceiptModule.model.attributes = goItems;
        } else {
            $scope.formDataReceiptModule.model = {};
            $scope.rowIndex = -1;
        }
        $scope.activeTab = iEditor;
    }

    cTables.initNgMgrCtrl(mgrModuleNameData, $scope);

    function initalBindProduct() {
        if ($scope.initalBindProductList) {//如果已经从后台读取过数据了，则不再访问后台获取列表
            var deferred = $q.defer();
            deferred.resolve($scope.initalBindProductList);
            return deferred.promise;
        } else {//第一次需要从后台读取列表
            return cResource.get('./product/used/listByStoreId').then(function(data){
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

    $scope.filterBindOptions = function () {
        var deferred = $q.defer();
        //tables获取数据,获取该门店下可绑定的所有设备
        $scope.tableBindOpts = new NgTableParams({}, {
            counts: [],
            dataset: $filter('filter')($scope.initalBindProductList, $scope.showCase.nameFilter || "")//根据搜索字段过滤数组中数据
        });
        $scope.loadByInit = true;
        $scope.tableOpts.page(1);
        $scope.tableBindOpts.reload();
        deferred.resolve($scope.tableBindOpts);
        return deferred.promise;
    }

    /* 跳转绑定设备组页面 */
    $scope.goDeviceBindProductEditor = function (rowIndex) {
        initalBindProduct().then(function () {
            $scope.filterBindOptions().then(function () {
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
                $scope.activeTab = iBind;
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

        cResource.save('./configuration/receiptPrinted/push2ProductUsed',{
            'pushString': JSON.stringify(result),
            'receiptPrintedId': $scope.formBindData.model.id
        }, {}).then(function(respSucc){
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
            $scope.goDataTable();
        });
    };

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

    //读取用户本地配置文件到表格
    /**  读取Json文件内容 **/
    $scope.readFile = function (input) {
        var file_name = $filter('getFileName')(input.files[0].name, $scope.mgrModuleNameData.constant.FILE_NAME_WITH_EXTERN);
        //支持chrome IE10
        if (window.FileReader) {
            var file = input.files[0];
            filename = file.name.split(".")[0];
            var reader = new FileReader();
            reader.readAsText(file, "utf8");
            reader.onload = function () {
                var obj = JSON.parse(this.result);
                var attr = [];
                angular.forEach(obj, function (indexData, index, array) {
                    //indexData等价于array[index]
                    attr.push({
                        align : indexData.align,
                        content : indexData.content,
                        font : indexData.font,
                        isBold : indexData.isBold,
                        isNewline : indexData.isNewline,
                        isUnderline : indexData.isUnderline,
                        itemType : indexData.itemType,
                        size : indexData.size,
                        show: true,
                        editing: false
                    });
                });
                if (!$scope.formDataReceiptModule.model.attributes) {
                    $scope.formDataReceiptModule.model.attributes = [];
                }
                //手动渲染。。。可以研究有什么更好的
                $scope.$apply(function () {
                    $scope.formDataReceiptModule.model.attributes = $scope.formDataReceiptModule.model.attributes.concat(attr);
                });
                reader.close();
            }
        }
        //支持IE 7 8 9
        else if (typeof window.ActiveXObject != 'undefined') {
            $timeout(function () {
                $filter('toasterManage')(5,"浏览器版本过低，请使用IE10以上或火狐或谷歌浏览器!",false);
            }, 0);
            return;
        }
    }

    //formly提交
    $scope.processSubmit = function () {
        var formly = $scope.mgrModuleNameData;
        formly.model = $scope.formDataReceiptModule.model;
        var items = [];
        if (!$scope.tableOpts.itemList) {
            $scope.tableOpts.itemList = [];
        }
        angular.forEach($scope.formDataReceiptModule.model.attributes, function (indexData, index, array) {
            if (indexData.show == true) {
                items.push({
                    align : indexData.align,
                    content : indexData.content,
                    font : indexData.font,
                    isBold : indexData.isBold,
                    isNewline : indexData.isNewline,
                    isUnderline : indexData.isUnderline,
                    itemType : indexData.itemType,
                    size : indexData.size
                });
            }
        });
        $scope.disableSubmit = true;
        cResource.save(mgrModuleNameData.api.update,{
            items : JSON.stringify(items)
        }, formly.model).then(function(response){
            $scope.disableSubmit = false;
            if ($scope.rowIndex < 0) {
                $scope.tableOpts.data.push(response.dataMap.updateResult);
            } else {
                $scope.tableOpts.data[$scope.rowIndex] = response.dataMap.updateResult;
                $scope.tableOpts.data[$scope.rowIndex].itemList.splice(0, $scope.tableOpts.data[$scope.rowIndex].itemList.length);
            }
            $scope.goDataTable();
        });
    };

    //预览
    $scope.preview = function () {
        var context = $scope.formDataReceiptModule.model.attributes;
        if ($scope.previewButtonName) {
            $scope.previewButtonName = false;
        } else {
            $scope.previewButtonName = true;
        }
    }

    //formly返回
    $scope.goDataTable = function () {
        $scope.activeTab = iDatatable;
        $scope.showBindEditor = false;
        $scope.showInfoEditor = false;
    };

    //formly配置项config
    $scope.formDataReceiptModule = {
        fields: $scope.mgrModuleNameData.fields
    };

    $scope.insertAttr = function (model) {

        if (!model.attributes) {
            model.attributes = [];
        }

        //校验通过新增一行
        model.attributes.push({
            itemType: true,
            isNewline: true,
            font: '',
            isBold: true,
            size: '',
            align: 1,
            isUnderline: true,
            content:'',
            show: true,
            editing: true
        });
    };

    //编辑一行
    $scope.goEditAttr = function (model, thisRow, index) {
        $scope.formDataReceiptModule.model.attributes[index].editing=true;
        $scope.formDataReceiptModule.model.attributes[index].typeDisabled = true;//禁止修改类型

    }

    $scope.cancelAttr = function (product, attr) {
        //var index = product.attributes.indexOf(attr);
        //product.attributes.splice(index, 1);
        attr.editing = false;
    };

    $scope.deleteAttr = function (model, attr) {
        cAlerts.confirm('确定删除?', function () {
            //点击确定回调
            $filter('toasterManage')(5, "操作成功!",true);
            //toaster.success({body: "操作成功!"});
            var index = model.attributes.indexOf(attr);
            //model.attributes.splice(index, 1);
            //为了文件假删除一行
            model.attributes[index].show = false;
            // $scope.formDataReceiptModule.model.attributes.splice(0, 1);
            // console.log(model.attributes)
        }, function () {

        });

    };

    //提交一行
    $scope.updateAttr = function (model, thisRow, index) {
        // cResource.upload(mgrData.api.updateItems,{item: model.attributes},null).then(function(res){
            thisRow.editing = false;
        // });
    };

}