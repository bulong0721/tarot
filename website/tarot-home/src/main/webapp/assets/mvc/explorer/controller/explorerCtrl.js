angular.module('myee', [])
    .controller('explorerCtrl', explorerCtrl);

/**
 * explorerCtrl - controller
 */
explorerCtrl.$inject = ['$scope', '$resource', '$filter','cfromly','Constants'];
function explorerCtrl($scope, $resource, $filter,cfromly,Constants) {

    var iDatatable = 0, iEditor = 1;
    $scope.showInfoPush = false;
    $scope.treeData = [{path: '/', name: '/', type: 0}];
    $scope.expandField = {field: 'name'};
    $scope.treeColumns = [
        {
            displayName: '修改时间',
            columnWidth: '15%',
            cellTemplate: '<span>{{cellTemplateScope.format(row.branch)}}</span>',
            cellTemplateScope: {
                format: function (data) {
                    if (!data.modified) return "-";
                    return $filter('date')(new Date(data.modified), 'yyyy-MM-dd HH:mm:ss');
                }
            }
        },
        {
            displayName: '类型',
            columnWidth: '10%',
            cellTemplate: '<span>{{cellTemplateScope.text(row.branch)}}</span>',
            cellTemplateScope: {
                text: function (data) {
                    return data.type == 0 ? '目录' : '文件';
                },
                style: function (data) {
                    return data.type == 0 ? 'label label-primary' : 'label label-danger';
                }
            }
        },
        {
            displayName: '大小',
            columnWidth: '10%',
            cellTemplate: '<span>{{cellTemplateScope.format(row.branch)}}</span>',
            cellTemplateScope: {
                format: function (data) {
                    if (!data.size) return '-';
                    var value = data.size;
                    if (value > 1024 * 1024 * 1024) {
                        return Math.ceil(value / (1024 * 1024 * 1024)) + 'G';
                    }
                    if (value > 1024 * 1024) {
                        return Math.ceil(value / (1024 * 1024)) + 'M';
                    }
                    if (value > 1024) {
                        return Math.ceil(value / 1024) + 'K';
                    }
                    return value + 'B';
                }
            }
        },
        {
            displayName: '操作',
            columnWidth: '150',
            cellTemplate: '<a><i ng-if="row.branch.type == 0" class="btn-icon fa fa-plus" ng-click="cellTemplateScope.add(row.branch)"></i></a>' +
            '<a><i ng-if="row.branch.type == 1" class="btn-icon fa fa-ban" title="文件不能新增"></i></a>' +
            '<span class="divider"></span>' +
            '<a><i ng-if="row.branch.type == 1" class="btn-icon fa fa-pencil" ng-click="cellTemplateScope.edit(row.branch)"></i></a>' +
            '<a><i ng-if="row.branch.type == 0" class="btn-icon fa fa-ban" title="文件夹不能编辑"></i></a>' +
            '<span class="divider"></span>' +
            '<a><i ng-if="row.branch.salt == row.branch.storeId && row.branch.path != \'/\'" class="btn-icon fa fa-trash-o" ng-click="cellTemplateScope.delete(row.branch)"></i></a>' +
            '<a><i ng-if="row.branch.salt != row.branch.storeId || row.branch.path == \'/\'" class="btn-icon fa fa-ban" title="没有权限删除"></i></a>' +
            '<span class="divider"></span>' +
            '<a><i ng-if="row.branch.type == 1" class="btn-icon fa fa-download" ng-click="cellTemplateScope.download(row.branch)"></i></a>' +
            '<a><i ng-if="row.branch.type == 0" class="btn-icon fa fa-ban" title="文件夹不能下载"></i></a>',
            cellTemplateScope: {
                add: function (data) {
                    alert('add:' + data);
                },
                edit: function (data) {
                    alert('edit:' + data);
                },
                delete: function (data) {
                    alert('delete:' + data);
                },
                download: function (data) {
                    alert('download:' + data);
                }
            }
        }
    ];

    $scope.handleSelect = function (data) {
        if (data.type == 0) {
            $resource('../admin/file/search').get({node: data.path}, {}, function success(resp) {
                angular.merge(data.children, resp.rows);
            });
        }
    };

    //点击推送按钮时调用
    $scope.goSend = function () {
        var arraySelected = [];
        var data = $scope.treeData;
        recursionTree(data, arraySelected);
        console.log(JSON.stringify(arraySelected))
        //$resource('../file/search').save({}, JSON.stringify(arraySelected)).$promise.then(saveSuccess, saveFailed);
        $scope.activeTab = iEditor;
        $scope.showInfoPush = true;
        $scope.formData.model.store = {name: Constants.thisMerchantStore.name};
        //var objectStr = {hotfixSet:arraySelected, publisher: '', transactional: false};
        $scope.formData.model.context = JSON.stringify(arraySelected);
    };

    //递归出所有选中的文件
    function recursionTree(data, arraySelected){

        angular.forEach(data, function(d){
            //console.log(d)
            if(d.checked == true && d.type == 1){
                arraySelected.push({url: d.url, name: d.name});
            }
            if(d.children.length > 0){
                recursionTree(d.children, arraySelected);
            }
        });
    }

    //查询推送设备下拉框内容
    function getDeviceUsedList() {
        var data = $resource("../deviceUsed/list").query();
        console.log(data)
        return data;
    }

    //查询推送应用下拉框内容
    function getAppList() {
        return [{name: 'gaea', value: 1},{name: 'gaea1', value: 2}];
    }

    var mgrData = {
        fields: [
            {
                id: 'store.name',
                key: 'store.name',
                type: 'c_input',
                templateOptions: {disabled: true, label: '推送门店', placeholder: '推送门店'}
            },
            {
                key: 'uniqueNo',
                type: 'c_select',
                className: 'c_select',
                templateOptions: {label: '选择推送设备', required: true, options: getDeviceUsedList()}
            },
            {
                key: 'appId',
                type: 'c_select',
                className: 'c_select',
                templateOptions: {label: '选择推动应用', required: true, options: getAppList()}
            },
            {key: 'timeout', type: 'datepicker', templateOptions: {label: '过期时间', placeholder: '过期时间',type: 'text', datepickerPopup: 'yyyy-MM-dd', datepickerOptions: {format: 'yyyy-MM-dd'}}},
            {
                key: 'context',
                type: 'c_textarea',
                ngModelAttrs: {
                    style: {attribute: 'style'}
                },
                templateOptions: {label: '推动内容', required: true, placeholder: '推动内容', rows: 10,style: 'max-width:450px'}
            }
        ],
        api: {
            //read: '../device/used/paging',
            push: '../admin/file/push',
            //delete: '../device/used/delete',
            //updateAttr: '../device/used/attribute/save',
            //deleteAttr: '../device/used/attribute/delete',
        }
    };

    //formly配置项
    $scope.formData = {
        fields: mgrData.fields
    };

    //formly提交
    $scope.pushSubmit = function () {
        var formly = $scope.formData;
        console.log(formly)
        if (formly.form.$valid) {
            formly.options.updateInitialValue();
            $resource(mgrData.api.push).save({}, formly.model).$promise.then(saveSuccess, saveFailed);
        }
    };

    //formly返回
    $scope.goPushDataTable = function () {
        $scope.activeTab = iDatatable;
        $scope.showInfoPush = false;
    };

    //成功后调用
    function saveSuccess(response) {

    }

    //失败调用
    function saveFailed(response) {
    }
}