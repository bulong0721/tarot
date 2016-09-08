angular.module('myee', [])
    .controller('clientPrizeCtrl', clientPrizeCtrl);

/**
 * clientPrizeCtrl - controller
 */
clientPrizeCtrl.$inject = ['$scope', 'Constants','cTables','cfromly','toaster','$resource','$filter','baseConstant'];
function clientPrizeCtrl($scope, Constants,cTables,cfromly,toaster,$resource,$filter,baseConstant) {
    var mgrData = {
        fields: [
            {
                key: 'name',
                type: 'c_input',
                templateOptions: {type: 'text', label: '奖券名称', required: true, placeholder: '奖券名称'},
                hideExpression: function ($viewValue, $modelValue, scope) {
                    if (scope.model.type != '2') {
                        return false;
                    } else {
                        return true;
                    }
                }
            },
            {
                key: 'type',
                type: 'c_select',
                className:'c_select',
                templateOptions: {
                    required: true,
                    label: '奖券类型',
                    options:  [{name:"手机",value: 0},{name:"验证码",value: 1},{name:"谢谢惠顾",value: 2}]
                }
            },
            {
                key: 'phonePrizeType',
                type: 'c_select',
                className:'c_select',
                templateOptions: {
                    required: true,
                    label: '手机奖券类型',
                    options:  [{name:"实物",value: 0},{name:"电影券",value: 1}]
                },
                hideExpression: function ($viewValue, $modelValue, scope) {
                    if (scope.model.type == '0') {
                        return false;
                    } else {
                        return true;
                    }
                }
            },
            {
                key: 'total',
                type: 'c_input',
                templateOptions: {type: 'text', label: '奖券数量', required: true, placeholder: '奖券数量'},
                hideExpression: function ($viewValue, $modelValue, scope) {
                    if (scope.model.type != '1') {
                        return false;
                    } else {
                        return true;
                    }
                }
            },
            {
                key: 'startDate',
                type: 'datepicker',
                templateOptions: {
                    label: '开始日期',
                    type: 'text',
                    required:true ,
                    datepickerPopup: 'yyyy-MM-dd',
                    datepickerOptions: {
                        format: 'yyyy-MM-dd'
                    }
                }
            },
            {
                key: 'endDate',
                type: 'datepicker',
                templateOptions: {
                    label: '结束日期',
                    type: 'text',
                    required:true ,
                    datepickerPopup: 'yyyy-MM-dd',
                    datepickerOptions: {
                        format: 'yyyy-MM-dd'
                    }
                }
            },

            {
                id: 'smallPic',
                key: 'smallPic',
                type: 'upload',
                templateOptions: {type: 'file', label: '奖券小图标', required: false, placeholder: '奖券小图标',
                    upAttr:{
                        name: 'img',//这个name是用来判断上传文件的类型，不判断为空('') || null
                        upType:2,
                        url:'./files/create',
                        param:{//这是url参数
                            type:'file',
                            path:'prizeLogo'
                        },
                        upMore:1
                    }
                },
                controller:['$scope', function ($scope) {
                    //资源回显
                    var md = $scope;
                    md.thumbnail = [];
                    md.len = true;
                    if(md.model.id && md.model.smallPic){
                        md.thumbnail.push({url:baseUrl.pushUrl+md.model.smallPic});
                        ;(md.thumbnail.length>=md.to.upAttr.upMore) && (md.len = false);
                    }
                    //删除资源
                    $scope.upRemove = function(index){
                        console.log(index);
                        $scope.model.smallPic = "";
                    }
                }]
            },
            {
                id: 'bigPic',
                key: 'bigPic',
                type: 'upload',
                templateOptions: {type: 'file', label: '奖券大图标', required: false, placeholder: '奖券大图标',
                    upAttr:{
                        name: 'img',//这个name是用来判断上传文件的类型，不判断为空('') || null
                        upType:2,
                        url:'./files/create',
                        param:{//这是url参数
                            type:'file',
                            path:'prizeLogo'
                        },
                        upMore:1
                    }
                },
                controller:['$scope', function ($scope) {
                    //资源回显
                    var md = $scope;
                    md.thumbnail = [];
                    md.len = true;
                    if(md.model.id && md.model.bigPic){
                        md.thumbnail.push({url:baseUrl.pushUrl+ md.model.bigPic});
                        ;(md.thumbnail.length>=md.to.upAttr.upMore) && (md.len = false);
                    }
                    //删除资源
                    $scope.upRemove = function(index){
                        console.log(index)
                        $scope.model.bigPic = "";
                    }
                }]
            },
            {
                key: 'description',
                type: 'c_textarea',
                ngModelAttrs: {
                    style: {attribute: 'style'}
                },
                templateOptions: {label: '奖券描述', placeholder: '奖券描述', rows: 10,style: 'max-width:500px'}
            },
            {
                key: 'activeStatus',
                type: 'c_input',
                className: 'formly-min-checkbox',
                templateOptions: {label: '启用状态', placeholder: '启用状态',type: 'checkbox'}
            }
        ],
        api: {
            read: './clientPrize/pagingList',
            update: './saveClientPrize',
            delete: './deleteClientPrize',
            upload: './files/create'
        }
    };
    cTables.initNgMgrCtrl(mgrData, $scope);

    $scope.processSubmit = function () {
        var formly = $scope.formData;
        if (formly.form.$valid) {
            var xhr = $resource(mgrData.api.update);
            xhr.save({}, formly.model).$promise.then($scope.saveSuccess, $scope.saveFailed);
        }
    };

    var iEditor = 1;
    //点击编辑
    $scope.goEditor = function (rowIndex) {
        //.element('#smallImage')[0].value = '';
        //angular.element('#bigImage')[0].value = '';//清空input[type=file]value[ 垃圾方式 建议不要使用]
        if (rowIndex > -1) {
            var data = $scope.tableOpts.data[rowIndex];
            $scope.formData.model = angular.copy(data);
            $scope.formData.model.smallPic =data.smallPic;
            $scope.formData.model.bigPic =  data.bigPic;
            $scope.rowIndex = rowIndex;
        } else {
            $scope.formData.model = {};
            $scope.rowIndex = -1;
        }
        $scope.activeTab = iEditor;
    };

    //上传控件监听器
    $scope.$on('fileToUpload', function (event, arg) {
        var uploadId = event.targetScope.id;
        var pic = arg[0].dataMap.tree.downloadPath;
        console.log(pic);
        if(uploadId == 'smallPic'){
            $scope.formData.model.smallPic = pic;
        }else if(uploadId == 'bigPic'){
            $scope.formData.model.bigPic = pic;
        }

    });
}
