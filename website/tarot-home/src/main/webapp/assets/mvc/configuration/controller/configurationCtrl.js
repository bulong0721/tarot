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

    var iDatatable = 0, iEditor = 5;

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
        }
    };

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
            console.log($scope.formDataReceiptModule.model.attributes)
        } else {
            $scope.formDataReceiptModule.model = {};
            $scope.rowIndex = -1;
        }
        $scope.activeTab = iEditor;
    }

    cTables.initNgMgrCtrl(mgrModuleNameData, $scope);

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
        console.log(items);
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
            console.log($scope.tableOpts.data[$scope.rowIndex])
            $scope.goDataTable();
        });
    };

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