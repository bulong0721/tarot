angular.module('myee', [])
    .controller('clientPrizeCtrl', clientPrizeCtrl);

/**
 * clientPrizeCtrl - controller
 */
clientPrizeCtrl.$inject = ['$scope', 'Constants','cTables','cfromly','toaster','cResource','$filter','baseConstant'];
function clientPrizeCtrl($scope, Constants,cTables,cfromly,toaster,cResource,$filter,baseConstant) {
    var mgrData = $scope.mgrData = {
        fields: [
            {
                key: 'name',
                type: 'c_input',
                templateOptions: {type: 'text', label: '奖券名称', required: true, placeholder: '奖券名称,255字以内',maxlength:255,isSearch:true},
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
                    options:  [{name:"手机",value: 0},{name:"验证码",value: 1},{name:"谢谢惠顾",value: 2}],
                    isSearch:true
                }
            },
            {
                key: 'phonePrizeType',
                type: 'c_select',
                className:'c_select',
                templateOptions: {
                    required: true,
                    label: '手机奖券类型',
                    options:  [{name:"实物",value: 0},{name:"电影券",value: 1},{name:"招行信用卡",value: 2}],
                    isSearch:true
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
                templateOptions: {type: 'number', label: '奖券数量', required: true, placeholder: '奖券数量,11位以内数字',maxlength:11,pattern: '^[0-9]*$'}
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
                    $scope.editFile = function(call){
                        if($scope.model.smallPic){
                            call({url:baseUrl.pushUrl+ $scope.model.smallPic})
                        }
                    };
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
                    $scope.editFile = function(call){
                        if($scope.model.bigPic){
                            call({url:baseUrl.pushUrl+ $scope.model.bigPic})
                        }
                    };
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
                templateOptions: {label: '奖券描述', placeholder: '奖券描述,255字以内', rows: 10,style: 'max-width:500px',maxlength:255}
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
            upload: './files/create',
            uploadCheckCode:'./clientPrize/checkCodeUpload'
        }
    };
    cTables.initNgMgrCtrl(mgrData, $scope);

    $scope.processSubmit = function () {
        var formly = $scope.formData;
        if (formly.form.$valid) {
            $scope.disableSubmit = true;
            cResource.save(mgrData.api.update,{},formly.model).then($scope.saveSuccess);
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
        if(uploadId == 'smallPic'){
            $scope.formData.model.smallPic = pic;
        }else if(uploadId == 'bigPic'){
            $scope.formData.model.bigPic = pic;
        }

    });

    $scope.checkCodeUp = function (file,id) {
        var fd = new FormData();
        fd.append('file', file.files[0]);
        fd.append('prizeId',id);
        cResource.upload(mgrData.api.uploadCheckCode,{},fd).then(function(res){

        });
    }
}
