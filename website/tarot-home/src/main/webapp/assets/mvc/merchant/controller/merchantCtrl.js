angular.module('myee', [])
    .controller('merchantCtrl', merchantCtrl);

/**
 * merchantCtrl - controller
 */
merchantCtrl.$inject = ['$scope', 'Constants', 'cTables', 'cfromly', '$resource','$filter','$timeout'];
function merchantCtrl($scope, Constants, cTables, cfromly, $resource,$filter,$timeout) {
    var mgrData = {
        fields: [
            {
                key: 'name',
                type: 'c_input',
                templateOptions: {type: 'text', label: '商户名称', required: true, placeholder: '商户名称,60字以内',maxlength:60}
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
                type: 'c_images',
                templateOptions: {type: 'file', label: '商户图标预览', required: false, placeholder: '商户图标预览',on:true},
                controller:['$scope', function ($scope) {
                    $scope.on = function(){
                        $scope.model.imgCropBool = true;
                        $timeout(function () {$('#fileInput').click()}, 0);;
                    }
                }]
            },
            {
                key: 'imgCrop',
                type: 'c_img_crop',
                templateOptions: {label: '商户图标裁剪'},
                hideExpression: function ($viewValue, $modelValue, scope) {
                    return scope.model.imgCropBool?false:true;
                },
                controller:['$scope', function ($scope) {
                    $scope.$watch('myCroppedImage',function(newValue,oldValue){
                        $scope.model.logoBase64 = newValue;
                    });
                }]
            },
            {
                key: 'description',
                type: 'c_textarea',
                ngModelAttrs: {
                    style: {attribute: 'style'}
                },
                templateOptions: {label: '商户描述', placeholder: '商户描述,255字以内', rows: 10, style: 'max-width:500px',maxlength:255}
            }
        ],
        api: {
            read: './merchant/paging',
            update: './merchant/save',
            delete: './merchant/delete',
            //upload: './files/create'
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
            $scope.formData.model.imgFile = data.logo?baseUrl.pushUrl + data.logo : "";
            $scope.rowIndex = rowIndex;
        } else {
            $scope.formData.model = {};
            $scope.rowIndex = -1;
        }

        $scope.activeTab = iEditor;
    };

    //上传控件监听器
    $scope.$on('fileToUpload', function (event, res) {
        //console.log(res)
        //把上传结果赋值到formly.model对象对应字段
        var pic = res[0].dataMap.tree.downloadPath;
        $scope.toasterManage($scope.toastUploadSucc);
        $scope.formData.model.logo = pic;
    });

    $scope.tips = "*管理所有商户，不受切换门店影响";

}
