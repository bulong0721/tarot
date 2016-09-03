angular.module('myee', [])
    .controller('clientPrizeCtrl', clientPrizeCtrl);

/**
 * clientPrizeCtrl - controller
 */
clientPrizeCtrl.$inject = ['$scope', 'Constants','cTables','cfromly','toaster','$resource'];
function clientPrizeCtrl($scope, Constants,cTables,cfromly,toaster,$resource) {
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
                id: 'smallImage',
                type: 'upload',
                name: 'img',
                templateOptions: {type: 'file', label: '奖券小图标', required: false, placeholder: '奖券小图标'}
            },
            {
                key: 'imagesSmall',
                type: 'c_images',
                templateOptions: {label: '奖券小图标预览', Multi: false}
            },
            {
                id: 'bigImage',
                type: 'upload',
                name: 'img',
                templateOptions: {type: 'file', label: '奖券大图标', required: false, placeholder: '奖券大图标'}
            },
            {
                key: 'imagesBig',
                type: 'c_images',
                templateOptions: {label: '奖券大图标预览', Multi: false}
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
        angular.element('#smallImage')[0].value = '';
        angular.element('#bigImage')[0].value = '';//清空input[type=file]value[ 垃圾方式 建议不要使用]
        if (rowIndex > -1) {
            var data = $scope.tableOpts.data[rowIndex];
            $scope.formData.model = angular.copy(data);
            //console.log(data)
            $scope.formData.model.imagesSmall =data.smallPic ? Constants.downloadHome + data.smallPic: Constants.blankPicUrl;
            $scope.formData.model.imagesBig = data.bigPic ? Constants.downloadHome+ data.bigPic: Constants.blankPicUrl;
            $scope.rowIndex = rowIndex;
        } else {
            $scope.formData.model = {};
            $scope.formData.model.imagesSmall = Constants.blankPicUrl
            $scope.formData.model.imagesBig = Constants.blankPicUrl;
            $scope.rowIndex = -1;
        }
        $scope.activeTab = iEditor;
    };

    //上传控件监听器
    $scope.$on('fileToUpload', function (event, arg) {
        //console.log(event)
        //上传文件到后台
        var uploadId = event.targetScope.id;
        $resource(mgrData.api.upload).save({
            path: "prizeLogo",
            type: "file"
        }, arg).$promise.then(function (res) {
                //console.log(res)
                if (0 != res.status) {
                    $scope.toasterManage($scope.toastError, res);
                    return;
                }
                $scope.toasterManage($scope.toastUploadSucc);
                var pic = res.dataMap.tree.downloadPath;
                if(uploadId == "smallImage"){
                    $scope.formData.model.smallPic = pic;
                    console.log($scope.downloadHome + pic);
                    $scope.formData.model.imagesSmall = Constants.downloadHome + pic;
                }else if(uploadId == "bigImage"){
                    $scope.formData.model.bigPic = pic;
                    $scope.formData.model.imagesBig = Constants.downloadHome + pic;
                }
            })
    });
}
