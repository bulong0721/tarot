angular.module('myee', [])
    .controller('explorerCtrl', explorerCtrl);

/**
 * explorerCtrl - controller
 */
explorerCtrl.$inject = ['$scope', '$resource', '$filter','cfromly','Constants','cAlerts','toaster','$http'];
function explorerCtrl($scope, $resource, $filter,cfromly,Constants,cAlerts,toaster,$http) {

    var iDatatable = 0, iPush = 1, iEditor = 2;
    $scope.activeTab = iDatatable;
    var nodeTypes =[{name:'目录',value:0},{name:'文件',value:1}];
    $scope.treeData = [{path: '/', name: '/', type: 0, salt: '/'}];
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
                    $scope.formData1.options.resetModel();
                    $scope.activeTab = iEditor;
                    $scope.formData1.model ={
                        salt:data.salt,
                        path:data.path
                    }
                    $scope.current = data;
                },
                edit: function (data) {
                    $scope.activeTab = iEditor;
                    $scope.formData1.model = {
                        salt:data.salt,
                        name:data.name,
                        currPath:data.path,
                        path:data.path,
                        type:data.type,
                    }
                    $scope.current = data;
                },
                delete: function (data) {
                    $scope.delete(data.salt,data.path);
                },
                download: function (data) {
                    $scope.download(data.salt,data.path);
                }
            }
        }
    ];

    $scope.handleSelect = function (data) {
        if (data.type == 0) {
            $resource('../admin/file/search').save({node: data.path}, {}).$promise.then(
                function success(resp) {
                    angular.merge(data.children, resp.rows);
                }
            );
        }
    };

    //点击推送按钮时调用
    $scope.goSend = function () {
        var arraySelected = [];
        var data = $scope.treeData;
        recursionTree(data, arraySelected);
        if(arraySelected.length == 0){
            toaster.warning({ body:"请选择文件！"});
            return;
        }
        var content = "";
        for(var i in arraySelected){
            content += '{"url": "'+ arraySelected[i].url +'","name": "'+ arraySelected[i].name +'"},';
        }
        $scope.activeTab = iPush;
        $scope.formData.model.store = {name: Constants.thisMerchantStore.name};
        $scope.formData.model.content = "[" + content.substr(0,content.length-1)  + "]";
    };

    //递归出所有选中的文件
    function recursionTree(data, arraySelected){
        angular.forEach(data, function(d){
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
                key: 'content',
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
            download: '../admin/file/download',
            delete: '../admin/file/delete',
        }
    };

    var mgrData1 = {
        fields: [
            {
                key: 'salt',
                type: 'c_input',
                templateOptions: {disabled: true, label: '隐藏ID', placeholder: '隐藏ID'},
                hideExpression: function ($viewValue, $modelValue, scope) {
                    return true;   //隐藏
                }
            },
            {
                id: 'path',
                key: 'path',
                type: 'c_input',
                templateOptions: { disabled: true, label: '绝对路径', placeholder: '绝对路径'}
            },
            {
                id: 'name',
                key: 'name',
                type: 'c_input',
                templateOptions: { required: true, label: '节点名称', placeholder: '节点名称'}
            },
            {
                id: 'currPath',
                key: 'currPath',
                type: 'c_input',
                templateOptions: { required: true, label: '节点路径', placeholder: '节点路径'},
                expressionProperties: {
                    'templateOptions.required': 'model.type==1?false:true' // disabled when ifEditor is true
                }
            },
            {
                id: 'type',
                key: 'type',
                type: 'c_select',
                className: 'c_select',
                templateOptions: { required: true, label: '节点类型',  options: nodeTypes }
            },
            {
                key: 'resFile',
                type: 'upload',
                templateOptions: {required: false,type: 'file', label: '节点文件' },
                hideExpression: function ($viewValue, $modelValue, scope) {
                    return scope.model.type == 0?true:false;//true新增文件夹时隐藏文件内容输入框 false新增时显示批量修改
                },
                expressionProperties: {
                    'templateOptions.disabled': 'model.ifEditor' // disabled when ifEditor is true
                }
            },
            {
                key: 'ifEditor',
                type: 'c_input',
                className:'formly-min-checkbox',
                templateOptions: {label: '文本编辑', required: false, type: 'checkbox'},
                defaultValue: false,
                hideExpression: function ($viewValue, $modelValue, scope) {
                    if(scope.model.ifEditor){
                        $scope.showContent($scope.current);
                    }else{
                        scope.model.content="";
                    }
                    return scope.model.type == 0?true:false;//新增文件夹时隐藏
                },
            },
            {
                id: 'content',
                key: 'content',
                type: 'c_textarea',
                ngModelAttrs: {
                    style: {attribute: 'style'}
                },
                templateOptions: {disabled : true,label: '文件内容', placeholder: '文件内容',rows: 10,style: 'max-width:500px' },
                hideExpression: function ($viewValue, $modelValue, scope) {
                    return scope.model.type == 0?true:false;//true新增文件夹时隐藏文件内容输入框 false新增时显示批量修改
                },
                expressionProperties: {
                    'templateOptions.disabled': '!model.ifEditor' // disabled when ifEditor is false
                }
            }
        ],
        api: {
            getContent: '../admin/content/get',
            create: '../admin/file/create',
            //delete: '../device/used/delete',
        }
    };
    //formly配置项push
    $scope.formData = {
        fields: mgrData.fields
    };

    //formly配置项editor
    $scope.formData1 = {
        fields: mgrData1.fields
    };

    //formly提交
    $scope.pushSubmit = function () {
        var formly = $scope.formData;
        console.log(formly)
        if (formly.form.$valid) {
            formly.options.updateInitialValue();
            $resource(mgrData.api.push).save({}, formly.model).$promise.then(
                function success(resp) {
                    if(resp != null && resp.status == 0){
                        toaster.success({ body:"发送成功"});
                        $scope.goDataTable();
                    }else{
                        toaster.error({ body:resp.statusMessage});
                    }
                }
            );
        }
    };

    //删除资源
    $scope.delete = function (salt, path) {
        cAlerts.confirm('确定删除?',function(){
            //点击确定回调
            $resource(mgrData.api.delete).save({salt: salt, path: path}, {}).$promise.then(saveSuccess, saveFailed);
        },function(){
            //点击取消回调
        });
    };

    $scope.download = function (salt, path) {
        $resource(mgrData.api.download).save({salt: salt, path: path}, {}).$promise.then(function(data) {
            window.location.href = data.rows[0].url;
        });
    };

    //formly提交
    //$scope.editorSubmit = function () {
    //    var formly = $scope.formData1;
    //    console.log(formly)
    //    if (formly.form.$valid) {
    //        formly.options.updateInitialValue();
    //        $resource(mgrData.api.create).save({}, formly.model).$promise.then(saveSuccess, saveFailed);
    //    }
    //};

    //formly提交
    $scope.editorSubmit = function () {
        var formly = $scope.formData1;
        //console.log(formly)
        //console.log(formly.model);
        if (formly.form.$valid) {
            //$scope.formData1.options.updateInitialValue();
            //console.log(formly.model)
            var  addFile = $scope.formData_addFile;
            if(!addFile){
                addFile = new FormData();
            }
            if($scope.formData1.model.type ==0){
                console.log($scope.formData1.model)
                console.log($scope.current);
                $scope.current.children.push({
                    name:$scope.formData1.model.name,
                    path:$scope.current.path+"/"+$scope.formData1.model.currPath,
                    salt:$scope.current.salt,
                    storeId:$scope.current.storeId,
                    url:$scope.current.url+"/"+$scope.formData1.model.currPath,
                    size:0,
                    modified:new Date(),
                    children:[],
                    type:0});
                $scope.goDataTable();
            }else{
                addFile.append('entityText', JSON.stringify($scope.formData1.model));
                $http.post(mgrData1.api.create, addFile, {
                    withCreadential: true,
                    headers: {'Content-Type': undefined, 'Access-Control-Allow-Methods': '*', 'Access-Control-Allow-Origin': '*'},
                    transformRequest: angular.identity
                }).success(function(res){
                    //formly.options.updateInitialValue();
                    //$scope.current.children.push(res.rows);
                    angular.merge($scope.current.children, res.rows);
                    $scope.goDataTable();
                });
            }

            /*$resource(mgrData1.api.create).save({}, $scope.formData).$promise.then(saveSuccess, saveFailed);*/
        }
    };
    $scope.formData_addFile = null;
    var unlisten = $scope.$on('fileToUpload', function(event, arg) {
        $scope.formData_addFile = arg;
    });
    $scope.$on('$destroy', unlisten);

    //formly返回
    $scope.goDataTable = function () {
        $scope.activeTab = iDatatable;
    };

    //成功后调用
    function saveSuccess(response) {

    }

    //失败调用
    function saveFailed(response) {
    }

    $scope.showContent = function(data){
        $resource(mgrData1.api.getContent).get({data: data}, {}).$promise.then(function success(resp) {
            console.log(resp)
            if(resp != null && resp.status == 0){
                $scope.formData1.model.content=resp.rows[0].message;
            }else{
                toaster.error({ body:resp.statusMessage});
            }
        });
    }
}