angular.module('myee', [])
    .controller('merchantCtrl', merchantCtrl);

/**
 * merchantCtrl - controller
 */
merchantCtrl.$inject = ['$scope', 'Constants', 'cTables', 'cfromly', '$resource'];
function merchantCtrl($scope, Constants, cTables, cfromly, $resource) {
    var mgrData = {
        fields: [
            {
                key: 'name',
                type: 'c_input',
                templateOptions: {type: 'text', label: '商户名称', required: true, placeholder: '商户名称'}
            },
            {
                key: 'businessType',
                type: 'c_select',
                className: 'c_select',
                templateOptions: {
                    required: true,
                    label: '商户类型',
                    options: Constants.merchantType
                }
            },
            {
                key: 'cuisineType',
                type: 'c_select',
                className: 'c_select',
                templateOptions: {
                    required:false,
                    label: '商户菜系',
                    options: Constants.merchantCuisine
                },
                hideExpression: function ($viewValue, $modelValue, scope) {
                    if (scope.model.businessType != 'FOOD') {
                        return true;   //隐藏
                    } else {
                        return false;  //餐厅类型为餐饮时显示
                    }
                }
            },
            {
                id: 'imgFile',
                key: 'imgFile',
                type: 'upload',
                templateOptions: {type: 'file', label: '商户图标', required: false, placeholder: '商户图标',
                    upAttr:{
                        name: 'img',//这个name是用来判断上传文件的类型，不判断为空('') || null
                        upType:4,
                        url:'./files/create',
                        param:{//这是url参数
                            type:'file',
                            path:'logo'
                        },
                        upMore:2
                    }
                },
                controller:['$scope', function ($scope) {
                    $scope.upRemove = function(index){
                        console.log(index)
                    }
                }]
            },
            {
                key: 'description',
                type: 'c_textarea',
                ngModelAttrs: {
                    style: {attribute: 'style'}
                },
                templateOptions: {label: '商户描述', placeholder: '商户描述', rows: 10, style: 'max-width:500px'}
            }
        ],
        api: {
            read: './merchant/paging',
            update: './merchant/save',
            delete: './merchant/delete',
            upload: './files/create'
        }
    };
    cTables.initNgMgrCtrl(mgrData, $scope);

    $scope.thisMerchantId = Constants.thisMerchantStore.merchant.id;

    var iEditor = 1;

    //formly提交
    $scope.processSubmit = function () {
        var formly = $scope.formData;
        if (formly.form.$valid) {
            //formly.options.updateInitialValue();
            var xhr = $resource(mgrData.api.update);
            xhr.save({}, formly.model).$promise.then($scope.saveSuccess, $scope.saveFailed);
        }
    };

    //点击编辑
    $scope.goEditor = function (rowIndex) {
        //angular.element('#imgFile')[0].value = '';//清空input[type=file]value[ 垃圾方式 建议不要使用]
        if (rowIndex > -1) {
            var data = $scope.tableOpts.data[rowIndex];
            $scope.formData.model = angular.copy(data);
            //console.log(data)
            $scope.formData.model.imgFile = data.logo?data.logo:"http://cdn.myee7.com/FuMJj5jpAK8_wd2c0KvdwEmCaATt?imageView2/1/w/150/h/95";

            $scope.rowIndex = rowIndex;
        } else {
            $scope.formData.model = {};
            $scope.rowIndex = -1;
        }
        $scope.activeTab = iEditor;
    };

    //上传控件监听器
    $scope.$on('fileToUpload', function (event, arg) {
        console.log(arg)
        //上传文件到后台

        /*$resource(mgrData.api.upload).save({path: "logo", type: "file"}, arg).$promise.then(function (res) {
            //console.log(res)
            if (0 != res.status) {
                $scope.toasterManage($scope.toastError, res);
                return;
            }
            var pic = res.dataMap.tree.downloadPath;
            $scope.toasterManage($scope.toastUploadSucc);
            $scope.formData.model.logo = pic;
            $scope.formData.model.imgFile = $scope.formData.model.logo = res.dataMap.tree.downloadPath;
        })*/
    });
}
