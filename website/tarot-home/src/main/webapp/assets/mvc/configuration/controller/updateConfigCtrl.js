angular.module('myee', [])
    .controller('updateConfigCtrl', updateConfigCtrl);

/**
 * updateConfigCtrl - controller
 */
updateConfigCtrl.$inject = ['$scope','$resource', 'cResource', '$filter', 'cfromly', 'Constants', 'cAlerts', 'toaster', '$rootScope', '$timeout', '$q','cTables'];
function updateConfigCtrl($scope,$resource, cResource, $filter, cfromly, Constants, cAlerts, toaster, $rootScope, $timeout, $q,cTables) {
    var lang = $rootScope.lang_zh;
    var iDatatable = 0, iConfig = 1;
    $scope.activeTab = iDatatable;
    $scope.selfDesignPadFileName = '';//缓存自研平板升级文件的文件名，因为要根据它去计算路径

    //查询类型下拉框内容
    function getTypeList() {
        return cResource.query('./updateConfig/listType');
    }
    //查询设备可见范围下拉框内容
    function getSeeTypeList() {
        return cResource.query('./updateConfig/listSeeType');
    }
    function getTypeDisabled() {
        return $scope.typeDisabled;
    }

//升级配置------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    var mgrUpdateConfigData = $scope.mgrUpdateConfigData = {
        fields: [
            {
                key: 'type',
                type: 'c_select',
                className: 'c_select',
                templateOptions: {
                    required: true,
                    label: '类型',
                    options: getTypeList(),
                    isSearch:true,
                    disabled:getTypeDisabled()
                },
                //设置select如果已经有值，则不可编辑
                "expressionProperties": {
                    "templateOptions.disabled": "model.type"
                }
                //controller: ['$scope', function (formlyScope) {
                //    formlyScope.$watch('model.typeDisabled', function (newValue, oldValue, theScope) {
                //        //这里的formlyScope和theScope都是formly作用域
                //        if (newValue != '' && newValue !== oldValue) {
                //
                //        }
                //    });
                //}]
            },
            {
                key: 'seeType',
                type: 'c_select',
                className: 'c_select',
                templateOptions: {
                    required: true,
                    label: '设备可见范围',
                    options: getSeeTypeList(),
                    isSearch:true
                }
            },
            {
                key: 'description',
                type: 'c_textarea',
                ngModelAttrs: {style: {attribute: 'style'}},
                templateOptions: {label: '描述', placeholder: '描述,255字以内', rows: 10, style: 'max-width:500px',maxlength:255,isSearch:false}
            }
            //{
            //    key: 'code',
            //    type: 'c_select',
            //    className: 'c_select',
            //    templateOptions: {
            //        label: '设备组',
            //        required: true,
            //        options: getProductUsedList()
            //    },
            //    controller: ['$scope', function (formlyScope) {
            //        formlyScope.$watch('model.code', function (newValue, oldValue, theScope) {
            //            //这里的formlyScope和theScope都是formly作用域
            //            if (newValue != '' && newValue !== oldValue) {
            //                var code = newValue;
            //                //每次读取文件前先清空缓存
            //                initialConfig();
            //                var data = {
            //                    "name": $scope.mgrUpdateConfigData.constant.FILE_NAME_MODULE,
            //                    "path": $scope.mgrUpdateConfigData.constant.BASE_PATH_MODULE + code + "/" + $scope.mgrUpdateConfigData.constant.FILE_NAME_MODULE,
            //                    "salt": Constants.thisMerchantStore.id
            //                }
            //                $scope.loadConfigFile(code, "模块配置读取失败！", data, $scope.mgrUpdateConfigData.constant.TYPE_MODULE);
            //            }
            //        });
            //    }]
            //}
        ],
        api: {
            read: './updateConfig/paging',
            update: './updateConfig/update',
            getContent: '../admin/content/get',
            create: '../admin/file/create',
            uploadFile: './files/create',
            isFileExist: './files/exist',
        },
        constant: {
            FILE_NAME_NO_EXTERN: 4,
            FILE_NAME_WITH_EXTERN: 2,
            FILE_EXTERN: 3,
            //TYPE_MODULE: 'module',
            //TYPE_APK: 'apk',
            //TYPE_AGENT: 'agent',
            //TYPE_AGENT_PATCH: 'agent_patch',
            //TYPE_SELF_DESIGN_PAD: 'self_design_pad',
            //FORCE_UPDATE_DEFAULT: 'N',
            DEFAULT_MERCHANT_STORE: 100,
            BASE_INFO_MODULE:{
                TYPE: 'module',
                FILE_NAME: 'moduleUpdateConfig.txt',
                GET_FILE_FAIL_MSG:"模块配置读取失败！",
                BASE_PATH: 'version/moduleUpdate/',
                FORCE_UPDATE_DEFAULT: 'N',
            },
            BASE_INFO_APK:{
                TYPE: 'apk',
                FILE_NAME: 'apkUpdateConfig.txt',
                GET_FILE_FAIL_MSG:"应用配置读取失败！",
                BASE_PATH: 'version/apkUpdate/',
                FORCE_UPDATE_DEFAULT: 'N',
            },
            BASE_INFO_AGENT:{
                TYPE: 'agent',
                FILE_NAME: 'agentUpdateConfig.txt',
                GET_FILE_FAIL_MSG:"agent配置读取失败！",
                BASE_PATH: 'version/agentUpdate/',
                FORCE_UPDATE_DEFAULT: 'Y',
            },
            BASE_INFO_AGENT_PATCH:{
                TYPE: 'agent_patch',
                FILE_NAME: 'agent_patchUpdateConfig.txt',
                GET_FILE_FAIL_MSG:"agent补丁包配置读取失败！",
                BASE_PATH: 'version/agent_patchUpdate/',
                FORCE_UPDATE_DEFAULT: 'Y',
            },
            BASE_INFO_SELF_DESIGN_PAD:{
                TYPE: 'self_design_pad',
                FILE_NAME: 'self_design_padUpdateConfig.txt',
                FILE_NAME_EXAMPLE:'Cooky-C001M01A001-RK3288_v2-20160804ota.zip',
                GET_FILE_FAIL_MSG:"自研平板配置读取失败！",
                BASE_PATH: 'version/self_design_padUpdate/',
                FORCE_UPDATE_DEFAULT: 'Y',
            },
            //BASE_PATH_MODULE: 'version/moduleUpdate/',
            //FILE_NAME_MODULE: 'moduleUpdateConfig.txt',
            //BASE_PATH_APK: 'version/apkUpdate/',
            //FILE_NAME_APK: 'apkUpdateConfig.txt',
            //BASE_PATH_AGENT: 'tinker/agent/',
            //FILE_NAME_AGENT: 'agentUpdateConfig.txt',
            //BASE_PATH_AGENT_PATCH: 'tinker/agentPatch/',
            //FILE_NAME_AGENT_PATCH: 'agent_patchUpdateConfig.txt',
            //BASE_PATH_BOARD_UPDATE: 'version/boardUpdate/',
            //FILE_NAME_EXAMPLE_BOARD_UPDATE: 'Cooky-C001M01A001-RK3288_v2-20160804ota.zip',
            //FILE_NAME_SELF_DESIGN_PAD: 'self_design_padUpdateConfig.txt',

        }
    };
    //自动生成文件名对照表
    var autoNameCheckList = [
        {key:'artemis',name:'Artemis',type:$scope.mgrUpdateConfigData.constant.BASE_INFO_APK.TYPE},
        {key:'gaea',name:'Gaea',type:$scope.mgrUpdateConfigData.constant.BASE_INFO_APK.TYPE},
        {key:'odin',name:'Odin',type:$scope.mgrUpdateConfigData.constant.BASE_INFO_APK.TYPE},
        {key:'M03',name:'starline',type:$scope.mgrUpdateConfigData.constant.BASE_INFO_MODULE.TYPE},
        {key:'M04',name:'sensor',type:$scope.mgrUpdateConfigData.constant.BASE_INFO_MODULE.TYPE},
        {key:'M06',name:'move',type:$scope.mgrUpdateConfigData.constant.BASE_INFO_MODULE.TYPE},
        {key:'M07',name:'power',type:$scope.mgrUpdateConfigData.constant.BASE_INFO_MODULE.TYPE},
        {key:'patch_signed_7zip',name:'agentPatch',type:$scope.mgrUpdateConfigData.constant.BASE_INFO_AGENT_PATCH.TYPE},
        {key:'Djinn',name:'agent',type:$scope.mgrUpdateConfigData.constant.BASE_INFO_AGENT.TYPE},
        {key:'1234',name:'selfDesignBoard',type:$scope.mgrUpdateConfigData.constant.BASE_INFO_SELF_DESIGN_PAD.TYPE},
    ];

    //根据类型获取该类型基本信息
    function getBaseInfoByType(type) {
        if (type == $scope.mgrUpdateConfigData.constant.BASE_INFO_MODULE.TYPE) {
            return $scope.mgrUpdateConfigData.constant.BASE_INFO_MODULE;
        }
        else if (type == $scope.mgrUpdateConfigData.constant.BASE_INFO_APK.TYPE) {
            return $scope.mgrUpdateConfigData.constant.BASE_INFO_APK;
        }
        else if (type == $scope.mgrUpdateConfigData.constant.BASE_INFO_AGENT.TYPE) {
            return $scope.mgrUpdateConfigData.constant.BASE_INFO_AGENT;
        }
        else if (type == $scope.mgrUpdateConfigData.constant.BASE_INFO_AGENT_PATCH.TYPE) {
            return $scope.mgrUpdateConfigData.constant.BASE_INFO_AGENT_PATCH;
        }
        else if (type == $scope.mgrUpdateConfigData.constant.BASE_INFO_SELF_DESIGN_PAD.TYPE) {
            return $scope.mgrUpdateConfigData.constant.BASE_INFO_SELF_DESIGN_PAD;
        }
    }

    cTables.initNgMgrCtrl(mgrUpdateConfigData, $scope);

    //判断当前切换的门店是不是默认门店，是才能进行配置文件操作
    function isSwitchDefaultStore () {
        if(Constants.thisMerchantStore.id == mgrUpdateConfigData.constant.DEFAULT_MERCHANT_STORE) {
            return true;
        }
        $filter('toasterManage')(5, "当前切换的门店没有操作权限",false);
        return false;
    }

    $scope.goEditor = function (rowIndex) {
        //判断当前切换的门店是不是默认门店，是才能进行配置文件操作
        if(!isSwitchDefaultStore()) {
            return false;
        }

        initialConfig();
        if (rowIndex > -1) {
            var data = $scope.tableOpts.data[rowIndex];
            $scope.formDataUpdateConfig.model = angular.copy(data);
            //复制完model参数后初始化attr列表
            $scope.formDataUpdateConfig.model.attributes = [];
            $scope.createTimeMills = $scope.formDataUpdateConfig.model.createTime;
            $scope.createTimeStamp = $filter('dateFormatter')($scope.createTimeMills,'yyyyMMddHHmmss');
            $scope.rowIndex = rowIndex;

            console.log($scope.formDataUpdateConfig.model)
            //读取配置文件
            var type = $scope.formDataUpdateConfig.model.type;
            var data = {
                "name": '',
                "path": $scope.formDataUpdateConfig.model.path,
                "salt": $scope.mgrUpdateConfigData.constant.DEFAULT_MERCHANT_STORE
            };
            var baseInfo = getBaseInfoByType(type);
            data.name = baseInfo.FILE_NAME;
            $scope.loadConfigFile(baseInfo.GET_FILE_FAIL_MSG, data, type);

            //if (type == $scope.mgrUpdateConfigData.constant.BASE_INFO_MODULE.TYPE) {
            //    data.name = $scope.mgrUpdateConfigData.constant.BASE_INFO_MODULE.FILE_NAME;
            //    $scope.loadConfigFile("模块配置读取失败！", data, type);
            //}
            //else if (type == $scope.mgrUpdateConfigData.constant.BASE_INFO_APK.TYPE) {
            //    //递归调用本函数取获取应用配置信息
            //    data.name = $scope.mgrUpdateConfigData.constant.BASE_INFO_APK.FILE_NAME;
            //    $scope.loadConfigFile("应用配置读取失败！", data, type);
            //}
            //else if (type == $scope.mgrUpdateConfigData.constant.BASE_INFO_AGENT.TYPE) {
            //    data.name = $scope.mgrUpdateConfigData.constant.BASE_INFO_AGENT.FILE_NAME;
            //    $scope.loadConfigFile("agent配置读取失败！", data, type);
            //}
            //else if (type == $scope.mgrUpdateConfigData.constant.BASE_INFO_AGENT_PATCH.TYPE) {
            //    data.name = $scope.mgrUpdateConfigData.constant.BASE_INFO_AGENT_PATCH.FILE_NAME;
            //    $scope.loadConfigFile("agentPatch配置读取失败！", data, type);
            //}
            //else if (type == $scope.mgrUpdateConfigData.constant.BASE_INFO_SELF_DESIGN_PAD.TYPE) {
            //    data.name = $scope.mgrUpdateConfigData.constant.BASE_INFO_SELF_DESIGN_PAD.FILE_NAME;
            //    $scope.loadConfigFile("自研平板配置读取失败！", data, type);
            //}
        } else {
            //$scope.formDataUpdateConfig.model = {};
            $scope.rowIndex = -1;
            $scope.createTimeMills = new Date().getTime();
            $scope.createTimeStamp = $filter('dateFormatter')($scope.createTimeMills,'yyyyMMddHHmmss');
        }


        $scope.activeTab = iConfig;
        //$scope.showTypeAgent = $scope.showTypeAgentPatch = true;
    };

    function initialConfig() {
        //formly配置项config
        $scope.formDataUpdateConfig = {
            model:{
                attributes : []
            },
            fields: $scope.mgrUpdateConfigData.fields
        };
        initialParams();
    }

    function initialParams() {
        $scope.submitResult = [];
        $scope.createTimeStamp = '';//创建时间，用于存储路径当文件夹名用
        //$scope.submitModuleResult = [];
        //$scope.submitApkResult = [];
        //$scope.submitAgentResult = [];
        //$scope.submitAgentPatchResult = [];
    }

    //选择类型的时候加载配置文件
    $scope.loadConfigFile = function (failMessage, data, type) {
        //先读取模块配置并加载
        cResource.get($scope.mgrUpdateConfigData.api.getContent,{data: data}).then(function(resp){
            if (!resp || resp.status != 0) {
                $filter('toasterManage')(5, failMessage + (resp.statusMessage?resp.statusMessage:''),false);
                $scope.formDataUpdateConfig.model.attributes = [];
            }
            else {
                var obj = JSON.parse(resp.rows[0].message);
                var attr = {};

                angular.forEach(obj, function (indexData, index, array) {
                    //indexData等价于array[index]
                    attr = {
                        name: indexData.name,
                        version: indexData.version,
                        force_update: indexData.force_update,
                        description: indexData.description,
                        //type: type,
                        md5: indexData.md5,
                        web: indexData.web,
                        uploadState: false,
                        md5InputValid: false,
                        show: true,
                        editing: false
                    };
                    $scope.formDataUpdateConfig.model.attributes.push(attr);
                });
                //if ($scope.mgrUpdateConfigData.constant.SWITCH_MERCHANT_STORE == 100 && ( type == $scope.mgrUpdateConfigData.constant.TYPE_AGENT || type == $scope.mgrUpdateConfigData.constant.TYPE_AGENT_PATCH )) {
                //    if($filter('isHasProp')(obj)){
                //        attr = agentObjToCommon(obj, type);
                //        $scope.formDataUpdateConfig.model.attributes.push(attr);
                //    }
                //}
                //else {
                //    angular.forEach(obj, function (indexData, index, array) {
                //        //indexData等价于array[index]
                //        attr = {
                //            name: indexData.name,
                //            version: indexData.version,
                //            force_update: indexData.force_update,
                //            description: indexData.description,
                //            //type: type,
                //            md5: indexData.md5,
                //            web: indexData.web,
                //            uploadState: false,
                //            md5InputValid: false,
                //            show: true,
                //            editing: false
                //        };
                //        $scope.formDataUpdateConfig.model.attributes.push(attr);
                //    });
                //}
            }
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

    ////查询推送设备组下拉框内容
    //function getProductUsedList() {
    //    return cResource.query('./product/used/listByStore4Select');
    //}



    ////校验是否选择了设备组
    //function checkProductUsedCodeOK(code) {
    //    if (!code || code == '') {
    //        $filter('toasterManage')(5, "请先选择要升级的设备组!",false);
    //        //toaster.error({body: "请先选择要升级的设备组!"});
    //        return false;
    //    }
    //    return true;
    //}
    //校验参数是否为空
    function checkParamOK(param,msg) {
        if (!param || param == '') {
            $filter('toasterManage')(5, msg,false);
            return false;
        }
        return true;
    }

    //formly提交config
    $scope.configSubmit = function () {
        //var code = $scope.formDataUpdateConfig.model.code;
        ////校验是否选择了设备组
        //if (!checkProductUsedCodeOK(code)) {
        //    return false;
        //}
        if (!checkParamOK($scope.formDataUpdateConfig.model.type,'请选择类型') || !checkParamOK($scope.formDataUpdateConfig.model.seeType,'请选择设备可见范围')) {
            return false;
        }

        var attr = {};
        var checkAllRowOK = true;
        angular.forEach($scope.formDataUpdateConfig.model.attributes, function (indexData, index, array) {
            //for循环终止条件，只要有一条数据校验不通过，就停止循环
            if (checkAllRowOK == false) {
                return false;
            }
            //校验信息填写是否完整,只校验没有假删除的
            if (indexData.show && !checkThisRowOK(indexData,index,$scope.formDataUpdateConfig.model.attributes)) {
                initialParams();
                checkAllRowOK = false;
                return false;
            }
            if($filter('isNullOrEmptyString')(indexData.editing) || indexData.editing == true){
                $timeout(function () {
                    $filter('toasterManage')(5, "有未保存的行，请点击√保存!",false);
                }, 0);
                initialParams();
                checkAllRowOK = false;
                return false;
            }

            if (indexData.show && (indexData.md5 == null || indexData.web == null || indexData.md5 == "" || indexData.web == "")) {
                $timeout(function () {
                    $filter('toasterManage')(5, indexData.name + "请上传文件并保存!",false);
                }, 0);
                initialParams();
                checkAllRowOK = false;
                return false;
            }

            if (indexData.show) {//只把没假删除的结果写入最终数据
                //indexData等价于array[index]
                attr = {
                    name: indexData.name,
                    version: indexData.version,
                    force_update: indexData.force_update,
                    description: indexData.description,
                    md5: indexData.md5,
                    web: indexData.web
                };
                $scope.submitResult.push(attr);
                //if (indexData.type == $scope.mgrUpdateConfigData.constant.TYPE_MODULE) {
                //    $scope.submitModuleResult.push(attr);
                //}
                //else if (indexData.type == $scope.mgrUpdateConfigData.constant.TYPE_APK) {
                //    $scope.submitApkResult.push(attr);
                //}
                //else if (indexData.type == $scope.mgrUpdateConfigData.constant.TYPE_AGENT) {
                //    $scope.submitAgentResult.push(change4Agent(attr));
                //}
                //else if (indexData.type == $scope.mgrUpdateConfigData.constant.TYPE_AGENT_PATCH) {
                //    $scope.submitAgentPatchResult.push(change4Agent(attr));
                //}
            }
        });
        //若有检测不通过，则不提交
        if (!checkAllRowOK) {
            return false;
        }

        if( !$scope.submitResult || $scope.submitResult.length == 0 ) {
            $filter('toasterManage')(5, "请至少设置一项配置详细",false);
            return false;
        }
        //var promises = [];
        var baseInfo = getBaseInfoByType($scope.formDataUpdateConfig.model.type);
        var pathNoFileName = getNoFileNamePathByType(baseInfo,$scope.selfDesignPadFileName);
        var data ={
            entityText: JSON.stringify({
                "salt": "/",
                "path": pathNoFileName,
                "ifEditor": true,
                "type": 1,
                "name": baseInfo.FILE_NAME,
                "content": $scope.submitResult.length > 0 ? $scope.submitResult : []
            })
        };

        //promises.push(
        //    $resource(mgrUpdateConfigData.api.create).save(data, {}).$promise
        //);

        //promises.push(
        //    $resource(mgrUpdateConfigData.api.create).save({
        //        entityText: JSON.stringify({
        //            "salt": "/",
        //            "path": $scope.mgrUpdateConfigData.constant.BASE_PATH_APK + code,
        //            "ifEditor": true,
        //            "type": 1,
        //            "name": $scope.mgrUpdateConfigData.constant.FILE_NAME_APK,
        //            "content": $scope.submitApkResult.length > 0 ? $scope.submitApkResult : []
        //        })
        //    }, {}).$promise
        //);
        //
        ////console.log(changeToVersionInfoXML($scope.submitApkResult));
        //promises.push(
        //    $resource(mgrUpdateConfigData.api.create).save({
        //        entityText: JSON.stringify({
        //            "salt": "/",
        //            "path": $scope.mgrUpdateConfigData.constant.BASE_PATH_AGENT,
        //            "ifEditor": true,
        //            "type": 1,
        //            "name": $scope.mgrUpdateConfigData.constant.FILE_NAME_AGENT,
        //            //"content": changeToVersionInfoXML($scope.submitAgentResult)
        //            "content": ($scope.mgrUpdateConfigData.constant.SWITCH_MERCHANT_STORE == 100 && $scope.submitAgentResult.length > 0) ? $scope.submitAgentResult[0] : {}
        //        })
        //    }, {}).$promise
        //);
        //
        //promises.push(
        //    $resource(mgrUpdateConfigData.api.create).save({
        //        entityText: JSON.stringify({
        //            "salt": "/",
        //            "path": $scope.mgrUpdateConfigData.constant.BASE_PATH_AGENT_PATCH,
        //            "ifEditor": true,
        //            "type": 1,
        //            "name": $scope.mgrUpdateConfigData.constant.FILE_NAME_AGENT_PATCH,
        //            //"content": changeToVersionInfoXML($scope.submitAgentPatchResult)
        //            "content": ($scope.mgrUpdateConfigData.constant.SWITCH_MERCHANT_STORE == 100 && $scope.submitAgentPatchResult.length > 0) ? $scope.submitAgentPatchResult[0] : {}
        //        })
        //    }, {}).$promise
        //);

        //存储表中结果到文件 {"salt":100,"path":"catch","ifEditor":true,"type":1,"name":"test.txt","content":"tetst"}
        //$q.all(promises).then(function (respArray) {//返回结果的序列顺序跟上面参数的promise数组顺序一致
        //    angular.forEach(respArray, function (indexData, index, array) {
        //        //indexData等价于array[index]
        //        if (0 != indexData.status) {
        //            $filter('toasterManage')(5, "保存配置文件失败!",false);
        //            return;
        //        }
        //    });
        //    $filter('toasterManage')(5, "保存配置文件成功!",true);
        //
        //    $scope.goDataTable();
        //});

        var formly = $scope.formDataUpdateConfig;
        console.log(formly)
        console.log(formly.form.$valid)
        if (formly.form.$valid) {
            $scope.disableSubmit = true;
            formly.options.updateInitialValue();
            //存储表中结果到文件 {"salt":100,"path":"catch","ifEditor":true,"type":1,"name":"test.txt","content":"tetst"}
            cResource.save($scope.mgrUpdateConfigData.api.create,data, {}).then(function (resp) {//返回结果的序列顺序跟上面参数的promise数组顺序一致
                if (0 != resp.status) {
                    $filter('toasterManage')(5, "保存配置文件失败!",false);
                    return false;
                }
                $filter('toasterManage')(5, "保存配置文件成功!",true);
                //保存配置记录到数据库
                var model = formly.model;
                var dataConfig = {
                    name:baseInfo.FILE_NAME,
                    description:model.description || '',
                    type:model.type,
                    seeType:model.seeType,
                    createTime:$scope.createTimeMills,
                    path:pathNoFileName + '/' + baseInfo.FILE_NAME,
                }
                cResource.save($scope.mgrUpdateConfigData.api.update,{},dataConfig).then(function (resp1) {
                    $scope.disableSubmit = false;
                    if (0 != resp1.status || !resp1.dataMap) {
                        $filter('toasterManage')(5, "保存升级配置历史记录失败!",false);
                        return false;
                    }
                    var data = resp1.dataMap.updateResult;//scope.formData.model;//response.rows[0].updateResult;//
                    if ($scope.rowIndex < 0) {
                        //scope.tableOpts.data.unshift(data);
                        $scope.tableOpts.data.splice(0, 0, data);
                    } else {
                        $scope.tableOpts.data.splice($scope.rowIndex, 1, data);
                    }
                    $scope.goDataTable();
                });
            });
        }

    };

    ////把通用字段修改成agent配套字段
    //function change4Agent(attr) {
    //    return {
    //        name: attr.name,
    //        versionName: '',
    //        versionCode: attr.version,
    //        description: attr.description,
    //        url: attr.web,
    //        verification: attr.md5
    //    }
    //}

    //把agent字段修改成通用字段
    //function agentObjToCommon(indexData, type) {
    //    var obj = {};
    //    if ($filter('isHasProp')(indexData)) {
    //        obj = {
    //            name: indexData.name,
    //            version: indexData.versionCode,
    //            force_update: '',
    //            description: indexData.description,
    //            //type: type,
    //            md5: indexData.verification,
    //            web: indexData.url,
    //            uploadState: false,
    //            md5InputValid: false,
    //            show: true,
    //            editing: false
    //        }
    //    }
    //    return obj;
    //}

    //formly返回
    $scope.configGoDataTable = function () {
        $scope.activeTab = iDatatable;
    };

    $scope.cancelAttr = function (product, attr) {
        //var index = product.attributes.indexOf(attr);
        //product.attributes.splice(index, 1);
        attr.editing = false;
    };

    $scope.deleteAttr = function (model, attr) {
        cAlerts.confirm('确定删除?', function () {
            //点击确定回调
            $filter('toasterManage')(5, "操作成功!",true);
            var index = model.attributes.indexOf(attr);
            //model.attributes.splice(index, 1);
            //为了文件假删除一行
            model.attributes[index].show = false;
            $scope.fileList[index] = {};
        }, function () {
            //点击取消回调
        });
    };

    //编辑一行属性
    $scope.goEditAttr = function (model, thisRow, index) {
        $scope.formDataUpdateConfig.model.attributes[index].editing=true;
        $scope.typeDisabled = true;//禁止修改类型
    }

    //提交一行属性
    $scope.updateAttr = function (model, thisRow, index) {
        //console.log(thisRow)
        if (!checkThisRowOK(thisRow,index,$scope.formDataUpdateConfig.model.attributes)) {
            return;
        }
        var _file = $scope.fileList[index];
        if (!_file) {
            $timeout(function () {
                $filter('toasterManage')(5, "请选择升级包文件!",false);
                //toaster.error({body: "请选择升级包文件!"})
            }, 0);
            return;
        }
        var fileName = _file.name;
        //if (thisRow.name != $filter('getFileName')(_file.name, $scope.mgrUpdateConfigData.constant.FILE_NAME_NO_EXTERN)) {
        //除了自研平板，其他名字都是从对照表取
        if ($scope.formDataUpdateConfig.model.type != mgrUpdateConfigData.constant.BASE_INFO_SELF_DESIGN_PAD.TYPE
                && thisRow.name != calNameByFileName(fileName,thisRow,index) ) {
            $timeout(function () {
                $filter('toasterManage')(5, "上传的文件名与模块名不匹配!",false);
            }, 0);
            return false;
        }
        else if( $scope.formDataUpdateConfig.model.type == mgrUpdateConfigData.constant.BASE_INFO_SELF_DESIGN_PAD.TYPE
            && !checkBoardUpdateFileNameOK(fileName) ) {
            //$timeout(function () {
            //    $filter('toasterManage')(5,"请选择自研平板升级文件！例如："
            //        + $scope.mgrUpdateConfigData.constant.BASE_INFO_SELF_DESIGN_PAD.FILE_NAME_EXAMPLE
            //        ,false);
            //}, 0);
            return false;
        }

        var fd = new FormData();
        var baseInfo = getBaseInfoByType($scope.formDataUpdateConfig.model.type);
        //自研平板的存储路径是从文件名计算出来的
        var pathNoFileName = getNoFileNamePathByType(baseInfo,fileName);
        $scope.selfDesignPadFileName = fileName;//缓存自研平板文件名
        //if ($scope.formDataUpdateConfig.model.type == $scope.mgrUpdateConfigData.constant.BASE_INFO_MODULE.TYPE) {
        //    path = $scope.mgrUpdateConfigData.constant.BASE_INFO_MODULE.BASE_PATH + $scope.createTimeStamp;
        //}
        //else if ($scope.formDataUpdateConfig.model.type == $scope.mgrUpdateConfigData.constant.BASE_INFO_APK.TYPE) {
        //    path = $scope.mgrUpdateConfigData.constant.BASE_INFO_APK.BASE_PATH + $scope.createTimeStamp;
        //}
        //else if ($scope.formDataUpdateConfig.model.type == $scope.mgrUpdateConfigData.constant.BASE_INFO_AGENT.TYPE) {
        //    path = $scope.mgrUpdateConfigData.constant.BASE_INFO_AGENT.BASE_PATH + $scope.createTimeStamp;
        //}
        //else if ($scope.formDataUpdateConfig.model.type == $scope.mgrUpdateConfigData.constant.BASE_INFO_AGENT_PATCH.TYPE) {
        //    path = $scope.mgrUpdateConfigData.constant.BASE_INFO_AGENT_PATCH.BASE_PATH + $scope.createTimeStamp;
        //}
        //else if ($scope.formDataUpdateConfig.model.type == $scope.mgrUpdateConfigData.constant.BASE_INFO_SELF_DESIGN_PAD.TYPE) {
        //    path = $scope.mgrUpdateConfigData.constant.BASE_INFO_SELF_DESIGN_PAD.BASE_PATH + $scope.createTimeStamp;
        //}
        //else {
        //    $filter('toasterManage')(5, "类型设置不正确，保存失败!",false);
        //    return;
        //}
        fd.append('file', _file);
        //先校验文件是否存在，若存在则不让重复上传——目前应该只对自研平板有效，其他模块的上传时间肯定不相同
        cResource.get($scope.mgrUpdateConfigData.api.isFileExist,{path: pathNoFileName,storeId:mgrUpdateConfigData.constant.DEFAULT_MERCHANT_STORE}).then(function(res){
            if (0 != res.status) {
                $timeout(function () {
                    $filter('toasterManage')(5, fileName + "查询是否存在失败!",false);
                }, 0);
                return false;
            } else {
                if( res.rows[0].fileIsExist ) {
                    $timeout(function () {
                        $filter('toasterManage')(5, fileName + "文件已存在，请先删除旧文件!",false);
                    }, 0);
                    angular.element('#file'+index)[0].value = '';//清空input[type=file]value[ 垃圾方式 建议不要使用]
                } else {
                    cResource.upload($scope.mgrUpdateConfigData.api.uploadFile,{'type': 'file',storeId:mgrUpdateConfigData.constant.DEFAULT_MERCHANT_STORE, path: pathNoFileName + '/' + fileName}, fd).then(function(res){
                        if (0 != res.status) {
                            $timeout(function () {
                                $filter('toasterManage')(5, fileName + "上传失败!",false);
                            }, 0);
                            $scope.formDataUpdateConfig.model.attributes[index].uploadState = false;
                            return;
                        } else {
                            $timeout(function () {
                                $filter('toasterManage')(5, fileName + "上传成功!",true);
                            }, 0);
                            $scope.formDataUpdateConfig.model.attributes[index].md5 = res.dataMap.tree.md5;
                            $scope.formDataUpdateConfig.model.attributes[index].web = baseUrl.pushUrl + res.dataMap.tree.downloadPath;
                            $scope.formDataUpdateConfig.model.attributes[index].uploadState = true;
                            thisRow.editing = false;
                        }
                    });
                }
            }
        });

    };

    //自研平板的存储路径是从文件名计算出来的
    function getNoFileNamePathByType(baseInfo,fileName) {
        if ( baseInfo.TYPE == mgrUpdateConfigData.constant.BASE_INFO_SELF_DESIGN_PAD.TYPE ) {
            var versionSplit = fileName.split("-");
            var projName = versionSplit[0];
            var softVer = versionSplit[1];
            var hardVer = versionSplit[2];

            return baseInfo.BASE_PATH
                + projName + "/" + hardVer + "/"
                + softVer /* + "/" + _file.name*/;
        }
        else {
            return baseInfo.BASE_PATH + $scope.createTimeStamp;
        }
    }

    //校验一行数据是否符合要求
    function checkThisRowOK(thisRow,index,attrs) {
        //console.log(thisRow.type)
        //console.log(!(thisRow.type == $scope.mgrUpdateConfigData.constant.TYPE_AGENT || thisRow.type == $scope.mgrUpdateConfigData.constant.TYPE_AGENT_PATCH ));
        var checkOK = true;
        if ($filter('isNullOrEmptyString')(thisRow)
            || $filter('isNullOrEmptyString')(thisRow.name)
            || $filter('isNullOrEmptyString')(thisRow.version)
            //|| $filter('isNullOrEmptyString')(thisRow.type)
            || $filter('isNullOrEmptyString')(thisRow.force_update) ) {
            $timeout(function () {
                $filter('toasterManage')(5,"请填写完整信息!",false);
            }, 0);
            checkOK = false;
            return checkOK;
        }
        //if (thisRow.type == $scope.mgrUpdateConfigData.constant.TYPE_APK && (typeof thisRow.version != 'number')) {
        //    $timeout(function () {
        //        $filter('toasterManage')(5,"类型为“应用”的请填写纯数字版本信息!",false);
        //    }, 0);
        //    checkOK = false;
        //    return checkOK;
        //}
        angular.forEach(attrs, function (indexData, attrsIndex, array) {
            //indexData等价于array[index]
            //for循环终止条件，只要有一条数据校验不通过，就停止循环
            if (checkOK == false) {
                return checkOK;
            }

            //获取当前类型对照表中名字的列表，提交时校验配置详细名字与对照表中是否一致
            var isFind = false;
            angular.forEach( autoNameCheckList, function (indexData2, index2, array2) {
                if(indexData2.name == indexData.name &&  indexData2.type == $scope.formDataUpdateConfig.model.type) {
                    isFind = true;
                }
            });
            if(!isFind) {
                $filter('toasterManage')(5, "配置详细与类型不匹配，请按照对照表设置配置详细!",false);
                checkOK = false;
            }

            //if (indexData.show && indexData.type == thisRow.type && index != attrsIndex && indexData.name == thisRow.name ) {
            //校验同一类型下不允许有重复的名称
            if (indexData.show && index != attrsIndex && indexData.name == thisRow.name ) {
                $filter('toasterManage')(5, "同一类型下不允许有重复的名称!",false);
                checkOK = false;
            }
        });
        return checkOK;
    }

    //下载配置文件函数
    //$scope.downloadConfig = function (type) {
    //    ////校验是否选择了设备组
    //    //if (!checkProductUsedCodeOK($scope.formDataUpdateConfig.model.code)) {
    //    //    return '';
    //    //}
    //
    //    var baseUrl = $rootScope.baseUrl.pushUrl
    //        + Constants.thisMerchantStore.id
    //        + "/";
    //    if (type == $scope.mgrUpdateConfigData.constant.TYPE_APK) {
    //        return baseUrl
    //            + $scope.mgrUpdateConfigData.constant.BASE_PATH_APK
    //            + $scope.formDataUpdateConfig.model.code
    //            + "/" + $scope.mgrUpdateConfigData.constant.FILE_NAME_APK;
    //    }
    //    else if (type == $scope.mgrUpdateConfigData.constant.TYPE_MODULE) {
    //        return baseUrl
    //            + $scope.mgrUpdateConfigData.constant.BASE_PATH_MODULE
    //            + $scope.formDataUpdateConfig.model.code
    //            + "/" + $scope.mgrUpdateConfigData.constant.FILE_NAME_MODULE;
    //    }
    //}

    //读取用户本地配置文件到表格
    /**  读取Json文件内容 **/
    $scope.readFile = function (input) {
        var file_name = $filter('getFileName')(input.files[0].name, $scope.mgrUpdateConfigData.constant.FILE_NAME_WITH_EXTERN);
        var baseInfo = getBaseInfoByType($scope.formDataUpdateConfig.model.type);
        var targetFileName = baseInfo.FILE_NAME;

        if (targetFileName != file_name) {
            $timeout(function () {
                $filter('toasterManage')(5,"请选择" + targetFileName + "文件!",false);
            }, 0);
            return;
        }

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
                    //var type;
                    ////读取模块配置文件
                    //if (file_name == $scope.mgrUpdateConfigData.constant.FILE_NAME_MODULE) {
                    //    type = $scope.mgrUpdateConfigData.constant.TYPE_MODULE;
                    //}
                    ////读取APK模块配置文件
                    //else if (file_name == $scope.mgrUpdateConfigData.constant.FILE_NAME_APK) {
                    //    type = $scope.mgrUpdateConfigData.constant.TYPE_APK;
                    //}
                    //indexData等价于array[index]
                    attr.push({
                        name: indexData.name,
                        version: indexData.version,
                        force_update: indexData.force_update,
                        description: indexData.description,
                        md5: indexData.md5,
                        web: indexData.web,
                        //type: type,
                        uploadState: false,
                        md5InputValid: false,
                        show: true,
                        editing: false
                    });
                });
                //手动渲染。。。可以研究有什么更好的
                $scope.$apply(function () {
                    $scope.formDataUpdateConfig.model.attributes = $scope.formDataUpdateConfig.model.attributes.concat(attr);
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

    //判断是不是要显示agent和agentPatch类型
    //function checkAgentORPatch(model) {
    //    //新增一行，判断是否有agent类型配置了，如果有了，则不再显示
    //    if (isOneType($scope.mgrUpdateConfigData.constant.TYPE_AGENT, model.attributes)) {
    //        $scope.showTypeAgent = false;
    //    }
    //    else {
    //        $scope.showTypeAgent = true;
    //    }
    //    //新增一行，判断是否有agentPatch类型配置了，如果有了，则不再显示
    //    if (isOneType($scope.mgrUpdateConfigData.constant.TYPE_AGENT_PATCH, model.attributes)) {
    //        $scope.showTypeAgentPatch = false;
    //    }
    //    else {
    //        $scope.showTypeAgentPatch = true;
    //    }
    //}

    //判断agent或agentPatch是不是已经有了，如果已经有了，返回true
    //function isOneType(typeName, attrs) {
    //    var isOne = false;
    //    angular.forEach(attrs, function (indexData, index, array) {
    //        //indexData等价于array[index]
    //        if (isOne == true) {
    //            return false;
    //        }
    //        if (indexData.show && typeName == indexData.type) {
    //            isOne = true;
    //        }
    //    });
    //    return isOne;
    //}

    //计算attr列表有效数据的长度
    function getUsefulAttrLength(attrs) {
        var length = 0;
        angular.forEach(attrs, function (indexData, index, array) {
            //indexData等价于array[index]
            if (indexData.show) {
                length++;
            }
        });
        return length;
    }

    $scope.insertAttr = function (model) {
        ////校验是否选择了设备组
        //if (!checkProductUsedCodeOK($scope.formDataUpdateConfig.model.code)) {
        //    return '';
        //}

        //校验是否选择了类型
        if (!checkParamOK($scope.formDataUpdateConfig.model.type,'请选择类型')) {
            return false;
        }
        if (!model.attributes) {
            model.attributes = [];
        }

        //倒序遍历判断是否有未填写完整的行，如果有，则报错；如果没有，再新增一行
        var totalLength = model.attributes.length;
        for ( var i=0;i< totalLength;i++ ) {
            var length = totalLength - i - 1;
            if (length >= 0){
                var thisRow = model.attributes[length];
                if( thisRow.show && !checkThisRowOK(thisRow,length,$scope.formDataUpdateConfig.model.attributes)) {
                    return false;
                }
            }
        }

        //判断是不是要显示agent和agentPatch类型
        //checkAgentORPatch(model);

        //判断agent/agentPatch/self_design_board的数据是否已经有一条
        if(($scope.formDataUpdateConfig.model.type == mgrUpdateConfigData.constant.BASE_INFO_AGENT.TYPE
                || $scope.formDataUpdateConfig.model.type == mgrUpdateConfigData.constant.BASE_INFO_AGENT_PATCH.TYPE
                || $scope.formDataUpdateConfig.model.type == mgrUpdateConfigData.constant.BASE_INFO_SELF_DESIGN_PAD.TYPE)
            && getUsefulAttrLength($scope.formDataUpdateConfig.model.attributes) > 0 ) {
            $filter('toasterManage')(5,"agent,agent补丁包,自研平板类型只能有一条配置详细!",false);
            return false;
        }

        //校验通过新增一行
        var baseInfo = getBaseInfoByType($scope.formDataUpdateConfig.model.type);
        model.attributes.push({
            name: '',
            version: '',
            force_update: baseInfo.FORCE_UPDATE_DEFAULT,
            description: '',
            //type: $scope.mgrUpdateConfigData.constant.TYPE_MODULE,
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

        var fileName = $filter('getFileName')(_file.name, $scope.mgrUpdateConfigData.constant.FILE_NAME_NO_EXTERN);
        var autoName = calNameByFileName(fileName,thisRow,index);
        $timeout(function () {
            //第一次读取文件时，按对照表对名称进行赋值
            if( !thisRow.name || thisRow.name == '' ){
                $scope.formDataUpdateConfig.model.attributes[index].name = thisRow.name = autoName;
                $scope.typeDisabled = true;
            }
            //按对照表查询名称与文件是否一致
            if (thisRow.name != autoName) {
                $filter('toasterManage')(5,"上传的文件名与模块或应用名不一致!",false);
                angular.element('#file'+index)[0].value = '';//清空input[type=file]value[ 垃圾方式 建议不要使用]
                return false;
            }
        }, 0);
    }

    //根据文件名从对照表查出模块名称
    function calNameByFileName(fileName,thisRow,index) {
        var resultName = undefined;
        var positionFind = -1;
        angular.forEach( autoNameCheckList, function (indexData, index, array) {
            //resultName有值了，就停止循环
            if( resultName && resultName != ''){
                return false;
            }
            positionFind = fileName.toLowerCase().indexOf( indexData.key.toLowerCase() );//变小写然后查找
            console.log(positionFind)
            //indexData等价于array[index]
            var type = $scope.formDataUpdateConfig.model.type;
            if ( indexData.type == type) {
                if( ( (type == $scope.mgrUpdateConfigData.constant.BASE_INFO_APK.TYPE  //应用命名规则Artemis_**
                        || type == $scope.mgrUpdateConfigData.constant.BASE_INFO_AGENT.TYPE
                        || type == $scope.mgrUpdateConfigData.constant.BASE_INFO_AGENT_PATCH.TYPE)  && positionFind == 0)
                    || (type == $scope.mgrUpdateConfigData.constant.BASE_INFO_MODULE.TYPE && positionFind == 4)//模块命名规则C001M08**
                ) {
                    resultName = indexData.name;
                }
                else if( type == $scope.mgrUpdateConfigData.constant.BASE_INFO_SELF_DESIGN_PAD.TYPE ) {
                    if (!checkBoardUpdateFileNameOK(fileName)) {
                        return false;
                    }
                    resultName = indexData.name;
                }
            }

        });
        if( !resultName ) {
            $timeout(function () {
                $filter('toasterManage')(5,"不符合对照表规则的文件!请更换!",false);
                angular.element('#file'+index)[0].value = '';//清空input[type=file]value[ 垃圾方式 建议不要使用]
            }, 0);
        }
        return resultName;
    }



    //自研平板升级文件上传-------------------------------------
    //$scope.boardUpdateFile = function (input) {
    //    var _file = input.files[0];
    //    var file_name = $filter('getFileName')(_file.name, $scope.mgrUpdateConfigData.constant.FILE_NAME_WITH_EXTERN);
    //    if (!checkBoardUpdateFileNameOK(file_name)) {
    //        //$timeout(function () {
    //        //    $filter('toasterManage')(5,"请选择自研平板升级文件！例如："
    //        //        + $scope.mgrUpdateConfigData.constant.BASE_INFO_SELF_DESIGN_PAD.FILE_NAME_EXAMPLE
    //        //        ,false);
    //        //}, 0);
    //        return false;
    //    }
    //
    //    var versionSplit = file_name.split("-");
    //    var projName = versionSplit[0];
    //    var softVer = versionSplit[1];
    //    var hardVer = versionSplit[2];
    //
    //    //上传升级文件
    //    var fd = new FormData();
    //    var path = $scope.mgrUpdateConfigData.constant.BASE_PATH_BOARD_UPDATE
    //        + projName + "/" + hardVer + "/" + softVer + "/" + _file.name;
    //    fd.append('file', _file);
    //    console.log(path)
    //    cResource.get($scope.mgrUpdateConfigData.api.isFileExist,{path: path,storeId:100}).then(function(res){
    //        if (0 != res.status) {
    //            $timeout(function () {
    //                $filter('toasterManage')(5, _file.name + "查询是否存在失败!",false);
    //            }, 0);
    //            return false;
    //        } else {
    //            if( res.rows[0].fileIsExist ) {
    //                $timeout(function () {
    //                    $filter('toasterManage')(5, _file.name + "文件已存在，请先删除旧文件!",false);
    //                }, 0);
    //                angular.element('#boardUpdateFile')[0].value = '';//清空input[type=file]value[ 垃圾方式 建议不要使用]
    //            } else {
    //                cResource.upload($scope.mgrUpdateConfigData.api.uploadFile,{'type': 'file', path: path}, fd).then(function(res){
    //                    if (0 != res.status) {
    //                        $timeout(function () {
    //                            $filter('toasterManage')(5, _file.name + "上传失败!",false);
    //                        }, 0);
    //                        return;
    //                    } else {
    //                        $timeout(function () {
    //                            $filter('toasterManage')(5, _file.name + "上传成功!",true);
    //                        }, 0);
    //                    }
    //                    angular.element('#boardUpdateFile')[0].value = '';//清空input[type=file]value[ 垃圾方式 建议不要使用]
    //                });
    //            }
    //        }
    //    });
    //
    //}

    //校验平板升级文件名，例如Cooky-C001M01A001-RK3288_v2-20160804ota.zip
    function checkBoardUpdateFileNameOK(fileName){
        var lowerFileName = fileName.toLowerCase();
        var versionSplit = lowerFileName.split("-");
        //console.log(versionSplit)
        var ok = true;
        if (versionSplit.length != 4) {
            ok = false;
        }
        var softVer = versionSplit[1];
        var time = versionSplit[3];
        if (softVer.indexOf('c') != 0) {
            ok = false;
        }
        else if(softVer.indexOf('m') != 4) {
            ok = false;
        }
        else if(softVer.indexOf('a') != 7) {
            ok = false;
        }
        else if(time.indexOf('ota') != 8){
            ok = false;
        }
        if( !ok ){
            $timeout(function () {
                $filter('toasterManage')(5,"请选择自研平板升级文件！例如："
                    + $scope.mgrUpdateConfigData.constant.BASE_INFO_SELF_DESIGN_PAD.FILE_NAME_EXAMPLE
                    ,false);
            }, 0);
        }
        return ok;
    }
}