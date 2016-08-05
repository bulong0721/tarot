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
                key: 'imgFile',
                type: 'upload',
                name: 'img',//这个name是用来判断上传文件的类型，不判断为空('') || null
                templateOptions: {type: 'file', label: '商户图标', placeholder: '商户图标'}
            },
            //{
            //    key: 'logo',
            //    type: 'c_input',
            //    templateOptions: {type: 'text', label: '商户图标', placeholder: '商户图标'},
            //    hideExpression: 'true'
            //},
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
        })
    });
}
