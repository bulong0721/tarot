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
                type: 'c_input',
                templateOptions: {type: 'text', label: '商户菜系', required: true, placeholder: '商户菜系'}
            },
            {
                id: 'imgFile',
                key: 'imgFile',
                type: 'upload',
                name: 'img',//这个name是用来判断上传文件的类型，不判断为空('') || null
                templateOptions: {type: 'file', label: '商户图标', placeholder: '商户图标'}
            },
            {
                key: 'logo',
                type: 'c_input',
                templateOptions: {type: 'text', label: '商户图标', placeholder: '商户图标'},
                hideExpression: 'true'
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
            read: '../admin/merchant/paging',
            update: '../admin/merchant/save',
            delete: '../admin/merchant/delete',
            upload: '../admin/file/create'
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
        angular.element('#imgFile')[0].value = '';//清空input[type=file]value[ 垃圾方式 建议不要使用]
        if (rowIndex > -1) {
            var data = $scope.tableOpts.data[rowIndex];
            $scope.formData.model = angular.copy(data);
            $scope.rowIndex = rowIndex;
        } else {
            $scope.formData.model = {};
            $scope.rowIndex = -1;
        }
        $scope.activeTab = iEditor;
    };

    //上传控件监听器
    $scope.$on('fileToUpload', function (event, arg) {
        //上传文件到后台
        $resource(mgrData.api.upload).save({
            entityText: {
                salt: Constants.thisMerchantStore.id,
                path: "logo"
            }
        }, {}).$promise.then(function (res) {
            console.log(res)
            if (0 != res.status) {
                $scope.toasterManage($scope.toastError,res);
                return;
            }
            $scope.toasterManage($scope.toastUploadSucc);
            $scope.formData.model.logo = res.rows[0].url;
        })
    });
}
