angular.module('inspinia', [])
    .controller('merchantCtrl', merchantCtrl);

/**
 * merchantCtrl - controller
 */
merchantCtrl.$inject = ['$scope', 'Constants','cTables'];
function merchantCtrl($scope, Constants,cTables) {
    var mgrData = {
        fields: [
            {
                'key': 'name',
                'type': 'input',
                'templateOptions': {'type': 'text', 'label': '商户名称', required: true, 'placeholder': '商户名称'}
            },
            {
                'key': 'businessType', 'type': 'select',
                'templateOptions': {
                    required: true,
                    'label': '商户类型',
                    'options': Constants.merchantType
                }
            },
            {
                'key': 'cuisineType',
                'type': 'input',
                'templateOptions': {'type': 'text', 'label': '商户菜系', required: true, 'placeholder': '商户菜系'}
            },
            {
                'key': 'imgFile',
                'type': 'input',
                'templateOptions': {'type': 'file', 'label': '商户图标', 'placeholder': '商户图标'}
            },
            {
                'key': 'description',
                'type': 'textarea',
                'templateOptions': {'label': '商户描述', 'placeholder': '商户描述', "rows": 10}
            }
        ],
        api: {
            read: '/admin/merchant/paging',
            update: '/admin/merchant/save'
        }
    };
    cTables.initNgMgrCtrl(mgrData, $scope);
}