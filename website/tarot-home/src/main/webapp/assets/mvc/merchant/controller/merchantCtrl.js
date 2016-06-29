angular.module('inspinia', [])
    .controller('merchantCtrl', merchantCtrl);

/**
 * merchantCtrl - controller
 */
merchantCtrl.$inject = ['$scope', 'Constants','cTables','cfromly'];
function merchantCtrl($scope, Constants,cTables,cfromly) {
    var mgrData = {
        fields: [
            {
                'key': 'name',
                'type': 'c_input',
                'className':'c_formly_line',
                'templateOptions': {'type': 'text', 'label': '商户名称', required: true, 'placeholder': '商户名称'}
            },
            {
                'key': 'businessType',
                'type': 'c_select',
                'className':'c_formly_line',
                'templateOptions': {
                    required: true,
                    'label': '商户类型',
                    'options': Constants.merchantType
                }
            },
            {
                'key': 'cuisineType',
                'type': 'c_input',
                'className':'c_formly_line',
                'templateOptions': {'type': 'text', 'label': '商户菜系', required: true, 'placeholder': '商户菜系'}
            },
            {
                'key': 'imgFile',
                'type': 'c_input',
                'className':'c_formly_line',
                'templateOptions': {'type': 'file', 'label': '商户图标', 'placeholder': '商户图标'}
            },
            {
                'key': 'description',
                'type': 'c_textarea',
                'className':'c_formly_line',
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