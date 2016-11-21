angular.module('myee', [])
    .controller('explorerCtrl', explorerCtrl);

/**
 * explorerCtrl - controller
 */
explorerCtrl.$inject = ['$scope', '$resource', '$filter', 'cfromly', 'Constants', 'cAlerts', 'toaster', '$rootScope', '$timeout','$q'];
function explorerCtrl($scope, $resource, $filter, cfromly, Constants, cAlerts, toaster, $rootScope, $timeout,$q) {
    var lang = $rootScope.lang_zh;
    var iDatatable = 0, iPush = 2, iEditor = 1, iConfig = 3;
    $scope.activeTab = iDatatable;
    var nodeTypes = [{name: '目录', value: 0}, {name: '文件', value: 1}];
    $scope.treeControl = {};
    $scope.treeData = [{path: '/', name: '/', type: 0, salt: '/'}];
    $scope.expandField = {field: 'name'};
    $scope.treeColumns = [
        {
            displayName: '所属门店',
            columnWidth: '15%',
            cellTemplate: '<span>{{cellTemplateScope.text(row.branch)}}</span>',
            cellTemplateScope: {
                text: function (data) {
                   return data.saltName;
                }
            }
        },
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
            columnWidth: '7%',
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
            columnWidth: '7%',
            cellTemplate: '<span>{{cellTemplateScope.format(row.branch)}}</span>',
            cellTemplateScope: {
                format: function (data) {
                    if (!data.size) return '-';
                    return $filter('sizeFormatter')(data.size)
                }
            }
        },
        {
            displayName: '操作',
            columnWidth: '120',
            cellTemplate: '<a><i ng-if="row.branch.type == 0" class="btn-icon fa fa-plus" tooltip-placement="top" uib-tooltip="' + lang.addResource + '" ng-click="cellTemplateScope.add(row.branch)"></i></a>' +
            '<a><i ng-if="row.branch.type == 1" class="btn-icon fa fa-ban" tooltip-placement="top" uib-tooltip="' + lang.noAddFile + '"></i></a>' +
            '<span class="divider"></span>' +
                //'<a><i ng-if="row.branch.type == 1" class="btn-icon fa fa-pencil" tooltip-placement="top" uib-tooltip="' + lang.edit + '" ng-click="cellTemplateScope.edit(row.branch)"></i></a>' +
                //'<a><i ng-if="row.branch.type == 0" class="btn-icon fa fa-ban" tooltip-placement="top" uib-tooltip="' + lang.noEditFolder + '"></i></a>' +
                //'<span class="divider"></span>' +
            '<a><i ng-if="row.branch.salt == row.branch.storeId && row.branch.path != \'/\'" class="btn-icon fa fa-trash-o" tooltip-placement="top" uib-tooltip="' + lang.delete + '" ng-click="cellTemplateScope.delete(row.branch)"></i></a>' +
            '<a><i ng-if="row.branch.salt != row.branch.storeId || row.branch.path == \'/\'" class="btn-icon fa fa-ban" tooltip-placement="top" uib-tooltip="' + lang.noAuthDelete + '"></i></a>' +
            '<span class="divider"></span>' +
            '<a ng-if="row.branch.type == 1" ng-href="{{row.branch.url}}" tooltip-placement="top" uib-tooltip="' + lang.download + '" download><i class="btn-icon fa fa-download" ></i></a>' +
            '<a ng-if="row.branch.type == 0"><i class="btn-icon fa fa-ban" tooltip-placement="top" uib-tooltip="' + lang.noDownloadFolder + '"></i></a>',
            cellTemplateScope: {
                add: function (data) {
                    $scope.handleSelect(data);
                    //$scope.formData.options.resetModel();
                    $scope.activeTab = iEditor;
                    $scope.formData.model = {
                        salt: data.salt,
                        path: data.path
                    }
                    if (angular.element('#file')[0]) {
                        angular.element('#file')[0].value = '';//清空input[type=file]value[ 垃圾方式 建议不要使用]
                    }
                    $scope.current = data;
                },
                edit: function (data) {
                    $scope.activeTab = iEditor;
                    $scope.formData.model = {
                        salt: data.salt,
                        name: data.name,
                        path: data.path,
                        type: data.type,
                        editorModel: 1
                    }
                    if (angular.element('#file')[0]) {
                        angular.element('#file')[0].value = '';//清空input[type=file]value[ 垃圾方式 建议不要使用]
                    }
                    $scope.current = data;
                    $scope.flag1 = true;
                },
                delete: function (data) {
                    $scope.delete(data);
                },
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
        if (arraySelected.length == 0) {
            toaster.warning({body: "请选择文件！"});
            return;
        }
        if (arraySelected.length > 100) {
            toaster.warning({body: "请选择不超过100个文件！"});
            return;
        }
        $scope.activeTab = iPush;
        $scope.formDataPusher.model.store = {name: Constants.thisMerchantStore.name};
        $scope.formDataPusher.model.content = arraySelected;
    };

    //递归出所有选中的文件
    function recursionTree(data, arraySelected) {
        angular.forEach(data, function (d) {
            if (d.checked == true && d.type == 1) {
                arraySelected.push({url: d.url, name: d.name});
            }
            if (d.children.length > 0) {
                recursionTree(d.children, arraySelected);
            }
        });
    }

    //查询推送设备下拉框内容
    function getDeviceUsedList() {
        var data = $resource("./device/used/list4Select").query();
        return data;
    }

    //查询推送应用下拉框内容
    function getAppList() {
        return [{name: 'gaea', value: 1}];
    }

    function parsePushContent(value) {
        return eval(value);
    }

    function formatPushContent(value) {
        return $filter('json')(value, 2);
    }

    var mgrDataPusher = {
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
                templateOptions: {label: '选择推送应用', required: true, options: getAppList()}
            },
            {
                key: 'timeout',
                type: 'datepicker',
                templateOptions: {
                    label: '过期时间',
                    placeholder: '过期时间',
                    type: 'text',
                    datepickerPopup: 'yyyy-MM-dd',
                    datepickerOptions: {format: 'yyyy-MM-dd'}
                }
            },
            {
                key: 'storagePath',
                type: 'c_input',
                ngModelAttrs: {
                    maxlen: {
                        attribute: 'maxlength'
                    }
                },
                templateOptions: {label: '终端存储路径', placeholder: '终端存储路径(长度小于50)', maxlen: 50}
            },
            {
                key: 'content',
                type: 'c_textarea',
                ngModelAttrs: {
                    style: {attribute: 'style'},
                    maxlen: {attribute: 'maxlength'}
                },
                parsers: [parsePushContent],
                formatters: [formatPushContent],
                templateOptions: {
                    label: '推送内容',
                    required: true,
                    placeholder: '推送内容(长度小于40000)',
                    rows: 20,
                    style: 'max-width:1000px',
                    maxlen: 40000
                }
            }
        ],
        api: {
            push: '../admin/file/push',
            delete: '../admin/file/delete',
        }
    };

    var mgrData = {
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
                key: 'editorModel',
                type: 'c_input',
                templateOptions: {disabled: true, label: '编辑模式', placeholder: '编辑模式'},
                hideExpression: function ($viewValue, $modelValue, scope) {
                    return true;   //隐藏
                }
            },
            {
                id: 'path',
                key: 'path',
                type: 'c_input',
                templateOptions: {disabled: true, label: '绝对路径', placeholder: '绝对路径'}
            },
            {
                id: 'name',
                key: 'name',
                type: 'c_input',
                templateOptions: {required: true, label: '文件名称', placeholder: '文件名称'},
            },
            {
                id: 'currPath',
                key: 'currPath',
                type: 'c_input',
                ngModelAttrs: {
                    maxlen: {
                        attribute: 'maxlength'
                    }
                },
                templateOptions: {label: '文件路径', placeholder: '文件路径(长度小于50)', maxlen: 50},
                hideExpression: function ($viewValue, $modelValue, scope) {
                    return scope.model.type == 0 ? true : false;//true新增文件夹时隐藏文件路径，默认路径和名称相同
                },
                expressionProperties: {
                    'templateOptions.disabled': 'model.editorModel==1?true:false', //编辑模式不能修改节点路径
                }
            },
            {
                id: 'type',
                key: 'type',
                type: 'c_select',
                className: 'c_select',
                templateOptions: {required: true, label: '文件类型', options: nodeTypes},
                expressionProperties: {
                    'templateOptions.disabled': 'model.editorModel==1?true:false' //编辑模式不能修改节点类型
                }
            },
            {
                id: "file",
                key: 'file',
                type: 'upload',
                name: '',
                templateOptions: {required: false, type: 'file', label: '上传文件'},
                hideExpression: function ($viewValue, $modelValue, scope) {
                    if (scope.model.type == 0) {  //新增文件夹
                        return true
                    } else {
                        return scope.model.ifEditor
                    }
                    //return scope.model.type == 0 ? true : false;//true新增文件夹时隐藏文件内容输入框 false新增时显示批量修改
                },
                //expressionProperties: {
                //    'templateOptions.disabled': 'model.ifEditor', // disabled when ifEditor is true
                //    //'templateOptions.disabled': 'model.editorModel==1?true:false'
                //}
            }
            //{
            //    key: 'ifEditor',
            //    type: 'c_input',
            //    className: 'formly-min-checkbox',
            //    templateOptions: {label: '文本编辑', required: false, type: 'checkbox'},
            //    defaultValue: false,
            //    hideExpression: function ($viewValue, $modelValue, scope) {
            //        var flag = scope.model.editorModel == 1 ? true : false;//是否是编辑模式
            //        if (scope.model.ifEditor && flag && $scope.flag1) {
            //            $scope.showContent($scope.current);
            //            $scope.flag1 = false;
            //        }
            //        return scope.model.type == 0 ? true : false;//新增文件夹时隐藏
            //    }
            //},
            //{
            //    id: 'content',
            //    key: 'content',
            //    type: 'c_textarea',
            //    ngModelAttrs: {
            //        style: {attribute: 'style'},
            //        maxlen: {attribute: 'maxlength'}
            //    },
            //    templateOptions: {
            //        disabled: true,
            //        label: '文件内容',
            //        placeholder: '文件内容(长度小于2000)',
            //        rows: 15,
            //        style: 'max-width:500px',
            //        maxlen: 2000
            //    },
            //    hideExpression: function ($viewValue, $modelValue, scope) {
            //        if(scope.model.type == 0  ){  //新增文件夹
            //           return true
            //        }else{
            //            return !scope.model.ifEditor
            //        }
            //    },
            //    expressionProperties: {
            //        'templateOptions.disabled': '!model.ifEditor', // disabled when ifEditor is false
            //        'templateOptions.required': 'model.ifEditor' // disabled when ifEditor is false
            //    }
            //}
        ],
        api: {
            getContent: '../admin/content/get',
            create: '../admin/file/create',
            //delete: './device/used/delete',
        }
    };
    //formly配置项push
    $scope.formDataPusher = {
        fields: mgrDataPusher.fields
    };

    //formly配置项editor
    $scope.formData = {
        fields: mgrData.fields
    };

    //formly提交
    $scope.pushSubmit = function () {
        var formly = $scope.formDataPusher;
        if (formly.form.$valid) {
            $scope.disableSubmit = true;
            formly.options.updateInitialValue();
            $resource(mgrDataPusher.api.push).save({}, formly.model).$promise.then(function success(resp) {
                    $scope.disableSubmit = false;
                    if (resp != null && resp.status == 0) {
                        toaster.success({body: resp.statusMessage});
                        $scope.goDataTable();
                    } else {
                        toaster.error({body: resp.statusMessage});
                    }
                }
            );
        }
    };

    //删除资源
    $scope.delete = function (data) {
        var parentNode = $scope.treeControl.get_parent_branch(data);
        cAlerts.confirm('确定删除?', function () {
            //点击确定回调
            $resource(mgrDataPusher.api.delete).save({
                salt: data.salt,
                path: data.path
            }, {}).$promise.then(function success(resp) {
                    if (resp != null && resp.status == 0) {
                        $scope.deleteDom(parentNode.children, data.uid);
                        toaster.success({body: "删除成功"});
                        $scope.goDataTable();
                    } else {
                        toaster.error({body: resp.statusMessage});
                    }
                });
        });
    };

    //删除资源dom删除
    $scope.deleteDom = function (childrenList, uid) {
        var i = 0;
        while (i < childrenList.length) {
            if (childrenList[i].uid == uid) {
                childrenList.splice(i, 1)
                break;
            }
            i++;
        }
    }

    //formly提交
    $scope.editorSubmit = function () {
        var formly = $scope.formData;
        if (formly.form.$valid) {
            var addFile = $scope.formData_addFile;
            if (!addFile) {
                addFile = new FormData();
            }
            if ($scope.formData.model.type == 0) { //新增文件夹
                var newFileName = $scope.formData.model.name;
                var childrenArray = $scope.current.children;
                var sameDirFlag = false;
                for (var i = 0; i < childrenArray.length; i++) {
                    if (newFileName == childrenArray[i].name) {
                        sameDirFlag = true;
                    }
                }
                if (sameDirFlag) {
                    cAlerts.confirm('已存在同名文件夹，是否覆盖?', function () {
                        //点击确定回调
                        createDir();
                    }, function () {
                        //点击取消回调
                    });
                } else {
                    createDir();
                }
            } else { //新增文件
                //添加同文件夹下同名检测
                var newFileName = $scope.formData.model.name;
                var childrenArray = $scope.current.children;
                var sameFileFlag = false;
                for (var i = 0; i < childrenArray.length; i++) {
                    var name = childrenArray[i].name;
                    if (newFileName == name) {
                        sameFileFlag = true;
                    }
                }
                if (sameFileFlag) {
                    cAlerts.confirm('已存在同名文件，是否覆盖?', function () {
                        //点击确定回调
                        createFile();
                    }, function () {
                        //点击取消回调
                    });
                } else {
                    createFile();
                }

                //暂时注释，勿删
                //var addFile = {};
                //var entityText = JSON.stringify($scope.formData.model);
                //if (!$scope.formData.model.ifEditor) { //文件上传
                //    addFile = $scope.formData_addFile;
                //    var object = JSON.parse(entityText);
                //    object.content = "";
                //    entityText = JSON.stringify(object);
                //}
                ////var addFile = $scope.formData_addFile || {};
                //$scope.disableSubmit = true;
                //$resource(mgrData.api.create).save({entityText: entityText}, addFile).$promise.then(function (res) {
                //    //if ($scope.formData.model.editorModel == 1) {
                //    //    var fileNewName = $scope.formData.model.name;
                //    //    $scope.current.name = fileNewName;
                //    //    var index = $scope.formData.model.path.lastIndexOf("/");
                //    //    var path = $scope.formData.model.path.substring(0, index);
                //    //    $scope.current.path = path + "/" + fileNewName;
                //    //} else {
                //    angular.merge($scope.current.children, res.rows);
                //    //}
                //    $scope.goDataTable();
                //});
            }
        }
    };

    function createFile() {
        var entityText = JSON.stringify($scope.formData.model);
        var addFile = $scope.formData_addFile;
        $scope.disableSubmit = true;
        $resource(mgrData.api.create).save({entityText: entityText}, addFile).$promise.then(function (res) {
            angular.merge($scope.current.children, res.rows);
            $scope.goDataTable();
        });
    }

    function createDir() {
        var parentPath = $scope.current.path == '/' ? "" : $scope.current.path;
        $scope.current.children.push({
            name: $scope.formData.model.name,
            path: parentPath + "/" + $scope.formData.model.name,
            salt: $scope.current.salt,
            storeId: $scope.current.storeId,
            url: $scope.current.url + "/" + $scope.formData.model.name,
            size: 0,
            modified: new Date(),
            children: [],
            type: 0
        });
        $scope.goDataTable();
    }

    $scope.$on('fileToUpload', function (event, arg) {
        $scope.formData_addFile = arg;
    });

    //formly返回
    $scope.goDataTable = function () {
        $scope.disableSubmit = false;
        $scope.activeTab = iDatatable;
    };

    //成功后调用
    function saveSuccess(response) {

    }

    //失败调用
    function saveFailed(response) {
    }

    $scope.disableSubmit = false;
    $scope.showContent = function (data) {
        $resource(mgrData.api.getContent).get({data: data}, {}).$promise.then(function success(resp) {
            if (resp != null && resp.status == 0) {
                $scope.formData.model.content = resp.rows[0].message;
                $scope.disableSubmit = false;
            } else {
                $scope.disableSubmit = true;
                toaster.error({body: resp.statusMessage});
            }
        });
    }



    //升级配置--------------------------------------------------------------
    $scope.goUpdateConfig = function () {
        $scope.activeTab = iConfig;
        $scope.formDataUpdateConfig.model.code = '';
        initialConfig();
    };

    function initialConfig(){
        $scope.formDataUpdateConfig.model.attributes = [];
        $scope.submitModuleResult = [];
        $scope.submitApkResult = [];
    }

    $scope.mgrUpdateConfigData = {
        fields: [
            {
                key: 'code',
                type: 'c_select',
                className: 'c_select',
                templateOptions: {
                    label: '要升级的设备组',
                    required: true,
                    options: getProductUsedList()
                },
                controller: ['$scope', function (formlyScope) {
                    formlyScope.$watch('model.code', function (newValue, oldValue, theScope) {
                        //这里的formlyScope和theScope都是formly作用域
                        if (newValue != '' && newValue !== oldValue) {
                            var code = newValue;
                            //每次读取文件前先清空缓存
                            initialConfig();
                            var data = {
                                "name": $scope.mgrUpdateConfigData.constant.FILE_NAME_MODULE,
                                "path": $scope.mgrUpdateConfigData.constant.BASE_PATH_MODULE + code + "/" + $scope.mgrUpdateConfigData.constant.FILE_NAME_MODULE,
                                "salt": Constants.thisMerchantStore.id
                            }
                            $scope.loadConfigFile(code,"模块配置读取失败！",data,$scope.mgrUpdateConfigData.constant.TYPE_MODULE);
                        }
                    });
                }]
            }
        ],
        api: {
            uploadFile: './files/create',
        },
        constant: {
            FILE_NAME_NO_EXTERN: 4,
            FILE_NAME_WITH_EXTERN: 2,
            FILE_EXTERN: 3,
            BASE_PATH_MODULE: 'version/moduleUpdate/',
            FILE_NAME_MODULE: 'moduleUpdateConfig.txt',
            BASE_PATH_APK: 'version/apkUpdate/',
            FILE_NAME_APK: 'apkUpdateConfig.txt',
            TYPE_MODULE: 'module',
            TYPE_APK: 'apk',
        }
    };

    //选择设备组的时候加载配置文件
    $scope.loadConfigFile = function (code,failMessage,data,type) {
        //console.log("code:" + code);
        //先读取模块配置并加载
        $resource(mgrData.api.getContent).get({data: data}, {}).$promise.then(function success(resp) {
            if (!resp || resp.status != 0) {
                toaster.error({body: code + failMessage + resp.statusMessage});
                return;
            }

            var obj = JSON.parse(resp.rows[0].message);
            //console.log(obj)
            angular.forEach(obj, function (indexData, index, array) {
                //indexData等价于array[index]
                $scope.formDataUpdateConfig.model.attributes.push({
                    name: indexData.name,
                    version: indexData.version,
                    force_update: indexData.force_update,
                    description:indexData.description,
                    type: type,
                    md5: indexData.md5,
                    web: indexData.web,
                    uploadState: true,
                    md5InputValid: false,
                    show: true,
                    editing: false
                });
            });

            //递归调用本函数时的终止条件
            if(data.name == $scope.mgrUpdateConfigData.constant.FILE_NAME_APK){
                return;
            }
            //递归调用本函数取获取应用配置信息
            data = {
                "name": $scope.mgrUpdateConfigData.constant.FILE_NAME_APK,
                "path": $scope.mgrUpdateConfigData.constant.BASE_PATH_APK + code + "/" + $scope.mgrUpdateConfigData.constant.FILE_NAME_APK,
                "salt": Constants.thisMerchantStore.id
            }
            $scope.loadConfigFile(code,"应用配置读取失败！",data,$scope.mgrUpdateConfigData.constant.TYPE_APK);
        });
    }

    //版本升级校验Md5是否一致
    $scope.showMd5Input = function (thisRow) {
        if (thisRow.md5InputValid == true) {
            thisRow.md5InputValid = false;
        } else {
            thisRow.md5InputValid = true;
        }
    }

    //查询推送设备组下拉框内容
    function getProductUsedList() {
        return $resource('./product/used/listByStore4Select').query();
    }

    //formly配置项config
    $scope.formDataUpdateConfig = {
        fields: $scope.mgrUpdateConfigData.fields
    };

    //校验是否选择了设备组
    function checkProductUsedCodeOK(code){
        if (!code || code == '') {
            toaster.error({body: "请先选择要升级的设备组!"});
            return false;
        }
        return true;
    }
    //formly提交config
    $scope.configSubmit = function () {
        var code = $scope.formDataUpdateConfig.model.code;
        //校验是否选择了设备组
        if (!checkProductUsedCodeOK(code)) {
            return;
        }

        var attr = {};
        angular.forEach($scope.formDataUpdateConfig.model.attributes, function (indexData, index, array) {
            //indexData等价于array[index]
            attr = {
                name: indexData.name,
                version: indexData.version,
                force_update: indexData.force_update,
                description:indexData.description,
                md5: indexData.md5,
                web: indexData.web
            };
            if (indexData.show) {//只把没假删除的结果写入最终数据
                if (indexData.type == $scope.mgrUpdateConfigData.constant.TYPE_MODULE) {
                    $scope.submitModuleResult.push(attr);
                }
                else if (indexData.type == $scope.mgrUpdateConfigData.constant.TYPE_APK) {
                    $scope.submitApkResult.push(attr);
                }

            }
        });
        //console.log($scope.submitModuleResult);
        //console.log($scope.submitApkResult);

        //存储追中结果到文件 {"salt":100,"path":"catch","ifEditor":true,"type":1,"name":"test.txt","content":"tetst"}
        $q.all([
            $resource(mgrData.api.create).save({
                entityText: JSON.stringify({
                    "salt": "/",
                    "path": $scope.mgrUpdateConfigData.constant.BASE_PATH_MODULE + code,
                    "ifEditor": true,
                    "type": 1,
                    "name": $scope.mgrUpdateConfigData.constant.FILE_NAME_MODULE,
                    "content": $scope.submitModuleResult
                })
            }, {}).$promise,
            $resource(mgrData.api.create).save({
                entityText: JSON.stringify({
                    "salt": "/",
                    "path": $scope.mgrUpdateConfigData.constant.BASE_PATH_APK + code,
                    "ifEditor": true,
                    "type": 1,
                    "name": $scope.mgrUpdateConfigData.constant.FILE_NAME_APK,
                    "content": $scope.submitApkResult
                })
            }, {}).$promise
        ]).then(function(respArray){//返回结果的序列顺序跟上面参数的promise数组顺序一致
            console.log(respArray)
            if (0 != respArray[0].status) {
                toaster.error({body: "保存模块配置文件失败!"})
                return;
            }
            toaster.success({body: "保存模块配置文件成功!"});
            if (0 != respArray[1].status) {
                toaster.error({body: "保存应用配置文件失败!"})
                return;
            }
            toaster.success({body: "保存应用配置文件成功!"});
            $scope.goDataTable();
        });
    };

    //formly返回
    $scope.configGoDataTable = function () {
        cAlerts.confirm('修改未保存，确认退出?', function () {
            //点击确定回调
            initialConfig();
            $scope.activeTab = iDatatable;
        }, function () {
            //点击取消回调
        });
    };

    $scope.cancelAttr = function (product, attr) {
        //var index = product.attributes.indexOf(attr);
        //product.attributes.splice(index, 1);
        attr.editing = false;
    };

    $scope.deleteAttr = function (model, attr) {
        cAlerts.confirm('确定删除?', function () {
            //点击确定回调
            toaster.success({body: "操作成功!"});
            var index = model.attributes.indexOf(attr);
            //model.attributes.splice(index, 1);
            //为了文件假删除一行
            model.attributes[index].show = false;
            $scope.fileList[index] = {};
        }, function () {
            //点击取消回调
        });

    };

    $scope.updateAttr = function (model, thisRow, index) {
        //console.log(thisRow)
        var _file = $scope.fileList[index];
        if( !_file ){
            $timeout(function () {
                toaster.error({body: "请选择升级包文件!"})
            }, 0);
            return;
        }
        if ( thisRow == null || thisRow.name == "" || thisRow.version == "" || thisRow.force_update == "" || thisRow.type == "") {
            $timeout(function () {
                toaster.error({body: "请填写完整信息!"})
            }, 0);
            return;
        }
        if( thisRow.type == $scope.mgrUpdateConfigData.constant.TYPE_APK && (typeof thisRow.version != 'number') ){
            $timeout(function () {
                toaster.error({body: "请填写纯数字版本信息!"})
            }, 0);
            return;
        }
        if (thisRow.name != $filter('getFileName')(_file.name, $scope.mgrUpdateConfigData.constant.FILE_NAME_NO_EXTERN)) {
            $timeout(function () {
                toaster.error({body: "上传的文件名与模块名不匹配!"})
            }, 0);
            return;
        }

        var fd = new FormData();
        var basePath = '';
        if (thisRow.type == $scope.mgrUpdateConfigData.constant.TYPE_MODULE) {
            basePath = $scope.mgrUpdateConfigData.constant.BASE_PATH_MODULE;
        }
        else if (thisRow.type == $scope.mgrUpdateConfigData.constant.TYPE_APK) {
            basePath = $scope.mgrUpdateConfigData.constant.BASE_PATH_APK;
        }
        else {
            toaster.error({body: "类型设置不正确，保存失败!"});
            return;
        }
        fd.append('file', _file);
        $resource($scope.mgrUpdateConfigData.api.uploadFile).save({
            'type': 'file',
            path: basePath + $scope.formDataUpdateConfig.model.code
        }, fd).$promise.then(function (res) {
                if (0 != res.status) {
                    $timeout(function () {
                        toaster.error({body: _file.name + "上传失败!"})
                    }, 0);
                    $scope.formDataUpdateConfig.model.attributes[index].uploadState = false;
                    return;
                } else {
                    $timeout(function () {
                        toaster.success({body: _file.name + "上传成功!"})
                    }, 0);
                    $scope.formDataUpdateConfig.model.attributes[index].md5 = res.dataMap.tree.md5;
                    $scope.formDataUpdateConfig.model.attributes[index].web = baseUrl.pushUrl + res.dataMap.tree.downloadPath;
                    $scope.formDataUpdateConfig.model.attributes[index].uploadState = true;
                    thisRow.editing = false;
                }
            });

    };

    //下载配置文件函数
    $scope.downloadConfig = function(type) {
        //校验是否选择了设备组
        if (!checkProductUsedCodeOK($scope.formDataUpdateConfig.model.code)) {
            return '';
        }

        var baseUrl = $rootScope.baseUrl.pushUrl
            + Constants.thisMerchantStore.id
            + "/";
        if(type == $scope.mgrUpdateConfigData.constant.TYPE_APK) {
            return baseUrl
                + $scope.mgrUpdateConfigData.constant.BASE_PATH_APK
                + $scope.formDataUpdateConfig.model.code
                + "/" + $scope.mgrUpdateConfigData.constant.FILE_NAME_APK;
        }
        else if(type == $scope.mgrUpdateConfigData.constant.TYPE_MODULE) {
            return baseUrl
                + $scope.mgrUpdateConfigData.constant.BASE_PATH_MODULE
                + $scope.formDataUpdateConfig.model.code
                + "/" + $scope.mgrUpdateConfigData.constant.FILE_NAME_MODULE;
        }
    }

    //读取用户本地配置文件到表格
    /**  读取Json文件内容 **/
    $scope.readFile = function (input) {
        var file_name = $filter('getFileName')(input.files[0].name, $scope.mgrUpdateConfigData.constant.FILE_NAME_WITH_EXTERN);
        if ($scope.mgrUpdateConfigData.constant.FILE_NAME_MODULE != file_name
            && $scope.mgrUpdateConfigData.constant.FILE_NAME_APK != file_name) {
            $timeout(function () {
                toaster.error({body: "请选择"+ $scope.mgrUpdateConfigData.constant.FILE_NAME_APK
                +"或"+ $scope.mgrUpdateConfigData.constant.FILE_NAME_MODULE  +"文件!"})
            }, 0);
            return;
        }

        //支持chrome IE10
        if (window.FileReader) {
            var file = input.files[0];
            filename = file.name.split(".")[0];
            var reader = new FileReader();
            reader.readAsText(file,"utf8");
            reader.onload = function () {
                var obj = JSON.parse(this.result);
                var attr = [];
                angular.forEach(obj, function (indexData, index, array) {
                    var type;
                    //读取模块配置文件
                    if (file_name == $scope.mgrUpdateConfigData.constant.FILE_NAME_MODULE) {
                        type = $scope.mgrUpdateConfigData.constant.TYPE_MODULE;
                    }
                    //读取APK模块配置文件
                    else if (file_name == $scope.mgrUpdateConfigData.constant.FILE_NAME_APK) {
                        type = $scope.mgrUpdateConfigData.constant.TYPE_APK;
                    }
                    //indexData等价于array[index]
                    attr.push({
                        name: indexData.name,
                        version: indexData.version,
                        force_update: indexData.force_update,
                        description: indexData.description,
                        md5: indexData.md5,
                        web: indexData.web,
                        type:type,
                        uploadState: false,
                        md5InputValid: false,
                        show: true,
                        editing: false
                    });
                });
                //手动渲染。。。可以研究有什么更好的
                $scope.$apply(function() {
                    $scope.formDataUpdateConfig.model.attributes = $scope.formDataUpdateConfig.model.attributes.concat(attr);
                    console.log($scope.formDataUpdateConfig.model.attributes)
                });
            }
        }
        //支持IE 7 8 9
        else if (typeof window.ActiveXObject != 'undefined') {
            $timeout(function () {
                toaster.error({body: "浏览器版本过低，请使用IE10以上或火狐或谷歌浏览器!"});
            }, 0);
            return;
        }
    }

    $scope.insertAttr = function (model) {
        if (!model.attributes) {
            model.attributes = [];
        }
        model.attributes.push({
            name: '',
            version: '',
            force_update: '',
            description: '',
            type: $scope.mgrUpdateConfigData.constant.TYPE_MODULE,
            md5: '',
            web: '',
            uploadState: false,
            md5InputValid: false,
            show: true,
            editing: true
        });
    };

    $scope.fileList = [];//存放上传的文件列表
    //校验要上传的升级文件文件名与模块/应用名称是否一致
    $scope.checkUpdate = function (file, thisRow, index) {
        var _file = file.files[0];
        $scope.fileList[index] = _file;
        if (thisRow.name != $filter('getFileName')(_file.name, $scope.mgrUpdateConfigData.constant.FILE_NAME_NO_EXTERN)) {
            $timeout(function () {
                toaster.error({body: "上传的文件名与模块或应用名不一致!"})
            }, 0);
            return;
        }
    }

}