angular.module('myee', [])
    .controller('campaignCtrl', campaignCtrl);

/**
 * merchantCtrl - controller
 */
campaignCtrl.$inject = ['$scope', 'Constants','cTables','cfromly'];
function campaignCtrl($scope, Constants,cTables,cfromly) {
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
                className:'c_select',
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
                type: 'c_input',
                templateOptions: {type: 'file', label: '商户图标', placeholder: '商户图标'}
            },
            {
                key: 'description',
                type: 'c_textarea',
                ngModelAttrs: {
                    style: {attribute: 'style'}
                },
                templateOptions: {label: '商户描述', placeholder: '商户描述', rows: 10,style: 'max-width:500px'}
            }
        ],
        api: {
            read: '../api/info/findHistoryInfoByStoreToday',
            //update: '../admin/merchant/save'
        }
    };
    cTables.initNgMgrCtrl(mgrData, $scope);

    $scope.checkCode = function() {
        $scope.loadByInit = true;
        $scope.tableOpts.reload();
    }
}
